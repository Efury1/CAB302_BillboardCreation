import javax.swing.*;
import java.awt.*;

public class ViewBillboard {
    public static void main(String args[]) {
        //TODO closes all frames on exit, need to fix this. Not main propriety though.
        //MessageAndInfo();
        //MessageImageInfo();
        //MessageAndImage();
        Message();
    }

    public static void Message(){
        JFrame frame1 = new JFrame("View Billboard");
        frame1.setSize(1000, 700);

        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension messageDim = frame1.getSize();
        double w = messageDim.width;

        String messageText = "Message text";

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());


        JLabel message = new JLabel(messageText, JLabel.CENTER);
        messagePanel.add(message);

        Font TitleFont = message.getFont();
        int messageSize = TitleFont.getSize();
        int messageWidth = message.getFontMetrics(TitleFont).stringWidth(messageText);

        while (messageWidth < w - 10) {
            messageSize++;
            message.setFont(new Font("Serif", Font.PLAIN, messageSize));
            messageWidth= message.getFontMetrics(message.getFont()).stringWidth(messageText);
        }

        //set font back to smaller to not overflowing
        message.setFont(new Font("Serif", Font.PLAIN, messageSize - 2));
        messageWidth = message.getFontMetrics(message.getFont()).stringWidth(messageText);
        System.out.println("Width :" + messageWidth);

        frame1.add(messagePanel);

        frame1.pack();
        frame1.setSize(1000, 700);
        frame1.setVisible(true);
    }


    public static void MessageAndInfo(){
        JFrame frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension messageDim = frame.getSize();
        double w = messageDim.width;

        String messageText = "Message text and more text";

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());


        JLabel message = new JLabel(messageText, JLabel.CENTER);
        messagePanel.add(message);

        Font TitleFont = message.getFont();
        int messageSize = TitleFont.getSize();
        int messageWidth = message.getFontMetrics(TitleFont).stringWidth(messageText);

        while (messageWidth < w - 10) {
            messageSize++;
            message.setFont(new Font("Serif", Font.PLAIN, messageSize));
            messageWidth= message.getFontMetrics(message.getFont()).stringWidth(messageText);
        }

        //set font back to smaller to not overflowing
        message.setFont(new Font("Serif", Font.PLAIN, messageSize - 2));
        messageWidth = message.getFontMetrics(message.getFont()).stringWidth(messageText);
        System.out.println("Width :" + messageWidth);


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

        String messageText = "Good morning";

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension messageDim = frame.getSize();
        double w = messageDim.width;
        double h = messageDim.height/3;


        JPanel messagePanel = new JPanel();
        gbc.weighty = 0.33;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.VERTICAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        messagePanel.setLayout(new GridBagLayout());


        JLabel message = new JLabel(messageText, JLabel.CENTER);
        messagePanel.add(message);

        Font TitleFont = message.getFont();
        int messageSize = TitleFont.getSize();
        int messageWidth = message.getFontMetrics(TitleFont).stringWidth(messageText);
        int messageHeight = message.getFontMetrics(TitleFont).getHeight();

        while (messageWidth < w - 10 && messageHeight < h) {
            messageSize++;
            message.setFont(new Font("Serif", Font.PLAIN, messageSize));
            messageWidth= message.getFontMetrics(message.getFont()).stringWidth(messageText);
            messageHeight = message.getFontMetrics(TitleFont).getHeight();
        }

        //set font back to smaller to not overflowing
        message.setFont(new Font("Serif", Font.PLAIN, messageSize - 2));
        messageWidth = message.getFontMetrics(message.getFont()).stringWidth(messageText);


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
