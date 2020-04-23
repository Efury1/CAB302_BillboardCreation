import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LogIn extends JFrame { //implements ActionListener {

    JPanel logPanel;
    JLabel lbusername, lbpassword;
    JTextField user_text;
    JPasswordField pass_text;
    LogIn() {
        //For Username
        lbusername = new JLabel();
        lbusername.setText("Enter Username: ");
        user_text = new JTextField(10);
        //For the password
        lbpassword = new JLabel();
        lbpassword.setText("Enter Password: ");
        pass_text = new JPasswordField(10);
        //Panels
        logPanel = new JPanel(new GridLayout(2,2));
        logPanel.add(lbusername);
        logPanel.add(user_text);
        logPanel.add(lbpassword);
        logPanel.add(pass_text);
        add(logPanel, BorderLayout.NORTH);



        //Screen set up
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Log In");
        setSize(250, 200);

        setVisible(true);


    }
    //For Testing
    /*public static void main(String[] args)
    {
        new LogIn();
    }*/


}

