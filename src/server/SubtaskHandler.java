package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.FileBackedTasksManager;
import epic.SubTask;

import java.io.IOException;
import java.net.URI;

public class SubtaskHandler extends TaskHandler implements HttpHandler {

    public SubtaskHandler(FileBackedTasksManager backedTasksManager) {
        super(backedTasksManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET" -> methodGet(exchange);
            case "DELETE" -> methodDelete(exchange);
            case "POST" -> methodPost(exchange);
            default -> unknownMethod(exchange);
        }
    }

    @Override
    protected void methodPost(HttpExchange exchange) throws IOException {
        try (exchange) {
            String response;
            String body = read(exchange);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (!jsonElement.isJsonObject()) {
                response = "Неправильный запрос";
                sendResponse(exchange, response, 400);
                return;
            }
            SubTask subTask = gson.fromJson(body, SubTask.class);
            if (!backedTasksManager.getSubTaskMap().containsKey(subTask.getId())) {
                backedTasksManager.createSubTask(subTask);
            } else {
                backedTasksManager.updateSubTask(subTask);
            }
            response = "Успешно";
            sendResponse(exchange, response, 201);
        }
    }

    @Override
    protected void methodDelete(HttpExchange exchange) throws IOException {
        try (exchange) {
            String response;
            URI uri = exchange.getRequestURI();
            if (uri.toString().equals("/tasks/subtask/")) {
                backedTasksManager.removeAllSubTasks();
                response = "Успешно";
                sendResponse(exchange, response, 200);
                return;
            }
            if (uri.getQuery() != null) {
                String[] query = uri.getQuery().split("=");
                if (query[1].chars().allMatch(Character::isDigit)) {
                    int id = Integer.parseInt(query[1]);
                    if (!backedTasksManager.getSubTaskMap().containsKey(id)) {
                        response = "Неправильный запрос";
                        sendResponse(exchange, response, 400);
                        return;
                    }
                    backedTasksManager.removeSubTaskById(id);
                    response = "Успешно";
                    sendResponse(exchange, response, 200);
                } else {
                    response = "Неправильный запрос";
                    sendResponse(exchange, response, 400);
                }
            }
        }
    }

    @Override
    protected void methodGet(HttpExchange exchange) throws IOException {
        String response;
        URI uri = exchange.getRequestURI();
        try (exchange) {
            if (uri.toString().equals("/tasks/subtask/")) {
                response = gson.toJson(backedTasksManager.getAllSubTasks());
                sendResponse(exchange, response, 200);
                return;
            }
            if (uri.toString().contains("/tasks/subtask/epic/?id=") && uri.getQuery() != null) {
                String[] query = uri.getQuery().split("=");
                if (query[1].chars().allMatch(Character::isDigit)) {
                    int id = Integer.parseInt(query[1]);
                    if (backedTasksManager.getEpicMap().containsKey(id)) {
                        response = gson.toJson(backedTasksManager.getEpicsSubtasks(id));
                        sendResponse(exchange, response, 200);
                        return;
                    }
                }
            }
            if (uri.toString().contains("/tasks/subtask/?id=") && uri.getQuery() != null) {
                String[] query = uri.getQuery().split("=");
                if (query[1].chars().allMatch(Character::isDigit)) {
                    int id = Integer.parseInt(query[1]);
                    if (!backedTasksManager.getSubTaskMap().containsKey(id)) {
                        response = "Неправильный запрос";
                        sendResponse(exchange, response, 400);
                        return;
                    }
                    response = gson.toJson(backedTasksManager.getSubTaskById(id));
                    sendResponse(exchange, response, 200);
                } else {
                    response = "Неправильный запрос";
                    sendResponse(exchange, response, 400);
                    return;
                }
                return;
            }
            response = "Неправильный запрос";
            sendResponse(exchange, response, 400);
        }
    }
}
