import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ViewBillboard {
    public static void main(String args[]) throws IOException {

        Billboard TestBill = new Billboard("testBillboard", "testUser");
        TestBill.setBMessage("Test text");
        TestBill.setBDescription("Test description");
        //showBillboard(TestBill);
        MessageImageInfo("Hello", "https://vignette.wikia.nocookie.net/habitrpg/images/9/92/Summer-Splash-Starfish.png/revision/latest?cb=20150702105822", "Info");
    }

    /**
     * Given a billboard, chooses which function should be called to display it and calls this function
     * @param b billboard to be displayed
     */
    public static void showBillboard(Billboard b) throws IOException {
        if(b.hasMessage()&&b.hasDescription()&&b.hasImage()){
            //All three elements present
            MessageImageInfo(b.getBMessage(), b.getBImageLink(), b.getBDescription());
        }
        else{
            if(b.hasMessage()&&b.hasDescription()){
                //Message and info only
                MessageAndInfo(b.getBMessage(), b.getBDescription());
            }
            else {
                if(b.hasDescription()&&b.hasImage()){
                    //Info and image only
                    //TODO
                }
                else{
                    if(b.hasMessage()&&b.hasImage()){
                        //Message and image only
                        //TODO
                    }
                    else{
                        if(b.hasMessage()){
                            //Message only
                            Message(b.getBMessage());
                        }
                        else{
                            if(b.hasImage()){
                                //Image only
                                //TODO
                            }
                            else{
                                if(b.hasDescription()){
                                    //Info only
                                    Info(b.getBDescription());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Displays billboard with only Message element
     * @param messageText Text of message to be displayed on billboard
     */
    public static void Message(String messageText){
        JFrame frame1 = new JFrame("View Billboard");
        frame1.setSize(1000, 700);

        Dimension messageDim = frame1.getSize();
        double w = messageDim.width;

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

        frame1.add(messagePanel);

        frame1.pack();
        frame1.setSize(1000, 700);
        frame1.setVisible(true);
    }

    /**
     * Displays billboard with only Information element
     * @param rawInfoText Text of information to be displayed on billboard
     */
    public static void Info(String rawInfoText){
        JFrame frame1 = new JFrame("View Billboard");
        frame1.setSize(1000, 700);

        //Wraps in HTML tags so can span multiple lines
        String infoText = "<html>"+ rawInfoText+"</html>";

        Dimension infoDim = frame1.getSize();
        double w = infoDim.width;

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());


        JLabel info = new JLabel(infoText, JLabel.CENTER);
        infoPanel.add(info);

        Font TitleFont = info.getFont();
        int infoSize = TitleFont.getSize();
        int infoWidth = info.getFontMetrics(TitleFont).stringWidth(infoText);

        while (infoWidth < w - 10) {
            infoSize++;
            info.setFont(new Font("Serif", Font.PLAIN, infoSize));
            infoWidth= info.getFontMetrics(info.getFont()).stringWidth(infoText);
        }

        //set font back to smaller to not overflowing
        info.setFont(new Font("Serif", Font.PLAIN, infoSize - 2));
        infoWidth = info.getFontMetrics(info.getFont()).stringWidth(infoText);

        frame1.add(infoPanel);

        frame1.pack();
        frame1.setSize(1000, 700);
        frame1.setVisible(true);
    }

    /**
     * Displays billboard with message and information elements
     * @param messageText Text of message to be displayed on billboard
     * @param infoText Text of information to be displayed on billboard
     */
    public static void MessageAndInfo(String messageText, String infoText){
        JFrame frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);

        Dimension messageDim = frame.getSize();
        double w = messageDim.width;

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


        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());

        JLabel info = new JLabel(infoText);
        infoPanel.add(info);


        frame.add(messagePanel);
        frame.add(infoPanel);

        frame.setLayout(new GridLayout(2,1));

        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);


    }

    /**
     * Displays billboard with Message, Image and Info
     * @param messageText Message text to display
     * @param imageString URL of image to display
     * @param infoText Info text to display
     * @throws IOException
     */
    public static void MessageImageInfo(String messageText, String imageString, String infoText) throws IOException {
        JFrame frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());

        JLabel message = new JLabel(messageText, JLabel.CENTER);
        messagePanel.add(message);

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridBagLayout());

        URL imageURL = new URL(imageString);
        //Turns URL to bufferedImage
        BufferedImage decodedImage = ImageIO.read(imageURL);
        //Turns bufferedImage to icon
        ImageIcon iconImage = new ImageIcon(decodedImage);

        JLabel image = new JLabel(iconImage, JLabel.CENTER);
        imagePanel.add(image);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());

        JLabel info = new JLabel(infoText);
        infoPanel.add(info);


        frame.add(messagePanel);
        frame.add(imagePanel);
        frame.add(infoPanel);

        frame.setLayout(new GridLayout(3,1));

        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

    /**
     * Displays billboard with message and image
     * @param messageText Text of message to display
     * @param imageString URL of image to display
     * @throws IOException
     */
    public static void MessageAndImage(String messageText, String imageString) throws IOException {
        JFrame frame = new JFrame("View Billboard");
        frame.setLayout(new GridBagLayout());
        frame.setSize(1000, 700);
        GridBagConstraints gbc = new GridBagConstraints();


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

        URL imageURL = new URL(imageString);
        //Turns URL to bufferedImage
        BufferedImage decodedImage = ImageIO.read(imageURL);
        //Turns bufferedImage to icon
        ImageIcon iconImage = new ImageIcon(decodedImage);

        JLabel image = new JLabel(iconImage, JLabel.CENTER);
        imagePanel.add(image);


        frame.add(imagePanel, gbc);


        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }
}