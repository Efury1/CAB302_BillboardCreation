import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI  {



    //Usually you will require both swing and awt packages
// even if you are working with just swings.
    public static void main(String args[]) {

        //Creating the Frame
        JFrame frame = new JFrame("Control Panel Review");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000, 800);

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
        JMenuItem m33 = new JMenuItem("Save");
        m1.add(m11);
        m1.add(m22);
        m1.add(m33);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        //Text box
        JLabel label1 = new JLabel("Enter Text");
        JTextField tf = new JTextField(10); // accepts upto 10 characters
        JButton submit = new JButton("Submit");
        JButton clear = new JButton("Clear");
        /*Color button */
        JButton color = new JButton("Color");
        panel.add(label1); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(submit);
        panel.add(clear);
        panel.add(color);


        // Text Area at the Center
        JTextArea ta = new JTextArea();

        //add action listener to button
        //submit.addActionListener();


        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        //Centre
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);



    }

    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("submit")){
            //setting text of label to text of the field
            l.setText(ta.getText());
        }
    }


}

