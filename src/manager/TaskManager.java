package manager;

import epic.Epic;
import epic.SubTask;
import epic.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager {

    Map<Integer, Task> getTaskMap();

    Map<Integer, Epic> getEpicMap();

    Map<Integer, SubTask> getSubTaskMap();


    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);


    void updateTask(Task task);


    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void updateStatus(Epic epic);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    List<SubTask> getEpicsSubtasks(int id);

    HistoryManager getHistoryManager();

    Set<Task> getPrioritizedSet();

}
