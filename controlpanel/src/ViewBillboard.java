import javax.swing.*;
import java.awt.*;

public class ViewBillboard {
    public static void main(String args[]) {
        //MessageAndInfo();
        //MessageImageInfo();
        MessageAndImage();
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

    public static void MessageImageInfo(){
        JFrame frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());

        JLabel message = new JLabel("Message", JLabel.CENTER);
        messagePanel.add(message);

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridBagLayout());

        JLabel image = new JLabel("Insert image here", JLabel.CENTER);
        imagePanel.add(image);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());

        JLabel info = new JLabel("Info text info text info text info text");
        infoPanel.add(info);


        frame.add(messagePanel);
        frame.add(imagePanel);
        frame.add(infoPanel);

        frame.setLayout(new GridLayout(3,1));

        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

    public static void MessageAndImage(){
        JFrame frame = new JFrame("View Billboard");
        frame.setLayout(new GridBagLayout());
        frame.setSize(1000, 700);
        GridBagConstraints gbc = new GridBagConstraints();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel messagePanel = new JPanel();
        gbc.weighty = 0.33;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.VERTICAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        messagePanel.setLayout(new GridBagLayout());


        JLabel message = new JLabel("Message", JLabel.CENTER);
        messagePanel.add(message);

        frame.add(messagePanel, gbc);



        JPanel imagePanel = new JPanel();
        gbc.weighty = 0.66;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        imagePanel.setLayout(new GridBagLayout());

        JLabel image = new JLabel("Insert image here", JLabel.CENTER);
        imagePanel.add(image);


        frame.add(imagePanel, gbc);


        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }
}
