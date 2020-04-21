import javax.swing.*;
import java.awt.*;

public class MessageOnly {

    public static void main(String args[]) {
        JFrame frame = new JFrame("Control Panel");
        int FRAME_WIDTH = 1000;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String message = "Welcome to the fundraiser!";
        String info = "Please take your seats in the ballroom. Seats are assigned, so please make use of the seating plan";

        JLabel LTitle = new JLabel(message, JLabel.CENTER);
        JLabel LInfo = new JLabel(info, JLabel.CENTER);

        frame.add(LTitle);
        frame.pack();
        frame.setSize(FRAME_WIDTH, 700);

        Dimension dim = frame.getSize();
        int x = dim.width;
        //int x = 1000;
        int y = dim.height;

        LTitle.setSize(x, y);
        Font TitleFont = LTitle.getFont();
        int size = TitleFont.getSize();
        int width = LTitle.getFontMetrics(TitleFont).stringWidth(message);
        System.out.println("Width :" + width);
        System.out.println("x :" + x);

        while(width<x-10){
            size++;
            LTitle.setFont(new Font("Serif", Font.PLAIN, size));
            width = LTitle.getFontMetrics(LTitle.getFont()).stringWidth(message);
            System.out.println("Width :" + width);
        }

        //set font back to smaller to not overflowing
        LTitle.setFont(new Font("Serif", Font.PLAIN, size-2));
        width = LTitle.getFontMetrics(LTitle.getFont()).stringWidth(message);
        System.out.println("Width :" + width);

        //frame.add(LInfo);
        //frame.pack();
        frame.setSize(FRAME_WIDTH, 700);

        frame.setVisible(true);
    }
}
