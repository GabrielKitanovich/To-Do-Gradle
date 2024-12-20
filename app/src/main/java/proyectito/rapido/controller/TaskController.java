package proyectito.rapido.controller;

import proyectito.rapido.model.Task;
import proyectito.rapido.model.ChecklistItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskController {
    private List<Task> tasks;
    private List<ChecklistItem> checklistItems;
    private List<Task> completedTasks;

    public TaskController() {
        tasks = new ArrayList<>();
        checklistItems = new ArrayList<>();
        completedTasks = new ArrayList<>();
        loadTasks();
    }

    public void addTask(String id, String name, String description, String category, long creationTime, long duration) {
        tasks.add(new Task(id, name, description, category, creationTime, duration));
        saveTasks();
    }

    public void addChecklistItem(String taskId, String description, String color) {
        checklistItems.add(new ChecklistItem(taskId, description, color));
        saveTasks();
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        checklistItems.removeIf(item -> item.getTaskId().equals(task.getId())); // Remove associated checklist items
        saveTasks();
    }

    public void completeTask(Task task) {
        tasks.remove(task);
        completedTasks.add(task);
        saveTasks();
    }

    public List<Task> getCompletedTasks() {
        return completedTasks;
    }

    public void removeChecklistItem(ChecklistItem item) {
        checklistItems.remove(item);
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

    public List<ChecklistItem> getChecklistItemsByTask(Task task) {
        List<ChecklistItem> filteredItems = new ArrayList<>();
        for (ChecklistItem item : checklistItems) {
            if (item.getTaskId().equals(task.getId())) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    public void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tasks.dat"))) {
            oos.writeObject(tasks);
            oos.writeObject(checklistItems);
            oos.writeObject(completedTasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        File file = new File("tasks.dat");
        if (!file.exists()) {
            try {
                file.createNewFile();
                return; // No tasks to load if the file was just created
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            tasks = (List<Task>) ois.readObject();
            checklistItems = (List<ChecklistItem>) ois.readObject();
            completedTasks = (List<Task>) ois.readObject();
        } catch (EOFException e) {
            // Handle the case where the file might not contain all the expected objects
            tasks = tasks == null ? new ArrayList<>() : tasks;
            checklistItems = checklistItems == null ? new ArrayList<>() : checklistItems;
            completedTasks = completedTasks == null ? new ArrayList<>() : completedTasks;
            saveTasks();
        } catch (InvalidClassException e) {
            System.err.println("Class version mismatch, recreating tasks.dat file.");
            file.delete();
            tasks = new ArrayList<>();
            checklistItems = new ArrayList<>();
            completedTasks = new ArrayList<>();
            saveTasks();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}