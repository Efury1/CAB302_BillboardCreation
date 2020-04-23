import java.sql.*;

public class Main {
    public static void main (String[] args) throws SQLException {

        Connection myConnection = DBConnection.getInstance();

        Statement myStatement = myConnection.createStatement();

        ResultSet myResults = myStatement.executeQuery("select * from test");
        myResults.next();
        int personId = myResults.getInt("personId");
        String personName = myResults.getString(2);
        float someNumber = myResults.getFloat("someNumber");

        System.out.println("personId: " + personId);
        System.out.println("personName: " + personName);
        System.out.println("someNumber: " + someNumber);

        //Close the connection
        myConnection.close();
    }
}