import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class ProcessRequest {
    private static Boolean Login(String user, String pass) throws SQLException {
        //process Username and password from database
        Connection myConnection = DBConnection.getInstance();

        PreparedStatement myStatement = myConnection.prepareStatement("SELECT password_hash, password_salt FROM users WHERE user = ?");
        myStatement.setString(1, user);
        ResultSet result = myStatement.executeQuery();

        //TODO
        //Code to compare the hashed passwords
        result.next();
        String salted_password = result.getString(1) + result.getString(2);

        boolean valid = false;
        if(salted_password == pass){
            valid = true;
        }

        return valid;
    }

    public static void ProcessRequest(Integer function_id, String token/*, Object[] vars*/) throws SQLException {
        //TODO Are we writing more ifs
        if(function_id == 1){
            Login("user", "pass");
        }
    }
}
