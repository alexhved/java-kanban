package manager;

import task.*;

import java.time.Duration;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;
    public static final Map<Integer, Task> taskMap = new HashMap<>();
    public static final Map<Integer, Epic> epicMap = new HashMap<>();
    public static final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    protected HistoryManager historyManager;

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
    }

    public static void setId(int id) {
        InMemoryTaskManager.id = id;
    }

    @Override
    public String toString() {
        return super.toString();
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
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTaskMap.values());
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
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTaskMap.get(id);
        if (subTask != null) {
            historyManager.add(subTask);
        }
        return subTask;
    }

    @Override
    public void removeTaskById(int id) {
        if (historyManager.getHistoryMap().containsKey(id)) {
            historyManager.remove(id);
        }
        taskMap.remove(id);
    }

    @Override
    public void createTask(Task task) {
        if (validateTask(task)) {
            task.setStatus(Status.NEW);
            task.setId(++id);
            taskMap.put(id, task);
        }
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
        if (validateTask(subTask)) {
            if (epicMap.containsKey(subTask.getEpicId())) {
                Epic epic = epicMap.get(subTask.getEpicId());
                epic.getSubTasksId().add(subTask.getId());
                epicMap.put(subTask.getEpicId(), epic);
                subTaskMap.put(subTask.getId(), subTask);
            } else {
                System.out.println("Нет подходящего эпика для этой подзадачи");
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        if (validateTask(task)) {
            if (taskMap.containsKey(task.getId())) {
                taskMap.put(task.getId(), task);
            } else {
                System.out.println("Неверный id");
            }
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
        if (validateTask(subTask)) {
            if (epicMap.containsKey(subTask.getEpicId())) {
                Epic epic = epicMap.get(subTask.getEpicId());
                updateStatus(epic);
                epicMap.put(subTask.getEpicId(), epic);
                subTaskMap.put(subTask.getId(), subTask);
            } else {
                System.out.println("Неверный id");
            }
        }
    }

    @Override
    public void updateStatus(Epic epic) {
        if (subTaskMap.isEmpty() || epic == null) {
            return;
        }
        int countNew = 0;
        int countDone = 0;
        for (int id : epic.getSubTasksId()) {
            if (subTaskMap.get(id).getStatus() == Status.NEW) {
                countNew++;
            } else if (subTaskMap.get(id).getStatus() == Status.DONE) {
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
        updateEpicTime(epic);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            for (int item : epic.getSubTasksId()) {
                subTaskMap.remove(item);
                historyManager.remove(item);
            }
            epicMap.remove(id);
            if (historyManager.getHistoryMap().containsKey(id)) {
                historyManager.remove(id);
            }
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        SubTask subTask = subTaskMap.get(id);
        if (subTask != null) {
            Epic epic = epicMap.get(subTask.getEpicId());
            subTaskMap.remove(id);
            historyManager.remove(id);
            epic.getSubTasksId().remove(Integer.valueOf(id));
            updateStatus(epic);
            epicMap.put(subTask.getEpicId(), epic);
        }
    }

    @Override
    public List<SubTask> getEpicsSubtasks(int id) {
        ArrayList<SubTask> subTaskList = new ArrayList<>();
        Epic epic = epicMap.get(id);
        if (epic != null) {
            for (int item : epic.getSubTasksId()) {
                subTaskList.add(subTaskMap.get(item));
            }
        }
        return subTaskList;
    }

    private void updateEpicTime(Epic epic) {
        if (epic.getSubTasksId().isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
            return;
        }

        for (Task task : getPrioritizedSet()) {
            if (task instanceof SubTask) {
                epic.setStartTime(task.getStartTime());
                break;
            }
        }

        Duration epicDuration = Duration.ZERO;
        for (Task task : getPrioritizedSet()) {
            if (task instanceof SubTask) {
                epicDuration = epicDuration.plus(task.getDuration());
            }
        }

        epic.setDuration(epicDuration);
        epic.setEndTime(epic.getEndTime());
    }


    public Set<Task> getPrioritizedSet() {
        Set<Task> priorytizedSet = new TreeSet<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                if (o1.getStartTime() != null && o2.getStartTime() != null) {
                    return o1.getStartTime().compareTo(o2.getStartTime());
                } else if (o1.getStartTime() != null && o2.getStartTime() == null) {
                    return -1;
                } else if (o1.getStartTime() == null && o2.getStartTime() != null) {
                    return 1;
                } else {
                    return 1;
                }
            }
        });
        List<Task> allTask = new ArrayList<>();
        allTask.addAll(taskMap.values());
        allTask.addAll(epicMap.values());
        allTask.addAll(subTaskMap.values());
        priorytizedSet.addAll(allTask);
        return priorytizedSet;
    }

    private boolean validateTask(Task newTask) {
        Set<Task> taskSet = getPrioritizedSet();
        try {
            for (Task task : taskSet) {
                if (task.getStartTime() != null && task.getDuration() != null) {
                    if (task.getStartTime().isEqual(newTask.getStartTime())
                            || task.getEndTime().isEqual(newTask.getEndTime())) {
                        throw new ValidateException("Одновремннно может выполняться только одна задача");
                    }
                    if (task.getStartTime().isBefore(newTask.getStartTime())
                            && task.getEndTime().isAfter(newTask.getStartTime())
                            || task.getStartTime().isBefore(newTask.getEndTime())
                            && task.getEndTime().isAfter(newTask.getEndTime())) {
                        throw new ValidateException("Одновремннно может выполняться только одна задача");
                    }
                }
            }
        } catch (ValidateException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}

