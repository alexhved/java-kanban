package taskmanager;

import taskmanager.task.*;
import taskmanager.manager.*;

public class Main {

    public static void main(String[] args) {
        Task task = new Task("1-5", "12345");
        Task task1 = new Task("5-10", "5678910");

        TaskManager taskManager = Managers.getDefault();

        System.out.println("create task");
        taskManager.createTask(task);
        taskManager.createTask(task1);
        System.out.println(taskManager.getTaskMap());

        System.out.println("update task");
        task.setStatus(Status.DONE);
        taskManager.updateTask(task);
        System.out.println(taskManager.getTaskMap());

        System.out.println("create epic");
        Epic epic1 = new Epic("drive", "moto");
        Epic epic = new Epic("learning", "Java course");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic1);
        System.out.println(taskManager.getEpicMap());

        System.out.println("update epic");
        epic.setName("обучение");
        epic.setDescription("Джава курс");
        taskManager.updateEpic(epic);
        System.out.println(taskManager.getEpicMap());

        System.out.println("create subtask");
        SubTask subTask = new SubTask("sprint 3", "OOP", epic.getId());
        taskManager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("sprint 4", "final sprint", epic.getId());
        taskManager.createSubTask(subTask1);
        System.out.println(taskManager.getEpicMap());
        System.out.println(taskManager.getSubTaskMap());

        System.out.println("update subtask");
        subTask.setStatus(Status.NEW);
        taskManager.updateSubTask(subTask);
        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);
        System.out.println(taskManager.getEpicMap());
        System.out.println(taskManager.getSubTaskMap());

        System.out.println("history------");
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);





        for (Task task2 : taskManager.getHistoryManager().getHistoryList()) {
            System.out.println(task2);
        }
        System.out.println(taskManager.getHistoryManager().getHistoryList().size());

        System.out.println();

        System.out.println("remove");
        taskManager.removeTaskById(1);
        taskManager.removeTaskById(2);
        /*System.out.println(taskManager.getTaskMap());*/
        taskManager.removeSubTaskById(5);
        /*System.out.println(taskManager.getEpicMap());
        System.out.println(taskManager.getSubTaskMap());*/
        taskManager.removeEpicById(3);
        taskManager.removeEpicById(4);
        /*System.out.println(taskManager.getEpicMap());
        System.out.println(taskManager.getSubTaskMap());*/

        System.out.println("history-------");



        for (Task element : taskManager.getHistoryManager().getHistoryList()) {
            System.out.println(element);
        }
    }
}
