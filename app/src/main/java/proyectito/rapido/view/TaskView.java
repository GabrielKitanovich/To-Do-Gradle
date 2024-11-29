package main.java.proyectito.rapido.view;

import proyectito.rapido.controller.TaskController;
import proyectito.rapido.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TaskView extends JFrame {
    private TaskController controller;
    private JTextArea importantNotUrgentArea;
    private JTextArea importantUrgentArea;
    private JTextArea notImportantNotUrgentArea;
    private JTextArea notImportantUrgentArea;

    public TaskView() {
        controller = new TaskController();

        setTitle("To-Do List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2));

        importantNotUrgentArea = new JTextArea();
        importantUrgentArea = new JTextArea();
        notImportantNotUrgentArea = new JTextArea();
        notImportantUrgentArea = new JTextArea();

        add(new JScrollPane(importantNotUrgentArea));
        add(new JScrollPane(importantUrgentArea));
        add(new JScrollPane(notImportantNotUrgentArea));
        add(new JScrollPane(notImportantUrgentArea));

        JPanel inputPanel = new JPanel();
        JTextField taskField = new JTextField(20);
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{
                "importante, no urgente", "importante y urgente", "no importante, no urgente", "no importante y urgente"
        });
        JButton addButton = new JButton("Agregar");

        inputPanel.add(taskField);
        inputPanel.add(categoryBox);
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String task = taskField.getText();
                String category = (String) categoryBox.getSelectedItem();
                controller.addTask(task, category);
                taskField.setText("");
                updateTaskAreas();
            }
        });

        updateTaskAreas();
    }

    private void updateTaskAreas() {
        importantNotUrgentArea.setText(getTasksAsString("importante, no urgente"));
        importantUrgentArea.setText(getTasksAsString("importante y urgente"));
        notImportantNotUrgentArea.setText(getTasksAsString("no importante, no urgente"));
        notImportantUrgentArea.setText(getTasksAsString("no importante y urgente"));
    }

    private String getTasksAsString(String category) {
        List<Task> tasks = controller.getTasksByCategory(category);
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task.getDescription()).append("\n");
        }
        return sb.toString();
    }

    public static void launchApp() {
        SwingUtilities.invokeLater(() -> {
            TaskView app = new TaskView();
            app.setVisible(true);
        });
    }
}