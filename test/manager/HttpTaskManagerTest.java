package manager;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import epic.Epic;
import epic.SubTask;
import epic.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.Set;

class HttpTaskManagerTest {
    KVServer kvServer;
    HttpTaskManager httpTaskManager;
    @BeforeEach
    public void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskManager = new HttpTaskManager("http://localhost:");

        httpTaskManager.createTask(new Task("1-5", "12345",
                LocalDateTime.of(2023, Month.JUNE, 1, 0, 0), Duration.ofMinutes(10)));
        httpTaskManager.createTask(new Task("5-10", "5678910",
                LocalDateTime.of(2023, Month.JUNE, 2, 0, 0), Duration.ofMinutes(10)));
        httpTaskManager.createEpic(new Epic("drive", "moto"));
        httpTaskManager.createSubTask(new SubTask("sprint 3", "OOP", 3,
                LocalDateTime.of(2023, Month.JUNE, 5, 0, 0), Duration.ofMinutes(10)));
        httpTaskManager.createSubTask(new SubTask("learning", "Java course", 3,
                LocalDateTime.of(2023, Month.JUNE, 6, 0, 0), Duration.ofMinutes(10)));
        httpTaskManager.getTaskById(1);
        httpTaskManager.getTaskById(2);
        httpTaskManager.getEpicById(3);
        httpTaskManager.getEpicById(4);
        httpTaskManager.getSubTaskById(5);
    }

    @AfterEach
    void tearDown() {
        httpTaskManager.removeAllTasks();
        httpTaskManager.removeAllEpics();
        httpTaskManager.removeAllSubTasks();
        httpTaskManager.getHistoryManager().getHistoryMap().clear();
        httpTaskManager.getPrioritizedSet().clear();
        InMemoryTaskManager.setId(0);

        kvServer.stop();
    }

    @Test
    void load() {
        Map<Integer, Task> taskMap = httpTaskManager.getTaskMap();
        Map<Integer, Epic> epicMap = httpTaskManager.getEpicMap();
        Map<Integer, SubTask> subTaskMap = httpTaskManager.getSubTaskMap();
        Set<Task> prioritizedSet = httpTaskManager.getPrioritizedSet();

        httpTaskManager.save();

        HttpTaskManager otherManager = new HttpTaskManager("http://localhost:");
        otherManager.load();

        Map<Integer, Task> otherManagerTaskMap = otherManager.getTaskMap();
        Map<Integer, Epic> otherManagerEpicMap = otherManager.getEpicMap();
        Map<Integer, SubTask> otherManagerSubTaskMap = otherManager.getSubTaskMap();
        Set<Task> otherManagerPrioritizedSet = otherManager.getPrioritizedSet();

        assertNotEquals(httpTaskManager, otherManager);
        assertEquals(taskMap, otherManagerTaskMap);
        assertEquals(epicMap, otherManagerEpicMap);
        assertEquals(subTaskMap, otherManagerSubTaskMap);
        assertEquals(prioritizedSet, otherManagerPrioritizedSet);
    }

}