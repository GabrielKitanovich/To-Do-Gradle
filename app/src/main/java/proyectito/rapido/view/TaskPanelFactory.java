
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton checkButton = new JButton("✔");
        JButton deleteButton = new JButton("✖");

        ActionListener deleteAction = e -> {
            controller.removeTask(task);
            taskView.updateTaskAreas();
        };

        checkButton.addActionListener(deleteAction);
        deleteButton.addActionListener(deleteAction);

        taskLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                taskView.showTaskDetails(task);
            }
        });

        buttonPanel.add(checkButton);
        buttonPanel.add(deleteButton);
        taskPanel.add(taskLabel, BorderLayout.CENTER);
        taskPanel.add(buttonPanel, BorderLayout.EAST);

        return taskPanel;
    }

    private static JButton createAddTaskButton(String category, TaskView taskView) {
        JButton addButton = new JButton("+");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> taskView.showAddTaskDialog(category));
        return addButton;
    }
}