import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI  {



    //Usually you will require both swing and awt packages
// even if you are working with just swings.
    public static void main(String args[]) {

        //Creating the Frame
        int Width = 900;
        int Height = 700;
        JFrame frame = new JFrame("Control Panel Review");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Width, Height);



        //Creating the MenuBar and adding components
        /*Across tab */
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Background");
        JMenu m3 = new JMenu("Image upload");
        JMenu m4 = new JMenu("Modify");
        JMenu m5 = new JMenu("Delete");
        /*Adding menu */
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);
        mb.add(m4);
        mb.add(m5);
        /*Under file */
        JMenuItem m11 = new JMenuItem("Import");
        JMenuItem m22 = new JMenuItem("Export");
        JMenuItem m33 = new JMenuItem("Billboards");
        JMenuItem m44 = new JMenuItem("Schedule and Save");
        m1.add(m11);
        m1.add(m22);
        m1.add(m33);
        m1.add(m44);

        JLabel resultLabel = new JLabel("--");

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        //Appears next to JTextField
        JLabel label = new JLabel("Type text and press enter");
        JTextField tf = new JTextField(10); // accepts up to 10 characters
        //Don't think text button is needed if TextField submits as you type to label
        //JButton submit = new JButton("Submit");
        /*Color button */
        JButton color = new JButton("Color");
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(color);

        //When you enter text, it submits to terminal
        //Working on printing to screen
        String testing = tf.getText();
        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String text = tf.getText();
                resultLabel.setText(text);
                //System.out.println("The entered text is: " + tf.getText());
            }
        });


        // Text Area at the Center
        //JTextArea ta = new JTextArea();
        //String message = "Welcome to today's fundraiser!";

        //int FRAME_WIDTH = 2000;
        //Sorting out the ratio
        String message = "Welcome to today's fundraiser!";
        String info = "Please take your seats in the ballroom. Seats are assigned, so please make use of the seating plan";
        //String info = "Please take your seats in the ballroom. Seats are assigned, so please make use of the seating plan";

        JLabel LTitle = new JLabel(message, JLabel.CENTER);
        JLabel LInfo = new JLabel(info, JLabel.CENTER);

        //frame.add(LTitle);
        //frame.pack();
        //frame.setSize(FRAME_WIDTH, 700);

        Dimension dim = frame.getSize();
        Width = dim.width;
        //int x = 1000;
        Height = dim.height;

        LTitle.setSize(Width, Height);
        Font TitleFont = LTitle.getFont();
        int size = TitleFont.getSize();
        int width = LTitle.getFontMetrics(TitleFont).stringWidth(message);
        System.out.println("Width :" + width);
        System.out.println("x :" + Width);

        while(width<Width-10){
            size++;
            LTitle.setFont(new Font("Serif", Font.PLAIN, size));
            width = LTitle.getFontMetrics(LTitle.getFont()).stringWidth(message);
            System.out.println("Width :" + width);
        }

        //set font back to smaller to not overflowing
        LTitle.setFont(new Font("Serif", Font.PLAIN, size-2));
        width = LTitle.getFontMetrics(LTitle.getFont()).stringWidth(message);
        System.out.println("Width :" + width);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        //frame.getContentPane().add(BorderLayout.CENTER, LInfo);
        frame.getContentPane().add(BorderLayout.CENTER, resultLabel);
        frame.setVisible(true);



    }

}

