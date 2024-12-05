package proyectito.rapido.view;

import proyectito.rapido.controller.TaskController;
import proyectito.rapido.model.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class TaskPanelFactory {

    public static JPanel createLabeledPanel(String labelText, JPanel panel, String category, TaskView taskView) {
        JPanel container = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText, JLabel.CENTER);
        container.add(label, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(createAddTaskButton(category, taskView), BorderLayout.SOUTH);
        return container;
    }

    public static JPanel createTaskPanel(Task task, TaskController controller, TaskView taskView) {
        JPanel taskPanel = new JPanel(new BorderLayout());
        Dimension taskSize = new Dimension(Integer.MAX_VALUE, 38);
        taskPanel.setPreferredSize(taskSize);
        taskPanel.setBorder(new LineBorder(Color.GRAY, 1));
        JLabel taskLabel = new JLabel(task.getName());
        taskLabel.setBorder(new EmptyBorder(0, 20, 0, 0)); // Add left margin

        JLabel completionLabel = new JLabel(String.format("%.2f%%", task.getCompletionPercentage()));
        completionLabel.setBorder(new EmptyBorder(0, 10, 0, 0)); // Add left margin

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton checkButton = new JButton("✔");
        JButton deleteButton = new JButton("✖");

        checkButton.addActionListener(e -> {
            taskView.showTaskDetails(task);
            task.calculateCompletionPercentage(controller.getChecklistItemsByTask(task));
            completionLabel.setText(String.format("%.2f%%", task.getCompletionPercentage()));
            updateTaskPanelBackground(taskPanel, task.getCompletionPercentage());
        });

        deleteButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(taskView, "¿Estás seguro de que deseas eliminar esta tarea?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                controller.removeTask(task);
                taskView.updateTaskAreas();
            }
        });

        taskLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                taskView.showTaskDetails(task);
                task.calculateCompletionPercentage(controller.getChecklistItemsByTask(task));
                completionLabel.setText(String.format("%.2f%%", task.getCompletionPercentage()));
                updateTaskPanelBackground(taskPanel, task.getCompletionPercentage());
            }
        });

        buttonPanel.add(checkButton);
        buttonPanel.add(deleteButton);
        taskPanel.add(taskLabel, BorderLayout.CENTER);
        taskPanel.add(completionLabel, BorderLayout.WEST);
        taskPanel.add(buttonPanel, BorderLayout.EAST);

        updateTaskPanelBackground(taskPanel, task.getCompletionPercentage());

        return taskPanel;
    }

    private static void updateTaskPanelBackground(JPanel taskPanel, double completionPercentage) {
        int greenValue = (int) (255 * (completionPercentage / 100));
        int grayValue = 255 - greenValue;
        taskPanel.setBackground(new Color(grayValue, 255, grayValue)); // Transition from light gray to green based on completion percentage
    }

    private static JButton createAddTaskButton(String category, TaskView taskView) {
        JButton addButton = new JButton("+");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> taskView.showAddTaskDialog(category));
        return addButton;
    }
}