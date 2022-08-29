package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.*;
import epic.Epic;
import epic.SubTask;
import epic.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;
    Gson gson = Managers.getGson();

    @BeforeEach
    public void setUp() throws IOException {
        httpTaskServer = new HttpTaskServer();
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        taskManager.createTask(new Task("1-5", "12345",
                LocalDateTime.of(2023, Month.JUNE, 1, 0, 0), Duration.ofMinutes(10)));
        taskManager.createTask(new Task("5-10", "5678910",
                LocalDateTime.of(2023, Month.JUNE, 2, 0, 0), Duration.ofMinutes(10)));
        taskManager.createEpic(new Epic("drive", "moto"));
        taskManager.createSubTask(new SubTask("sprint 3", "OOP", 3,
                LocalDateTime.of(2023, Month.JUNE, 5, 0, 0), Duration.ofMinutes(10)));
        taskManager.createSubTask(new SubTask("learning", "Java course", 3,
                LocalDateTime.of(2023, Month.JUNE, 6, 0, 0), Duration.ofMinutes(10)));
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getSubTaskById(5);

        httpTaskServer.start();
    }

    @AfterEach
    void tearDown() {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubTasks();
        taskManager.getHistoryManager().getHistoryMap().clear();
        taskManager.getPrioritizedSet().clear();
        InMemoryTaskManager.setId(0);

        httpTaskServer.stop();
    }

    @Test
    public void getTasks() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> taskList = gson.fromJson(response.body(), type);

        assertEquals(taskManager.getAllTasks(), taskList);
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task task = gson.fromJson(response.body(), Task.class);
        assertEquals(taskManager.getTaskById(1), task);
    }

    @Test
    public void createTask() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();
        Task newTask = new Task("task name", "task description",
                LocalDateTime.of(2030, Month.JUNE, 1, 0, 0), Duration.ofMinutes(10));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = Managers.getGson();
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(taskManager.getTaskMap().containsKey(6));
        assertEquals("Успешно", response.body());
    }

    @Test
    public void deleteTasks() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Успешно", response.body());
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Успешно", response.body());
        assertFalse(taskManager.getTaskMap().containsKey(1));
    }

    @Test
    public void getEpics() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type type = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> epicList = gson.fromJson(response.body(), type);

        assertEquals(taskManager.getAllEpics(), epicList);
    }

    @Test
    public void getEpicById() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Epic epic = gson.fromJson(response.body(), Epic.class);
        assertEquals(taskManager.getEpicById(3), epic);
    }

    @Test
    public void createEpic() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();
        Epic newEpic = new Epic("epic name", "epic description");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = Managers.getGson();
        String json = gson.toJson(newEpic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(taskManager.getEpicMap().containsKey(6));
        assertEquals("Успешно", response.body());
    }

    @Test
    public void deleteEpics() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Успешно", response.body());
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    public void deleteEpicById() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Успешно", response.body());
        assertFalse(taskManager.getEpicMap().containsKey(3));
    }

    @Test
    public void getSubtasks() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type type = new TypeToken<ArrayList<SubTask>>() {
        }.getType();
        List<SubTask> SubtaskList = gson.fromJson(response.body(), type);

        assertEquals(taskManager.getAllSubTasks(), SubtaskList);
    }

    @Test
    public void getSubtaskById() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        SubTask subTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals(taskManager.getSubTaskById(5), subTask);
    }

    @Test
    public void createSubtask() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();
        SubTask newSubTask = new SubTask("subtask name", "subtask description", 3,
                LocalDateTime.of(2030, Month.JUNE, 1, 0, 0), Duration.ofMinutes(10));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = Managers.getGson();
        String json = gson.toJson(newSubTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertTrue(taskManager.getSubTaskMap().containsKey(6));
        assertEquals("Успешно", response.body());
    }

    @Test
    public void deleteSubtasks() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Успешно", response.body());
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    public void deleteSubtaskById() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Успешно", response.body());
        assertFalse(taskManager.getSubTaskMap().containsKey(5));
    }

    @Test
    public void badUrl() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/taskSS/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Неправильный запрос", response.body());
    }

    @Test
    public void badMethod() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(HttpRequest.BodyPublishers.ofString("test")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
        assertEquals("Неизвестный метод", response.body());
    }

    @Test
    public void BadId() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=500");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Неправильный запрос", response.body());
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> historyList = gson.fromJson(response.body(), type);

        assertNotNull(historyList);
        assertEquals(taskManager.getHistoryManager().getHistoryList().size(), historyList.size());
    }

    @Test
    public void getAllTasks() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type type = new TypeToken<Set<Task>>() {
        }.getType();
        Set<Task> taskSet = gson.fromJson(response.body(), type);

        assertNotNull(taskSet);
        assertEquals(taskManager.getPrioritizedSet().size(), taskSet.size());
    }

    @Test
    public void getEpicSubtasks() throws IOException, InterruptedException {
        TaskManager taskManager = httpTaskServer.getBackedTasksManager();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type type = new TypeToken<ArrayList<SubTask>>() {
        }.getType();
        List<SubTask> subTaskList = gson.fromJson(response.body(), type);

        assertNotNull(subTaskList);
        assertEquals(taskManager.getEpicsSubtasks(3), subTaskList);
    }
}