package taskmanager.manager;

import taskmanager.task.Epic;
import taskmanager.task.SubTask;
import taskmanager.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    HashMap<Integer, Task> getTaskMap();

    HashMap<Integer, Epic> getEpicMap();

    HashMap<Integer, SubTask> getSubTaskMap();


    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTasks();

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

    ArrayList<SubTask> getEpicsSubtasks(int id);

    HistoryManager getHistoryManager();

}
