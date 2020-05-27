import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Opens a connection to the server then checks and implements the schema to the database.
 * Initialises the database and contained tables.
 */
public class InitDatabase {
    //  Constructor of InitDatabase
    public static void main(String[] args) throws SQLException {
        Connection myConnection = DBConnection.getInstance();
        Statement myStatement = myConnection.createStatement();

        myStatement.executeQuery("CREATE DATABASE IF NOT EXISTS billboard_database");
        myStatement.executeQuery("USE billboard_database");
        myStatement.executeQuery(
                "CREATE TABLE IF NOT EXISTS users (" +
                "username VARCHAR(40) PRIMARY KEY NOT NULL," +
                "password_hash VARCHAR(255) NOT NULL, " +
                "password_salt VARCHAR(255) NOT NULL, " +
                "perm_create BOOLEAN NOT NULL, " +
                "perm_edit_all_billboards BOOLEAN NOT NULL, " +
                "perm_edit_users BOOLEAN NOT NULL, " +
                "perm_schedule BOOLEAN NOT NULL)");
        myStatement.executeQuery(
                "CREATE TABLE IF NOT EXISTS billboards (" +
                //  "billboard_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                "billboard_name VARCHAR(255) PRIMARY KEY NOT NULL, " +
                "title VARCHAR(255)," +
                "description TEXT," +
                "picture MEDIUMBLOB," +
                "background_colour CHAR(6) NOT NULL DEFAULT \"FFFFFF\"," +
                "title_colour CHAR(6) DEFAULT \"000000\"," +
                "description_colour CHAR(6) DEFAULT \"000000\")");
        myStatement.executeQuery(
                "CREATE TABLE IF NOT EXISTS schedules (" +
                "schedule_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                "schedule_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "start_date DATE NOT NULL, " +
                "end_date DATE NOT NULL, " +
                "time_start TIME NOT NULL, " +
                "duration INT NOT NULL, " +
                "repeats BOOLEAN NOT NULL, " +
                "repeat_frequency INT)");
        myStatement.executeQuery(
                "CREATE TABLE IF NOT EXISTS user_billboards (" +
                "username VARCHAR(40) NOT NULL," +
                //  "billboard_ID INT NOT NULL," +
                "billboard_name VARCHAR(255) NOT NULL,"+
                "PRIMARY KEY (username, billboard_ID)," +
                "FOREIGN KEY (username) REFERENCES users(username)," +
                "FOREIGN KEY (billboard_name) REFERENCES billboards(billboard_name))");
                //  "FOREIGN KEY (billboard_ID) REFERENCES billboards(billboard_ID))");
        myStatement.executeQuery(
                "CREATE TABLE IF NOT EXISTS billboard_schedule (" +
                "schedule_ID INT NOT NULL," +
                //  "billboard_ID INT NOT NULL," +
                "billboard_name VARCHAR(255) NOT NULL,"+
                "PRIMARY KEY (schedule_ID)," +
                "FOREIGN KEY (schedule_ID) REFERENCES schedules(schedule_ID)," +
                "FOREIGN KEY (billboard_name) REFERENCES billboards(billboard_name))");
                //  "FOREIGN KEY (billboard_ID) REFERENCES billboards(billboard_ID))");


//        myResults.next();
//        int personId = myResults.getInt("personId");
//        String personName = myResults.getString(2);
//        float someNumber = myResults.getFloat("someNumber");
//
//        System.out.println("personId: " + personId);
//        System.out.println("personName: " + personName);
//        System.out.println("someNumber: " + someNumber);

        //Close the connection
        myConnection.close();
    }
}