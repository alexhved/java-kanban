package manager;

import epic.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistoryList();

    Map<Integer, Node<Task>> getHistoryMap();
}
