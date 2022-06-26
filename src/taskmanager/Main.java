package taskmanager;

import taskmanager.task.*;
import taskmanager.manager.*;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        FileBackedTasksManager backedManager = Managers.getBacked();

        Task task = new Task("1-5", "12345");
        Task task1 = new Task("5-10", "5678910");
        System.out.println("create task");
        backedManager.createTask(task);
        backedManager.createTask(task1);
        //System.out.println(taskManager.getTaskMap());

        System.out.println("update task");
        task.setStatus(Status.DONE);
        backedManager.updateTask(task);
        //System.out.println(taskManager.getTaskMap());

        System.out.println("create epic");
        Epic epic1 = new Epic("drive", "moto");
        Epic epic = new Epic("learning", "Java course");
        backedManager.createEpic(epic);
        backedManager.createEpic(epic1);
        //System.out.println(taskManager.getEpicMap());

        System.out.println("update epic");
        epic.setName("обучение");
        epic.setDescription("Джава курс");
        backedManager.updateEpic(epic);
        //System.out.println(taskManager.getEpicMap());

        System.out.println("create subtask");
        SubTask subTask = new SubTask("sprint 3", "OOP", epic.getId());
        backedManager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("sprint 4", "final sprint", epic.getId());
        backedManager.createSubTask(subTask1);
        //System.out.println(taskManager.getEpicMap());
        //System.out.println(taskManager.getSubTaskMap());

        System.out.println("update subtask");
        subTask.setStatus(Status.NEW);
        backedManager.updateSubTask(subTask);
        subTask1.setStatus(Status.DONE);
        backedManager.updateSubTask(subTask1);
        //System.out.println(taskManager.getEpicMap());
        //System.out.println(taskManager.getSubTaskMap());

        System.out.println("history------");
        backedManager.getTaskById(1);
        backedManager.getTaskById(2);
        backedManager.getEpicById(3);
        backedManager.getEpicById(4);
        backedManager.getSubTaskById(5);
        backedManager.getSubTaskById(6);
        backedManager.getTaskById(1);
        backedManager.getEpicById(3);
        System.out.println("-----maps-----");
        System.out.println(backedManager.getTaskMap());
        System.out.println(backedManager.getEpicMap());
        System.out.println(backedManager.getSubTaskMap());
        System.out.println("-----history------");
        System.out.println(backedManager.getHistoryManager().getHistoryList());

        backedManager = FileBackedTasksManager.loadFromFile(backedManager);

        System.out.println("----upload----");
        System.out.println("-----maps-----");
        System.out.println(backedManager.getTaskMap());
        System.out.println(backedManager.getEpicMap());
        System.out.println(backedManager.getSubTaskMap());
        System.out.println("history------");
        System.out.println(backedManager.getHistoryManager().getHistoryList());

//код ниже можно раскомментировать для теста

        /*System.out.println("remove");
        backedManager.removeTaskById(1);
        backedManager.removeTaskById(2);
        System.out.println(backedManager.getTaskMap());
        backedManager.removeSubTaskById(5);
        backedManager.removeSubTaskById(6);
        System.out.println(backedManager.getEpicMap());
        System.out.println(backedManager.getSubTaskMap());
        backedManager.removeEpicById(3);
        backedManager.removeEpicById(4);
        System.out.println(backedManager.getEpicMap());
        System.out.println(backedManager.getSubTaskMap());


        System.out.println("-----maps-----");
        System.out.println(backedManager.getTaskMap());
        System.out.println(backedManager.getEpicMap());
        System.out.println(backedManager.getSubTaskMap());
        System.out.println("history------");
        System.out.println(backedManager.getHistoryManager().getHistoryList());

        backedManager = FileBackedTasksManager.loadFromFile(backedManager);

        System.out.println("----upload----");
        System.out.println("-----maps-----");
        System.out.println(backedManager.getTaskMap());
        System.out.println(backedManager.getEpicMap());
        System.out.println(backedManager.getSubTaskMap());
        System.out.println("history------");
        System.out.println(backedManager.getHistoryManager().getHistoryList());*/
    }
}
