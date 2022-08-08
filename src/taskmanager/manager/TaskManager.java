package taskmanager.manager;

import taskmanager.task.Epic;
import taskmanager.task.SubTask;
import taskmanager.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    void removeTaskById(int id);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);


    void updateTask(Task task);


    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void updateStatus(Epic epic);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    List<SubTask> getEpicsSubtasks(int id);

    HistoryManager getHistoryManager();

}
