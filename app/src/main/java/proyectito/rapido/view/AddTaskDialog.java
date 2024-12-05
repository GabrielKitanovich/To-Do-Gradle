package proyectito.rapido.view;

import proyectito.rapido.controller.TaskController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.UUID;

public class AddTaskDialog {

    public static void showAddTaskDialog(String category, TaskController controller, TaskView taskView) {
        JTextField nameField = new JTextField(20);
        nameField.setBorder(BorderFactory.createTitledBorder("Nombre"));
        JTextArea descriptionField = new JTextArea(5, 20);
        descriptionField.setBorder(BorderFactory.createTitledBorder("Descripción"));

        JPanel checklistPanel = new JPanel(new GridBagLayout());
        JScrollPane checklistScrollPane = new JScrollPane(checklistPanel);
        checklistScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        checklistScrollPane.setPreferredSize(new Dimension(0, 100));

        JTextField newItemField = new JTextField(20);
        newItemField.setBorder(BorderFactory.createTitledBorder("Agregar ítem"));
        String[] colors = {"Verde", "Amarillo", "Rojo"};
        JComboBox<String> colorComboBox = new JComboBox<>(colors);
        JButton addItemButton = new JButton("Agregar");
        addItemButton.setPreferredSize(new Dimension(100, 30));
        addItemButton.addActionListener(e -> {
            String newItem = newItemField.getText();
            String selectedColor = (String) colorComboBox.getSelectedItem();
            if (!newItem.isEmpty() && selectedColor != null) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                JLabel itemLabel = new JLabel(newItem);
                itemLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
                itemLabel.setOpaque(true);
                switch (selectedColor) {
                    case "Verde":
                        itemLabel.setBackground(Color.GREEN);
                        break;
                    case "Amarillo":
                        itemLabel.setBackground(Color.YELLOW);
                        break;
                    case "Rojo":
                        itemLabel.setBackground(Color.RED);
                        break;
                }
                JButton removeItemButton = new JButton("Eliminar");
                removeItemButton.addActionListener(event -> {
                    checklistPanel.remove(itemPanel);
                    checklistPanel.revalidate();
                    checklistPanel.repaint();
                });
                itemPanel.add(itemLabel, BorderLayout.CENTER);
                itemPanel.add(removeItemButton, BorderLayout.EAST);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = checklistPanel.getComponentCount();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                gbc.insets = new Insets(2, 2, 2, 2);
                checklistPanel.add(itemPanel, gbc);

                checklistPanel.revalidate();
                checklistPanel.repaint();
                newItemField.setText("");
            }
        });

        JPanel addItemPanel = new JPanel(new BorderLayout());
        addItemPanel.add(newItemField, BorderLayout.CENTER);
        addItemPanel.add(colorComboBox, BorderLayout.WEST);
        addItemPanel.add(addItemButton, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.add(nameField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fieldsPanel.add(new JScrollPane(descriptionField));
        
        JTextField timeField = new JTextField(10);
        timeField.setBorder(BorderFactory.createTitledBorder("Tiempo"));
        timeField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) {
                    return;
                }
                if (str.matches("\\d+")) { // Only allow digits
                    super.insertString(offs, str, a);
                }
            }
        });
        String[] timeUnits = {"Horas", "Días", "Años"};
        JComboBox<String> timeUnitComboBox = new JComboBox<>(timeUnits);

        JPanel timePanel = new JPanel();
        timePanel.add(timeField);
        timePanel.add(timeUnitComboBox);

        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fieldsPanel.add(timePanel);

        panel.add(fieldsPanel, BorderLayout.NORTH);
        panel.add(checklistScrollPane, BorderLayout.CENTER);
        panel.add(addItemPanel, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(taskView, panel, "Nueva Tarea", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String taskName = nameField.getText().trim();
            String taskDescription = descriptionField.getText().trim();
            String timeValue = timeField.getText().trim();
            String timeUnit = (String) timeUnitComboBox.getSelectedItem();
            if (!taskName.isEmpty() && !taskDescription.isEmpty()) {
                String taskId = UUID.randomUUID().toString();
                long creationTime = System.currentTimeMillis();
                long duration = 0;
                if (!timeValue.isEmpty() && timeUnit != null) {
                    duration = parseDuration(timeValue, timeUnit);
                }
                controller.addTask(taskId, taskName, taskDescription, category, creationTime, duration);
                if (checklistPanel.getComponentCount() == 0) {
                    controller.addChecklistItem(taskId, "Completar la tarea", "Amarillo");
                } else {
                    for (Component component : checklistPanel.getComponents()) {
                        if (component instanceof JPanel) {
                            for (Component subComponent : ((JPanel) component).getComponents()) {
                                if (subComponent instanceof JLabel) {
                                    String itemDescription = ((JLabel) subComponent).getText();
                                    String itemColor = getColorString(((JLabel) subComponent).getBackground());
                                    controller.addChecklistItem(taskId, itemDescription, itemColor);
                                }
                            }
                        }
                    }
                }
                taskView.updateTaskAreas();
            } else {
                JOptionPane.showMessageDialog(taskView, "El nombre y la descripción no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static String getColorString(Color color) {
        if (Color.GREEN.equals(color)) {
            return "Verde";
        } else if (Color.YELLOW.equals(color)) {
            return "Amarillo";
        } else if (Color.RED.equals(color)) {
            return "Rojo";
        } else {
            return "Blanco";
        }
    }

    private static long parseDuration(String timeValue, String timeUnit) {
        long value = Long.parseLong(timeValue);
        switch (timeUnit) {
            case "Horas":
                return value * 3600000L; // 1 hour in milliseconds
            case "Días":
                return value * 86400000L; // 1 day in milliseconds
            case "Años":
                return value * 31536000000L; // 1 year in milliseconds
            default:
                return 0;
        }
    }
}
