package proyectito.rapido.view;

import proyectito.rapido.controller.TaskController;
import proyectito.rapido.model.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

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

        JLabel timeLabel = new JLabel();
        timeLabel.setBorder(new EmptyBorder(0, 0, 0, 10)); // Add right margin
        updateTimeLabel(timeLabel, task);

        taskLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                taskView.showTaskDetails(task);
                task.calculateCompletionPercentage(controller.getChecklistItemsByTask(task));
                completionLabel.setText(String.format("%.2f%%", task.getCompletionPercentage()));
                updateTaskPanelBackground(taskPanel, task.getCompletionPercentage());
                updateTimeLabel(timeLabel, task);
            }
        });

        taskPanel.add(taskLabel, BorderLayout.CENTER);
        taskPanel.add(completionLabel, BorderLayout.WEST);
        taskPanel.add(timeLabel, BorderLayout.EAST);

        updateTaskPanelBackground(taskPanel, task.getCompletionPercentage());

        return taskPanel;
    }

    public static JPanel createCompletedTaskPanel(Task task, TaskController controller, TaskView taskView) {
        JPanel taskPanel = new JPanel(new BorderLayout());
        Dimension taskSize = new Dimension(Integer.MAX_VALUE, 38);
        taskPanel.setPreferredSize(taskSize);
        taskPanel.setBorder(new LineBorder(Color.GRAY, 1));
        JLabel taskLabel = new JLabel(task.getName());
        taskLabel.setBorder(new EmptyBorder(0, 20, 0, 0)); // Add left margin

        JLabel completionLabel = new JLabel(String.format("%.2f%%", task.getCompletionPercentage()));
        completionLabel.setBorder(new EmptyBorder(0, 10, 0, 0)); // Add left margin

        taskLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CompletedTaskDetailsDialog.showTaskDetails(task, controller, taskView);
            }
        });

        taskPanel.add(taskLabel, BorderLayout.CENTER);
        taskPanel.add(completionLabel, BorderLayout.WEST);

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

    private static void updateTimeLabel(JLabel timeLabel, Task task) {
        long currentTime = System.currentTimeMillis();
        long endTime = task.getCreationTime() + task.getDuration();
        long remainingTime = endTime - currentTime;

        if (task.getDuration() <= 0) {
            timeLabel.setText("");
        } else if (remainingTime <= 0) {
            timeLabel.setText("Tiempo finalizado");
        } else {
            timeLabel.setText(formatRemainingTime(remainingTime));
            timeLabel.setIcon(new ImageIcon("path/to/clock/icon.png")); // Replace with the actual path to the clock icon
        }
    }

    private static String formatRemainingTime(long remainingTime) {
        long seconds = remainingTime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = days / 365;

        if (years > 0) {
            return years + " años";
        } else if (months > 0) {
            return months + " meses";
        } else if (days > 0) {
            return days + " días";
        } else if (hours > 0) {
            return hours + " horas";
        } else if (minutes > 0) {
            return minutes + " minutos";
        } else {
            return seconds + " segundos";
        }
    }
}