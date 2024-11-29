package main.java.proyectito.rapido.model;

import java.io.Serializable;

public class Task implements Serializable {
    private String description;
    private String category;

    public Task(String description, String category) {
        this.description = description;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }
}