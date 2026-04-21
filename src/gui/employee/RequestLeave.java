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
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class RequestLeave extends JFrame {

    // Colours
    private static final Color ACCENT = new Color(102, 51, 153);
    private static final Color BACKGROUND = new Color(245, 245, 245);

    // Fields
    private JComboBox leaveTypeBox;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField commentField;
    private JTable leaveTable;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton submitButton;
    private JButton clearButton;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    // Employee ID (passed from dashboard)
    private int employeeId;

    public RequestLeave(int employeeId) {

        super("Request Leave");
        this.employeeId = employeeId;
        setSize(800, 600);                              
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBackground(BACKGROUND);

        leaveTypeBox = new JComboBox(new String[]{"Annual", "Sick", "Unpaid", "Maternity", "Other"});
        startDateField = new JTextField();
        endDateField = new JTextField();
        commentField = new JTextField();

        formPanel.add(new JLabel("Leave Type:"));
        formPanel.add(leaveTypeBox);
        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        formPanel.add(startDateField);
        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        formPanel.add(endDateField);
        formPanel.add(new JLabel("Comment (optional):"));
        formPanel.add(commentField);

        add(formPanel, BorderLayout.NORTH);             

        // Table
        String[] columns = {"Start Date", "End Date", "Leave Type", "Status", "Comment"};
        tableModel = new DefaultTableModel(columns, 0);

        leaveTable = new JTable(tableModel);
        leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leaveTable.setBackground(BACKGROUND);
        add(new JScrollPane(leaveTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND);

        submitButton = new JButton("Submit");
        clearButton = new JButton("Clear");

        submitButton.setBackground(ACCENT);
        clearButton.setBackground(ACCENT);
        submitButton.setForeground(Color.WHITE);
        clearButton.setForeground(Color.WHITE);

        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Handlers
        ButtonHandler handler = new ButtonHandler();
        submitButton.addActionListener(handler);
        clearButton.addActionListener(handler);

        loadLeave();

        setVisible(true);
    }

    // Validation
    private boolean validateFields() {

        if (startDateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Start date cannot be empty!");
            return false;
        }

        if (!startDateField.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Start date must be in format YYYY-MM-DD!");
            return false;
        }

        if (endDateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "End date cannot be empty!");
            return false;
        }

        if (!endDateField.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "End date must be in format YYYY-MM-DD!");
            return false;
        }

        return true;
    }

    // Clear form
    private void clearForm() {

        leaveTypeBox.setSelectedIndex(0);
        startDateField.setText("");
        endDateField.setText("");
        commentField.setText("");

    }

    // Insert leave request
    private void insertLeave() {

        try {
            connection = DBConnection.getConnection();
            // Request sets to pending by default for later approval
            pstat = connection.prepareStatement("INSERT INTO `leave` (employee_id, start_date, end_date, leave_type, status, comment) VALUES (?, ?, ?, ?, 'Pending', ?)");

            pstat.setInt(1, employeeId);
            pstat.setString(2, startDateField.getText().trim());
            pstat.setString(3, endDateField.getText().trim());
            pstat.setString(4, leaveTypeBox.getSelectedItem().toString());
            pstat.setString(5, commentField.getText().trim());

            pstat.executeUpdate();
            JOptionPane.showMessageDialog(RequestLeave.this, "Leave request submitted successfully.");
            clearForm();
            loadLeave();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(RequestLeave.this, "Error submitting leave request!");
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

    private void loadLeave() {
    try {
        connection = DBConnection.getConnection();
        pstat = connection.prepareStatement("SELECT start_date, end_date, leave_type, status, comment FROM `leave` WHERE employee_id = ? ORDER BY start_date DESC");
        pstat.setInt(1, employeeId);
        resultSet = pstat.executeQuery();

        tableModel.setRowCount(0);

        while (resultSet.next()) {

            tableModel.addRow(new Object[]{

                resultSet.getString("start_date"),
                resultSet.getString("end_date"),
                resultSet.getString("leave_type"),
                resultSet.getString("status"),
                resultSet.getString("comment")

            });
        }
    } 
    
    catch (SQLException sqlException) {
        sqlException.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading leave requests!");
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

    // Button handler
    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            if (event.getSource() == submitButton) {
                if (validateFields()) {
                    insertLeave();
                }
            } 
            
            else if (event.getSource() == clearButton) {
                clearForm();
            }
        }
    }
}