package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.manager.TaskManager;
import taskmanager.task.Task;

import java.util.Map;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskmanager;
    @Test
    public void shouldReturnTaskmap() {
        Map<Integer, Task> taskMap = taskmanager.getTaskMap();
        Assertions.assertEquals(taskMap, taskmanager.getTaskMap());
    }
}
