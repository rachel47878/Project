package crud;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.table.AbstractTableModel;

public class DepartmentCRUD{
        Connection connection = null;
        PreparedStatement pstat = null;
        ResultSet resultSet = null;
        int i = 0;
        final String DATABASE_URL = "jdbc:mysql://localhost:3306/hr_system";

        // Create (insert a department)
        public void insertDepartment(int departmentId, String departmentName, int managerId){ 

            try {
                // Establish a connection to the database
                connection = DBConnection.getConnection();

                // Create Prepared Statement for inserting data into table
                pstat = connection.prepareStatement("INSERT INTO departments (department_id, department_name, manager_id) VALUES (?,?,?)");
                pstat.setInt(1, departmentId);
                pstat.setString(2, departmentName);
                pstat.setInt(3, managerId);

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

        // Update (update a department's details)
        public void updateDepartment(int departmentId, String departmentName, int managerId){

            try{
                // Establish a connection to the database
                connection = DBConnection.getConnection();

                // Create Prepared Statement for updating data in the table
                pstat = connection.prepareStatement("UPDATE departments SET department_name = ?, manager_id = ? WHERE department_id = ?");
                pstat.setString(1, departmentName);
                pstat.setInt(2, managerId);
                pstat.setInt(3, departmentId);

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

        // Retrieve (display a department's details)
        public void retrieveDepartment(int departmentId, String departmentName, String managerId){

            try{
                // Establish a connection to the database
                connection = DBConnection.getConnection();

                // Create Prepared Statement for retrieving data from the table
                pstat = connection.prepareStatement("SELECT department_name, manager_id FROM departments WHERE department_id = ?");
                pstat.setInt(1, departmentId);
                
                // Query data in the table
                resultSet = pstat.executeQuery();

                // Process query results
                ResultSetMetaData metaData = resultSet.getMetaData();
                int numberOfColumns = metaData.getColumnCount();
                System.out.println("Departments Database: \n");

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

        // Delete (remove a department)
        public void deleteDepartment(int departmentId){

            try{
                // Establish a connection to the database
                connection = DBConnection.getConnection();

                // Create Prepared Statement for deleting data from the table
                pstat = connection.prepareStatement("DELETE FROM departments WHERE department_id = ?");
                pstat.setInt(1, departmentId);

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