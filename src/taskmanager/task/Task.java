package taskmanager.task;

import taskmanager.manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    Status status;
    protected String name;
    protected String description;
    protected int id;

    protected LocalDateTime startTime;

    protected Duration duration;
    public Task() {}

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", id, Type.TASK, name, status, description, startTime, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && description.equals(task.description) && status.equals(task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setId(Integer id) {
        if (id!=null) {
            this.id = id;
        }
    }

    public void setStatus(Status status) {
        try {
            switch (status) {
                case NEW, DONE -> this.status = status;
                case IN_PROGRESS -> {
                    if (!InMemoryTaskManager.getTaskLock()) {
                        this.status = status;
                    } else {
                        throw new StatusException("Завершите предыдущую задачу");
                    }
                }
            }
        } catch (StatusException e) {
            System.out.println(e.getMessage());
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEndTime() {
        try {
            if (startTime != null && duration != null) {
                return LocalDateTime.from(startTime.plus(duration));
            } else {
                throw new NullPointerException("Ошибка при подсчете времени!");
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
