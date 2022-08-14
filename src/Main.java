import manager.*;



public class Main {
    public static void checkTaskOnEquals(TaskManager taskManager, TaskManager backedManager) {
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

    }
}
