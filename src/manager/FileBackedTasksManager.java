package manager;

import epic.Epic;
import epic.Status;
import epic.SubTask;
import epic.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final Path backedTasks = Paths.get("src/files/", "Backed_tasks.csv");

    public FileBackedTasksManager() {
        super();
        if (!Files.exists(backedTasks)) {
            try {
                Files.createFile(backedTasks);
            } catch (IOException e) {
                System.out.println("Ошибка" + e.getMessage());
            }
        }
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateStatus(Epic epic) {
        super.updateStatus(epic);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    public static FileBackedTasksManager loadFromFile(FileBackedTasksManager backedManager) {
        backedManager.loadTasks();
        try (BufferedReader reader = new BufferedReader(new FileReader(backedTasks.toString()))) {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.isBlank()) {
                    break;
                }
            }
            String line = reader.readLine();
            if (line != null) {
                List<Integer> history = historyFromString(line);
                for (Integer id : history) {
                    if (taskMap.containsKey(id)) {
                        backedManager.historyManager.add(taskMap.get(id));
                    } else if (epicMap.containsKey(id)) {
                        backedManager.historyManager.add(epicMap.get(id));
                    } else if (subTaskMap.containsKey(id)) {
                        backedManager.historyManager.add(subTaskMap.get(id));
                    } else {
                        System.out.println("Ошибка при загрузке истории из файла || история пуста");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
        return backedManager;
    }

    private static String toString(HistoryManager manager) {
        List<Task> taskList = manager.getHistoryList();
        StringBuilder stringBuilder = new StringBuilder();
        if (taskList.size() != 0) {
            for (Task task : taskList) {
                stringBuilder.append(task.getId()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        }
        return stringBuilder.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();
        String[] line = value.split(",");
        for (String s : line) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(backedTasks.toString())) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                fileWriter.write(task.toString() + "\n");
            }
            for (Epic epic : getAllEpics()) {
                fileWriter.write(epic.toString() + "\n");
            }
            for (SubTask subTask : getAllSubTasks()) {
                fileWriter.write(subTask.toString() + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(toString(historyManager));

        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл");
        } catch (IOException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

    private Task taskFromString(String value) {
        String[] line = value.split(",");
        switch (line[1]) {
            case "TASK" -> {
                Task task = new Task();
                task.setId(Integer.parseInt(line[0]));
                task.setName(line[2]);
                task.setStatus(Status.valueOf(line[3]));
                task.setDescription(line[4]);
                if (line[5].equals("null")) {
                    task.setStartTime(null);
                } else {
                    task.setStartTime(LocalDateTime.parse(line[5]));
                }
                if (line[6].equals("null")) {
                    task.setDuration(null);
                } else {
                    task.setDuration(Duration.parse(line[6]));
                }
                return task;
            }
            case "EPIC" -> {
                Epic epic = new Epic();
                epic.setId(Integer.parseInt(line[0]));
                epic.setName(line[2]);
                epic.setStatus(Status.valueOf(line[3]));
                epic.setDescription(line[4]);
                if (line[5].equals("null")) {
                    epic.setStartTime(null);
                } else {
                    epic.setStartTime(LocalDateTime.parse(line[5]));
                }
                if (line[6].equals("null")) {
                    epic.setDuration(null);
                } else {
                    epic.setDuration(Duration.parse(line[6]));
                }
                return epic;
            }
            case "SUBTASK" -> {
                SubTask subTask = new SubTask();
                subTask.setId(Integer.parseInt(line[0]));
                subTask.setName(line[2]);
                subTask.setStatus(Status.valueOf(line[3]));
                subTask.setDescription(line[4]);
                subTask.setEpicId(Integer.parseInt(line[5]));
                if (line[6].equals("null")) {
                    subTask.setStartTime(null);
                } else {
                    subTask.setStartTime(LocalDateTime.parse(line[6]));
                }
                if (line[7].equals("null")) {
                    subTask.setDuration(null);
                } else {
                    subTask.setDuration(Duration.parse(line[7]));
                }
                return subTask;
            }
        }
        return null;
    }

    private void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(backedTasks.toString()))) {
            while (reader.ready()) {
                String s = reader.readLine();
                if (!s.isBlank() && Character.isDigit(s.charAt(0))) {
                    Task task = taskFromString(s);
                    if (task instanceof Epic epic) {
                        epicMap.put(epic.getId(), epic);
                    } else if (task instanceof SubTask subTask) {
                        subTaskMap.put(subTask.getId(), subTask);
                    } else if (task != null) {
                        taskMap.put(task.getId(), task);
                    }
                }
                if (s.isBlank()) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

}
