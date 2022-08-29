package manager;

import org.junit.jupiter.api.Test;
import epic.Epic;
import epic.Status;
import epic.SubTask;
import epic.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskmanager;

    public void clear() {
        taskmanager.removeAllTasks();
        taskmanager.removeAllEpics();
        taskmanager.removeAllSubTasks();
        InMemoryTaskManager.setId(0);
    }

    @Test
    public void ReturnTaskmap() {
        int size = 2;
        assertNotNull(taskmanager.getTaskMap());
        assertEquals(size, taskmanager.getTaskMap().size());
    }

    @Test
    public void ReturnTaskmapWithEmpty() {
        clear();
        assertNotNull(taskmanager.getTaskMap());
        assertEquals(0, taskmanager.getTaskMap().size());
    }

    @Test
    public void ReturnEpicmap() {
        int size = 2;
        assertNotNull(taskmanager.getEpicMap());
        assertEquals(size, taskmanager.getEpicMap().size());
    }

    @Test
    public void ReturnEpicmapWithEmpty() {
        clear();
        int size = 0;
        assertNotNull(taskmanager.getEpicMap());
        assertEquals(size, taskmanager.getEpicMap().size());
    }

    @Test
    public void ReturnSubTaskmap() {
        int size = 4;
        assertNotNull(taskmanager.getSubTaskMap());
        assertEquals(size, taskmanager.getSubTaskMap().size());
    }

    @Test
    public void ReturnSubTaskmapWithEmpty() {
        clear();
        int size = 0;
        assertNotNull(taskmanager.getSubTaskMap());
        assertEquals(size, taskmanager.getSubTaskMap().size());
    }

    @Test
    public void getAllTasks() {
        assertNotNull(taskmanager.getAllTasks());
    }

    @Test
    public void getAllTasksWithEmpty() {
        clear();
        assertEquals(taskmanager.getTaskMap().size(), taskmanager.getAllTasks().size());
        assertNotNull(taskmanager.getAllTasks());
    }

    @Test
    public void getAllEpics() {
        assertNotNull(taskmanager.getAllEpics());
        assertEquals(taskmanager.getEpicMap().size(), taskmanager.getAllEpics().size());
    }

    @Test
    public void getAllEpicsWithEmpty() {
        clear();
        assertNotNull(taskmanager.getAllEpics());
        assertEquals(taskmanager.getEpicMap().size(), taskmanager.getAllEpics().size());
    }

    @Test
    public void getAllSubTasks() {
        assertNotNull(taskmanager.getAllSubTasks());
        assertEquals(taskmanager.getSubTaskMap().size(), taskmanager.getAllSubTasks().size());
    }

    @Test
    public void getAllSubTasksWithEmpty() {
        clear();
        assertNotNull(taskmanager.getAllSubTasks());
        assertEquals(taskmanager.getSubTaskMap().size(), taskmanager.getAllSubTasks().size());
    }

    @Test
    public void RemoveTasks() {
        taskmanager.removeAllTasks();
        assertEquals(0, taskmanager.getTaskMap().size());
    }

    @Test
    public void RemoveTasksWithEmpty() {
        clear();
        taskmanager.removeAllTasks();
        assertEquals(0, taskmanager.getTaskMap().size());
        assertTrue(taskmanager.getPrioritizedSet().isEmpty());

    }

    @Test
    public void RemoveEpics() {
        taskmanager.removeAllEpics();
        assertEquals(0, taskmanager.getEpicMap().size());
    }

    @Test
    public void RemoveEpicsWithEmpty() {
        clear();
        taskmanager.removeAllEpics();
        assertEquals(0, taskmanager.getEpicMap().size());
    }

    @Test
    public void RemoveSubtasks() {
        taskmanager.removeAllSubTasks();
        assertEquals(0, taskmanager.getSubTaskMap().size());
    }

    @Test
    public void RemoveSubtasksWithEmpty() {
        taskmanager.removeAllSubTasks();
        assertEquals(0, taskmanager.getSubTaskMap().size());
    }

    @Test
    public void ReturnTaskById() {
        int id = 1;
        assertNotNull(taskmanager.getTaskById(id));
        assertEquals(id, taskmanager.getTaskById(id).getId());
    }

    @Test
    public void ReturnTaskByIdWithEmpty() {
        clear();
        int id = 1;
        assertNull(taskmanager.getTaskById(id));
    }

    @Test
    public void returnEpicById() {
        int id = 3;
        assertNotNull(taskmanager.getEpicById(id));
        assertEquals(id, taskmanager.getEpicById(id).getId());
    }

    @Test
    public void returnEpicByIdWithEmpty() {
        clear();
        int id = 3;
        assertNull(taskmanager.getEpicById(id));
    }

    @Test
    public void returnSubTaskById() {
        int id = 5;
        assertNotNull(taskmanager.getSubTaskById(id));
        assertEquals(id, taskmanager.getSubTaskById(id).getId());
    }

    @Test
    public void returnSubTaskByIdWithEmpty() {
        clear();
        int id = 5;
        assertNull(taskmanager.getSubTaskById(id));
    }

    @Test
    public void createTask() {
        Task task = new Task("name", "description", LocalDateTime.now(), Duration.ofMinutes(10));
        taskmanager.createTask(task);
        int id = task.getId();
        Task equalTask = taskmanager.getTaskById(id);
        assertNotNull(equalTask);
        assertEquals(task, equalTask);
        assertTrue(taskmanager.getTaskMap().containsValue(task));
        assertTrue(taskmanager.getPrioritizedSet().contains(task));
    }

    @Test
    public void createTaskWithEmpty() {
        clear();
        Task task = new Task("name", "description", LocalDateTime.now(), Duration.ofMinutes(10));
        taskmanager.createTask(task);
        int id = task.getId();
        Task equalTask = taskmanager.getTaskById(id);
        assertNotNull(equalTask);
        assertEquals(task, equalTask);
        assertTrue(taskmanager.getPrioritizedSet().contains(task));
        assertTrue(taskmanager.getTaskMap().containsValue(task));

    }

    @Test
    public void createEpic() {
        Epic epic = new Epic("name", "description");
        taskmanager.createEpic(epic);
        int id = epic.getId();
        Epic equalEpic = taskmanager.getEpicById(id);
        assertNotNull(equalEpic);
        assertEquals(epic, equalEpic);
        assertTrue(taskmanager.getEpicMap().containsValue(epic));
    }

    @Test
    public void createEpicWithEmpty() {
        clear();
        Epic epic = new Epic("name", "description");
        taskmanager.createEpic(epic);
        int id = epic.getId();
        Epic equalEpic = taskmanager.getEpicById(id);
        assertNotNull(equalEpic);
        assertEquals(epic, equalEpic);
        assertTrue(taskmanager.getEpicMap().containsValue(epic));
    }

    @Test
    public void createSubtask() {
        SubTask subTask = new SubTask("name", "description", 4, LocalDateTime.now(), Duration.ofMinutes(10));
        taskmanager.createSubTask(subTask);
        int id = subTask.getId();
        SubTask equalSubtask = taskmanager.getSubTaskById(id);
        assertNotNull(equalSubtask);
        assertEquals(subTask, equalSubtask);
        assertTrue(taskmanager.getSubTaskMap().containsValue(subTask));
        assertTrue(taskmanager.getPrioritizedSet().contains(subTask));
    }

    @Test
    public void createSubtaskWithEmpty() {
        clear();
        SubTask subTask = new SubTask("name", "description", 4, LocalDateTime.now(), Duration.ofMinutes(10));
        taskmanager.createSubTask(subTask);
        int id = subTask.getId();
        SubTask equalSubtask = taskmanager.getSubTaskById(id);
        assertNull(equalSubtask);
        assertFalse(taskmanager.getSubTaskMap().containsValue(subTask));
        assertFalse(taskmanager.getPrioritizedSet().contains(subTask));
    }

    @Test
    public void updateTask() {
        Task task = new Task("name", "description", LocalDateTime.now(), Duration.ofMinutes(10));
        taskmanager.createTask(task);
        int id = task.getId();
        Task equalTask = taskmanager.getTaskById(id);
        equalTask.setStatus(Status.DONE);
        equalTask.setName("name2");
        equalTask.setDescription("descr2");
        taskmanager.updateTask(task);
        assertNotNull(equalTask);
        assertEquals("name2", task.getName());
        assertEquals("descr2", task.getDescription());
        assertTrue(taskmanager.getTaskMap().containsValue(task));
        assertTrue(taskmanager.getPrioritizedSet().contains(task));
        Status status = equalTask.getStatus();
        switch (status) {
            case NEW -> {
                assertNull(task.getStartTime());
                assertNull(task.getDuration());
            }
            case IN_PROGRESS -> assertNotNull(task.getStartTime());
            case DONE -> {
                if (task.getStartTime() != null) {
                    assertNotNull(task.getDuration());
                }
            }
        }
    }

    @Test
    public void updateTaskWithEmpty() {
        clear();
        Task task = new Task("name", "description", LocalDateTime.now(), Duration.ofMinutes(10));
        taskmanager.createTask(task);
        int id = task.getId();
        Task equalTask = taskmanager.getTaskById(id);
        equalTask.setStatus(Status.DONE);
        equalTask.setName("name2");
        equalTask.setDescription("descr2");
        taskmanager.updateTask(task);
        assertNotNull(equalTask);
        assertEquals("name2", task.getName());
        assertEquals("descr2", task.getDescription());
        assertTrue(taskmanager.getTaskMap().containsValue(task));
        assertTrue(taskmanager.getPrioritizedSet().contains(task));
        Status status = equalTask.getStatus();
        switch (status) {
            case NEW -> {
                assertNull(task.getStartTime());
                assertNull(task.getDuration());
            }
            case IN_PROGRESS -> assertNotNull(task.getStartTime());
            case DONE -> {
                if (task.getStartTime() != null) {
                    assertNotNull(task.getDuration());
                }
            }
        }
    }

    @Test
    public void updateEpic() {
        Epic epic = new Epic("name", "description");
        taskmanager.createEpic(epic);
        epic.setName("name2");
        epic.setDescription("descr2");
        taskmanager.updateEpic(epic);
        int id = epic.getId();
        Epic equalEpic = taskmanager.getEpicById(id);
        assertNotNull(equalEpic);
        assertEquals("name2", equalEpic.getName());
        assertEquals("descr2", equalEpic.getDescription());
        assertTrue(taskmanager.getEpicMap().containsValue(epic));
    }

    @Test
    public void updateEpicWithEmpty() {
        clear();
        Epic epic = new Epic("name", "description");
        taskmanager.createEpic(epic);
        epic.setName("name2");
        epic.setDescription("descr2");
        taskmanager.updateEpic(epic);
        int id = epic.getId();
        Epic equalEpic = taskmanager.getEpicById(id);
        assertNotNull(equalEpic);
        assertEquals("name2", equalEpic.getName());
        assertEquals("descr2", equalEpic.getDescription());
        assertTrue(taskmanager.getEpicMap().containsValue(epic));
    }

    @Test
    public void updateSubtask() {
        SubTask subTask = new SubTask("name", "description", 4, LocalDateTime.now(), Duration.ofMinutes(10));
        taskmanager.createSubTask(subTask);
        int id = subTask.getId();
        subTask.setName("name2");
        subTask.setDescription("descr2");
        taskmanager.updateSubTask(subTask);
        SubTask equalSubtask = taskmanager.getSubTaskById(id);
        assertNotNull(equalSubtask);
        assertEquals("name2", equalSubtask.getName());
        assertEquals("descr2", equalSubtask.getDescription());
        assertTrue(taskmanager.getSubTaskMap().containsValue(subTask));
        assertTrue(taskmanager.getPrioritizedSet().contains(subTask));
    }

    @Test
    public void updateSubtaskWithEmpty() {
        clear();
        SubTask subTask = new SubTask("name", "description", 4, LocalDateTime.now(), Duration.ofMinutes(10));
        taskmanager.createSubTask(subTask);
        int id = subTask.getId();
        subTask.setName("name2");
        subTask.setDescription("descr2");
        taskmanager.updateSubTask(subTask);
        SubTask equalSubtask = taskmanager.getSubTaskById(id);
        assertNull(equalSubtask);

    }

    @Test
    public void removeTaskById() {
        Task task = taskmanager.getTaskMap().get(1);
        taskmanager.removeTaskById(1);
        assertFalse(taskmanager.getTaskMap().containsValue(task));
        assertFalse(taskmanager.getPrioritizedSet().contains(task));

    }

    @Test
    public void removeTaskByIdWithEmpty() {
        clear();
        Task task = taskmanager.getTaskMap().get(1);
        taskmanager.removeTaskById(1);
        assertFalse(taskmanager.getTaskMap().containsValue(task));
        assertFalse(taskmanager.getPrioritizedSet().contains(task));

    }

    @Test
    public void removeEpicById() {
        Epic epic = taskmanager.getEpicById(3);
        taskmanager.removeEpicById(3);
        assertFalse(taskmanager.getEpicMap().containsKey(3));
        assertFalse(taskmanager.getSubTaskMap().containsKey(5));
        assertFalse(taskmanager.getSubTaskMap().containsKey(6));
        assertFalse(taskmanager.getPrioritizedSet().contains(epic));

    }

    @Test
    public void removeEpicByIdWithEmpty() {
        clear();
        Epic epic = taskmanager.getEpicById(3);
        taskmanager.removeEpicById(3);
        assertFalse(taskmanager.getEpicMap().containsKey(3));
        assertFalse(taskmanager.getSubTaskMap().containsKey(5));
        assertFalse(taskmanager.getSubTaskMap().containsKey(6));
        assertFalse(taskmanager.getPrioritizedSet().contains(epic));

    }

    @Test
    public void removeSubtaskById() {
        SubTask subTask = taskmanager.getSubTaskById(5);
        taskmanager.removeSubTaskById(5);
        assertFalse(taskmanager.getSubTaskMap().containsKey(5));
        assertFalse(taskmanager.getEpicById(3).getSubTasksId().contains(5));
        assertFalse(taskmanager.getPrioritizedSet().contains(subTask));

    }

    @Test
    public void removeSubtaskByIdWithEmpty() {
        clear();
        SubTask subTask = taskmanager.getSubTaskById(5);
        taskmanager.removeSubTaskById(5);
        assertFalse(taskmanager.getSubTaskMap().containsKey(5));
        assertFalse(taskmanager.getPrioritizedSet().contains(subTask));

    }

    @Test
    public void getEpicsSubtasks() {
        Epic epic = taskmanager.getEpicById(4);
        List<SubTask> subTaskList = taskmanager.getEpicsSubtasks(4);
        List<Integer> subtasksIds = subTaskList.stream().map(Task::getId).collect(Collectors.toList());
        assertEquals(subtasksIds, epic.getSubTasksId());
    }

    @Test
    public void getEpicsSubtasksWithEmpty() {
        clear();
        Epic epic = taskmanager.getEpicById(4);
        List<SubTask> subTaskList = taskmanager.getEpicsSubtasks(4);
        assertNull(epic);
        assertTrue(subTaskList.isEmpty());
    }

    @Test
    public void getPrioritizedSet() {
        Task task = taskmanager.getTaskById(1);
        task.setStatus(Status.IN_PROGRESS);
        taskmanager.updateTask(task);
        task.setStatus(Status.DONE);
        taskmanager.updateTask(task);

        Task task1 = taskmanager.getTaskById(2);
        task1.setStatus(Status.DONE);

        SubTask subTask = taskmanager.getSubTaskById(5);
        subTask.setStatus(Status.IN_PROGRESS);
        taskmanager.updateSubTask(subTask);

        Set<Task> priorityzedSet = taskmanager.getPrioritizedSet();
        assertNotNull(priorityzedSet);
        assertFalse(priorityzedSet.isEmpty());
        List<LocalDateTime> startTime = priorityzedSet.stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .toList();
        for (int i = 0; i < startTime.size() - 1; i++) {
            assertTrue(startTime.get(i).isBefore(startTime.get(i + 1)));
        }
    }

    @Test
    public void getPrioritizedSetWithEmpty() {
        clear();
        Set<Task> priorityzedSet = taskmanager.getPrioritizedSet();
        assertNotNull(priorityzedSet);
        assertTrue(priorityzedSet.isEmpty());
    }

    @Test
    public void updateStatusEpicWithEmpty() {
        clear();
        Epic epic = taskmanager.getEpicById(3);
        taskmanager.updateStatus(epic);
        assertNull(epic);
    }

    @Test
    public void updateStatusEpicAllNew() {
        Epic epic = taskmanager.getEpicById(3);
        taskmanager.updateStatus(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void updateStatusEpicAllDone() {
        Epic epic = taskmanager.getEpicById(3);
        for (SubTask subTask : taskmanager.getEpicsSubtasks(3)) {
            subTask.setStatus(Status.DONE);
        }
        taskmanager.updateStatus(epic);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void updateStatusEpicNewInProgressDone() {
        Epic epic = taskmanager.getEpicById(3);
        SubTask subTask = taskmanager.getSubTaskById(5);
        SubTask subTask2 = taskmanager.getSubTaskById(6);
        SubTask subTask3 = new SubTask("name", "description", 3, LocalDateTime.now(), Duration.ofMinutes(10));
        subTask.setStatus(Status.NEW);
        subTask2.setStatus(Status.IN_PROGRESS);
        subTask3.setStatus(Status.DONE);
        taskmanager.updateStatus(epic);
        assertNotNull(taskmanager.getEpicsSubtasks(3));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}
