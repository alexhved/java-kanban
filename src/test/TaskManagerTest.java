package test;

import org.junit.jupiter.api.Test;
import taskmanager.manager.TaskManager;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskmanager;
    @Test
    public void test() {
        taskmanager.getAllTasks();
    }
}
