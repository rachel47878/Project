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
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class AnnouncementsManager extends JFrame {

    // Colours
    private static final Color ACCENT = new Color(102, 51, 153);
    private static final Color BACKGROUND = new Color(245, 245, 245);

    // Table
    private JTable announcementTable;
    private DefaultTableModel tableModel;

    // Form fields
    private JTextField titleField;
    private JTextArea  contentArea;         // JTextArea for multi line input (suitable for announcements)

    // Buttons
    private JButton saveButton;
    private JButton deleteButton;
    private JButton clearButton;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    // -1 means no announcement was selected
    private int selectedAnnouncementId = -1;

    public AnnouncementsManager() {

        super("Announcements - Manager");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);              // Can't use EXIT_ON_CLOSE - would close entire app
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Table
        String[] columns = {"ID", "Title", "Content", "Date Posted"};
        tableModel = new DefaultTableModel(columns, 0);

        announcementTable = new JTable(tableModel);
        announcementTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        announcementTable.setBackground(BACKGROUND);
        add(new JScrollPane(announcementTable), BorderLayout.CENTER);

        // Pre-fill form when a row is clicked
        announcementTable.getSelectionModel().addListSelectionListener(new RowClickHandler());

        // Form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));      // 2 rows, 2 columns - 5px gaps
        formPanel.setBackground(BACKGROUND);

        titleField  = new JTextField();
        contentArea = new JTextArea(3, 20);                             // 3 rows tall, 20 columns wide
        contentArea.setLineWrap(true);                                  // Used to wrap the text
        contentArea.setWrapStyleWord(true);                             // Ensures no word is cut off midway

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Content:"));
        formPanel.add(new JScrollPane(contentArea));                    // Scroll pane for text

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

        loadAnnouncements();

        setVisible(true);
    }

    // Load announcements method
    private void loadAnnouncements() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT announcement_id, title, content, date_posted FROM announcements ORDER BY date_posted DESC");
            resultSet = pstat.executeQuery();

            tableModel.setRowCount(0);                                  // Clear existing rows before reloading

            while (resultSet.next()) {

                tableModel.addRow(new Object[]{

                    resultSet.getInt("announcement_id"),
                    resultSet.getString("title"),
                    resultSet.getString("content"),
                    resultSet.getString("date_posted")
                });
            }

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading announcements!");

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

        // Only need to validate title & content. The date is auto populated
        if (titleField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Title cannot be empty!");
            return false;

        }

        // Check if content is empty (after being trimmed)
        if (contentArea.getText().trim().isEmpty()) {           

            JOptionPane.showMessageDialog(this, "Must contain text!");
            return false;

        }

        return true;
    }

    // Clear form
    private void clearForm() {

        selectedAnnouncementId = -1;
        titleField.setText("");
        contentArea.setText("");
        announcementTable.clearSelection();

    }

    // Insert announcement
    private void insertAnnouncement() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("INSERT INTO announcements (title, content, date_posted) VALUES (?, ?, ?)");

            pstat.setString(1, titleField.getText().trim());
            pstat.setString(2, contentArea.getText().trim());
            pstat.setString(3, LocalDate.now().toString());             // Auto populate todays date

            pstat.executeUpdate();
            JOptionPane.showMessageDialog(AnnouncementsManager.this, "Announcement added successfully.");
            clearForm();
            loadAnnouncements();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(AnnouncementsManager.this, "Error adding announcement.");
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

    // Update announcement
    private void updateAnnouncement() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("UPDATE announcements SET title=?, content=? WHERE announcement_id=?");

            pstat.setString(1, titleField.getText().trim());
            pstat.setString(2, contentArea.getText().trim());
            pstat.setInt(3, selectedAnnouncementId);

            pstat.executeUpdate();
            JOptionPane.showMessageDialog(AnnouncementsManager.this, "Announcement updated successfully.");
            clearForm();
            loadAnnouncements();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(AnnouncementsManager.this, "Error updating announcement.");
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

    // Delete announcement
    private void deleteAnnouncement() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("DELETE FROM announcements WHERE announcement_id=?");

            pstat.setInt(1, selectedAnnouncementId);
            pstat.executeUpdate();
            JOptionPane.showMessageDialog(AnnouncementsManager.this, "Announcement deleted successfully.");
            clearForm();
            loadAnnouncements();
        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(AnnouncementsManager.this, "Error deleting announcement.");
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

                int row = announcementTable.getSelectedRow();

                if (row != -1) {

                    selectedAnnouncementId = (int) tableModel.getValueAt(row, 0);
                    titleField.setText(tableModel.getValueAt(row, 1).toString());
                    contentArea.setText(tableModel.getValueAt(row, 2).toString());
                }
            }
        }
    }

    // Button handler
    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            if (event.getSource() == saveButton) {

                if (validateFields()) {

                    if (selectedAnnouncementId == -1) {
                        insertAnnouncement();
                    } 
                    
                    else {
                        updateAnnouncement();
                    }
                }

            } 
            
            else if (event.getSource() == deleteButton) {

                if (selectedAnnouncementId == -1) {
                    JOptionPane.showMessageDialog(AnnouncementsManager.this, "Please select an announcement to delete.");
                    return;
                } 
                
                else {
                    int confirm = JOptionPane.showConfirmDialog(AnnouncementsManager.this, "Are you sure you want to delete this announcement?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteAnnouncement();
                    }
                }

            } 
            
            else if (event.getSource() == clearButton) {
                clearForm();
            }
        }
    }
}