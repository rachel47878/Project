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

public class ViewAnnouncements extends JFrame {

    // Colours
    private static final Color BACKGROUND = new Color(245, 245, 245);

    // Table
    private JTable announcementTable;
    private DefaultTableModel tableModel;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    public ViewAnnouncements() {

        super("Announcements");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Table
        String[] columns = {"Title", "Content", "Date Posted"};
        tableModel = new DefaultTableModel(columns, 0);

        announcementTable = new JTable(tableModel);
        announcementTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        // Allow only one row to be selected
        announcementTable.setBackground(BACKGROUND);
        add(new JScrollPane(announcementTable), BorderLayout.CENTER);                   // Add table to scroll pane

        loadAnnouncements();

        setVisible(true);
    }

    // Load announcements
    private void loadAnnouncements() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT title, content, date_posted FROM announcements ORDER BY date_posted DESC");
            resultSet = pstat.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {

                tableModel.addRow(new Object[]{

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
}