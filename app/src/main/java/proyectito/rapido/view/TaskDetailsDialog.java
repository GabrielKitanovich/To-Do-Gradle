
package proyectito.rapido.view;

import proyectito.rapido.model.Task;

import javax.swing.*;
import java.awt.*;

public class TaskDetailsDialog {

    public static void showTaskDetails(Task task, JFrame parentFrame) {
        JTextField nameField = new JTextField(task.getName(), 20);
        nameField.setEditable(false); // Make the field non-editable
        JTextField descriptionField = new JTextField(task.getDescription(), 20);
        descriptionField.setEditable(false); // Make the field non-editable
        JTextArea checklistArea = new JTextArea(5, 20);
        checklistArea.setEditable(false); // Make the field non-editable
        for (String item : task.getChecklist()) {
            checklistArea.append(item + "\n");
        }
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Checklist:"));
        panel.add(new JScrollPane(checklistArea));

        JOptionPane.showMessageDialog(parentFrame, panel, "Detalles de la Tarea", JOptionPane.INFORMATION_MESSAGE);
    }
}