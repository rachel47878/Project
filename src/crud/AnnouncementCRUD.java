package crud;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.table.AbstractTableModel;

public class AnnouncementCRUD {

        Connection connection = null;
        PreparedStatement pstat = null;
        ResultSet resultSet = null;
        int i = 0;
        final String DATABASE_URL = "jdbc:mysql://localhost:3306/hr_system";

    // Create (insert an announcement)
    public void insertAnnouncement(int announcementID, String title, String content, String datePosted){ 

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(DATABASE_URL, "root", "password");

            // create Prepared Statement for inserting data into table
            pstat = connection.prepareStatement("INSERT INTO announcements (announcement_ID, title, content, date_Posted) VALUES (?,?,?,?)");
            pstat.setInt(1, announcementID);
            pstat.setString(2, title);
            pstat.setString(3, content);
            pstat.setString(4, datePosted);

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

    // Update (update an announcement's details)
    public void updateAnnouncement(int announcementID, String title, String content){

        try{
            // Establish a connection to the database
            connection = DriverManager.getConnection(DATABASE_URL, "root", "password");
        
            // Create Prepared Statement for updating data in the table
            pstat = connection.prepareStatement("UPDATE announcements SET announcement_id=? Where title=?");
            pstat.setInt(1, announcementID);
            pstat.setString(2, title);
            pstat.setString(3, content);

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
    // Retrieve (display an announcement's details)
    public void retrieveAnnouncement(int announcementID, String title, String content){

        try{
                // Establish a connection to the database
                connection = DriverManager.getConnection(DATABASE_URL, "root", "password");

                // Create Prepared Statement for updating data in the table
                pstat = connection.prepareStatement("SELECT announcement_id, title, content from announcements");

                // Query data in the table
                resultSet = pstat.executeQuery();

                // Process the query results
                ResultSetMetaData metaData = resultSet.getMetaData();
                int numberOfColumns = metaData.getColumnCount();
                System.out.println("Announcements Database: \n");

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
    // Delete (delete an announcement)
    public void deleteAnnouncement(int announcementID, String title){

        announcementID = 5;

        try{
                // Establish a connection to the database
                connection = DriverManager.getConnection(DATABASE_URL, "root", "password");

                // Create Prepared Statement for deleting data from the table
                pstat = connection.prepareStatement("DELETE FROM announcements WHERE announcement_id = 1");
                pstat.setInt(1, announcementID);

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