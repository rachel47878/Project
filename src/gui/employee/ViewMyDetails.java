package gui.employee;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ViewMyDetails extends JFrame {

    // Colours
    private static final Color ACCENT = new Color(102, 51, 153);
    private static final Color BACKGROUND = new Color(245, 245, 245);

    // Fields
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField jobTitleField;
    private JTextField hireDateField;
    private JTextField emailField;
    private JTextField departmentField;

    // Buttons
    private JButton saveButton;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    // Store employee ID for update
    private int employeeId;

    public ViewMyDetails(int employeeId) {

        super("My Details");
        this.employeeId = employeeId;
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBackground(BACKGROUND);

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        jobTitleField = new JTextField();
        hireDateField = new JTextField();
        emailField = new JTextField();
        departmentField = new JTextField();

        // Hire date and department not editable
        hireDateField.setEditable(false);
        departmentField.setEditable(false);

        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Job Title:"));
        formPanel.add(jobTitleField);
        formPanel.add(new JLabel("Hire Date:"));
        formPanel.add(hireDateField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(departmentField);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND);

        saveButton = new JButton("Save");
        saveButton.setBackground(ACCENT);
        saveButton.setForeground(Color.WHITE);

        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Handler
        saveButton.addActionListener(new ButtonHandler());

        loadDetails();

        setVisible(true);
    }

    // Load employee details
    private void loadDetails() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT e.first_name, e.last_name, e.job_title, e.hire_date, e.email, d.department_name FROM employees e INNER JOIN departments d ON e.department_id = d.department_id WHERE e.employee_id = ?");
            pstat.setInt(1, employeeId);
            resultSet = pstat.executeQuery();

            if (resultSet.next()) {
                firstNameField.setText(resultSet.getString("first_name"));
                lastNameField.setText(resultSet.getString("last_name"));
                jobTitleField.setText(resultSet.getString("job_title"));
                hireDateField.setText(resultSet.getString("hire_date"));
                emailField.setText(resultSet.getString("email"));
                departmentField.setText(resultSet.getString("department_name"));
            }

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading details!");
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

    // Validation
    private boolean validateFields() {

        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name cannot be empty!");
            return false;
        }

        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name cannot be empty!");
            return false;
        }

        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email cannot be empty!");
            return false;
        }

        if (!emailField.getText().trim().contains("@")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!");
            return false;
        }

        return true;
    }

    // Update employee details
    private void updateDetails() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement( "UPDATE employees SET first_name=?, last_name=?, job_title=?, email=? WHERE employee_id=?");

            pstat.setString(1, firstNameField.getText().trim());
            pstat.setString(2, lastNameField.getText().trim());
            pstat.setString(3, jobTitleField.getText().trim());
            pstat.setString(4, emailField.getText().trim());
            pstat.setInt(5, employeeId);

            pstat.executeUpdate();
            JOptionPane.showMessageDialog(ViewMyDetails.this, "Details updated successfully.");

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(ViewMyDetails.this, "Error updating details!");
        } 
        
        finally {

            try {
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

    // Button handler
    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            if (validateFields()) {
                updateDetails();
            }
            
        }
    }
}