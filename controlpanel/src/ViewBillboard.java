import javax.swing.*;
import java.awt.*;

public class ViewBillboard {
    public static void main(String args[]) {
        MessageAndInfo();
    }

    public static void MessageAndInfo(){
        JFrame frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());


        JLabel message = new JLabel("Message", JLabel.CENTER);
        messagePanel.add(message);



        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());

        JLabel info = new JLabel("Info text info text info text info text");
        infoPanel.add(info);


        frame.add(messagePanel);
        frame.add(infoPanel);

        frame.setLayout(new GridLayout(2,1));

        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);


    }
}
