import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Connect {
    public static void main (String[] args) throws SQLException {
        Connection myConnection = DBConnection.getInstance();
        Statement myStatement = myConnection.createStatement();
        //populateDB();
        ArrayList<String> data = new ArrayList<String>();
        data = getBillboards();


       for(int i = 0; i<data.size(); i++){
            System.out.println("Creator: " + data.get(i));
            i++;
            System.out.println("Billboard name: " + data.get(i));
            System.out.println();
        }
        myConnection.close();

    }
    public static ArrayList<String> getBillboards() throws SQLException{
        ArrayList<String> billboardData = new ArrayList<String>();

        Connection myConnection = DBConnection.getInstance();
        Statement myStatement = myConnection.createStatement();

        ResultSet billboardResults = myStatement.executeQuery("SELECT username, billboard_name FROM billboard_database.user_billboards;");

        while(billboardResults.next()){
            String bOwner = billboardResults.getString("username");
            billboardData.add(bOwner);
            String bName = billboardResults.getString("billboard_name");
            billboardData.add(bName);

            //System.out.println(bName + "   " + bOwner);
        }

        myConnection.close();
        return billboardData;
    }

    public static void connectTest() throws SQLException{
        Connection myConnection = DBConnection.getInstance();

        Statement myStatement = myConnection.createStatement();

        //"FOREIGN KEY (billboard_ID) REFERENCES billboards(billboard_ID))");

        // myStatement.executeQuery("INSERT INTO billboard_database.billboards (billboard_name, title) VALUES ('HelloWorldBillboard', 'Hello World')");
        //myStatement.executeQuery("INSERT INTO billboard_database.user_billboards (username, billboard_name) VALUES ('fudog', 'HelloWorldBillboard')");

        ResultSet myResults = myStatement.executeQuery("SELECT * FROM billboard_database.users;");
        ResultSet billboardResults = myStatement.executeQuery("SELECT username, billboard_name FROM billboard_database.user_billboards");
        //myResults.next();
        while(myResults.next()){
            String user = myResults.getString("Username");
            String pass = myResults.getString("password_hash");

            //System.out.println("User " + user + " has password " + pass);
            //System.out.println("Username: " + user);
        }

        while(billboardResults.next()){
            String bOwner = billboardResults.getString("username");
            String bName = billboardResults.getString("billboard_name");

            System.out.println(bName + "   " + bOwner);
        }



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

    public static void populateDB() throws SQLException{
        Connection myConnection = DBConnection.getInstance();

        Statement myStatement = myConnection.createStatement();

        myStatement.executeQuery("INSERT INTO billboard_database.billboards (billboard_name, title) VALUES ('NewBillboard', 'Welcome')");
        myStatement.executeQuery("INSERT INTO billboard_database.user_billboards (username, billboard_name) VALUES ('sunpark', 'NewBillboard')");

        myStatement.executeQuery("INSERT INTO billboard_database.billboards (billboard_name, title) VALUES ('BirthdayMessage', 'Happy Birthday!')");
        myStatement.executeQuery("INSERT INTO billboard_database.user_billboards (username, billboard_name) VALUES ('superuser', 'BirthdayMessage')");

        myConnection.close();
    }

}