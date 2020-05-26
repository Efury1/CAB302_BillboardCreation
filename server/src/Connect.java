/* */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
    public static void main (String[] args) throws SQLException {

        Connection myConnection = DBConnection.getInstance();

        Statement myStatement = myConnection.createStatement();

        ResultSet myResults = myStatement.executeQuery("SELECT * FROM billboard_database.users;");
        //myResults.next();
        while(myResults.next()){
            String user = myResults.getString("Username");
            String pass = myResults.getString("password_hash");

            System.out.println("User " + user + " has password " + pass);
            //System.out.println("Username: " + user);
        }

        System.out.println(myResults);




        //  Code for adding data to database (through java)
//        int personId = myResults.getInt("personId");
//        String personName = myResults.getString(2);
//        float someNumber = myResults.getFloat("someNumber");
//
//        System.out.println("personId: " + personId);
//        System.out.println("personName: " + personName);
//        System.out.println("someNumber: " + someNumber);

        //  Close the connection
        myConnection.close();
    }
}