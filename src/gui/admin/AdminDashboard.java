package gui.admin;

import gui.admin.UserManagement;
import gui.employee.ViewAnnouncements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

public class AdminDashboard extends JFrame {

    // Colours 
    private static final Color ACCENT = new Color(102, 51, 153);                // Purple 
    private static final Color BACKGROUND = new Color(245, 245, 245);           // Light grey
    private static final Color TEXT = Color.WHITE; 

    // Menu bar and related menus
    private JMenuBar menuBar;
    private JMenu usersMenu;
    private JMenu announcementsMenu;

    // Menu items
    private JMenuItem manageUsers;
    private JMenuItem viewAnnouncements;

    // Welcome
    private JLabel welcomeLabel;

    public AdminDashboard(String username) {

        // Set up frame
        super("HR System - Admin Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main panel
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        add(panel, BorderLayout.CENTER);

        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(ACCENT);
        panel.add(welcomeLabel);

        // Menu bar
        menuBar = new JMenuBar();
        menuBar.setBackground(ACCENT);

        // Users menu
        usersMenu = new JMenu("Users");
        usersMenu.setForeground(TEXT);
        manageUsers = new JMenuItem("Manage Users");
        usersMenu.add(manageUsers);
        menuBar.add(usersMenu);

        // Announcements menu
        announcementsMenu = new JMenu("Announcements");
        announcementsMenu.setForeground(TEXT);
        viewAnnouncements = new JMenuItem("View Announcements");
        announcementsMenu.add(viewAnnouncements);
        menuBar.add(announcementsMenu);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(ACCENT);
        logoutButton.setForeground(Color.WHITE);

        logoutButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent event) {

            dispose();
            new gui.LoginScreen();
        }
        });

        menuBar.add(logoutButton);

        // Attach the menu bar to the frame
        setJMenuBar(menuBar);

        // Event handlers
        MenuHandler handler = new MenuHandler();
        manageUsers.addActionListener(handler);
        viewAnnouncements.addActionListener(handler);

        // Set frame to be visible after all components are added
        setVisible(true);
    }

    // Menu Handler
    private class MenuHandler implements ActionListener {
        
        public void actionPerformed(ActionEvent event) {

            if (event.getSource() == manageUsers) {
                new UserManagement();
            } 
            
            else if (event.getSource() == viewAnnouncements) {
                new ViewAnnouncements();
            } 
        
        }
    }
}