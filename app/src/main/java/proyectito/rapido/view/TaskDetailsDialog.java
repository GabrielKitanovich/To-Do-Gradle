package proyectito.rapido.view;

import proyectito.rapido.model.Task;

import javax.swing.*;
import java.awt.*;

public class TaskDetailsDialog {

    public static void showTaskDetails(Task task, JFrame parentFrame) {
        JTextField nameField = new JTextField(task.getName(), 20);
        nameField.setEditable(false);
        nameField.setBorder(BorderFactory.createTitledBorder("Nombre"));
        nameField.setFocusable(false); // Make the field completely non-interactive

        JTextArea descriptionField = new JTextArea(task.getDescription(), 5, 20);
        descriptionField.setEditable(false);
        descriptionField.setBorder(BorderFactory.createTitledBorder("Descripción"));
        descriptionField.setFocusable(false); // Make the field completely non-interactive

        String[] columnNames = {"Item", "Completado"};
        Object[][] data = new Object[task.getChecklist().size()][2];
        for (int i = 0; i < task.getChecklist().size(); i++) {
            data[i][0] = task.getChecklist().get(i);
            data[i][1] = task.isItemCompleted(i); // Get the completion status from the task
        }
        JTable checklistTable = new JTable(data, columnNames);
        checklistTable.getColumnModel().getColumn(1).setCellRenderer(checklistTable.getDefaultRenderer(Boolean.class));
        checklistTable.getColumnModel().getColumn(1).setCellEditor(checklistTable.getDefaultEditor(Boolean.class));

        // Adjust the table height based on the number of items
        int rowHeight = checklistTable.getRowHeight();
        int tableHeight = rowHeight * task.getChecklist().size();
        checklistTable.setPreferredScrollableViewportSize(new Dimension(checklistTable.getPreferredScrollableViewportSize().width, tableHeight));

        JPanel panel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.add(nameField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fieldsPanel.add(new JScrollPane(descriptionField));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fieldsPanel.add(new JScrollPane(checklistTable));

        panel.add(fieldsPanel, BorderLayout.NORTH);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Detalles de la Tarea", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < task.getChecklist().size(); i++) {
                task.setItemCompleted(i, (Boolean) checklistTable.getValueAt(i, 1)); // Save the completion status
            }
        }
    }
}