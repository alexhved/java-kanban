package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.FileBackedTasksManager;
import manager.Managers;
import epic.Task;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class TaskHandler implements HttpHandler {
    protected final FileBackedTasksManager backedTasksManager;
    protected final Gson gson = Managers.getGson();

    public TaskHandler(FileBackedTasksManager backedTasksManager) {
        this.backedTasksManager = backedTasksManager;
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
            Task task = gson.fromJson(body, Task.class);
            if (!backedTasksManager.getTaskMap().containsKey(task.getId())) {
                backedTasksManager.createTask(task);
            } else {
                backedTasksManager.updateTask(task);
            }
            response = "Успешно";
            sendResponse(exchange, response, 201);
        }
    }

    protected void methodDelete(HttpExchange exchange) throws IOException {
        try (exchange) {
            String response;
            URI uri = exchange.getRequestURI();
            if (uri.toString().equals("/tasks/task/")) {
                backedTasksManager.removeAllTasks();
                response = "Успешно";
                sendResponse(exchange, response, 200);
                return;
            }
            if (uri.getQuery() != null) {
                String[] query = uri.getQuery().split("=");
                if (query[1].chars().allMatch(Character::isDigit)) {
                    int id = Integer.parseInt(query[1]);
                    if (!backedTasksManager.getTaskMap().containsKey(id)) {
                        response = "Неправильный запрос";
                        sendResponse(exchange, response, 400);
                        return;
                    }
                    backedTasksManager.removeTaskById(id);
                    response = "Успешно";
                    sendResponse(exchange, response, 200);
                } else {
                    response = "Неправильный запрос";
                    sendResponse(exchange, response, 400);
                }
            }
        }
    }

    protected void methodGet(HttpExchange exchange) throws IOException {
        String response;
        URI uri = exchange.getRequestURI();
        try (exchange) {
            if (uri.toString().equals("/tasks/task/")) {
                response = gson.toJson(backedTasksManager.getAllTasks());
                sendResponse(exchange, response, 200);
                return;
            }
            if (uri.getQuery() != null) {
                String[] query = uri.getQuery().split("=");
                if (query[1].chars().allMatch(Character::isDigit)) {
                    int id = Integer.parseInt(query[1]);
                    if (!backedTasksManager.getTaskMap().containsKey(id)) {
                        response = "Неправильный запрос";
                        sendResponse(exchange, response, 400);
                        return;
                    }
                    response = gson.toJson(backedTasksManager.getTaskById(id));
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

    protected void sendResponse(HttpExchange exchange, String response, int rCode) throws IOException {
        exchange.sendResponseHeaders(rCode, 0);
        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
    }

    protected String read(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void unknownMethod(HttpExchange exchange) throws IOException {
        try (exchange) {
            String response = "Неизвестный метод";
            sendResponse(exchange, response, 405);
        }
    }
}
