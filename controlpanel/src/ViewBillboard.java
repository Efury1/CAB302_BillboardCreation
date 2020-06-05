import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * Handles displaying the billboards.
 */
public class ViewBillboard {
    public static JFrame frame;
    /**
     * Given a billboard, chooses which function should be called to display it and calls this function
     * @param b billboard to be displayed
     */
    public static void showBillboard(Billboard b) throws IOException, SQLException {
        //TODO Add colour support
        if(b.hasMessage()&&b.hasDescription()&&b.hasImage()){
            //All three elements present
            MessageImageInfo(b.getBMessage(), b.getBBlobData(), b.getBDescription(), b.getBMessageColour(), b.getBDescriptionColour(), b.getBBackgroundColour());
        }
        else{
            if(b.hasMessage()&&b.hasDescription()){
                //Message and info only
                MessageAndInfo(b.getBMessage(), b.getBDescription(), b.getBMessageColour(), b.getBDescriptionColour(), b.getBBackgroundColour());
            }
            else {
                if(b.hasDescription()&&b.hasImage()){
                    //Info and image only
                    ImageAndInfo(b.getBBlobData(), b.getBDescription(), b.getBDescriptionColour(), b.getBBackgroundColour());
                }
                else{
                    if(b.hasMessage()&&b.hasImage()){
                        //Message and image only
                        MessageAndImage(b.getBMessage(), b.getBBlobData(), b.getBMessageColour(), b.getBBackgroundColour());
                    }
                    else{
                        if(b.hasMessage()){
                            //Message only
                            Message(b.getBMessage(), b.getBMessageColour(), b.getBBackgroundColour());
                        }
                        else{
                            if(b.hasImage()){
                                //Image only
                                Image(b.getBBlobData(), b.getBBackgroundColour());
                            }
                            else{
                                if(b.hasDescription()){
                                    //Info only
                                    Info(b.getBDescription(), b.getBDescriptionColour(), b.getBBackgroundColour());
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
    public static void Message(String messageText, String messageColour, String backgroundColour){
        frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);
        frame.setForeground(ConvertToRGB(backgroundColour));

        Dimension messageDim = frame.getSize();
        double w = messageDim.width;

        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(ConvertToRGB(backgroundColour));
        messagePanel.setLayout(new GridBagLayout());


        JLabel message = new JLabel(messageText, JLabel.CENTER);
        message.setForeground(ConvertToRGB(messageColour));
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

        frame.add(messagePanel);

        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

    /**
     * Displays billboard with only Information element
     * @param rawInfoText Text of information to be displayed on billboard
     */
    public static void Info(String rawInfoText, String infoColour, String backgroundColour){
        frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);
        frame.setForeground(ConvertToRGB(backgroundColour));

        //Wraps in HTML tags so can span multiple lines
        String infoText = "<html>"+ rawInfoText+"</html>";

        Dimension infoDim = frame.getSize();
        double w = infoDim.width;

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(ConvertToRGB(backgroundColour));
        infoPanel.setLayout(new GridBagLayout());


        JLabel info = new JLabel(infoText, JLabel.CENTER);
        info.setForeground(ConvertToRGB(infoColour));
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

        frame.add(infoPanel);

        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

    /**
     * Displays billboard with message and information elements
     * @param messageText Text of message to be displayed on billboard
     * @param infoText Text of information to be displayed on billboard
     */
    public static void MessageAndInfo(String messageText, String infoText, String messageColour, String infoColour, String backgroundColour){
        frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);
        frame.setForeground(ConvertToRGB(backgroundColour));

        Dimension messageDim = frame.getSize();
        double w = messageDim.width;

        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(ConvertToRGB(backgroundColour));
        messagePanel.setLayout(new GridBagLayout());


        JLabel message = new JLabel(messageText, JLabel.CENTER);
        message.setForeground(ConvertToRGB(messageColour));
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
        infoPanel.setBackground(ConvertToRGB(backgroundColour));
        infoPanel.setLayout(new GridBagLayout());

        JLabel info = new JLabel(infoText);
        info.setForeground(ConvertToRGB(infoColour));
        infoPanel.add(info);


        frame.add(messagePanel);
        frame.add(infoPanel);

        frame.setLayout(new GridLayout(2,1));

        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

    private static Color ConvertToRGB(String hexColour) {
        return new Color(Integer.valueOf(hexColour.substring(0, 2), 16), Integer.valueOf(hexColour.substring(2, 4), 16), Integer.valueOf(hexColour.substring(4, 6), 16));
    }

    public static void CloseBillboard(){

    }

    /**
     * Displays billboard with Message, Image and Info
     * @param messageText Message text to display
     * @param imageString URL of image to display
     * @param infoText Info text to display
     * @throws IOException
     */

    public static void MessageImageInfo(String messageText, Blob imageString, String infoText, String messageColour, String infoColour, String backgroundColour) throws IOException, SQLException {
        frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);
        Container c = frame.getContentPane();
        c.setBackground(ConvertToRGB(backgroundColour));

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        messagePanel.setBackground(ConvertToRGB(backgroundColour));
        JLabel message = new JLabel(messageText, JLabel.CENTER);
        message.setForeground(ConvertToRGB(messageColour));
        messagePanel.add(message);

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.setBackground(ConvertToRGB(backgroundColour));

        //URL imageURL = new URL(imageString);
        //Turns URL to bufferedImage
        //BufferedImage decodedImage = ImageIO.read(imageURL);
        //Turns bufferedImage to icon
        //ImageIcon iconImage = new ImageIcon(decodedImage);

        ImageIcon iconImage = new ImageIcon();

        iconImage = ConvertToImageIcon(imageString);

        JLabel image = new JLabel(iconImage, JLabel.CENTER);
        imagePanel.add(image);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setBackground(ConvertToRGB(backgroundColour));

        JLabel info = new JLabel(infoText);
        info.setForeground(ConvertToRGB(infoColour));
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
    public static void MessageAndImage(String messageText, Blob imageString, String messageColour, String backgroundColour) throws IOException, SQLException {
        frame = new JFrame("View Billboard");
        frame.setForeground(ConvertToRGB(backgroundColour));
        frame.setLayout(new GridBagLayout());
        frame.setSize(1000, 700);
        GridBagConstraints gbc = new GridBagConstraints();


        Dimension messageDim = frame.getSize();
        double w = messageDim.width;
        double h = messageDim.height/3;


        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(ConvertToRGB(backgroundColour));
        gbc.weighty = 0.33;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.VERTICAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        messagePanel.setLayout(new GridBagLayout());


        JLabel message = new JLabel(messageText, JLabel.CENTER);
        message.setForeground(ConvertToRGB(messageColour));
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
        imagePanel.setBackground(ConvertToRGB(backgroundColour));
        gbc.weighty = 0.66;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        imagePanel.setLayout(new GridBagLayout());

        //URL imageURL = new URL(imageString);
        //Turns URL to bufferedImage
        //BufferedImage decodedImage = ImageIO.read(imageURL);
        //Turns bufferedImage to icon
        //ImageIcon iconImage = new ImageIcon(decodedImage);

        ImageIcon iconImage = ConvertToImageIcon(imageString);

        JLabel image = new JLabel(iconImage, JLabel.CENTER);
        imagePanel.add(image);


        frame.add(imagePanel, gbc);


        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

    /**
     * Shows billboard with image only
     * @param imageLink URL of image to be displayed
     * @throws IOException
     */
    public static void Image(Blob imageLink, String backgroundColour) throws IOException, SQLException {
        frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);
        frame.setForeground(ConvertToRGB(backgroundColour));


        Dimension imageDim = frame.getSize();
        double w = imageDim.width;

        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(ConvertToRGB(backgroundColour));
        imagePanel.setLayout(new GridBagLayout());


        //URL imageURL = new URL(imageLink);
        //Turns URL to bufferedImage
        //BufferedImage decodedImage = ImageIO.read(imageURL);
        //Turns bufferedImage to icon
        //ImageIcon iconImage = new ImageIcon(decodedImage);

        ImageIcon imageIcon = ConvertToImageIcon(imageLink);

        JLabel image = new JLabel(imageIcon, JLabel.CENTER);

        imagePanel.add(image);

        frame.add(imagePanel);

        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

    /**
     * Converts a BLOB from the server into a displayable Image Icon.
     * @param imageBlob The Blob Object of the picture to display
     * @return An ImageIcon Object to use with the GUI.
     * @throws SQLException
     * @throws IOException
     */
    private static ImageIcon ConvertToImageIcon(Blob imageBlob) throws SQLException, IOException {
        InputStream inputStream = imageBlob.getBinaryStream();
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        Image image = bufferedImage;
        return new ImageIcon(image);
    }

    /**
     * Shows billboard with image and info only
     * @param imageString URL of image to display
     * @param infoText Text of information to display
     * @throws IOException
     */
    public static void ImageAndInfo(Blob imageString, String infoText, String infoColour, String backgroundColour) throws IOException, SQLException {
        frame = new JFrame("View Billboard");
        frame.setSize(1000, 700);
        frame.setForeground(ConvertToRGB(backgroundColour));

        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(ConvertToRGB(backgroundColour));
        imagePanel.setLayout(new GridBagLayout());

        //URL imageURL = new URL(imageString);
        //Turns URL to bufferedImage
        //BufferedImage decodedImage = ImageIO.read(imageURL);
        //Turns bufferedImage to icon
        //ImageIcon iconImage = new ImageIcon(decodedImage);

        ImageIcon iconImage = new ImageIcon();

        iconImage = ConvertToImageIcon(imageString);

        JLabel image = new JLabel(iconImage, JLabel.CENTER);
        imagePanel.add(image);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(ConvertToRGB(backgroundColour));
        infoPanel.setLayout(new GridBagLayout());

        JLabel info = new JLabel(infoText);
        info.setForeground(ConvertToRGB(infoColour));
        infoPanel.add(info);


        frame.add(imagePanel);
        frame.add(infoPanel);

        frame.setLayout(new GridLayout(2,1));

        frame.pack();
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }
}