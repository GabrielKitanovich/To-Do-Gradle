package proyectito.rapido.controller;

import proyectito.rapido.model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskController {
    private List<Task> tasks;

    public TaskController() {
        tasks = new ArrayList<>();
        loadTasks();
    }

    public void addTask(String description, String category) {
        tasks.add(new Task(description, category));
        saveTasks();
    }

    public List<Task> getTasksByCategory(String category) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getCategory().equals(category)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tasks.dat"))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tasks.dat"))) {
            tasks = (List<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}