package taskmanager.manager;

import taskmanager.task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;

    private static final Map<Integer, Task> taskMap = new HashMap<>();
    private static final Map<Integer, Epic> epicMap = new HashMap<>();
    private static final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    private final HistoryManager historyManager;

    InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    @Override
    public Map<Integer, Epic> getEpicMap() {
        return epicMap;
    }

    @Override
    public Map<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }


    @Override
    public List<Task> getAllTasks() {
        return (ArrayList<Task>) taskMap.values();
    }

    @Override
    public List<Epic> getAllEpics() {
        return (ArrayList<Epic>) epicMap.values();
    }

    @Override
    public List<SubTask> getAllSubTasks() {
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
        task.setStatus(Status.NEW);
        task.setId(++id);
        taskMap.put(id, task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setStatus(Status.NEW);
        epic.setId(++id);
        epicMap.put(id, epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setId(++id);
        subTask.setStatus(Status.NEW);

        if (epicMap.containsKey(subTask.getEpicId())) {
            Epic epic = getEpicById(subTask.getEpicId());
            epic.getSubTasksId().add(subTask.getId());
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
        for (int id : epic.getSubTasksId()) {
            if (getSubTaskById(id).getStatus() == Status.NEW) {
                countNew++;
            } else if (getSubTaskById(id).getStatus() == Status.DONE) {
                countDone++;
            }
        }
        if (countNew == epic.getSubTasksId().size()) {
            epic.setStatus(Status.NEW);
        } else if (countDone == epic.getSubTasksId().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = getEpicById(id);
        for (int item : epic.getSubTasksId()) {
            subTaskMap.remove(item);
        }
        epicMap.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        SubTask subTask = getSubTaskById(id);
        Epic epic = getEpicById(subTask.getEpicId());
        subTaskMap.remove(id);
        epic.getSubTasksId().remove(Integer.valueOf(id));
        updateStatus(epic);
        epicMap.put(subTask.getEpicId(), epic);
    }

    @Override
    public List<SubTask> getEpicsSubtasks(int id) {
        ArrayList<SubTask> subTaskList = new ArrayList<>();
        Epic epic = getEpicById(id);
        for (int item : epic.getSubTasksId()) {
            subTaskList.add(subTaskMap.get(item));
        }
        return subTaskList;
    }
}
