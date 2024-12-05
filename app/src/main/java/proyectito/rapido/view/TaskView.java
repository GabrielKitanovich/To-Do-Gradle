package proyectito.rapido.view;

import proyectito.rapido.controller.TaskController;
import proyectito.rapido.model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TaskView extends JFrame {
    private TaskController controller;
    private JPanel importantNotUrgentPanel;
    private JPanel importantUrgentPanel;
    private JPanel notImportantNotUrgentPanel;
    private JPanel notImportantUrgentPanel;
    private JPanel completedTasksPanel;

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

        add(createLabeledPanel("Importante | No Urgente", importantNotUrgentPanel, "importante, no urgente"));
        add(createLabeledPanel("Importante | Urgente", importantUrgentPanel, "importante y urgente"));
        add(createLabeledPanel("No Importante | No Urgente", notImportantNotUrgentPanel, "no importante, no urgente"));
        add(createLabeledPanel("No Importante | Urgente", notImportantUrgentPanel, "no importante y urgente"));

        JButton viewCompletedTasksButton = new JButton("Ver Tareas Completadas");
        viewCompletedTasksButton.addActionListener(e -> showCompletedTasksDialog());
        add(viewCompletedTasksButton);

        updateTaskAreas();
    }

    private JPanel createLabeledPanel(String labelText, JPanel panel, String category) {
        return TaskPanelFactory.createLabeledPanel(labelText, panel, category, this);
    }

    void updateTaskAreas() {
        updateTaskPanel(importantNotUrgentPanel, "importante, no urgente");
        updateTaskPanel(importantUrgentPanel, "importante y urgente");
        updateTaskPanel(notImportantNotUrgentPanel, "no importante, no urgente");
        updateTaskPanel(notImportantUrgentPanel, "no importante y urgente");
    }

    public void updateTaskPanel(JPanel panel, String category) {
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
            panel.add(TaskPanelFactory.createTaskPanel(task, controller, this), gbc);
            gbc.gridy++;
        }
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc); // Add glue to push components to the top
        panel.revalidate();
        panel.repaint();
    }

    public void updateCompletedTasksPanel() {
        completedTasksPanel.removeAll();
        List<Task> completedTasks = controller.getCompletedTasks();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        for (Task task : completedTasks) {
            completedTasksPanel.add(TaskPanelFactory.createTaskPanel(task, controller, this), gbc);
            gbc.gridy++;
        }
        gbc.weighty = 1.0;
        completedTasksPanel.add(Box.createVerticalGlue(), gbc); // Add glue to push components to the top
        completedTasksPanel.revalidate();
        completedTasksPanel.repaint();
    }

    public void showTaskDetails(Task task) {
        TaskDetailsDialog.showTaskDetails(task, controller, this);
    }

    public void showAddTaskDialog(String category) {
        AddTaskDialog.showAddTaskDialog(category, controller, this);
    }

    private void showCompletedTasksDialog() {
        JDialog dialog = new JDialog(this, "Tareas Completadas", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());

        JPanel completedTasksPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(completedTasksPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        dialog.add(scrollPane, BorderLayout.CENTER);

        List<Task> completedTasks = controller.getCompletedTasks();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        for (Task task : completedTasks) {
            completedTasksPanel.add(TaskPanelFactory.createCompletedTaskPanel(task, controller, this), gbc);
            gbc.gridy++;
        }
        gbc.weighty = 1.0;
        completedTasksPanel.add(Box.createVerticalGlue(), gbc); // Add glue to push components to the top

        dialog.setVisible(true);
    }

    public static void launchApp() {
        SwingUtilities.invokeLater(() -> {
            TaskView app = new TaskView();
            app.setVisible(true);
        });
    }
}