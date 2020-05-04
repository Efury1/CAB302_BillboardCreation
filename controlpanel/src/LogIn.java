
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
        //Message Label
        //var message = new JLabel();

        //Panels

        var panel = new JPanel();
        var logPanel = new JPanel();
        panel.add(loginButton);
        panel.add(cancelButton);
        //panel.add(message);

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
        String username = passwordField.getText();
        String password = userTextField.getText();
        if (username.trim().equals("admin") && password.trim().equals("admin")) {
            System.out.println("successful");
            //message.setText("Hello" + userTextField + "");
        }
        else {
            System.out.println("Unsuccessful");
            //message.setText(InvalidUser);
        }


    }
    public static void main(String[] a){
        LogIn frame=new LogIn();
        frame.setTitle("User Login");
        frame.setVisible(true);
        frame.setSize(300,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);


    }




}
