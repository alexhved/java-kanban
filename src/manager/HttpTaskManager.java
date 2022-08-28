package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.KVTaskClient;
import epic.Epic;
import epic.SubTask;
import epic.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private final Gson gson;

    public HttpTaskManager(String url) {
        kvTaskClient = new KVTaskClient(url);
        gson = Managers.getGson();
    }

    public void load() {
        String tasks = kvTaskClient.load("tasks");
        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> taskList = gson.fromJson(tasks, taskType);
        taskList.forEach(task -> taskMap.put(task.getId(), task));

        String epics = kvTaskClient.load("epics");
        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> epicList = gson.fromJson(epics, epicType);
        epicList.forEach(epic -> epicMap.put(epic.getId(), epic));

        String subtasks = kvTaskClient.load("subtasks");
        Type subtaskType = new TypeToken<ArrayList<SubTask>>() {
        }.getType();
        List<SubTask> subTaskList = gson.fromJson(subtasks, subtaskType);
        subTaskList.forEach(subTask -> subTaskMap.put(subTask.getId(), subTask));

        prioritizedSet.addAll(taskList);
        prioritizedSet.addAll(epicList);
        prioritizedSet.addAll(subTaskList);

        Type listInteger = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        String history = kvTaskClient.load("history");
        List<Integer> historyIds = gson.fromJson(history, listInteger);
        for (Task task : prioritizedSet) {
            if (historyIds.contains(task.getId())) {
                getHistoryManager().add(task);
            }
        }

        int actualId = prioritizedSet.size();
        setId(actualId);
    }

    public void save() {
        String tasks = gson.toJson(getAllTasks());
        kvTaskClient.put("tasks", tasks);

        String epics = gson.toJson(getAllEpics());
        kvTaskClient.put("epics", epics);

        String subtasks = gson.toJson(getAllSubTasks());
        kvTaskClient.put("subtasks", subtasks);

        List<Integer> historyList = getHistoryManager().getHistoryList().stream()
                .map(Task::getId)
                .toList();
        String history = gson.toJson(historyList);
        kvTaskClient.put("history", history);
    }
}
