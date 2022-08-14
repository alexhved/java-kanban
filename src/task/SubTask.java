package task;

import java.util.Objects;

public class SubTask extends Task {
    /*private LocalDateTime startTime;

    private Duration duration;*/
    private int epicId;
    public SubTask() {super();}

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, Type.SUBTASK, name, status, description, epicId, startTime, duration);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int id) {
        this.epicId = id;
    }
}