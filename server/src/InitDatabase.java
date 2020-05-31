import java.sql.*;

/**
 * Opens a connection to the server then checks and implements the schema to the database.
 * Initialises the database and contained tables.
 */
public class InitDatabase {
    public static String admin_username = "staff";
    public static String admin_password = "today123";
    //  Constructor of InitDatabase
    public static void main(String[] args) throws SQLException {
        Connection myConnection = DBConnection.getInstance();
        Statement myStatement = myConnection.createStatement();

        //Admin details

        SaltHandler saltHandler = new SaltHandler();


        String hashed_password = saltHandler.HashString(admin_password);
        String salt = saltHandler.GetSalt();                            //send this to DB
        String salted_password = salt + hashed_password;
        String salted_hash = saltHandler.HashString(salted_password);   //also send this to DB





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
                "title VARCHAR(255), " +
                "description TEXT, " +
                "picture MEDIUMBLOB, " +
                "background_colour CHAR(6) NOT NULL DEFAULT \"FFFFFF\", " +
                "title_colour CHAR(6) DEFAULT \"000000\", " +
                "description_colour CHAR(6) DEFAULT \"000000\")");
        myStatement.executeQuery(
                "CREATE TABLE IF NOT EXISTS schedules (" +
                "schedule_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                "schedule_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "start_date DATE NOT NULL, " +
                "end_date DATE NOT NULL, " +
                "start_time TIME NOT NULL, " +
                "duration INT NOT NULL, " +
                "repeats BOOLEAN NOT NULL, " +
                "repeat_frequency INT)");
        myStatement.executeQuery(
                "CREATE TABLE IF NOT EXISTS user_billboards (" +
                "username VARCHAR(40) NOT NULL, " +
                //  "billboard_ID INT NOT NULL," +
                "billboard_name VARCHAR(255) NOT NULL, "+
                "PRIMARY KEY (username, billboard_name), " +
                "FOREIGN KEY (username) REFERENCES users(username), " +
                "FOREIGN KEY (billboard_name) REFERENCES billboards(billboard_name))");
                //  "FOREIGN KEY (billboard_ID) REFERENCES billboards(billboard_ID))");
        myStatement.executeQuery(
                "CREATE TABLE IF NOT EXISTS billboard_schedule (" +
                "schedule_ID INT NOT NULL," +
                //  "billboard_ID INT NOT NULL," +
                "billboard_name VARCHAR(255) NOT NULL,"+
                "PRIMARY KEY (schedule_ID), " +
                "FOREIGN KEY (schedule_ID) REFERENCES schedules(schedule_ID), " +
                "FOREIGN KEY (billboard_name) REFERENCES billboards(billboard_name))");
                //  "FOREIGN KEY (billboard_ID) REFERENCES billboards(billboard_ID))");

        //Check that the admin is in the user table
        PreparedStatement preparedStatementCheck = myConnection.prepareStatement("SELECT COUNT(username) FROM users WHERE username = ?");
        preparedStatementCheck.setString(1, admin_username);
        ResultSet checkAdminExists = preparedStatementCheck.executeQuery();

        //Add admin user if it doesn't already exist
        if(checkAdminExists.next() && checkAdminExists.getInt(1) == 0) {
            PreparedStatement preparedStatement = myConnection.prepareStatement("INSERT INTO users (username, password_hash, password_salt, perm_create, perm_edit_all_billboards, perm_edit_users, perm_schedule) VALUES (?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1, admin_username);
            preparedStatement.setString(2, salted_hash);
            preparedStatement.setString(3, salt);
            preparedStatement.setBoolean(4, true);
            preparedStatement.setBoolean(5, true);
            preparedStatement.setBoolean(6, true);
            preparedStatement.setBoolean(7, true);
            preparedStatement.executeUpdate();
        }

        //TODO delet this
        TokenHandler testCache = new TokenHandler();
        Object[] billboard_info = ProcessRequests.ProcessRequest(4, testCache, new Object[]{"billboard100", "no", "nothing", new byte[1], "n", "n", "c", "user1"});
        for(int i = 0; i < billboard_info.length; i++) {
            System.out.println();
        }


//        myStatement.executeUpdate("INSERT INTO billboards (billboard_name, title, description, picture, background_colour, title_colour, description_colour) VALUES " +
//                "('billboard1', 'title1', 'blah blah', '', '', '', ''), " +
//                "('billboard2', 'title2', 'blah blah', '', '', '', ''), " +
//                "('billboard3', 'title3', 'blah blah', '', '', '', ''), " +
//                "('billboard4', 'title4', 'blah blah', '', '', '', ''), " +
//                "('billboard5', 'title5', 'blah blah', '', '', '', '')");
//
//        myStatement.executeUpdate("INSERT INTO users (username, password_hash, password_salt, perm_create, perm_edit_all_billboards, perm_edit_users, perm_schedule) VALUES " +
//                "('user1', 'password1', 'salt1', true, true, false, false), " +
//                "('user2', 'password1', 'salt1', true, true, false, false), " +
//                "('user3', 'password1', 'salt1', true, true, false, false), " +
//                "('user4', 'password1', 'salt1', true, true, false, false), " +
//                "('user5', 'password1', 'salt1', true, true, false, false), " +
//                "('user6', 'password1', 'salt1', true, true, false, false)");
//
//        myStatement.executeUpdate("INSERT INTO user_billboards (username, billboard_name) VALUES " +
//                "('user1', 'billboard1'), " +
//                "('user2', 'billboard2'), " +
//                "('user3', 'billboard3')");





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