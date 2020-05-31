
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.File;
import java.sql.Blob;
import java.sql.SQLException;
/* Main screen */
/**
 * @author Eliza & Lauren & Lachie
 * @version last edited 31.05.20
 * Worked on designing GUI() screen.
 * Implemented action listeners that allows screen to pick up text
 * and change color of background.
 * Linked functionality to billboard (@author Lauren).
 * Renamed functions to understandable names (@author Lachie).
 * Implemented Logout function and revised login function (@author Lachie).
 */

/*Note: we may need threading when it comes to refreshing the ratios.
* So, it doesn't interfere with the programming logic. */
    //TODO Need to scale image so it's not going over into other buttons
    //TODO Schedule needs drop down menu
    //TODO Add and Delete user screens
public class GUI extends Component {

    public GUI () {
        JTextArea jt;

        JFrame frame = new JFrame("Control Panel Review");
        frame.setBounds(30, 30, 1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();

        JPanel infoPanel = new JPanel();
        JLabel info = new JLabel(" ");



        jt = new JTextArea(80, 20);
        infoPanel.add(jt);

        JButton submitButton = new JButton("Submit");


        JMenu bkMenu = new JMenu("Background");
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JButton viewBillboards = new JButton("View Billboards");

        /*Adding menu */
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewBillboards);
        /*Under file */
        JMenuItem importMenu = new JMenuItem("Import");
        JMenuItem exportMenu = new JMenuItem("Export");
        JMenuItem saveAndSchedule = new JMenuItem("Schedule and Save");
        JMenuItem menuLogout = new JMenuItem("Logout");
        JMenuItem colorYellow = new JMenuItem("Yellow");
        JMenuItem colorBlue = new JMenuItem("Blue");
        JMenuItem colorRed = new JMenuItem("Red");


        fileMenu.add(importMenu);
        fileMenu.add(exportMenu);
        fileMenu.add(saveAndSchedule);
        fileMenu.add(menuLogout);
        bkMenu.add(colorYellow);
        bkMenu.add(colorBlue);
        bkMenu.add(colorRed);
        /*Under edit */
        JMenuItem menuImageLoad = new JMenuItem("Upload Image");
        JMenuItem editUserPermission = new JMenuItem("Edit Users");
        editMenu.add(editUserPermission);
        editMenu.add(menuImageLoad);
        editMenu.add(bkMenu);

        JLabel resultLabel = new JLabel(" ");
        JLabel label = new JLabel("Type message and press enter");
        JTextField tf = new JTextField(10); // accepts up to 10 characters
        JLabel label1 = new JLabel("Type information");
        //To button b where I want it.
        JLabel spacer = new JLabel("                                                                                             ");
        JLabel imagel = new JLabel(" ");
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(imagel);
        //panel.add(color);
        JRadioButton r1 = new JRadioButton("Yellow");
        JRadioButton r2 = new JRadioButton("Blue");
        JRadioButton r3 = new JRadioButton("Red");
        panel.add(r1);
        panel.add(r2);
        panel.add(r3);
        panel.add(spacer);
        panel.add(submitButton);
        panel1.add(resultLabel);
        panel1.add(info);



        /* actionPerformed */
        /**
         * The user can interact with GUI to create their billboard.
         * There's also, the ability to go to other frames like View Billboards, schedule etc.
         * @param aEvent from these buttons: 1.Upload Image 2.Edit Users 3.Schedule 4. Typing text etc
         */
        menuImageLoad.addActionListener(e -> {
            //selectFile();

                JFileChooser file = new JFileChooser();
                file.setCurrentDirectory(new File("."));
                String[] extensions = ImageIO.getReaderFileSuffixes();
                file.setFileFilter(new FileNameExtensionFilter("Image files", extensions));
                int result = file.showSaveDialog(null);
                if(result == JFileChooser.APPROVE_OPTION)
                {
                    try{
                        File selectedFile = file.getSelectedFile();
                        String path = selectedFile.getAbsolutePath();
                        Blob imageBlob = ReadImage(selectedFile);

                        ImageIcon icon = ConvertToImageIcon(imageBlob);
                        imagel.setIcon(icon);

                    } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex); }


                }

        });



        editUserPermission.addActionListener(e -> {
            editUsers();
        });

        saveAndSchedule.addActionListener(e -> {
            ScheduleModel cal = new ScheduleModel();
            this.setSize(700, 400);
            this.setVisible(true);

            //Schedule session1 = new Schedule();
            //SaveSchedule session2 = new SaveSchedule();
            //session1.setVisible(true);
        });

        menuLogout.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "You have successfully logged out.");
            System.exit(0);
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
                } else if (event.getSource() == colorRed) {
                    color = Color.RED;
                    colorRed.setBackground(color);
                    panel1.setBackground(color);
                }
                setBackground(color);
                repaint();
            }
        }

        submitButton.addActionListener(new ActionListener()
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


        viewBillboards.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                SelectBillboard session1 = new SelectBillboard();
                try {
                    session1.showSelectionScreen();
                } catch (IOException ex) {
                    ex.printStackTrace(); //TODO remove printStackTrace
                }
            }
        });

        colorYellow.addActionListener(new Listener());
        colorBlue.addActionListener(new Listener());
        colorBlue.addActionListener(new Listener());


        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, panel1);
        frame.getContentPane().add(BorderLayout.EAST, infoPanel);
        frame.setVisible(true);

    }

    private ImageIcon ConvertToImageIcon(Blob imageBlob) throws SQLException, IOException {
        InputStream inputStream = imageBlob.getBinaryStream();
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        Image image = bufferedImage;
        return new ImageIcon(image);
    }

    private Blob ReadImage(File selectedFile) throws IOException, SQLException {
        BufferedImage image = ImageIO.read(selectedFile);
        ByteArrayOutputStream Output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", Output);
        Blob myBlob = new SerialBlob(Output.toByteArray());
        return myBlob;
    }


    public void editUsers() {

        /* Create and set up a frame window */
        JFrame frame = new JFrame("Edit Users");

        //Delete Button
        JButton deleteBtn = new JButton("Delete User");
        JButton changePass = new JButton("Change Password");
        JButton addUser = new JButton("Add User");
        JPanel panel = new JPanel();
        panel.add(addUser);
        panel.add(changePass);
        panel.add(deleteBtn);
        String data[][]={ {"User1","Permission"},
                {"User2","Permission"},
                {"User3","Permission"}};
        String column[]={"User","PermissionType"};
        //JTable table = new JTable(data, column);
        DefaultTableModel model = new DefaultTableModel(data, column);
        JTable userTable=new JTable(model);
        ListSelectionModel select = userTable.getSelectionModel();


        //TODO ready for sever functionality, needs to get connection
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Data = null;
                int[] row = userTable.getSelectedRows();
                int[] columns = userTable.getSelectedColumns();
                for( int i = userTable.getRowCount() - 1; i >= 0; )
                {
                    DefaultTableModel model =
                            (DefaultTableModel)userTable.getModel();
                    model.removeRow(i);
                }
            }
        });

        /*Add user */
        addUser.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Create window

                JFrame w1 = new JFrame();
                // set panel
                JPanel p = new JPanel();
                // create a label
                JLabel l = new JLabel("Testing");
                // set border
                p.setBorder(BorderFactory.createLineBorder(Color.darkGray));
                p.add(l);
                w1.add(p);
                // set background
                p.setBackground(Color.white);
                //Set Location
                w1.setLocation(300, 300);
                // setsize of window
                w1.setSize(200, 100);
                // set visibility of window
                w1.setVisible(true);
                w1.setDefaultCloseOperation(frame.HIDE_ON_CLOSE);
            }
        });

        /*You're able to change table value */
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        select.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String Data = null;
                int[] row = userTable.getSelectedRows();
                int[] columns = userTable.getSelectedColumns();
                JButton deleteBtn = new JButton("Delete User");
                for (int i = 0; i < row.length; i++)
                {
                    for (int j = 0; j < columns.length; j++)
                    {
                        Data = (String) userTable.getValueAt(row[i], columns[j]);
                    }
                    //Tells you want has been selected, this is more for testing
                    System.out.println("The selected table element is: " + Data);
                }

            }
        });




        JScrollPane sp=new JScrollPane(userTable);
        JPanel panel2 = new JPanel();
        panel2.add(sp);
        frame.setLayout(new BorderLayout());
        //frame.add(new JLabel("Edit Users"), BorderLayout.NORTH);
        //frame.add(userTable, BorderLayout.CENTER);
        frame.getContentPane().add(BorderLayout.CENTER, panel2);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);

    }



    }



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



