package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

class HistoryManagerTest {
    TaskManager taskmanager;
    HistoryManager historyManager;

    @BeforeEach
    public void setUp () {
        taskmanager = Managers.getDefault();
        historyManager = taskmanager.getHistoryManager();
        taskmanager.createTask(new Task("1-5", "12345"));
        taskmanager.createTask(new Task("5-10", "5678910"));
        taskmanager.createEpic(new Epic("drive", "moto"));
        taskmanager.createEpic(new Epic("learning", "Java course"));
        taskmanager.createSubTask(new SubTask("sprint 3", "OOP", 3));
        taskmanager.createSubTask(new SubTask("learning", "Java course", 3));
        taskmanager.createSubTask(new SubTask("learninffsfsg", "Java coursedfd", 4));
        taskmanager.createSubTask(new SubTask("sfgsgs", "Java dsff", 4));
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
        InMemoryTaskManager.setId(0);
    }
    @Test
    void addWithEmptyHistory() {
        clear();
        Task task = new Task("name", "description");
        taskmanager.createTask(task);
        taskmanager.getHistoryManager().add(task);
        Assertions.assertFalse(historyManager.getHistoryMap().isEmpty());
        Assertions.assertEquals(task, historyManager.getHistoryMap().get(task.getId()).data);
        Assertions.assertEquals(taskmanager.getTaskById(1), historyManager.getHistoryMap().get(1).data);
    }
    @Test
    void addTwice() {
        Task task = new Task("name", "description");
        taskmanager.createTask(task);
        taskmanager.getHistoryManager().add(task);
        int size = historyManager.getHistoryMap().size();
        taskmanager.getHistoryManager().add(task);
        int size2 = historyManager.getHistoryMap().size();
        Assertions.assertEquals(size, size2);
        Assertions.assertFalse(historyManager.getHistoryMap().isEmpty());
        Assertions.assertEquals(task, historyManager.getHistoryMap().get(task.getId()).data);
        Assertions.assertEquals(taskmanager.getTaskById(1), historyManager.getHistoryMap().get(1).data);
    }
    @Test
    void removeWithEmptyHistory() {
        clear();
        historyManager.remove(1);
        Assertions.assertFalse(historyManager.getHistoryMap().containsKey(1));
    }
    @Test
    void removeTwice() {
        historyManager.remove(1);
        historyManager.remove(1);
        Assertions.assertFalse(historyManager.getHistoryMap().containsKey(1));
    }
    @Test
    void removeFirst() {
        historyManager.remove(1);
        Assertions.assertFalse(historyManager.getHistoryMap().containsKey(1));
        Assertions.assertFalse(historyManager.getHistoryMap().isEmpty());
    }
    @Test
    void removeMedium() {
        historyManager.remove(3);
        Assertions.assertFalse(historyManager.getHistoryMap().containsKey(3));
        Assertions.assertFalse(historyManager.getHistoryMap().isEmpty());
    }
    @Test
    void removeLast() {
        historyManager.remove(6);
        Assertions.assertFalse(historyManager.getHistoryMap().containsKey(6));
        Assertions.assertFalse(historyManager.getHistoryMap().isEmpty());
    }
    @Test
    void getHistoryListWithEmptyHistory() {
        clear();
        List<Task> historyList = historyManager.getHistoryList();
        Assertions.assertNotNull(historyList);
        Assertions.assertTrue(historyList.isEmpty());
    }
    @Test
    void getHistoryList() {
        List<Task> historyList = historyManager.getHistoryList();
        Assertions.assertNotNull(historyList);
        Assertions.assertFalse(historyList.isEmpty());
    }
}