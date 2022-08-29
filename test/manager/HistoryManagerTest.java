package manager;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import epic.Epic;
import epic.SubTask;
import epic.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

class HistoryManagerTest {
    TaskManager taskmanager;
    HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        taskmanager = Managers.getDefault();
        historyManager = taskmanager.getHistoryManager();
        taskmanager.createTask(new Task("1-5", "12345",
                LocalDateTime.of(2023, Month.JUNE, 1, 0, 0), Duration.ofMinutes(10)));
        taskmanager.createTask(new Task("5-10", "5678910",
                LocalDateTime.of(2023, Month.JUNE, 2, 0, 0), Duration.ofMinutes(10)));
        taskmanager.createEpic(new Epic("drive", "moto"));
        taskmanager.createEpic(new Epic("learning", "Java course"));
        taskmanager.createSubTask(new SubTask("sprint 3", "OOP", 3,
                LocalDateTime.of(2023, Month.JUNE, 5, 0, 0), Duration.ofMinutes(10)));
        taskmanager.createSubTask(new SubTask("learning", "Java course", 3,
                LocalDateTime.of(2023, Month.JUNE, 6, 0, 0), Duration.ofMinutes(10)));
        taskmanager.createSubTask(new SubTask("learninffsfsg", "Java coursedfd", 4,
                LocalDateTime.of(2023, Month.JUNE, 7, 0, 0), Duration.ofMinutes(10)));
        taskmanager.createSubTask(new SubTask("sfgsgs", "Java dsff", 4,
                LocalDateTime.of(2023, Month.JUNE, 8, 0, 0), Duration.ofMinutes(10)));
        taskmanager.getTaskById(1);
        taskmanager.getTaskById(2);
        taskmanager.getEpicById(3);
        taskmanager.getEpicById(4);
        taskmanager.getSubTaskById(5);
        taskmanager.getSubTaskById(6);
    }

    @AfterEach
    public void clear() {
        taskmanager.removeAllTasks();
        taskmanager.removeAllEpics();
        taskmanager.removeAllSubTasks();
        historyManager.getHistoryMap().clear();
        taskmanager.getPrioritizedSet().clear();
        InMemoryTaskManager.setId(0);
    }

    @Test
    void addWithEmptyHistory() {
        clear();
        Task task = new Task("name", "description", LocalDateTime.now(), Duration.ofMinutes(10));
        taskmanager.createTask(task);
        taskmanager.getHistoryManager().add(task);
        assertFalse(historyManager.getHistoryMap().isEmpty());
        assertEquals(task, historyManager.getHistoryMap().get(task.getId()).data);
        assertEquals(taskmanager.getTaskById(1), historyManager.getHistoryMap().get(1).data);
    }

    @Test
    void addTwice() {
        Task task = new Task("name", "description", LocalDateTime.now(), Duration.ofMinutes(10));
        taskmanager.createTask(task);
        taskmanager.getHistoryManager().add(task);
        int size = historyManager.getHistoryMap().size();
        taskmanager.getHistoryManager().add(task);
        int size2 = historyManager.getHistoryMap().size();
         assertEquals(size, size2);
         assertFalse(historyManager.getHistoryMap().isEmpty());
         assertEquals(task, historyManager.getHistoryMap().get(task.getId()).data);
         assertEquals(taskmanager.getTaskById(1), historyManager.getHistoryMap().get(1).data);
    }

    @Test
    void removeWithEmptyHistory() {
        clear();
        historyManager.remove(1);
        assertFalse(historyManager.getHistoryMap().containsKey(1));
    }

    @Test
    void removeTwice() {
        historyManager.remove(1);
        historyManager.remove(1);
        assertFalse(historyManager.getHistoryMap().containsKey(1));
    }

    @Test
    void removeFirst() {
        historyManager.remove(1);
        assertFalse(historyManager.getHistoryMap().containsKey(1));
        assertFalse(historyManager.getHistoryMap().isEmpty());
    }

    @Test
    void removeMedium() {
        historyManager.remove(3);
        assertFalse(historyManager.getHistoryMap().containsKey(3));
        assertFalse(historyManager.getHistoryMap().isEmpty());
    }

    @Test
    void removeLast() {
        historyManager.remove(6);
        assertFalse(historyManager.getHistoryMap().containsKey(6));
        assertFalse(historyManager.getHistoryMap().isEmpty());
    }

    @Test
    void getHistoryListWithEmptyHistory() {
        clear();
        List<Task> historyList = historyManager.getHistoryList();
        assertNotNull(historyList);
        assertTrue(historyList.isEmpty());
    }

    @Test
    void getHistoryList() {
        List<Task> historyList = historyManager.getHistoryList();
        assertNotNull(historyList);
        assertFalse(historyList.isEmpty());
    }
}