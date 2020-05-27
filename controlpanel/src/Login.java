import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login {


    private JTextField textField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();

    /**
     * Default constructor for Login()
     */
    Login() {
        JFrame frame = new JFrame("Control Panel Review");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 500, 400);
        frame.setResizable(false);
        JPanel contentPane = new JPanel();
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login to continue");
        lblNewLabel.setBounds(195, 110, 120, 40);
        contentPane.add(lblNewLabel);
        textField = new JTextField();
        //Username text field
        textField.setBounds(200, 170, 200, 35);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 230, 200, 35);
        contentPane.add(passwordField);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(50, 166, 193, 52);
        contentPane.add(userLabel);

        JLabel passLabel = new JLabel("Password");

        passLabel.setBounds(50, 230, 193, 52);
        contentPane.add(passLabel);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(200, 300, 90, 20);
        contentPane.add(loginButton);
        //loginButton.addActionListener((ActionListener) this);

        //  Screen set up
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setTitle("User Login");
        frame.setVisible(true);
        frame.setSize(500, 400);
        frame.setResizable(false);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uName = textField.getText();
                String pass = passwordField.getText();  //  getText() is deprecated for JPasswordField (find other method)
                /*Tutor will now be able to login in with name and password */
                if(uName.equals("name") && pass.equals("password"))
                {
                    JOptionPane.showMessageDialog(frame, "You have successfully login!");
                    //Create object
                    GUI gui = new GUI();

                }
                else
                {
                    //This currently works
                    JOptionPane.showMessageDialog(frame,"Invalid username and/or password!");
                }
            }
        });
    }

    public static void main(String[] args) throws SQLException {
        Login testing = new Login();
    }
}

    /*
     * !! For testing
     * @param args
     * @throws SQLException
     */
    /*public static void main(String[] args) throws SQLException {
        Login yeehawWindow = new Login();
    }

    /**
     * Code to connect to database
     * @return  A connection
     */
    /*public Connection Connection()
    {
        return DBConnection.getInstance();
    }

    /**
     * Action listener of all buttons in the panel.
     * !! Tweak this so it uses a scalable method (eg. https://youtu.be/P-80XbVFHRU?t=2039)
     * @param e
     */
   /*@Override
    public void actionPerformed(ActionEvent e)
    {
        String user = textField.getText().trim();
        char[] pass = passwordField.getPassword();



        try {
            QueryLogin(user, pass);//  !! Show and error message that the credentials couldn't be matched
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//      //  !! Example ActionListener code for multiple buttons
//        Object source = e.getSource();
//        if (source == loginButton)
//        {
//            JButton sourceJButton = (JButton) source;
//        }
//        else if (source == cancelButton)
//        {
//            JButton sourceJButton = (JButton) source;
//        }
    }

    /**
     * Compares the parameters user and pass to the database and returns true if successfully validated.
     * @param user Username that's sent to server
     * @param pass Password that's sent to server
     * @return True if credentials successfully authenticated
     * @throws SQLException
     */
   /* public Boolean QueryLogin(String user, char[] pass) throws SQLException
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
}*/