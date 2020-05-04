
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogIn extends JFrame implements ActionListener {

    Container container=getContentPane();
    JLabel userLabel=new JLabel("Username");
    JLabel passwordLabel=new JLabel("Password");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");
    JButton cancelButton =new JButton("Cancel");



    LogIn()
    {

        userLabel = new JLabel();
        userLabel.setText("Enter Username: ");
        userTextField = new JTextField(10);
        passwordField = new JPasswordField();
        //Panels

        var panel = new JPanel();
        var logPanel = new JPanel();
        panel.add(loginButton);
        panel.add(cancelButton);

        logPanel = new JPanel(new GridLayout(4, 2));
        logPanel.add(userLabel);
        logPanel.add(userTextField);
        logPanel.add(passwordLabel);
        logPanel.add(passwordField);
        add(logPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.SOUTH);

        loginButton.addActionListener(this);
        cancelButton.addActionListener(this);


    }


    @Override
    public void actionPerformed(ActionEvent e) {


    }
    public static void main(String[] a){
        LogIn frame=new LogIn();
        frame.setTitle("User Login");
        frame.setVisible(true);
        frame.setSize(400,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);


    }




}
