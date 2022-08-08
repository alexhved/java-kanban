package taskmanager;

import taskmanager.task.*;
import taskmanager.manager.*;



public class Main {



    public static void checkTaskOnEquals(TaskManager taskManager, FileBackedTasksManager backedManager) {
        boolean isEqual = true;
        for (int i = 0; i<taskManager.getAllTasks().size(); i++) {
            if (!taskManager.getAllTasks().get(i).equals(backedManager.getAllTasks().get(i))) {
                isEqual = false;
                break;
            }
        }
        for (int i = 0; i<taskManager.getAllEpics().size(); i++) {
            if (!taskManager.getAllEpics().get(i).equals(backedManager.getAllEpics().get(i))) {
                isEqual = false;
                break;
            }
        }
        for (int i = 0; i<taskManager.getAllSubTasks().size(); i++) {
            if (!taskManager.getAllSubTasks().get(i).equals(backedManager.getAllSubTasks().get(i))) {
                isEqual = false;
                break;
            }
        }
        System.out.println("Соттветствие элементов двух разных менеджеров: "+isEqual);
    }

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("1-5", "12345");
        Task task1 = new Task("5-10", "5678910");
        System.out.println("create task");
        taskManager.createTask(task);
        taskManager.createTask(task1);
        //System.out.println("task starttime "+task.getStartTime());

        System.out.println("update task");
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        System.out.println("task start "+task.getStartTime());
        task.setStatus(Status.DONE);
        taskManager.updateTask(task);
        System.out.println("task endtime "+task.getEndTime());

        System.out.println("create epic");
        Epic epic1 = new Epic("drive", "moto");
        Epic epic = new Epic("learning", "Java course");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic1);

        System.out.println("update epic");
        epic.setName("обучение");
        epic.setDescription("Джава курс");
        taskManager.updateEpic(epic);

        System.out.println("create subtask");
        SubTask subTask = new SubTask("sprint 3", "OOP", epic.getId());
        taskManager.createSubTask(subTask);
        //System.out.println("subtask starttime "+subTask.getStartTime());
        SubTask subTask1 = new SubTask("sprint 4", "final sprint", epic.getId());
        taskManager.createSubTask(subTask1);

        System.out.println("update subtask");
        subTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask);
        System.out.println("subtask start time "+ subTask.getStartTime());
        System.out.println("epic start time "+epic.getStartTime());
        subTask.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask);
        System.out.println("subtask end time "+ subTask.getEndTime());
        subTask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask1);
        System.out.println("subtask1 start time "+ subTask1.getStartTime());
        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);
        System.out.println("subtask1 end time "+subTask1.getEndTime());
        System.out.println("epic end time "+epic.getEndTime());



        FileBackedTasksManager backedManager = Managers.getBacked();


        Task task_ = new Task("1-5", "12345");
        Task task1_ = new Task("5-10", "5678910");
        System.out.println("create task");
        backedManager.createTask(task_);
        backedManager.createTask(task1_);

        System.out.println("update task");
        task_.setStatus(Status.DONE);
        backedManager.updateTask(task_);

        System.out.println("create epic");
        Epic epic1_ = new Epic("drive", "moto");
        Epic epic_ = new Epic("learning", "Java course");
        backedManager.createEpic(epic_);
        backedManager.createEpic(epic1_);

        System.out.println("update epic");
        epic_.setName("обучение");
        epic_.setDescription("Джава курс");
        backedManager.updateEpic(epic_);

        System.out.println("create subtask");
        SubTask subTask_ = new SubTask("sprint 3", "OOP", epic_.getId());
        backedManager.createSubTask(subTask_);
        SubTask subTask1_ = new SubTask("sprint 4", "final sprint", epic_.getId());
        backedManager.createSubTask(subTask1_);

        System.out.println("update subtask");
        subTask_.setStatus(Status.NEW);
        backedManager.updateSubTask(subTask_);
        subTask1_.setStatus(Status.DONE);
        backedManager.updateSubTask(subTask1_);

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

        FileBackedTasksManager backedManager2 = FileBackedTasksManager.loadFromFile(backedManager);

        System.out.println("----upload----");
        System.out.println("-----maps-----");
        System.out.println(backedManager2.getTaskMap());
        System.out.println(backedManager2.getEpicMap());
        System.out.println(backedManager2.getSubTaskMap());
        System.out.println("history------");
        System.out.println(backedManager2.getHistoryManager().getHistoryList());

        System.out.println("----------------");
        checkTaskOnEquals(taskManager, backedManager2);
        System.out.println("----------------");

/*        System.out.println("remove");
        backedManager2.removeTaskById(1);
        backedManager2.removeTaskById(2);
        System.out.println(backedManager2.getTaskMap());
        backedManager2.removeSubTaskById(5);
        backedManager2.removeSubTaskById(6);
        System.out.println(backedManager2.getEpicMap());
        System.out.println(backedManager2.getSubTaskMap());
        backedManager2.removeEpicById(3);
        backedManager2.removeEpicById(4);
        System.out.println(backedManager2.getEpicMap());
        System.out.println(backedManager2.getSubTaskMap());


        System.out.println("-----maps-----");
        System.out.println(backedManager2.getTaskMap());
        System.out.println(backedManager2.getEpicMap());
        System.out.println(backedManager2.getSubTaskMap());
        System.out.println("history------");
        System.out.println(backedManager2.getHistoryManager().getHistoryList());*/


    }
}
