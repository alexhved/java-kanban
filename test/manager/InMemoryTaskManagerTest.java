package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import epic.Epic;
import epic.SubTask;
import epic.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskmanager = Managers.getDefault();
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
}
