import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {
    public static void main(String args[]) {
        new GUI();
    }


        //Usually you will require both swing and awt packages
// even if you are working with just swings.

    public GUI() {
        //JPanel buttonPanel;
        //Creating the Frame
        int Width = 900;
        int Height = 700;
        JFrame frame = new JFrame("Control Panel Review");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Width, Height);


        //Creating the MenuBar and adding components
        /*Across tab */
        JMenu bkMenu = new JMenu("Background");
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Edit");
        JMenu m3 = new JMenu("View Billboards");
        /*Adding menu */
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);
        /*Under file */
        JMenuItem m11 = new JMenuItem("Import");
        JMenuItem m22 = new JMenuItem("Export");
        JMenuItem m44 = new JMenuItem("Schedule and Save");
        JMenuItem colorYellow = new JMenuItem("Yellow");
        JMenuItem colorBlue = new JMenuItem("Blue");
        JMenuItem colorRed = new JMenuItem("Red");
        //Button actions
        var yellowAction = new ColorAction(Color.YELLOW);
        var blueAction = new ColorAction(Color.BLUE);
        var redAction = new ColorAction(Color.RED);
        //Association
        colorYellow.addActionListener(yellowAction);
        colorBlue.addActionListener(blueAction);
        colorRed.addActionListener(blueAction);

        m1.add(m11);
        m1.add(m22);
        m1.add(m44);
        bkMenu.add(colorYellow);
        bkMenu.add(colorBlue);
        bkMenu.add(colorRed);
        /*Under edit */
        //JMenuItem menuBackground = new JMenuItem("Background");
        JMenuItem menuImageLoad = new JMenuItem("Upload Image");
        //m2.add(menuBackground);
        m2.add(menuImageLoad);
        m2.add(bkMenu);



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

        //public void ssWindow(){

            final boolean RESIZABLE = true;
            final boolean CLOSABLE = true;
            final boolean ICONIFIABLE = true;
            final boolean MAXIMIZABLE = true;

            JInternalFrame firstWindow = new JInternalFrame("schedule and save", RESIZABLE,
                    CLOSABLE, MAXIMIZABLE, ICONIFIABLE);
            firstWindow.setSize(100, 100);
            firstWindow.setLocation(10, 10);
            firstWindow.setVisible(true);
            frame.add(firstWindow);

        //}



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
        //int bkMenu = 1000;
        Height = dim.height;

        LTitle.setSize(Width, Height);
        Font TitleFont = LTitle.getFont();
        int size = TitleFont.getSize();
        int width = LTitle.getFontMetrics(TitleFont).stringWidth(message);
        System.out.println("Width :" + width);
        System.out.println("bkMenu :" + Width);

        while (width < Width - 10) {
            size++;
            LTitle.setFont(new Font("Serif", Font.PLAIN, size));
            width = LTitle.getFontMetrics(LTitle.getFont()).stringWidth(message);
            System.out.println("Width :" + width);
        }

        //set font back to smaller to not overflowing
        LTitle.setFont(new Font("Serif", Font.PLAIN, size - 2));
        width = LTitle.getFontMetrics(LTitle.getFont()).stringWidth(message);
        System.out.println("Width :" + width);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        //frame.getContentPane().add(BorderLayout.CENTER, LInfo);
        frame.getContentPane().add(BorderLayout.CENTER, resultLabel);
        frame.setVisible(true);

    }

    //ActionListener will call this class when Schedule and save button is clicked.
    //Code works, just trying to implement, so it's its own class that can still get frame information



}


    class ColorAction implements ActionListener{
        private Color backgroundColor;
        public ColorAction(Color c) {
            backgroundColor = c;
        }
        public void actionPerformed(ActionEvent event) {
            //buttonPanel.setBackground(backgroundColor);
            System.out.println("Success");
        }
        }

