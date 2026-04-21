package gui.manager;

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

public class PayrollManager extends JFrame {

    // Colour
    private static final Color BACKGROUND = new Color(245, 245, 245);

    // Table
    private JTable payrollTable;
    private DefaultTableModel tableModel;

    // Database
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;

    public PayrollManager() {

        super("Payroll Manager");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        // Table
        String[] columns = {"Payroll ID", "Employee Name", "Payment Date", "Gross Pay", "Tax", "Deductions", "Net Pay", "Bonus"};
        tableModel = new DefaultTableModel(columns, 0);

        payrollTable = new JTable(tableModel);
        payrollTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        payrollTable.setBackground(BACKGROUND);
        add(new JScrollPane(payrollTable), BorderLayout.CENTER);

        loadPayroll();

        setVisible(true);
    }

    // Load all payroll records
    private void loadPayroll() {

        try {
            connection = DBConnection.getConnection();
            pstat = connection.prepareStatement("SELECT p.payroll_id, e.first_name, e.last_name, p.payment_date, p.gross_pay, p.tax, p.deductions, p.net_pay, p.bonus FROM payroll p INNER JOIN employees e ON p.employee_id = e.employee_id ORDER BY p.payment_date DESC");
            resultSet = pstat.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {

                tableModel.addRow(new Object[]{

                    resultSet.getInt("payroll_id"),
                    resultSet.getString("first_name") + " " + resultSet.getString("last_name"),
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