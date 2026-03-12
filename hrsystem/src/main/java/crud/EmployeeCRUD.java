package crud;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.table.AbstractTableModel;

public class EmployeeCRUD {

        Connection connection = null;
        PreparedStatement pstat = null;
        ResultSet resultSet = null;
        int i = 0;
        final String DATABASE_URL = "jdbc:mysql://localhost:3306/hr_system";

        // Create (insert an employee)
        public void insertEmployee(String firstName, String lastName, String position, String jobTitle, String hireDate, String email, int departmentId){ 

            try {
                // Establish a connection to the database
                connection = DBConnection.getConnection();

                // Create Prepared Statement for inserting data into table
                pstat = connection.prepareStatement("INSERT INTO employees (first_name, last_name, position, job_title, hire_date, email, department_id) VALUES (?,?,?,?,?,?,?)");
                pstat.setString(1, firstName);
                pstat.setString(2, lastName);
                pstat.setString(3, position);
                pstat.setString(4, jobTitle);
                pstat.setString(5, hireDate);
                pstat.setString(6, email);
                pstat.setInt(7, departmentId);

                // insert data into table
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

        // Update (update an employee's details)
        public void updateEmployee(String firstName, String lastName, String email, int departmentId, String jobTitle){

            try{
                // Establish a connection to the database
                connection = DBConnection.getConnection();
            
                // Create Prepared Statement for updating data in the table
                pstat = connection.prepareStatement("UPDATE EMPLOYEES SET first_name=? Where last_name=?");
                pstat.setString(1, firstName);
                pstat.setString(2, lastName);

                // Update data in the table
                i = pstat.executeUpdate();
                System.out.println(i + "record updated successfully");
            }

            catch(SQLException sqlException){
                sqlException.printStackTrace();
            }

            finally{
                try{
                    pstat.close();
                    connection.close();
                }

                catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        }
        // Retrieve (display an employee's details)
        public void retrieveEmployee(String firstName, String lastName){

            try{
                    // Establish a connection to the database
                    connection = DBConnection.getConnection();

                    // Create Prepared Statement for updating data in the table
                    pstat = connection.prepareStatement("SELECT employee_ID, first_name, last_name FROM employees");

                    // Query data in the table
                    resultSet = pstat.executeQuery();

                    // Process query results
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int numberOfColumns = metaData.getColumnCount();
                    System.out.println("Employees Database: \n");

                    for (int i = 1; i <= numberOfColumns; i++)
                            System.out.println(metaData.getColumnName(i) + "\t");
                            System.out.println();

                    while(resultSet.next()){
                        for(int i = 1; i <= numberOfColumns; i++)
                            System.out.print(resultSet.getObject(i) + "\t");
                            System.out.println();
            }
        }

        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        finally{
            try{
                resultSet.close();
                pstat.close();
                connection.close();
            }
            catch (Exception exception){
                exception.printStackTrace();
            }
        }
        }

        // Delete (delete an employee)
        public void deleteEmployee(String firstName, String lastName, String email, int departmentId, String jobTitle){

            int employeeId = 5;

            try{
                    // Establish a connection to the database
                    connection = DBConnection.getConnection();

                    // Create Prepared Statement for deleting data from the table
                    pstat = connection.prepareStatement("DELETE FROM EMPLOYEES WHERE employee_Id = 5");
                    pstat.setInt(1, employeeId);

                    // Delete data from the table
                    i = pstat.executeUpdate();
                    System.out.println(i + "record successfully removed from the table");
            }

            catch (SQLException sqlException){
                sqlException.printStackTrace();
            }

            finally {
                try{
                    pstat.close();
                    connection.close();
                }

                catch(Exception exception){
                    exception.printStackTrace();
                }
            }
    }
    
}