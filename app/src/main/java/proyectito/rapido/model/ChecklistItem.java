package proyectito.rapido.model;

import java.io.Serializable;

public class ChecklistItem implements Serializable {
    private static final long serialVersionUID = 1L; // Add serialVersionUID
    private String taskId;
    private String description;
    private boolean completed;
    private String color;

    public ChecklistItem(String taskId, String description, String color) {
        this.taskId = taskId;
        this.description = description;
        this.completed = false; // Default to not completed
        this.color = color;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}