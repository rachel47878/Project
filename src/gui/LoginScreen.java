package gui;

import db.DBConnection;
import gui.manager.ManagerDashboard;
import gui.employee.EmployeeDashboard;
import gui.admin.AdminDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginScreen extends JFrame {

    // Components
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    public LoginScreen() {

        // Frame setup
        setTitle("HR System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);                        // Centres the window to the screen
        setResizable(false);

        // Panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        // Title
        titleLabel = new JLabel("HR System Login");
        titleLabel.setBounds(130, 20, 200, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel);

        // Username
        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(60, 70, 100, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 70, 160, 25);
        panel.add(usernameField);

        // Password
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(60, 110, 100, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 110, 160, 25);
        panel.add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setBounds(150, 155, 100, 30);
        panel.add(loginButton);

        // Message label (for errors)
        messageLabel = new JLabel("");
        messageLabel.setBounds(60, 200, 300, 25);
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel);

        // Login button action
        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {

                login();

            }
        });

        setVisible(true);
    }

    private void login() {

        // Get input values
        // Didn't trim either field - spaces could be intentional or a security risk for passwords
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Validate fields are not empty
        if (username.isEmpty()) {
            messageLabel.setText("Please enter a username.");
            return;
        }

        if (password.isEmpty()) {
            messageLabel.setText("Please enter a password.");
            return;
        }

        int employeeId = -1;        // Default value to indicate no user found

        try {
            // Establish connection
            connection = DBConnection.getConnection();

            // Query users table for matching username and password
            pstat = connection.prepareStatement("SELECT role FROM users WHERE username = ? AND password = ?");
            pstat.setString(1, username);
            pstat.setString(2, password);

            resultSet = pstat.executeQuery();

            if (resultSet.next()) {

                // Login successful - make sure to get role to route to dashboard
                String role = resultSet.getString("role");
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Login successful. Welcome, " + username + ".");

                // Route to correct dashboard based on role
                if (role.equals("HR Manager")) {
                    new ManagerDashboard(username);
                    dispose();                              // Close login screen
                } 
                
                else if (role.equals("Employee")) {       
                    // Look up employee_id by matching username to email
                    pstat = connection.prepareStatement("SELECT employee_id FROM employees WHERE email = ?");
                    pstat.setString(1, username + "@company.com");
                    resultSet = pstat.executeQuery();

                    if (resultSet.next()) {
                        employeeId = resultSet.getInt("employee_id");
                    }

                    new EmployeeDashboard(username, employeeId);
                    dispose();
                } 
                
                else {
                    new AdminDashboard(username);
                    dispose();
                } 

            } 
            
            else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid username or password.");
            }

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Database error. Please try again.");
        } 
        
        finally {
            try {

                if (resultSet != null) {
                    resultSet.close();
                }

                if (pstat != null) {
                    pstat.close();
                }

                if (connection != null) {
                    connection.close();
                }

            } 
            
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}