package taskmanager.manager;

import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static FileBackedTasksManager getBacked() {
        return new FileBackedTasksManager();
    }
}
