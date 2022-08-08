package taskmanager.manager;

import taskmanager.task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;

    public static List<Task> allTask = new ArrayList<>();

    protected static final Map<Integer, Task> taskMap = new HashMap<>();
    protected static final Map<Integer, Epic> epicMap = new HashMap<>();
    public static final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    protected HistoryManager historyManager;

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
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
        historyManager.remove(id);
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
            Epic epic = epicMap.get(subTask.getEpicId());
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
            switch (task.getStatus()) {
                case NEW -> {
                    task.setStartTime(null);
                    task.setDuration(null);
                }
                case IN_PROGRESS -> task.setStartTime(LocalDateTime.now());

                case DONE -> {
                    if (task.getStartTime()!=null) {
                        task.setDuration(Duration.between(task.getStartTime(), LocalDateTime.now()));
                    }
                }
            }
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
            Epic epic = epicMap.get(subTask.getEpicId());
            switch (subTask.getStatus()) {
                case NEW -> {
                    subTask.setStartTime(null);
                    subTask.setDuration(null);
                }
                case IN_PROGRESS -> subTask.setStartTime(LocalDateTime.now());
                case DONE -> {
                    if (subTask.getStartTime()!=null) {
                        subTask.setDuration(Duration.between(subTask.getStartTime(), LocalDateTime.now()));
                    }
                }
            }
            updateEpicTime(epic);
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
            //epic.setStatus(Status.NEW);
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
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epicMap.get(id);
        for (int item : epic.getSubTasksId()) {
            subTaskMap.remove(item);
            historyManager.remove(item);
        }
        epicMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        SubTask subTask = subTaskMap.get(id);
        Epic epic = epicMap.get(subTask.getEpicId());
        subTaskMap.remove(id);
        historyManager.remove(id);
        epic.getSubTasksId().remove(Integer.valueOf(id));
        updateStatus(epic);
        epicMap.put(subTask.getEpicId(), epic);
    }

    @Override
    public List<SubTask> getEpicsSubtasks(int id) {
        ArrayList<SubTask> subTaskList = new ArrayList<>();
        Epic epic = epicMap.get(id);
        for (int item : epic.getSubTasksId()) {
            subTaskList.add(subTaskMap.get(item));
        }
        return subTaskList;
    }
    public void updateEpicTime(Epic epic) {
        if (epic.getSubTasksId().isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        }
        int countP =0;
        int countD = 0;
        for (int id : epic.getSubTasksId()) {
            if (subTaskMap.get(id).getStatus() == Status.IN_PROGRESS) {
                countP++;
            }
            if (subTaskMap.get(id).getStatus() == Status.DONE) {
                countD++;
            }
        }
        if (countP == 1) {
            epic.setStartTime(LocalDateTime.now());
        }
        if (countD == epic.getSubTasksId().size()) {
            epic.setDuration(Duration.between(epic.getStartTime(), LocalDateTime.now()));
        }
    }
    public static boolean getTaskLock() {
        allTask.addAll(taskMap.values());
        allTask.addAll(subTaskMap.values());
        return allTask.stream().anyMatch(task -> task.getStatus() == Status.IN_PROGRESS);
    }
    public static Set<Task> getPrioritizedSet() {
        Set<Task> priorytizedSet = new TreeSet<>((o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));
        priorytizedSet.addAll(allTask);
        return priorytizedSet;
    }
}
