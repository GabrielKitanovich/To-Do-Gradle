package proyectito.rapido.view;

import proyectito.rapido.controller.TaskController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddTaskDialog {

    public static void showAddTaskDialog(String category, TaskController controller, TaskView taskView) {
        JTextField nameField = new JTextField(20);
        nameField.setBorder(BorderFactory.createTitledBorder("Nombre"));
        JTextArea descriptionField = new JTextArea(5, 20); // Increase the size of the description field
        descriptionField.setBorder(BorderFactory.createTitledBorder("Descripción"));
        
        // Panel con layout para los ítems
        JPanel checklistPanel = new JPanel(new GridBagLayout());
        JScrollPane checklistScrollPane = new JScrollPane(checklistPanel);
        checklistScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        checklistScrollPane.setPreferredSize(new Dimension(0, 100)); // Ensure the panel is visible
        
        JTextField newItemField = new JTextField(20);
        newItemField.setBorder(BorderFactory.createTitledBorder("Agregar ítem"));
        JButton addItemButton = new JButton("Agregar");
        addItemButton.setPreferredSize(new Dimension(100, 30)); // Improve button appearance
        addItemButton.addActionListener(e -> {
            String newItem = newItemField.getText();
            if (!newItem.isEmpty()) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add visible border
                JLabel itemLabel = new JLabel(newItem);
                itemLabel.setBorder(new EmptyBorder(0, 10, 0, 0)); // Add left margin
                JButton removeItemButton = new JButton("Eliminar");
                removeItemButton.addActionListener(event -> {
                    checklistPanel.remove(itemPanel);
                    checklistPanel.revalidate();
                    checklistPanel.repaint();
                });
                itemPanel.add(itemLabel, BorderLayout.CENTER);
                itemPanel.add(removeItemButton, BorderLayout.EAST);

                // Añadir ítem al panel
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = checklistPanel.getComponentCount(); // Índice correcto para nuevo ítem
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0; // Ocupar todo el ancho disponible
                gbc.insets = new Insets(2, 2, 2, 2); // Espaciado entre ítems
                checklistPanel.add(itemPanel, gbc);

                checklistPanel.revalidate();
                checklistPanel.repaint();
                newItemField.setText(""); // Limpia el campo
            }
        });

        // Panel para agregar nuevos ítems
        JPanel addItemPanel = new JPanel(new BorderLayout());
        addItemPanel.add(newItemField, BorderLayout.CENTER);
        addItemPanel.add(addItemButton, BorderLayout.EAST);

        // Configuración del panel principal
        JPanel panel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.add(nameField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between fields
        fieldsPanel.add(new JScrollPane(descriptionField));
        panel.add(fieldsPanel, BorderLayout.NORTH);
        panel.add(checklistScrollPane, BorderLayout.CENTER);
        panel.add(addItemPanel, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(taskView, panel, "Nueva Tarea", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String taskName = nameField.getText();
            String taskDescription = descriptionField.getText();
            List<String> checklistItems = new ArrayList<>();
            for (Component component : checklistPanel.getComponents()) {
                if (component instanceof JPanel) {
                    for (Component subComponent : ((JPanel) component).getComponents()) {
                        if (subComponent instanceof JLabel) {
                            checklistItems.add(((JLabel) subComponent).getText());
                        }
                    }
                }
            }
            if (!taskName.isEmpty()) {
                controller.addTask(taskName, taskDescription, category, checklistItems.toArray(new String[0]));
                taskView.updateTaskAreas();
            }
        }
    }
}
