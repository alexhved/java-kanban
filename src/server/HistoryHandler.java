package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.FileBackedTasksManager;
import java.io.IOException;
import java.net.URI;

public class HistoryHandler extends TaskHandler implements HttpHandler {

    public HistoryHandler(FileBackedTasksManager backedTasksManager) {
        super(backedTasksManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        URI uri = exchange.getRequestURI();
        try (exchange) {
            if (uri.toString().equals("/tasks/history/")) {
                response = gson.toJson(backedTasksManager.getHistoryManager().getHistoryList());
                sendResponse(exchange, response, 200);
                return;
            }
            response = "Неправильный запрос";
            sendResponse(exchange, response, 400);
        }
    }
}
