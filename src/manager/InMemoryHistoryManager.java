package manager;

import epic.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> historyMap;
    private Node<Task> head;
    private Node<Task> tail;


    InMemoryHistoryManager() {
        head = null;
        tail = null;
        historyMap = new HashMap<>();
    }

    @Override
    public Map<Integer, Node<Task>> getHistoryMap() {
        return historyMap;
    }

    @Override
    public void add(Task task) {
        int taskId = task.getId();
        if (historyMap.containsKey(taskId)) {
            removeNode(historyMap.get(taskId));
        }
        historyMap.put(taskId, linkLast(task));
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistoryList() {
        return getTasks();
    }

    private Node<Task> linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    private List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        if (!historyMap.isEmpty()) {
            if (head != null) {
                Node<Task> node = head;
                while (true) {
                    taskList.add(node.data);
                    if (node.next == null) {
                        break;
                    }
                    node = node.next;
                }
            }
        }
        return taskList;

    }

    private void removeNode(Node<Task> node) {
        final Node<Task> prev = node.prev;
        final Node<Task> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }
        node.data = null;
    }
}
