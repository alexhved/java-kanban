package test;

import org.junit.jupiter.api.BeforeEach;
import taskmanager.manager.FileBackedTasksManager;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @BeforeEach
    public void beforeEach() {
        taskmanager = new FileBackedTasksManager();
    }
}
