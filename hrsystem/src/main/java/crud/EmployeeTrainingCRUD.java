package crud;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.table.AbstractTableModel;

public class EmployeeTrainingCRUD{

        Connection connection = null;
        PreparedStatement pstat = null;
        ResultSet resultSet = null;
        int i = 0;
        final String DATABASE_URL = "jdbc:mysql://localhost:3306/hr_system";

        // Create a training module for employees
        public void setTraining(int employeeId, int courseId, int assignedBy, String dueDate){

            try{
                // Establish a connection to the database
                connection = DBConnection.getConnection();

                // Create Prepared Statement for inserting data into table
                pstat = connection.prepareStatement("INSERT INTO employee_training (employee_id, course_id, assigned_by, due_date) VALUES (?,?,?,?)");
                pstat.setInt(1, employeeId);
                pstat.setInt(2, courseId);
                pstat.setInt(3, assignedBy);
                pstat.setString(4, dueDate);

                // Insert data into table
                i = pstat.executeUpdate();
                System.out.println(i + " record successfully added to the table.");

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

        // Update an employee's training details
        public void updateTraining(int employeeId, int courseId, int assignedBy, String dueDate){

            try{
                // Establish a connection to the database
                connection = DBConnection.getConnection();

                // Create Prepared Statement for updating data in table
                pstat = connection.prepareStatement("UPDATE employee_training SET assigned_by=?, due_date=? WHERE employee_id=? AND course_id=?");
                pstat.setInt(1, assignedBy);
                pstat.setString(2, dueDate);
                pstat.setInt(3, employeeId);
                pstat.setInt(4, courseId);

                // Update data in table
                i = pstat.executeUpdate();
                System.out.println(i + " record successfully updated in the table.");

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

        // Retrieve training details for an employee
        public void retrieveTraining(int employeeId){

            try{
                // Establish a connection to the database
                connection = DBConnection.getConnection();

                // Create Prepared Statement for retrieving data from table
                pstat = connection.prepareStatement("SELECT * FROM employee_training WHERE employee_id=?");
                pstat.setInt(1, employeeId);

                // Retrieve data from table
                resultSet = pstat.executeQuery();

                // Process query results
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while(resultSet.next()){
                    for(int i = 1; i <= columnCount; i++){
                        System.out.print(resultSet.getObject(i) + " ");
                    }
                    System.out.println();
                }
            }

            catch(SQLException sqlException){
                sqlException.printStackTrace();
            }

            finally{
                try{
                    resultSet.close();
                    pstat.close();
                    connection.close();
                }

                catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        }

        // Deleting record not necessary - records will be retained for audit purposes
}