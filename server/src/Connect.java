import java.sql.*;

public class Connect {
    public static void main (String[] args) throws SQLException {

        Connection myConnection = DBConnection.getInstance();

        Statement myStatement = myConnection.createStatement();

        ResultSet myResults = myStatement.executeQuery("select * from users");
        myResults.next();




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