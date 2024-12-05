package proyectito.rapido.model;

import java.io.Serializable;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L; // Add serialVersionUID
    private String id;
    private String name;
    private String description;
    private String category;

    public Task(String id, String name, String description, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}