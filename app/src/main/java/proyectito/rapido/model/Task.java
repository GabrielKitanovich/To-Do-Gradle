package proyectito.rapido.model;

import java.io.Serializable;
import java.util.List;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L; // Add serialVersionUID
    private String id;
    private String name;
    private String description;
    private String category;
    private double completionPercentage;

    public Task(String id, String name, String description, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.completionPercentage = 0.0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void calculateCompletionPercentage(List<ChecklistItem> checklistItems) {
        int totalScore = 0;
        int completedScore = 0;

        for (ChecklistItem item : checklistItems) {
            int itemScore = 0;
            switch (item.getColor()) {
                case "Verde":
                    itemScore = 1;
                    break;
                case "Amarillo":
                    itemScore = 2;
                    break;
                case "Rojo":
                    itemScore = 3;
                    break;
            }
            totalScore += itemScore;
            if (item.isCompleted()) {
                completedScore += itemScore;
            }
        }

        if (totalScore > 0) {
            this.completionPercentage = (double) completedScore / totalScore * 100;
        } else {
            this.completionPercentage = 0.0;
        }
    }
}