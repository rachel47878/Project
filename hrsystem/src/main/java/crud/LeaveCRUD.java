package crud;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.table.AbstractTableModel;

public class LeaveCRUD {
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;
    int i = 0;

    // Create a new leave request
    public void createLeave(int requestId, int employeeId, String startDate, String endDate, String leaveType, String status) {
        try {
            // Establish connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for inserting data into table
            pstat = connection.prepareStatement("INSERT into `leave` (request_id, employee_id, start_date, end_date, leave_type, status) VALUES (?,?,?,?,?,?)");
            pstat.setInt(1, requestId);
            pstat.setInt(2, employeeId);
            pstat.setString(3, startDate);
            pstat.setString(4, endDate);
            pstat.setString(5, leaveType);
            pstat.setString(6, status);

            // Insert data into table
            i = pstat.executeUpdate();
            System.out.println(i + " record successfully added to the table.");
        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        finally {
            try{
                pstat.close();
                connection.close();
            }

            catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    // Update an existing leave request
    public void updateLeave(int requestId, String status) {
        try {
            // Establish connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for updating data in table
            pstat = connection.prepareStatement("UPDATE `leave` SET status = ? WHERE request_id = ?");
            pstat.setString(1, status);
            pstat.setInt(2, requestId);

            // Update data in table
            i = pstat.executeUpdate();
            System.out.println(i + " record successfully updated in the table.");
        } 
        
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        finally {
            try{
                pstat.close();
                connection.close();
            }

            catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    // Retrieve leave requests for a specific employee
    public void retrieveLeave(int employeeId) {

        try {
            // Establish a connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for retrieving data from table
            pstat = connection.prepareStatement("SELECT request_id, start_date, end_date, leave_type, status FROM `leave` WHERE employee_id = ?");
            pstat.setInt(1, employeeId);

            // Query data in the table
            resultSet = pstat.executeQuery();

            // Process query results
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            System.out.println("Leave Requests for Employee ID: " + employeeId);

            while(resultSet.next()) {
                for (i = 1; i <= numberOfColumns; i++) {
                    System.out.println(resultSet.getObject(i) + " ");
                }
                System.out.println(); 
            }
        }

            catch(SQLException sqlException) {
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

        // Retrieve all leave requests
        public void retrieveAllLeave() {

            try {
                // Establish a connection to the database
                connection = DBConnection.getConnection();

                // Create Prepared Statement for retrieving data from table
                pstat = connection.prepareStatement("SELECT request_id, employee_id, start_date, end_date, leave_type, status FROM `leave`");

                // Query data in the table
                resultSet = pstat.executeQuery();

                // Process query results
                ResultSetMetaData metaData = resultSet.getMetaData();
                int numberOfColumns = metaData.getColumnCount();
                System.out.println("All Leave Requests: \n");

                while(resultSet.next()) {
                    for (i = 1; i <= numberOfColumns; i++) {
                        System.out.println(resultSet.getObject(i) + " ");
                    }
                    System.out.println(); 
                }
            }

            catch(SQLException sqlException) {
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

        // Deleting record not necessary - records will be retained for audit purposes
}