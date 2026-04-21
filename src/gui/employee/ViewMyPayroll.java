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

public class ViewMyPayroll extends JFrame {

    // Colours
    private static final Color BACKGROUND = new Color(245, 245, 245);

    // Table
    private JTable payrollTable;
    private DefaultTableModel tableModel;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    private int employeeId;

    public ViewMyPayroll(int employeeId) {

        super("My Payroll");
        this.employeeId = employeeId;
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Table
        String[] columns = {"Payment Date", "Gross Pay", "Tax", "Deductions", "Net Pay", "Bonus"};
        tableModel = new DefaultTableModel(columns, 0);

        payrollTable = new JTable(tableModel);
        payrollTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        payrollTable.setBackground(BACKGROUND);
        add(new JScrollPane(payrollTable), BorderLayout.CENTER);

        loadPayroll();

        setVisible(true);
    }

    // Load payroll 
    private void loadPayroll() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT payment_date, gross_pay, tax, deductions, net_pay, bonus FROM payroll WHERE employee_id = ? ORDER BY payment_date DESC");            
            pstat.setInt(1, employeeId);
            resultSet = pstat.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {

                tableModel.addRow(new Object[]{

                    resultSet.getString("payment_date"),
                    resultSet.getDouble("gross_pay"),
                    resultSet.getDouble("tax"),
                    resultSet.getDouble("deductions"),
                    resultSet.getDouble("net_pay"),
                    resultSet.getDouble("bonus")

                });
            }

        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading payroll records!");
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