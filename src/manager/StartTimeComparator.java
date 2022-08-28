package manager;

import epic.Task;

import java.util.Comparator;

public class StartTimeComparator implements Comparator<Task> {

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
}
