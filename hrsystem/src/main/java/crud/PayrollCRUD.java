package crud;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class PayrollCRUD {

    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;
    int i = 0;
    final String DATABASE_URL = "jdbc:mysql://localhost:3306/hr_system";

    // Create (insert a payroll record)
    public void insertPayroll(int payrollId, int employeeId, String paymentDate, double grossPay, double tax, double deductions, double netPay, double bonus) {

        try {
            // Establish a connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for inserting data into table
            pstat = connection.prepareStatement("INSERT INTO payroll (payroll_id, employee_id, payment_date, gross_pay, tax, deductions, net_pay, bonus) VALUES (?,?,?,?,?,?,?,?)");
            pstat.setInt(1, payrollId);
            pstat.setInt(2, employeeId);
            pstat.setString(3, paymentDate);
            pstat.setDouble(4, grossPay);
            pstat.setDouble(5, tax);
            pstat.setDouble(6, deductions);
            pstat.setDouble(7, netPay);
            pstat.setDouble(8, bonus);

            // Insert data into table
            i = pstat.executeUpdate();
            System.out.println(i + " record successfully added to the table.");
        }

        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        finally {
            try {
                pstat.close();
                connection.close();
            }

            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    // Update (update a payroll record)
    public void updatePayroll(int payrollId, String paymentDate, double grossPay, double tax, double deductions, double netPay, double bonus) {

        try {
            // Establish a connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for updating data in the table
            pstat = connection.prepareStatement("UPDATE payroll SET payment_date = ?, gross_pay = ?, tax = ?, deductions = ?, net_pay = ?, bonus = ? WHERE payroll_id = ?");
            pstat.setString(1, paymentDate);
            pstat.setDouble(2, grossPay);
            pstat.setDouble(3, tax);
            pstat.setDouble(4, deductions);
            pstat.setDouble(5, netPay);
            pstat.setDouble(6, bonus);
            pstat.setInt(7, payrollId);

            // Update data in the table
            i = pstat.executeUpdate();
            System.out.println(i + " record successfully updated in the table.");
        }

        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        finally {
            try {
                pstat.close();
                connection.close();
            }

            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    // Retrieve (display a payroll record)
    public void retrievePayroll(int employeeId) {

        try {
            // Establish a connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for retrieving data from the table
            pstat = connection.prepareStatement("SELECT payroll_id, employee_id, payment_date, gross_pay, tax, deductions, net_pay, bonus FROM payroll WHERE employee_id = ?");
            pstat.setInt(1, employeeId);

            // Query data in the table
            resultSet = pstat.executeQuery();

            // Process query results
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            System.out.println("Payroll Database: \n");

            while (resultSet.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    System.out.print(resultSet.getObject(i) + " ");
                }
                System.out.println();
            }
        }

        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        finally {
            try {
                resultSet.close();
                pstat.close();
                connection.close();
            }

            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    // Delete (delete a payroll record)
    public void deletePayroll(int payrollId) {

        try {
            // Establish a connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for deleting data from the table
            pstat = connection.prepareStatement("DELETE FROM payroll WHERE payroll_id = ?");
            pstat.setInt(1, payrollId);

            // Delete data from the table
            i = pstat.executeUpdate();
            System.out.println(i + " record successfully removed from the table.");
        }

        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        finally {
            try {
                pstat.close();
                connection.close();
            }

            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}