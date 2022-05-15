package taskmanager;

import taskmanager.task.*;
import taskmanager.manager.*;

public class Main {

    public static void main(String[] args) {
        Task task = new Task("1-5", "12345");
        Task task1 = new Task("5-10", "5678910");

        TaskManager inMemoryTaskManager = Managers.getDefault();


        System.out.println("create task");
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(task1);
        System.out.println(inMemoryTaskManager.getTaskMap());

        System.out.println("update task");
        task.setStatus(Status.DONE);
        inMemoryTaskManager.updateTask(task);
        System.out.println(inMemoryTaskManager.getTaskMap());

        System.out.println("create epic");
        Epic epic = new Epic("learning", "Java course");
        inMemoryTaskManager.createEpic(epic);
        System.out.println(inMemoryTaskManager.getEpicMap());

        System.out.println("update epic");
        epic.setName("обучение");
        epic.setDescription("Джава курс");
        inMemoryTaskManager.updateEpic(epic);
        System.out.println(inMemoryTaskManager.getEpicMap());

        System.out.println("create subtask");
        SubTask subTask = new SubTask("sprint 3", "OOP", epic.getId());
        inMemoryTaskManager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("sprint 4", "final sprint", epic.getId());
        inMemoryTaskManager.createSubTask(subTask1);
        System.out.println(inMemoryTaskManager.getEpicMap());
        System.out.println(inMemoryTaskManager.getSubTaskMap());

        System.out.println("update subtask");
        subTask.setStatus(Status.NEW);
        inMemoryTaskManager.updateSubTask(subTask);
        subTask1.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubTask(subTask1);
        System.out.println(inMemoryTaskManager.getEpicMap());
        System.out.println(inMemoryTaskManager.getSubTaskMap());

        System.out.println("history------");
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);


        for (Task task2 : inMemoryTaskManager.getHistoryManager().getHistory()) {
            System.out.println(task2);
        }
        System.out.println(inMemoryTaskManager.getHistoryManager().getHistory().size());

        System.out.println();

        System.out.println("remove");
        inMemoryTaskManager.removeTaskById(1);
        System.out.println(inMemoryTaskManager.getTaskMap());
        inMemoryTaskManager.removeSubTaskById(5);
        System.out.println(inMemoryTaskManager.getEpicMap());
        System.out.println(inMemoryTaskManager.getSubTaskMap());
        inMemoryTaskManager.removeEpicById(3);
        System.out.println(inMemoryTaskManager.getEpicMap());
        System.out.println(inMemoryTaskManager.getSubTaskMap());
        inMemoryTaskManager.removeAllTasks();
        System.out.println(inMemoryTaskManager.getTaskMap());

    }
}
