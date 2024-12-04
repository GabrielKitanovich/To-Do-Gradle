package proyectito.rapido.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {
    private String name;
    private String description;
    private String category;
    private List<String> checklist;

    public Task(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.checklist = new ArrayList<>();
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

    public List<String> getChecklist() {
        return checklist;
    }

    public void addChecklistItem(String item) {
        checklist.add(item);
    }

    public void removeChecklistItem(String item) {
        checklist.remove(item);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}