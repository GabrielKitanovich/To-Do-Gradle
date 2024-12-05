package proyectito.rapido;

import proyectito.rapido.view.TaskView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class App {
    private static TrayIcon trayIcon;

    public static void main(String[] args) {
        TaskView.launchApp();
        setupSystemTray();
    }

    public static void setupSystemTray() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = null;
            try {
                image = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/icon.png"));
            } catch (Exception e) {
                System.err.println("Icon image not found. Please ensure the icon.png file is in the resources folder.");
                return;
            }
            PopupMenu popup = new PopupMenu();

            MenuItem openItem = new MenuItem("Open");
            openItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    TaskView.getInstance().setVisible(true);
                    TaskView.getInstance().setExtendedState(Frame.NORMAL);
                    tray.remove(trayIcon);
                }
            });
            popup.add(openItem);

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    System.exit(0);
                }
            });
            popup.add(exitItem);

            trayIcon = new TrayIcon(image, "Task App", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) { // Left-click
                        TaskView.getInstance().setVisible(true);
                        TaskView.getInstance().setExtendedState(Frame.NORMAL);
                        tray.remove(trayIcon);
                    }
                }
            });

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }
}
