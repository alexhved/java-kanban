package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import task.Epic;
import task.SubTask;
import task.Task;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp () {
        taskmanager = Managers.getDefault();
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
        InMemoryTaskManager.setId(0);
        taskmanager.historyManager.remove(1);
        taskmanager.historyManager.remove(2);
        taskmanager.historyManager.remove(3);
        taskmanager.historyManager.remove(4);
        taskmanager.historyManager.remove(5);
        taskmanager.historyManager.remove(6);
    }
}
