import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame implements ActionListener {

    private JTextField userTextField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();

    Login() {
        Container container = getContentPane();
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");

        //  For Username
        userLabel = new JLabel("Enter Username: ");
        userTextField = new JTextField(10);

        //  For the password
        passLabel = new JLabel("Enter Password: ");
        passwordField = new JPasswordField(10);

        //  Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        //  Screen set up
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("User Login");
        setVisible(true);
        setSize(300, 200);
        setResizable(true);

        //Panels
        JPanel credentialsPanel = new JPanel(new GridLayout(4, 2));
        credentialsPanel.add(userLabel);
        credentialsPanel.add(userTextField);
        credentialsPanel.add(passLabel);
        credentialsPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        //  Adding Components to the frame.
        add(credentialsPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //  !! For Testing
    public static void main(String[] args) throws SQLException {
        Login yeehawWindow = new Login();
    }

    /**
     * //  Code to connect to database
     * @return
     */
    public Connection Connection()
    {
        Connection myConnection = DBConnection.getInstance();
        return myConnection;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object sauce = e.getSource();
        //  !! make this better
        JButton butin = (JButton) sauce;

        String user = userTextField.getText().trim();
        char[] pass = passwordField.getPassword();
        try {
            if( QueryLogin(user, pass))
            {
                //  !! Show and error message that the credentials couldn't be matched
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Boolean QueryLogin(String user, char[] pass) throws SQLException
    {
        Connection connection = Connection();

        StringBuilder builder = new StringBuilder();
        for(char s : pass)
        {
            builder.append(s);
        }

        //  Gets the username and password from the database to validate credentials
        PreparedStatement myStatement = connection.prepareStatement("SELECT COUNT(username) FROM users WHERE username = ? AND password_hash = ?");
        myStatement.setString(1, user);
        myStatement.setString(2, builder.toString());

        //  Assigns credentials to a comparable statement
        ResultSet myResults = myStatement.executeQuery();
        myResults.next();

        //  Validate credentials
        if(myResults.getInt(1) == 1)
        {
            //  !!
            System.out.println("Login Successful! we're in");
            return  true;
        }
        else
        {
            //  !!
            System.out.println("Login Failed!");
        }

        return false;
    }
}