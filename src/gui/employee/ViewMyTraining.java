package gui.employee;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ViewMyTraining extends JFrame {

    // Colours
    private static final Color BACKGROUND = new Color(245, 245, 245);

    // Table
    private JTable trainingTable;
    private DefaultTableModel tableModel;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    private int employeeId;

    public ViewMyTraining(int employeeId) {

        super("My Training");
        this.employeeId = employeeId;
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Table
        String[] columns = {"Course Title", "Status", "Due Date", "Feedback"};
        tableModel = new DefaultTableModel(columns, 0);

        trainingTable = new JTable(tableModel);
        trainingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trainingTable.setBackground(BACKGROUND);
        add(new JScrollPane(trainingTable), BorderLayout.CENTER);

        loadTraining();

        setVisible(true);
    }

    // Load training
    private void loadTraining() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT t.title, et.status, et.due_date, et.feedback FROM employee_training et INNER JOIN training t ON et.course_id = t.course_id WHERE et.employee_id = ? ORDER BY et.due_date");
            pstat.setInt(1, employeeId);
            resultSet = pstat.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {

                tableModel.addRow(new Object[]{

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
}