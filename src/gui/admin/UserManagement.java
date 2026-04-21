package gui.admin;

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
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class UserManagement extends JFrame {

    // Colours
    private static final Color ACCENT = new Color(102, 51, 153);
    private static final Color BACKGROUND = new Color(245, 245, 245);

    // Table
    private JTable userTable;
    private DefaultTableModel tableModel;

    // Form fields
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox roleBox;

    // Buttons
    private JButton saveButton;
    private JButton deleteButton;
    private JButton clearButton;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    // -1 means no row selected
    private int selectedUserId = -1;

    public UserManagement() {

        super("User Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Table
        String[] columns = {"User ID", "Username", "Role", "Last Login"};
        tableModel = new DefaultTableModel(columns, 0);

        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setBackground(BACKGROUND);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Row click - get user ID for delete
        userTable.getSelectionModel().addListSelectionListener(new RowClickHandler());

        // Form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBackground(BACKGROUND);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        roleBox = new JComboBox(new String[]{"Employee", "HR Manager", "Admin"});

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleBox);

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

        loadUsers();

        setVisible(true);
    }

    // Load all users
    private void loadUsers() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT user_id, username, role, last_login FROM users ORDER BY username");
            resultSet = pstat.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {

                tableModel.addRow(new Object[]{

                    resultSet.getInt("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("role"),
                    resultSet.getString("last_login")
                
                });
            }

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users!");
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

    // Validation for fields
    private boolean validateFields() {

        if (usernameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty!");
            return false;
        }

        if (new String(passwordField.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty!");
            return false;
        }

        return true;
    }

    // Clear form
    private void clearForm() {

        selectedUserId = -1;
        usernameField.setText("");
        passwordField.setText("");
        roleBox.setSelectedIndex(0);
        userTable.clearSelection();

    }

    // Insert new user
    private void insertUser() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");

            pstat.setString(1, usernameField.getText().trim());
            pstat.setString(2, new String(passwordField.getPassword()));                // passwordField.getPassword returns char array, need to convert to String
            pstat.setString(3, roleBox.getSelectedItem().toString());

            pstat.executeUpdate();
            JOptionPane.showMessageDialog(UserManagement.this, "User added successfully.");
            clearForm();
            loadUsers();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(UserManagement.this, "Error adding user!");
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

    // Delete user
    private void deleteUser() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("DELETE FROM users WHERE user_id = ?");

            pstat.setInt(1, selectedUserId);
            pstat.executeUpdate();
            JOptionPane.showMessageDialog(UserManagement.this, "User deleted successfully.");
            clearForm();
            loadUsers();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(UserManagement.this, "Error deleting user!");
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

            if (!event.getValueIsAdjusting()) {

                int row = userTable.getSelectedRow();
                
                if (row != -1) {
                    selectedUserId = (int) tableModel.getValueAt(row, 0);           // Get user ID from hidden column (cast to int) - would return Object otherwise
                }
            }
        }
    }

    // Button handler
    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            if (event.getSource() == saveButton) {
                if (validateFields()) {
                    insertUser();
                }

            } 
            
            else if (event.getSource() == deleteButton) {
                
                if (selectedUserId == -1) {
                    JOptionPane.showMessageDialog(UserManagement.this, "Please select a user to delete.");
                    return;
                } 
                
                else {
                    int confirm = JOptionPane.showConfirmDialog(UserManagement.this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteUser();
                    }
                }

            } 
            
            else if (event.getSource() == clearButton) {
                clearForm();
            }
        }
    }
}