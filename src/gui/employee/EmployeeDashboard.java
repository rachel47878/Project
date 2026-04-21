package gui.employee;

import gui.employee.ViewMyDetails;
import gui.employee.ViewMyPayroll;
import gui.employee.ViewMyTraining;
import gui.employee.ViewAnnouncements;
import gui.employee.RequestLeave;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

public class EmployeeDashboard extends JFrame {

    // Colours
    private static final Color ACCENT = new Color(102,51,153);              // Purple
    private static final Color BACKGROUND = new Color(245,245,245);         // Grey
    private static final Color TEXT = Color.WHITE;                                  

    // Menu bar and menu
    private JMenuBar menuBar;
    private JMenu myDetails;
    private JMenu myPayroll;
    private JMenu myLeave;
    private JMenu myTraining;
    private JMenu myAnnouncements;

    // Menu items
    private JMenuItem viewMyDetails;
    private JMenuItem viewMyPayroll;
    private JMenuItem viewMyLeave;
    private JMenuItem viewMyTraining;
    private JMenuItem viewMyAnnouncements;

    // Welcome label
    private JLabel welcomeLabel;

    private int employeeId;        

    public EmployeeDashboard(String username, int employeeId) {

        // Set up frame
        super("HR System - Employee Dashboard");
        this.employeeId = employeeId;                       // Store employee ID
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main panel
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        add(panel, BorderLayout.CENTER);

        // Welcome label
        welcomeLabel = new JLabel ("Welcome " + username, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        welcomeLabel.setForeground(ACCENT);
        panel.add(welcomeLabel);

        // Menu bar
        menuBar = new JMenuBar();
        menuBar.setBackground(ACCENT);

        // Details menu
        myDetails = new JMenu("My Details");
        myDetails.setForeground(TEXT);
        viewMyDetails = new JMenuItem("View/Edit Details");
        myDetails.add(viewMyDetails);
        menuBar.add(myDetails);

        // Payroll menu
        myPayroll = new JMenu("My Payroll");
        myPayroll.setForeground(TEXT);
        viewMyPayroll = new JMenuItem("View Payroll");
        myPayroll.add(viewMyPayroll);
        menuBar.add(myPayroll);

        // Leave menu
        myLeave = new JMenu("My Leave");
        myLeave.setForeground(TEXT);
        viewMyLeave = new JMenuItem("View/Request Leave");
        myLeave.add(viewMyLeave);
        menuBar.add(myLeave);

        // TRaining menu
        myTraining = new JMenu("My Training");
        myTraining.setForeground(TEXT);
        viewMyTraining =  new JMenuItem("View Training");
        myTraining.add(viewMyTraining);
        menuBar.add(myTraining);

        // Announcements menu
        myAnnouncements = new JMenu("My Announcements");
        myAnnouncements.setForeground(TEXT);
        viewMyAnnouncements = new JMenuItem("View Announcements");
        myAnnouncements.add(viewMyAnnouncements);
        menuBar.add(myAnnouncements);

        // Log out button
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

        // Attach the menu to the frame
        setJMenuBar(menuBar);

        // Register handlers
        MenuHandler handler = new MenuHandler();
        viewMyDetails.addActionListener(handler);
        viewMyPayroll.addActionListener(handler);
        viewMyLeave.addActionListener(handler);
        viewMyTraining.addActionListener(handler);
        viewMyAnnouncements.addActionListener(handler);

        setVisible(true);

    }

    // Event handlers
    private class MenuHandler implements ActionListener {

            public void actionPerformed(ActionEvent event) {
    
            if (event.getSource() == viewMyDetails) {
                new ViewMyDetails(employeeId);

            } 
            
            else if (event.getSource() == viewMyPayroll) {
                new ViewMyPayroll(employeeId);

            } 
            
            else if (event.getSource() == viewMyLeave) {
                new RequestLeave(employeeId);

            } 
            
            else if (event.getSource() == viewMyTraining) {
                new ViewMyTraining(employeeId);

            } 
            
            else if (event.getSource() == viewMyAnnouncements) {
                new ViewAnnouncements();        // No employee ID needed - shows all announcements
            }

        }
}
}