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


public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void setUp() {
        taskmanager = Managers.getBacked();
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
        taskmanager.getPrioritizedSet().clear();
        InMemoryTaskManager.setId(0);
        taskmanager.historyManager.remove(1);
        taskmanager.historyManager.remove(2);
        taskmanager.historyManager.remove(3);
        taskmanager.historyManager.remove(4);
        taskmanager.historyManager.remove(5);
        taskmanager.historyManager.remove(6);
    }

    @Test
    public void loadFromFileWithEmpty() {
        clear();
        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(taskmanager);
        assertTrue(tasksManager2.getTaskMap().isEmpty());
        assertTrue(tasksManager2.getEpicMap().isEmpty());
        assertTrue(tasksManager2.getSubTaskMap().isEmpty());
    }

    @Test
    public void loadFromFile() {
        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(taskmanager);
        assertFalse(tasksManager2.getTaskMap().isEmpty());
        assertFalse(tasksManager2.getEpicMap().isEmpty());
        assertFalse(tasksManager2.getSubTaskMap().isEmpty());
        assertEquals(taskmanager.getEpicMap(), tasksManager2.getEpicMap());
        assertEquals(taskmanager.getTaskMap(), tasksManager2.getTaskMap());
        assertEquals(taskmanager.getSubTaskMap(), tasksManager2.getSubTaskMap());
    }

    @Test
    public void loadFromFileEpicWithoutSubtasks() {
        taskmanager.removeSubTaskById(5);
        taskmanager.removeSubTaskById(6);
        taskmanager.removeSubTaskById(7);
        taskmanager.removeSubTaskById(8);
        assertTrue(taskmanager.getEpicsSubtasks(3).isEmpty());
        assertTrue(taskmanager.getEpicsSubtasks(4).isEmpty());

        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(taskmanager);
        assertFalse(tasksManager2.getTaskMap().isEmpty());
        assertFalse(tasksManager2.getEpicMap().isEmpty());
        assertTrue(tasksManager2.getSubTaskMap().isEmpty());
        assertEquals(taskmanager.getEpicMap(), tasksManager2.getEpicMap());
        assertEquals(taskmanager.getTaskMap(), tasksManager2.getTaskMap());
        assertEquals(taskmanager.getSubTaskMap(), tasksManager2.getSubTaskMap());
    }
}
