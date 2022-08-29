package server;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.FileBackedTasksManager;
import epic.Epic;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends TaskHandler implements HttpHandler {

    public EpicHandler(FileBackedTasksManager backedTasksManager) {
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
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (!jsonElement.isJsonObject()) {
                response = "Неправильный запрос";
                sendResponse(exchange, response, 400);
                return;
            }
            Epic epic = gson.fromJson(body, Epic.class);
            if (!backedTasksManager.getTaskMap().containsKey(epic.getId())) {
                backedTasksManager.createEpic(epic);
            } else {
                backedTasksManager.updateEpic(epic);
            }
            response = "Успешно";
            sendResponse(exchange, response, 200);
        }
    }

    @Override
    protected void methodDelete(HttpExchange exchange) throws IOException {
        String response;
        URI uri = exchange.getRequestURI();
        try (exchange) {
            if (uri.toString().equals("/tasks/epic/")) {
                backedTasksManager.removeAllEpics();
                response = "Успешно";
                sendResponse(exchange, response, 200);
                return;
            }
            if (uri.getQuery() != null) {
                String[] query = uri.getQuery().split("=");
                if (query[1].chars().allMatch(Character::isDigit)) {
                    int id = Integer.parseInt(query[1]);
                    if (!backedTasksManager.getEpicMap().containsKey(id)) {
                        response = "Неправильный запрос";
                        sendResponse(exchange, response, 400);
                        return;
                    }
                    backedTasksManager.removeEpicById(id);
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
            if (uri.toString().equals("/tasks/epic/")) {
                response = gson.toJson(backedTasksManager.getAllEpics());
                sendResponse(exchange, response, 200);
                return;
            }
            if (uri.getQuery() != null) {
                String[] query = uri.getQuery().split("=");
                if (query[1].chars().allMatch(Character::isDigit)) {
                    int id = Integer.parseInt(query[1]);
                    if (!backedTasksManager.getEpicMap().containsKey(id)) {
                        response = "Неправильный запрос";
                        sendResponse(exchange, response, 400);
                        return;
                    }
                    response = gson.toJson(backedTasksManager.getEpicById(id));
                    sendResponse(exchange, response, 200);
                } else {
                    response = "Неправильный запрос";
                    sendResponse(exchange, response, 400);
                }
                return;
            }
            response = "Неправильный запрос";
            sendResponse(exchange, response, 400);
        }
    }
}
