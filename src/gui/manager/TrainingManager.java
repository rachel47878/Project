package gui.manager;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class TrainingManager extends JFrame {

    // Colours
    private static final Color ACCENT = new Color(102, 51, 153);
    private static final Color BACKGROUND = new Color(245, 245, 245);

    // Table
    private JTable trainingTable;
    private DefaultTableModel tableModel;

    // Form fields
    private JComboBox employeeBox;
    private JComboBox courseBox;
    private JComboBox statusBox;
    private JTextField dueDateField;
    private JTextField feedbackField;

    // Buttons
    private JButton saveButton;
    private JButton deleteButton;
    private JButton clearButton;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    // Track selected row for delete
    private int selectedEmployeeId = -1;
    private int selectedCourseId = -1;

    public TrainingManager() {

        super("Training Manager");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);                              // Dispose ensures only this window closes
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Table
        String[] columns = {"Employee ID", "Course ID", "Employee Name", "Course Title", "Status", "Due Date", "Feedback"};
        tableModel = new DefaultTableModel(columns, 0);

        trainingTable = new JTable(tableModel);
        trainingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);            // Only allow one row to be selected at a time
        trainingTable.setBackground(BACKGROUND);
        add(new JScrollPane(trainingTable), BorderLayout.CENTER);

        // Row click - only used to get IDs for delete
        trainingTable.getSelectionModel().addListSelectionListener(new RowClickHandler());

        // Form
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 5, 5));      // 3 rows, 4 columns, 5px gaps
        formPanel.setBackground(BACKGROUND);

        employeeBox = new JComboBox();
        courseBox = new JComboBox();
        statusBox = new JComboBox(new String[]{"Not Started", "In Progress", "Completed"});     // Fixed options
        dueDateField = new JTextField();
        feedbackField = new JTextField();

        formPanel.add(new JLabel("Employee:"));
        formPanel.add(employeeBox);
        formPanel.add(new JLabel("Course:"));
        formPanel.add(courseBox);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusBox);
        formPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        formPanel.add(dueDateField);
        formPanel.add(new JLabel("Feedback:"));
        formPanel.add(feedbackField);

        add(formPanel, BorderLayout.SOUTH);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND);

        saveButton = new JButton("Save");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        saveButton.setBackground(ACCENT);
        deleteButton.setBackground(ACCENT);
        clearButton.setBackground(ACCENT);
        saveButton.setForeground(Color.WHITE);
        deleteButton.setForeground(Color.WHITE);
        clearButton.setForeground(Color.WHITE);

        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Handlers
        ButtonHandler handler = new ButtonHandler();
        saveButton.addActionListener(handler);
        deleteButton.addActionListener(handler);
        clearButton.addActionListener(handler);

        loadEmployees();
        loadCourses();
        loadTraining();

        setVisible(true);
    }

    // Load employees
    private void loadEmployees() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT employee_id, first_name, last_name FROM employees ORDER BY first_name");
            resultSet = pstat.executeQuery();

            employeeBox.removeAllItems();

            while (resultSet.next()) 
                {employeeBox.addItem(resultSet.getString("first_name") + " " + resultSet.getString("last_name") + " (" + resultSet.getInt("employee_id") + ")");
            }

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employees!");
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

    // Load courses
    private void loadCourses() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT course_id, title FROM training ORDER BY title");
            resultSet = pstat.executeQuery();

            courseBox.removeAllItems();             // Clear existing items

            while (resultSet.next()) {
                courseBox.addItem(resultSet.getString("title") + " (" + resultSet.getInt("course_id") + ")");
            }

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading courses!");
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

    // Load training
    private void loadTraining() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT et.employee_id, et.course_id, e.first_name, e.last_name, t.title, et.status, et.due_date, et.feedback FROM employee_training et INNER JOIN employees e ON et.employee_id = e.employee_id INNER JOIN training t ON et.course_id = t.course_id ORDER BY e.first_name");
            resultSet = pstat.executeQuery();

            tableModel.setRowCount(0);              // Clear existing rows

            while (resultSet.next()) {

                tableModel.addRow(new Object[]{         

                    resultSet.getInt("employee_id"),
                    resultSet.getInt("course_id"),
                    resultSet.getString("first_name") + " " + resultSet.getString("last_name"),
                    resultSet.getString("title"),
                    resultSet.getString("status"),
                    resultSet.getString("due_date"),
                    resultSet.getString("feedback")
                });
            }

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading training records!");
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

    // Validate fields
    private boolean validateFields() {

        // Can't be empty
        if (dueDateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Due date cannot be empty!");
            return false;
        }

        // Must follow SQL format for dates
        if (!dueDateField.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Due date must be in format YYYY-MM-DD!");
            return false;
        }

        return true;
    }

    // Clear form
    private void clearForm() {

        selectedEmployeeId = -1;
        selectedCourseId = -1;
        employeeBox.setSelectedIndex(0);
        courseBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        dueDateField.setText("");
        feedbackField.setText("");
        trainingTable.clearSelection();

    }

    // Extract ID from combo box item
    private int extractId(String item) {

        String idStr = item.substring(item.lastIndexOf("(") + 1, item.lastIndexOf(")"));
        return Integer.parseInt(idStr);                 // Convert to integer

    }

    // Instert training
    private void insertTraining() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("INSERT INTO employee_training (employee_id, course_id, assigned_by, due_date, status, feedback) VALUES (?, ?, 1, ?, ?, ?)");

            pstat.setInt(1, extractId(employeeBox.getSelectedItem().toString()));       // Get the ID from combo box
            pstat.setInt(2, extractId(courseBox.getSelectedItem().toString()));     
            pstat.setString(3, dueDateField.getText().trim());
            pstat.setString(4, statusBox.getSelectedItem().toString());
            pstat.setString(5, feedbackField.getText().trim());

            pstat.executeUpdate();
            JOptionPane.showMessageDialog(TrainingManager.this, "Training assignment added successfully.");
            clearForm();
            loadTraining();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(TrainingManager.this, "Error adding training assignment.");
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

    // Delete training
    private void deleteTraining() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("DELETE FROM employee_training WHERE employee_id=? AND course_id=?");

            pstat.setInt(1, selectedEmployeeId);
            pstat.setInt(2, selectedCourseId);

            pstat.executeUpdate();
            JOptionPane.showMessageDialog(TrainingManager.this, "Training assignment deleted successfully.");
            clearForm();
            loadTraining();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(TrainingManager.this, "Error deleting training assignment.");
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

    // Row click handler
    private class RowClickHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent event) {

            if (!event.getValueIsAdjusting()) {                 // Movement needs to be finished

                int row = trainingTable.getSelectedRow();

                // Get the ID from column (not visible to user)
                if (row != -1) {
                    selectedEmployeeId = (int) tableModel.getValueAt(row, 0);       // Cast to int (stored as object)
                    selectedCourseId = (int) tableModel.getValueAt(row, 1);
                }
            }
        }
    }

    // Button handler
    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            if (event.getSource() == saveButton) {

                if (validateFields()) {
                    insertTraining();
                }

            } 
            
            else if (event.getSource() == deleteButton) {

                if (selectedEmployeeId == -1) {
                    JOptionPane.showMessageDialog(TrainingManager.this, "Please select a training record to delete.");
                    return;
                } 
                
                else {
                    int confirm = JOptionPane.showConfirmDialog(TrainingManager.this, "Are you sure you want to delete this training record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteTraining();
                    }
                }

            } 
            
            else if (event.getSource() == clearButton) {
                clearForm();
            }
        }
}
}