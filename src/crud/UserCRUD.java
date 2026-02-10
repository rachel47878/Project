package crud;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.table.AbstractTableModel;

public class UserCRUD {

    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet = null;
    int i = 0;
    final String DATABASE_URL = "jdbc:mysql://localhost:3306/hr_system";

     // Create new user in database
     public void createUser(int userId, String username, String password, String role) {

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(DATABASE_URL, "root", "password");

            // Create Prepared Statement for inserting data into table
            pstat = connection.prepareStatement("INSERT into users (user_id, username, password, role) VALUES (?,?,?,?)");
            pstat.setInt(1, userId);
            pstat.setString(2, username);
            pstat.setString(3, password);
            pstat.setString(4, role);

            // Insert data into table
            pstat.executeUpdate();
            System.out.println(i + "record successfully added to the table.");
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

     // Update a user's details
     public void updateUser(int userId, String username, String role){

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(DATABASE_URL, "root", "password");

            // Create Prepared Statement for updating data in the table
            pstat = connection.prepareStatement("UPDATE users SET username = ?, role = ? WHERE user_id = ?");
            pstat.setString(1, username);
            pstat.setString(2, role);
            pstat.setInt(3, userId);

            // Update data in table
            pstat.executeUpdate();
            System.out.println(i + "record successfully updated in the table.");
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

        // Retrieve a user's details
        public void retrieveUser(int userId){

            try {
                // Establish a connection to the database
                connection = DriverManager.getConnection(DATABASE_URL, "root", "password");

                // Create Prepared Statement for retrieving data from the table
                pstat = connection.prepareStatement("SELECT username, role FROM users WHERE user_id = ?");
                pstat.setInt(1, userId);

                // Query data in the table
                resultSet = pstat.executeQuery();

                while(resultSet.next()){
                    System.out.println("Username: " + resultSet.getString("username"));
                    System.out.println("Role: " + resultSet.getString("role"));
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

        // Delete a user from the database
        public void deleteUser(int userId){

            try {
                // Establish a connection to the database
                connection = DriverManager.getConnection(DATABASE_URL, "root", "password");

                // Create Prepared Statement for deleting data from the table
                pstat = connection.prepareStatement("DELETE FROM users WHERE user_id = ?");
                pstat.setInt(1, userId);

                // Delete data from the table
                i = pstat.executeUpdate();
                System.out.println(i + "record successfully removed from the table.");
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