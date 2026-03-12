package crud;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class TrainingCRUD {

    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;
    int i = 0;
    final String DATABASE_URL = "jdbc:mysql://localhost:3306/hr_system";

    // Create (insert a training course)
    public void insertTraining(int courseId, String title, String description, String assignedDate, String dueDate) {

        try {
            // Establish a connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for inserting data into table
            pstat = connection.prepareStatement("INSERT INTO training (course_id, title, description, assigned_date, due_date) VALUES (?,?,?,?,?)");
            pstat.setInt(1, courseId);
            pstat.setString(2, title);
            pstat.setString(3, description);
            pstat.setString(4, assignedDate);
            pstat.setString(5, dueDate);

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

    // Update (update a training course)
    public void updateTraining(int courseId, String title, String description, String assignedDate, String dueDate) {

        try {
            // Establish a connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for updating data in the table
            pstat = connection.prepareStatement("UPDATE training SET title = ?, description = ?, assigned_date = ?, due_date = ? WHERE course_id = ?");
            pstat.setString(1, title);
            pstat.setString(2, description);
            pstat.setString(3, assignedDate);
            pstat.setString(4, dueDate);
            pstat.setInt(5, courseId);

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

    // Retrieve (display a training course)
    public void retrieveTraining(int courseId) {

        try {
            // Establish a connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for retrieving data from the table
            pstat = connection.prepareStatement("SELECT course_id, title, description, assigned_date, due_date FROM training WHERE course_id = ?");
            pstat.setInt(1, courseId);

            // Query data in the table
            resultSet = pstat.executeQuery();

            // Process query results
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            System.out.println("Training Database: \n");

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

    // Delete (delete a training course)
    public void deleteTraining(int courseId) {

        try {
            // Establish a connection to the database
            connection = DBConnection.getConnection();

            // Create Prepared Statement for deleting data from the table
            pstat = connection.prepareStatement("DELETE FROM training WHERE course_id = ?");
            pstat.setInt(1, courseId);

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