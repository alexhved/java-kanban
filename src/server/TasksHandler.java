package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.FileBackedTasksManager;

import java.io.IOException;
import java.net.URI;

public class TasksHandler extends TaskHandler implements HttpHandler {
    public TasksHandler(FileBackedTasksManager backedTasksManager) {
        super(backedTasksManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        URI uri = exchange.getRequestURI();
        if (!exchange.getRequestMethod().equals("GET")) {
            unknownMethod(exchange);
        }
        try (exchange) {
            if (uri.toString().equals("/tasks/")) {
                response = gson.toJson(backedTasksManager.getPrioritizedSet());
                sendResponse(exchange, response, 200);
                return;
            }
            response = "Неправильный запрос";
            sendResponse(exchange, response, 400);
        }
    }
}
