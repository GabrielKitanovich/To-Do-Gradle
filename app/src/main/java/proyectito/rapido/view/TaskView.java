package proyectito.rapido.view;

import proyectito.rapido.controller.TaskController;
import proyectito.rapido.model.Task;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class TaskView extends JFrame {
    private TaskController controller;
    private JPanel importantNotUrgentPanel;
    private JPanel importantUrgentPanel;
    private JPanel notImportantNotUrgentPanel;
    private JPanel notImportantUrgentPanel;

    public TaskView() {
        controller = new TaskController();

        setTitle("To-Do List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2));

        importantNotUrgentPanel = new JPanel(new GridBagLayout());
        importantUrgentPanel = new JPanel(new GridBagLayout());
        notImportantNotUrgentPanel = new JPanel(new GridBagLayout());
        notImportantUrgentPanel = new JPanel(new GridBagLayout());

        add(createLabeledPanel("Importante, no urgente", importantNotUrgentPanel, "importante, no urgente"));
        add(createLabeledPanel("Importante y urgente", importantUrgentPanel, "importante y urgente"));
        add(createLabeledPanel("No importante, no urgente", notImportantNotUrgentPanel, "no importante, no urgente"));
        add(createLabeledPanel("No importante y urgente", notImportantUrgentPanel, "no importante y urgente"));

        updateTaskAreas();
    }

    private JPanel createLabeledPanel(String labelText, JPanel panel, String category) {
        JPanel container = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText, JLabel.CENTER);
        container.add(label, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(createAddTaskButton(category), BorderLayout.SOUTH);
        return container;
    }

    private void updateTaskAreas() {
        updateTaskPanel(importantNotUrgentPanel, "importante, no urgente");
        updateTaskPanel(importantUrgentPanel, "importante y urgente");
        updateTaskPanel(notImportantNotUrgentPanel, "no importante, no urgente");
        updateTaskPanel(notImportantUrgentPanel, "no importante y urgente");
    }

    private void updateTaskPanel(JPanel panel, String category) {
        panel.removeAll();
        List<Task> tasks = controller.getTasksByCategory(category);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        for (Task task : tasks) {
            panel.add(createTaskPanel(task), gbc);
            gbc.gridy++;
        }
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc); // Add glue to push components to the top
        panel.revalidate();
        panel.repaint();
    }

    private JPanel createTaskPanel(Task task) {
        JPanel taskPanel = new JPanel(new BorderLayout());
        Dimension taskSize = new Dimension(Integer.MAX_VALUE, 50);
        taskPanel.setPreferredSize(taskSize);
        taskPanel.setBorder(new LineBorder(Color.GRAY, 1));
        JLabel taskLabel = new JLabel(task.getDescription());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton checkButton = new JButton("✔");
        JButton deleteButton = new JButton("✖");

        ActionListener deleteAction = e -> {
            controller.removeTask(task);
            updateTaskAreas();
        };

        checkButton.addActionListener(deleteAction);
        deleteButton.addActionListener(deleteAction);

        buttonPanel.add(checkButton);
        buttonPanel.add(deleteButton);
        taskPanel.add(taskLabel, BorderLayout.CENTER);
        taskPanel.add(buttonPanel, BorderLayout.EAST);

        return taskPanel;
    }

    private JButton createAddTaskButton(String category) {
        JButton addButton = new JButton("+");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> showAddTaskDialog(category));
        return addButton;
    }

    private void showAddTaskDialog(String category) {
        JTextField taskField = new JTextField(20);
        int result = JOptionPane.showConfirmDialog(this, taskField, "Nueva Tarea", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String taskDescription = taskField.getText();
            if (!taskDescription.isEmpty()) {
                controller.addTask(taskDescription, category);
                updateTaskAreas();
            }
        }
    }

    public static void launchApp() {
        SwingUtilities.invokeLater(() -> {
            TaskView app = new TaskView();
            app.setVisible(true);
        });
    }
}