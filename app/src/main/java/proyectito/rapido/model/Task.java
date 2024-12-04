package proyectito.rapido.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {
    private String name;
    private String description;
    private String category;
    private List<String> checklist;
    private List<Boolean> checklistCompletion; // Track completion status

    public Task(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.checklist = new ArrayList<>();
        this.checklistCompletion = new ArrayList<>(); // Initialize completion status list
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
        checklistCompletion.add(false); // Default to not completed
    }

    public void removeChecklistItem(String item) {
        int index = checklist.indexOf(item);
        if (index != -1) {
            checklist.remove(index);
            checklistCompletion.remove(index);
        }
    }

    public boolean isItemCompleted(int index) {
        return checklistCompletion.get(index);
    }

    public void setItemCompleted(int index, boolean completed) {
        checklistCompletion.set(index, completed);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}