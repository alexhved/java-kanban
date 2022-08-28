package server;

import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import manager.Managers;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final int PORT = 8080;
    private final HttpServer httpServer;
    private final FileBackedTasksManager backedTasksManager;

    public HttpTaskServer(FileBackedTasksManager taskManager) throws IOException {
        this.backedTasksManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task/", new TaskHandler(backedTasksManager));
        httpServer.createContext("/tasks/epic/", new EpicHandler(backedTasksManager));
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler(backedTasksManager));
        httpServer.createContext("/tasks/history/", new HistoryHandler(backedTasksManager));
        httpServer.createContext("/tasks/", new TasksHandler(backedTasksManager));
    }

    public HttpTaskServer() throws IOException {
        this(Managers.getBacked());
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public FileBackedTasksManager getBackedTasksManager() {
        return backedTasksManager;
    }
}
