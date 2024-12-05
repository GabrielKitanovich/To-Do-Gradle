package proyectito.rapido.view;

import proyectito.rapido.model.Task;
import proyectito.rapido.model.ChecklistItem;
import proyectito.rapido.controller.TaskController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TaskDetailsDialog {

    public static void showTaskDetails(Task task, TaskController controller, JFrame parentFrame) {
        JTextField nameField = new JTextField(task.getName(), 20);
        nameField.setEditable(false);
        nameField.setBorder(BorderFactory.createTitledBorder("Nombre"));
        nameField.setFocusable(false); // Make the field completely non-interactive

        JTextArea descriptionField = new JTextArea(task.getDescription(), 5, 20);
        descriptionField.setEditable(false);
        descriptionField.setBorder(BorderFactory.createTitledBorder("Descripción"));
        descriptionField.setFocusable(false); // Make the field completely non-interactive

        List<ChecklistItem> checklistItems = controller.getChecklistItemsByTask(task);
        JPanel checklistPanel = new JPanel();
        checklistPanel.setLayout(new BoxLayout(checklistPanel, BoxLayout.Y_AXIS));
        checklistPanel.setBorder(BorderFactory.createTitledBorder("Checklist"));

        JLabel completionLabel = new JLabel();
        completionLabel.setBorder(BorderFactory.createTitledBorder("Porcentaje de Completado"));

        for (ChecklistItem item : checklistItems) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            JLabel itemLabel = new JLabel(item.getDescription());
            itemLabel.setOpaque(true);
            switch (item.getColor()) {
                case "Verde":
                    itemPanel.setBackground(Color.GREEN);
                    itemLabel.setBackground(Color.GREEN);
                    break;
                case "Amarillo":
                    itemPanel.setBackground(Color.YELLOW);
                    itemLabel.setBackground(Color.YELLOW);
                    break;
                case "Rojo":
                    itemPanel.setBackground(Color.RED);
                    itemLabel.setBackground(Color.RED);
                    break;
                default:
                    itemPanel.setBackground(Color.WHITE);
                    itemLabel.setBackground(Color.WHITE);
                    break;
            }
            JCheckBox completedCheckBox = new JCheckBox();
            completedCheckBox.setSelected(item.isCompleted());
            completedCheckBox.addActionListener(e -> {
                item.setCompleted(completedCheckBox.isSelected());
                task.calculateCompletionPercentage(checklistItems);
                completionLabel.setText("Porcentaje de Completado: " + String.format("%.2f", task.getCompletionPercentage()) + "%");
            });

            itemPanel.add(itemLabel, BorderLayout.CENTER);
            itemPanel.add(completedCheckBox, BorderLayout.EAST);
            checklistPanel.add(itemPanel);
        }

        task.calculateCompletionPercentage(checklistItems);
        completionLabel.setText("Porcentaje de Completado: " + String.format("%.2f", task.getCompletionPercentage()) + "%");

        JLabel creationTimeLabel = new JLabel("Creado: " + formatTime(task.getCreationTime()));
        creationTimeLabel.setBorder(BorderFactory.createTitledBorder("Tiempo de Creación"));

        JLabel endTimeLabel = new JLabel();
        if (task.getDuration() > 0) {
            endTimeLabel.setText("Duración: " + formatDuration(task.getDuration()));
            endTimeLabel.setBorder(BorderFactory.createTitledBorder("Tiempo de Finalización"));
        }

        JPanel panel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.add(nameField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fieldsPanel.add(new JScrollPane(descriptionField));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fieldsPanel.add(new JScrollPane(checklistPanel));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fieldsPanel.add(completionLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fieldsPanel.add(creationTimeLabel);
        if (task.getDuration() > 0) {
            fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            fieldsPanel.add(endTimeLabel);
        }

        JButton deleteButton = new JButton("Eliminar Tarea");
        deleteButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(parentFrame, "¿Estás seguro de que deseas eliminar esta tarea?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                controller.removeTask(task);
                ((TaskView) parentFrame).updateTaskAreas();
                SwingUtilities.getWindowAncestor(deleteButton).dispose(); // Close the dialog
            }
        });

        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fieldsPanel.add(deleteButton);

        panel.add(fieldsPanel, BorderLayout.NORTH);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Detalles de la Tarea", JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(panel);
        if (dialog != null) {
            dialog.setLocationRelativeTo(parentFrame); // Center the dialog on the parent frame
        }
        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < checklistItems.size(); i++) {
                checklistItems.get(i).setCompleted(((JCheckBox) ((JPanel) checklistPanel.getComponent(i)).getComponent(1)).isSelected()); // Save the completion status
            }
            task.calculateCompletionPercentage(checklistItems); // Recalculate completion percentage
            controller.saveTasks(); // Save the updated checklist items and task

            if (task.getCompletionPercentage() == 100.0) {
                int completeResponse = JOptionPane.showConfirmDialog(parentFrame, "La tarea está al 100%. ¿Deseas marcarla como completada y moverla a la lista de tareas completadas?", "Completar Tarea", JOptionPane.YES_NO_OPTION);
                if (completeResponse == JOptionPane.YES_OPTION) {
                    controller.completeTask(task);
                    ((TaskView) parentFrame).updateTaskAreas();
                }
            }
        }
    }

    private static String formatTime(long timeInMillis) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date(timeInMillis);
        return sdf.format(date);
    }

    private static String formatDuration(long durationInMillis) {
        long hours = durationInMillis / 3600000L;
        long days = durationInMillis / 86400000L;
        long years = durationInMillis / 31536000000L;

        if (years > 0) {
            return years + " años";
        } else if (days > 0) {
            return days + " días";
        } else {
            return hours + " horas";
        }
    }
}