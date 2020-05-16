import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/*Note: we may need threading when it comes to refreshing the ratios.
* So, it doesn't interfere with the programming logic */
public class GUI extends Component {
    public static void main(String args[]) {
        new GUI();
    }


    public GUI () {
        JTextArea jt;

        JFrame frame = new JFrame("Control Panel Review");
        frame.setBounds(30, 30, 1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();

        JPanel infoPanel = new JPanel();
        JLabel info = new JLabel("Waiting for information");


        jt = new JTextArea(80, 20);
        infoPanel.add(jt);

        JButton b = new JButton("Submit");


        JMenu bkMenu = new JMenu("Background");
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Edit");
        JButton m3 = new JButton("View Billboards");

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


        m1.add(m11);
        m1.add(m22);
        m1.add(m44);
        bkMenu.add(colorYellow);
        bkMenu.add(colorBlue);
        bkMenu.add(colorRed);
        /*Under edit */
        JMenuItem menuImageLoad = new JMenuItem("Upload Image");
        m2.add(menuImageLoad);
        m2.add(bkMenu);

        JLabel resultLabel = new JLabel("--");
        JLabel label = new JLabel("Type Message and press enter");
        JTextField tf = new JTextField(10); // accepts up to 10 characters
        JButton color = new JButton("Color");
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(color);
        panel.add(b);
        panel1.add(resultLabel);
        panel1.add(info);

        //Action Listeners
        menuImageLoad.addActionListener(e -> {
            selectFile();
        });

        m44.addActionListener(e -> {
            SaveSchedule session2 = new SaveSchedule();
            session2.setVisible(true);
        });


        String testing = tf.getText();
        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String text = tf.getText();
                resultLabel.setText(text);

            }
        });



        class Listener extends JPanel implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                Color color = null;
                if (event.getSource() == colorYellow) {
                    color = Color.YELLOW;
                    colorYellow.setBackground(color);
                    panel1.setBackground(color);
                } else if (event.getSource() == colorBlue) {
                    color = Color.BLUE;
                    colorBlue.setBackground(color);
                    panel1.setBackground(color);
                }
                /*Color Red doesn't work, unsure what's happening */
                //(event.getSource() == colorRed)
                else  {
                    color = Color.BLUE;
                    colorRed.setBackground(color);
                    panel1.setBackground(color);
                }

                setBackground(color);
                repaint();
            }
        }

        b.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String s = e.getActionCommand();
                if(s.equals("Submit")) {
                    info.setText(jt.getText());
                }
                //SelectBillboard session1 = new SelectBillboard();
                //session1.showSelectionScreen();
            }
        });


        m3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                //SelectBillboard session1 = new SelectBillboard();
                //session1.showSelectionScreen();
            }
        });

        colorYellow.addActionListener(new Listener());
        colorBlue.addActionListener(new Listener());
        colorBlue.addActionListener(new Listener());


        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, panel1);
        frame.getContentPane().add(BorderLayout.EAST, infoPanel);
        frame.setVisible(true);

    }

    public class SaveSchedule extends JFrame

    {


        public SaveSchedule()
        {
            super("Save and schedule");
            setSize(300, 200);

        }


    }


    /*User needs to be able to close this class without exiting all windows, Eliza */
    public class Billboard extends JFrame

    {
        JScrollPane scrollpane;

        public Billboard()
        {
            super("Billboard Viewer");
            setSize(300, 200);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            String categories[] = {"Example 1", "Example 2", "Example 3","Example 4", "Example 5", "Example 6", "Example 7", "Example 8", "Example 9" };
            JList billboardList = new JList(categories);
            scrollpane = new JScrollPane(billboardList);
            getContentPane().add(scrollpane, BorderLayout.CENTER);
        }


    }

    /*public class DrawCanvas extends JPanel {

        // Override paintComponent to perform your own painting
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);     // paint parent's background
            setBackground(Color.BLACK);  // set background color for this JPanel
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("testing", 10, 20);
        }
    }*/
    //Selecting File
    public void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }


    }



    }


/*class ssWindow() {

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

        }*/



//String message = "Welcome to today's fundraiser!";

//int FRAME_WIDTH = 2000;
//Sorting out the ratio
        /*String message = "Welcome to today's fundraiser!";
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
        System.out.println("Width :" + width);*/



