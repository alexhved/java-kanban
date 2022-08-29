package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    TaskManager taskManager;

    @Test
    void getDefault() {
        taskManager = Managers.getDefault();
        assertNotNull(taskManager);
        assertTrue(taskManager.getTaskMap().isEmpty());
        assertTrue(taskManager.getEpicMap().isEmpty());
        assertTrue(taskManager.getSubTaskMap().isEmpty());
    }

    @Test
    void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
        assertTrue(historyManager.getHistoryMap().isEmpty());
    }

    @Test
    void getBacked() {
        taskManager = Managers.getBacked();
        assertNotNull(taskManager);
        assertTrue(taskManager.getTaskMap().isEmpty());
        assertTrue(taskManager.getEpicMap().isEmpty());
        assertTrue(taskManager.getSubTaskMap().isEmpty());
    }
    @Test
    public void getInMemoryManager() {
        taskManager = Managers.getInMemory();
        assertNotNull(taskManager);
        assertTrue(taskManager.getTaskMap().isEmpty());
        assertTrue(taskManager.getEpicMap().isEmpty());
        assertTrue(taskManager.getSubTaskMap().isEmpty());
        assertTrue(taskManager.getPrioritizedSet().isEmpty());
        assertTrue(taskManager.getHistoryManager().getHistoryMap().isEmpty());
    }
}