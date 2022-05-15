package taskmanager.manager;

import taskmanager.task.*;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;

    private static HashMap<Integer, Task> taskMap = new HashMap<>();
    private static HashMap<Integer, Epic> epicMap = new HashMap<>();
    private static HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private HistoryManager historyManager;

    InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }


    @Override
    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    @Override
    public HashMap<Integer, Epic> getEpicMap() {
        return epicMap;
    }

    @Override
    public HashMap<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }


    @Override
    public ArrayList<Task> getAllTasks() {
        return (ArrayList<Task>) taskMap.values();
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return (ArrayList<Epic>) epicMap.values();
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return (ArrayList<SubTask>) subTaskMap.values();
    }

    @Override
    public void removeAllTasks() {
        taskMap.clear();
    }

    @Override
    public void removeAllEpics() {
        epicMap.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTaskMap.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = taskMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTaskMap.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public void removeTaskById(int id) {
        taskMap.remove(id);
    }

    @Override
    public void createTask(Task task) {
        id++;
        task.setStatus(Status.NEW);
        task.setId(id);
        taskMap.put(id, task);
    }

    @Override
    public void createEpic(Epic epic) {
        id++;
        epic.setStatus(Status.NEW);
        epic.setId(id);
        epicMap.put(id, epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        id++;
        subTask.setId(id);
        subTask.setStatus(Status.NEW);

        if (epicMap.containsKey(subTask.getEpicId())) {
            Epic epic = getEpicById(subTask.getEpicId());
            epic.subTasksId.add(subTask.getId());
            epicMap.put(subTask.getEpicId(), epic);
            subTaskMap.put(subTask.getId(), subTask);
        } else {
            System.out.println("Нет подходящего эпика для этой подзадачи");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (taskMap.containsKey(task.getId())) {
            taskMap.put(task.getId(), task);
        } else {
            System.out.println("Неверный id");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicMap.containsKey(epic.getId())) {
            updateStatus(epic);
            epicMap.put(epic.getId(), epic);
        } else {
            System.out.println("Неверный id");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (epicMap.containsKey(subTask.getEpicId())) {
            Epic epic = getEpicById(subTask.getEpicId());
            updateStatus(epic);
            epicMap.put(subTask.getEpicId(), epic);
            subTaskMap.put(subTask.getId(), subTask);
        } else {
            System.out.println("Неверный id");
        }
    }

    @Override
    public void updateStatus(Epic epic) {
        if (subTaskMap.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int countNew = 0;
        int countDone = 0;
        for (int id : epic.subTasksId) {
            if (getSubTaskById(id).getStatus() == Status.NEW) {
                countNew++;
            } else if (getSubTaskById(id).getStatus() == Status.DONE) {
                countDone++;
            }
        }
        if (countNew == epic.subTasksId.size()) {
            epic.setStatus(Status.NEW);
        } else if (countDone == epic.subTasksId.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = getEpicById(id);
        for (int item : epic.subTasksId) {
            subTaskMap.remove(item);
        }
        epicMap.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        SubTask subTask = getSubTaskById(id);
        Epic epic = getEpicById(subTask.getEpicId());
        subTaskMap.remove(id);
        epic.subTasksId.remove(Integer.valueOf(id));
        updateStatus(epic);
        epicMap.put(subTask.getEpicId(), epic);
    }

    @Override
    public ArrayList<SubTask> getEpicsSubtasks(int id) {
        ArrayList<SubTask> subTaskList = new ArrayList<>();
        Epic epic = getEpicById(id);
        for (int item : epic.subTasksId) {
            subTaskList.add(subTaskMap.get(item));
        }
        return subTaskList;
    }
}
