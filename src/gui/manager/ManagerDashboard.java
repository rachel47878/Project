package gui.manager;

import gui.manager.ViewAmendEmployee;
import gui.manager.AnnouncementsManager;
import gui.manager.TrainingManager;
import gui.manager.LeaveManager;
import gui.manager.PayrollManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

public class ManagerDashboard extends JFrame {

    // Colours used for dashboard. Used final to save on repetition and for convenience
    private static final Color ACCENT = new Color(102,51,153);              // Purple
    private static final Color BACKGROUND = new Color(245,245,245);         // Light grey
    private static final Color TEXT = Color.WHITE;                                 

    // Menu bar and related menus
    private JMenuBar menuBar;
    private JMenu employeeMenu;
    private JMenu trainingMenu;
    private JMenu leaveMenu;
    private JMenu payrollMenu;
    private JMenu announcementsMenu;

    // Menu items
    private JMenuItem viewEmployees;
    private JMenuItem viewTraining;
    private JMenuItem viewLeave;
    private JMenuItem viewPayroll;
    private JMenuItem viewAnnouncements;

    // Welcome 
    private JLabel welcomeLabel;

    public ManagerDashboard(String username) {

        // Set up frame
        super("HR System - Manager Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main panel
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        add(panel, BorderLayout.CENTER);    

        // Welcome label
        welcomeLabel = new JLabel("Welcome " + username, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));            // Font requires three parameters, so set Font.PLAIN
        welcomeLabel.setForeground(ACCENT);
        panel.add(welcomeLabel);

        // Menu bar
        menuBar = new JMenuBar();
        menuBar.setBackground(ACCENT);

        // Employess menu
        employeeMenu = new JMenu("Employees");
        employeeMenu.setForeground(TEXT);
        viewEmployees = new JMenuItem("View/Amend Employees");
        employeeMenu.add(viewEmployees);
        menuBar.add(employeeMenu);

        // Training menu
        trainingMenu = new JMenu("Training");
        trainingMenu.setForeground(TEXT);
        viewTraining = new JMenuItem("View/Manage Training");
        trainingMenu.add(viewTraining);
        menuBar.add(trainingMenu);

        // Leave menu
        leaveMenu = new JMenu("Leave");
        leaveMenu.setForeground(TEXT);
        viewLeave = new JMenuItem("View/Approve Leave");
        leaveMenu.add(viewLeave);
        menuBar.add(leaveMenu);

        // Payroll menu
        payrollMenu = new JMenu("Payroll");
        payrollMenu.setForeground(TEXT);
        viewPayroll = new JMenuItem("View Payroll");
        payrollMenu.add(viewPayroll);
        menuBar.add(payrollMenu);

        // Announcements menu
        announcementsMenu = new JMenu("Announcements");
        announcementsMenu.setForeground(TEXT);
        viewAnnouncements = new JMenuItem("View/Create Announcements");
        announcementsMenu.add(viewAnnouncements);
        menuBar.add(announcementsMenu);

        // Attach the menu bar to the frame
        setJMenuBar(menuBar);

        // Event handlers 
        MenuHandler handler = new MenuHandler();
        viewEmployees.addActionListener(handler);
        viewTraining.addActionListener(handler);
        viewLeave.addActionListener(handler);
        viewPayroll.addActionListener(handler);
        viewAnnouncements.addActionListener(handler);

        // Set frame to be visible after all components are added
        setVisible(true);
    }

        // Inner class to handle menu actions
        private class MenuHandler implements ActionListener {

            public void actionPerformed(ActionEvent event) {
    
                if (event.getSource() == viewEmployees) {
                    new ViewAmendEmployee();
                } 
                
                else if (event.getSource() == viewTraining) {
                    new TrainingManager();
                } 

                else if (event.getSource() == viewLeave) {
                    new LeaveManager();
                } 
                
                else if (event.getSource() == viewPayroll) {
                    new PayrollManager();
                } 

                else if (event.getSource() == viewAnnouncements) {
                    new AnnouncementsManager();
                } 
                
            }

    }
}