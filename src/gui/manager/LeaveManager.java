package gui.manager;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class LeaveManager extends JFrame {

    // Colours
    private static final Color ACCENT = new Color(102, 51, 153);
    private static final Color BACKGROUND = new Color(245, 245, 245);

    // Table
    private JTable leaveTable;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton approveButton;
    private JButton rejectButton;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    // -1 means no row selected
    private int selectedRequestId = -1;

    public LeaveManager() {

        super("Leave Manager");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Table
        String[] columns = {"Request ID", "Employee Name", "Start Date", "End Date", "Leave Type", "Status", "Comment"};
        tableModel = new DefaultTableModel(columns, 0);

        leaveTable = new JTable(tableModel);
        leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leaveTable.setBackground(BACKGROUND);
        add(new JScrollPane(leaveTable), BorderLayout.CENTER);

        // Row click - get request ID for approve/reject
        leaveTable.getSelectionModel().addListSelectionListener(new RowClickHandler());

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND);

        approveButton = new JButton("Approve");
        rejectButton = new JButton("Reject");

        approveButton.setBackground(ACCENT);
        rejectButton.setBackground(ACCENT);
        approveButton.setForeground(Color.WHITE);
        rejectButton.setForeground(Color.WHITE);

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Handlers
        ButtonHandler handler = new ButtonHandler();
        approveButton.addActionListener(handler);
        rejectButton.addActionListener(handler);

        loadLeave();

        setVisible(true);
    }

    // Load leave
    private void loadLeave() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT l.request_id, e.first_name, e.last_name, l.start_date, l.end_date, l.leave_type, l.status, l.comment FROM `leave` l INNER JOIN employees e ON l.employee_id = e.employee_id ORDER BY l.status, l.start_date");
            resultSet = pstat.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {

                tableModel.addRow(new Object[]{

                    resultSet.getInt("request_id"),
                    resultSet.getString("first_name") + " " + resultSet.getString("last_name"),
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

    // Update leave status
    private void updateStatus(String status) {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("UPDATE `leave` SET status=? WHERE request_id=?");      // Leave is a reserved word, backticks needed

            pstat.setString(1, status);
            pstat.setInt(2, selectedRequestId);

            pstat.executeUpdate();
            JOptionPane.showMessageDialog(LeaveManager.this, "Leave request " + status + " .");
            selectedRequestId = -1;                                                                         // Reset selection for future reference
            loadLeave();

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(LeaveManager.this, "Error updating leave request!");
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

                int row = leaveTable.getSelectedRow();

                if (row != -1) {
                    selectedRequestId = (int) tableModel.getValueAt(row, 0);        // Get request ID from hidden column
                }
            }
        }
    }

    // Button handler
    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            // Possible future error, no check for existing status
            
            if (selectedRequestId == -1) {
                JOptionPane.showMessageDialog(LeaveManager.this, "Please select a leave request first.");
                return;
            }

            if (event.getSource() == approveButton) {
                int confirm = JOptionPane.showConfirmDialog(LeaveManager.this, "Approve this leave request?", "Confirm Approve", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    updateStatus("Approved");
                }

            } 
            
            else if (event.getSource() == rejectButton) {

                int confirm = JOptionPane.showConfirmDialog(LeaveManager.this, "Reject this leave request?", "Confirm Reject", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    updateStatus("Rejected");
                }
            }
        }
    }
}