package gui.manager;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewAmendEmployee extends JFrame {

    // Colours
    private static final Color ACCENT = new Color(102,51,153);
    private static final Color BACKGROUND = new Color(245,245,245);

    // Table
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    // Fields
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField positionField;
    private JTextField jobTitleField;
    private JTextField hireDateField;
    private JTextField emailField;
    private JComboBox departmentBox;                // Use combo box for department (prevent incorrect entries)

    // Buttons
    private JButton saveButton;
    private JButton deleteButton;
    private JButton clearButton;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    // Initialise below to -1 to show no employee selected
    // This will be used as the toggle to save or insert (i.e. if no emp selected, insert a new employee)

    private int selectedEmployeeId = -1;

    public ViewAmendEmployee() {

        super("View/Amend Employee");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Table
        String[] columns = {"ID", "First Name", "Last Name", "Position", "Job Title", "Hire Date", "Email", "Department"};
        tableModel = new DefaultTableModel(columns, 0);                         // DefaultTableModel expects column names and initial row count (0)

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);             // Only allow single selection
        employeeTable.setBackground(BACKGROUND);
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);                        // Add table to center with scroll pane

        // Populate fields after selection
        employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                
                if (!event.getValueIsAdjusting()) {                                     // .getValueIsAdjusting() belongs to ListSelectionEvent, returns true if selection still in progress
                    
                    int row = employeeTable.getSelectedRow();

                    if (row != -1) {                                                    // Check if a row is actually selected

                        selectedEmployeeId = (int) tableModel.getValueAt(row, 0);       // Get employee ID from the first column - use casting as getValueAt returns Object otherwise
                        firstNameField.setText((String) tableModel.getValueAt(row, 1));
                        lastNameField.setText((String) tableModel.getValueAt(row, 2));

                        if (tableModel.getValueAt(row, 3) != null) {
                            positionField.setText(tableModel.getValueAt(row, 3).toString());
                        } 
                        
                        else {
                            positionField.setText("");
                        }

                        jobTitleField.setText((String) tableModel.getValueAt(row, 4));
                        hireDateField.setText((String) tableModel.getValueAt(row, 5));
                        emailField.setText((String) tableModel.getValueAt(row, 6));
                        departmentBox.setSelectedItem((String) tableModel.getValueAt(row, 7));

                    }

                }

            }

        }
        );

        // Form 
        JPanel formPanel = new JPanel(new GridLayout(4,4,5,5));         // 4 rows, 4 columns, 5px gaps (horizontal and vertical)
        formPanel.setBackground(BACKGROUND);

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        positionField = new JTextField();
        jobTitleField = new JTextField();
        hireDateField = new JTextField();
        emailField = new JTextField();
        departmentBox = new JComboBox();                               // Prevents incorrect entries by enforcing specific options

        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Position:"));
        formPanel.add(positionField);
        formPanel.add(new JLabel("Job Title:"));
        formPanel.add(jobTitleField);
        formPanel.add(new JLabel("Hire Date (YYYY-MM-DD):"));     // MySQL stores dates this way - easier to use this format
        formPanel.add(hireDateField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(departmentBox);

        add(formPanel, BorderLayout.SOUTH);                             // Fields sit at bottom, below table

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

        add(buttonPanel, BorderLayout.NORTH);                            // Button all sit at top, above table

        // Handlers
        ButtonHandler handler = new ButtonHandler();
        saveButton.addActionListener(handler);
        deleteButton.addActionListener(handler);
        clearButton.addActionListener(handler);

        loadDepartments();                                              // Has to run first - otherwise dept combo box will be empty when loading employees
        loadEmployees();

        setVisible(true);
    }

    // Method to load departments
    private void loadDepartments() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT department_name FROM departments");
            resultSet = pstat.executeQuery();

            departmentBox.removeAllItems();                                                     // Clear existing items to prevent duplicates

            while (resultSet.next()) {
                departmentBox.addItem(resultSet.getString("department_name"));
            }
        }

        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading departments!");          // Use 'this' to refer to current JFrame so dialog is centered on it
        } 
        
        finally {
            try {
                // Need to check if these are null before closing - NullPointerException otherwise

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

    private void loadEmployees() {

        try {
            connection = DBConnection.getConnection();
            // Use INNER JOIN to get department name from dept table (instead of just ID)
            pstat = connection.prepareStatement("SELECT e.employee_id, e.first_name, e.last_name, e.position, e.job_title, e.hire_date, e.email, d.department_name FROM employees e INNER JOIN departments d ON e.department_id = d.department_id ORDER BY e.employee_id");
            resultSet = pstat.executeQuery();

            // Clear existing rows to prevent duplicates
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                tableModel.addRow(new Object[] {                    // Use Object array to add row with multiple columns
                    resultSet.getInt("employee_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("position"),
                    resultSet.getString("job_title"),
                    resultSet.getDate("hire_date"),
                    resultSet.getString("email"),
                    resultSet.getString("department_name")
                });
            }
        }

        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employees!");
        } 
        
        finally {
            // Check if these are null before closing - NullPointerException error possible

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

    // Validation checks
    private boolean validateFields() {

        // Check if any fields are empty
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name cannot be empty!");
            return false;
        }

        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name cannot be empty!");
            return false;
        }

        if (jobTitleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Job title cannot be empty!");
            return false;
        }

        if (hireDateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hire date cannot be empty!");
            return false;
        }

        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email cannot be empty!");
            return false;
        }

        // Validate hire date
        if(!hireDateField.getText().matches("\\d{4}-\\d{2}-\\d{2}")) {                          // Need to follow mySQL date format
            JOptionPane.showMessageDialog(this, "Hire date must be in format YYYY-MM-DD!");
            return false;
        }

        // Validate email
        if(!emailField.getText().trim().contains("@")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!");
            return false;
        }

        return true;
    }

    // Handler for button clicks
    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            if(event.getSource() == saveButton) {

                if (validateFields()) {

                    if (selectedEmployeeId == -1) {
                        insertEmployee();
                    } 
                    
                    else {
                        updateEmployee();
                    }

                }
            }

            else if (event.getSource() == deleteButton) {

                if(selectedEmployeeId == -1) {
                    JOptionPane.showMessageDialog(ViewAmendEmployee.this, "Please select an employee to delete.");     // ViewAmendEmployee.this is needed as ButtonHandler is an inner class
                    return;
                }

                else {
                    int confirm = JOptionPane.showConfirmDialog(ViewAmendEmployee.this, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION); // Prevent accidental deletions

                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteEmployee();
                    }
                }
            }

            else if (event.getSource() == clearButton) {
                clearForm();
            }

        }
    }

    // Insert employee method
    private void insertEmployee() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement(
                "INSERT INTO employees (first_name, last_name, position, job_title, hire_date, email, department_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, (SELECT department_id FROM departments WHERE department_name = ?))"
            );

            pstat.setString(1, firstNameField.getText().trim());
            pstat.setString(2, lastNameField.getText().trim());
            pstat.setString(3, positionField.getText().trim());
            pstat.setString(4, jobTitleField.getText().trim());
            pstat.setString(5, hireDateField.getText().trim());
            pstat.setString(6, emailField.getText().trim());
            pstat.setString(7, departmentBox.getSelectedItem().toString());

            pstat.executeUpdate();
            JOptionPane.showMessageDialog(ViewAmendEmployee.this, "Employee added successfully.");
            clearForm();
            loadEmployees();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(ViewAmendEmployee.this, "Error adding employee.");
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

    // Update employee method
    private void updateEmployee() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement(
                "UPDATE employees SET first_name=?, last_name=?, position=?, job_title=?, hire_date=?, email=?, " +
                "department_id=(SELECT department_id FROM departments WHERE department_name=?) " +
                "WHERE employee_id=?"
            );

            pstat.setString(1, firstNameField.getText().trim());
            pstat.setString(2, lastNameField.getText().trim());
            pstat.setString(3, positionField.getText().trim());
            pstat.setString(4, jobTitleField.getText().trim());
            pstat.setString(5, hireDateField.getText().trim());
            pstat.setString(6, emailField.getText().trim());
            pstat.setString(7, departmentBox.getSelectedItem().toString());
            pstat.setInt(8, selectedEmployeeId);

            pstat.executeUpdate();
            JOptionPane.showMessageDialog(ViewAmendEmployee.this, "Employee updated successfully.");
            clearForm();
            loadEmployees();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(ViewAmendEmployee.this, "Error updating employee.");
        } 
        
        finally {
            try {
                if (pstat != null) pstat.close();
                if (connection != null) connection.close();
            } 
            
            catch (Exception exception) {
                exception.printStackTrace(); 
            }
        }
    }

    // Delete employee method
    
    private void deleteEmployee() {
        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement(
                "DELETE FROM employees WHERE employee_id = ?"
            );

            pstat.setInt(1, selectedEmployeeId);
            pstat.executeUpdate();
            JOptionPane.showMessageDialog(ViewAmendEmployee.this, "Employee deleted successfully.");
            clearForm();
            loadEmployees();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(ViewAmendEmployee.this, "Error deleting employee.");
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

    // Clear form method
    private void clearForm() {

        selectedEmployeeId = -1;
        firstNameField.setText("");
        lastNameField.setText("");
        positionField.setText("");
        jobTitleField.setText("");
        hireDateField.setText("");
        emailField.setText("");
        departmentBox.setSelectedIndex(0);
        employeeTable.clearSelection();
    }

}