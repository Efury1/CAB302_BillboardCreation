
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.File;
import java.sql.Array;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
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

    private String username;

    public GUI (String username) {
        this.username = username;

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

        if (!GetUserPerms()) {
            JOptionPane.showMessageDialog(frame, "Could not retrieve your user permissions, please log-out and try again.");
            System.exit(0);
        }

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
        editUserPermission.setEnabled(permUsers);
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
        JFrame editFrame = new JFrame("Edit Users");

        //Delete Button
        JButton deleteBtn = new JButton("Delete User");
        JButton changePass = new JButton("Change Password");
        JButton addUser = new JButton("Add User");
        JButton refresh = new JButton("Refresh");
        JPanel panel = new JPanel();
        panel.add(addUser);
        panel.add(changePass);
        panel.add(deleteBtn);
        panel.add(refresh);

        //GetUserPerms(frame)

        //String data1[][];

        final Object[][][] allUsersArray = {new Object[][]{}};

        try {
            Object[] allUsers = ClientRequests.ListUsers();
            allUsersArray[0] = new Object[allUsers.length][5];

            for (int i = 0; i < allUsers.length; i++) {
                Object[] userPerms = ClientRequests.GetUserPermissions(allUsers[i].toString());
                allUsersArray[0][i][0] = (String)allUsers[i].toString();
                allUsersArray[0][i][1] = (Boolean)userPerms[0];
                allUsersArray[0][i][2] = (Boolean)userPerms[1];
                allUsersArray[0][i][3] = (Boolean)userPerms[2];
                allUsersArray[0][i][4] = (Boolean)userPerms[3];

            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(editFrame, e.getMessage());
            return;
        }

        String column[]={"User", "Create", "Edit All Billboards", "Edit Users", "Schedule Billboards"};

        DefaultTableModel model = new DefaultTableModel(allUsersArray[0], column);
        JTable userTable = new JTable(model);

        ListSelectionModel select = userTable.getSelectionModel();


        //TODO ready for server functionality, needs to get connection
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for( int i = 0; i < userTable.getRowCount(); i++)
                {
                    if (userTable.isRowSelected(i)){
                        String userToDelete = (String)userTable.getValueAt(i, 0);
                        if(userToDelete.equals(username)){
                            JOptionPane.showMessageDialog(editFrame, "You cannot delete yourself");
                        } else {
                            try {
                                ClientRequests.DeleteUser(userToDelete);
                                model.removeRow(i);
                            } catch (IOException | ClassNotFoundException deleteUserError) {
                                JOptionPane.showMessageDialog(editFrame, deleteUserError);
                            } finally {
                                break;
                            }
                        }

                    }
                }
            }
        });

        /*Add user */
        addUser.addActionListener(new ActionListener() {

            private JTextField textField = new JTextField();
            private JPasswordField passwordField = new JPasswordField();

            @Override
            public void actionPerformed(ActionEvent e) {
                //Create window

                JFrame confirmFrame = new JFrame("Control Panel Review");
                confirmFrame.setDefaultCloseOperation(confirmFrame.EXIT_ON_CLOSE);
                confirmFrame.setBounds(100, 100, 500, 400);
                confirmFrame.setResizable(false);
                JPanel contentPane = new JPanel();
                confirmFrame.setContentPane(contentPane);
                contentPane.setLayout(null);

                JLabel createUserLabel = new JLabel("Create User...");
                Font font = createUserLabel.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                createUserLabel.setFont(font.deriveFont(attributes));
                createUserLabel.setBounds(50, 40, 120, 40);
                contentPane.add(createUserLabel);
                textField = new JTextField();
                //Username text field
                textField.setBounds(50, 100, 200, 20);
                contentPane.add(textField);
                textField.setColumns(10);

                passwordField = new JPasswordField();
                passwordField.setBounds(50, 140, 200, 20);
                contentPane.add(passwordField);

                JLabel userLabel = new JLabel("Username:");
                userLabel.setBounds(50, 65, 193, 52);
                contentPane.add(userLabel);

                JLabel passLabel = new JLabel("Password:");

                passLabel.setBounds(50, 105, 193, 52);
                contentPane.add(passLabel);

                //RADIO LABELS
                JRadioButton permCreate = new JRadioButton("Create Billboards");
                JRadioButton permEditBB = new JRadioButton("Edit Billboards");
                JRadioButton permUsers = new JRadioButton("Edit Users");
                JRadioButton permSchedule = new JRadioButton("Schedule Billboards");

                //RADIO PANEL
                JPanel radioPanel = new JPanel();
                radioPanel.setLayout(new GridLayout(4, 1));
                radioPanel.add(permCreate);
                radioPanel.add(permEditBB);
                radioPanel.add(permUsers);
                radioPanel.add(permSchedule);

                radioPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "Permissions:"));

                radioPanel.setBounds(300, 50, 150, 150);
                contentPane.add(radioPanel);


                JButton confirmButton = new JButton("Confirm");
                confirmButton.setBounds(50, 170, 100, 30);
                contentPane.add(confirmButton);
                confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String uName = textField.getText();
                        String pass = passwordField.getText();  //  getText() is deprecated for JPasswordField (find other method)
                        /*Tutor will now be able to login in with name and password */
                        try {
                            ClientRequests.CreateUser(uName, permCreate.isSelected(), permEditBB.isSelected(), permUsers.isSelected(), permSchedule.isSelected(), pass);
                            JOptionPane.showMessageDialog(confirmFrame, "You have successfully created user: " + uName + ".");
                            model.fireTableDataChanged();
                            editFrame.setVisible(false);
                            editUsers();
                            confirmFrame.setVisible(false);
                        } catch (ClassNotFoundException | IOException error1) {
                            JOptionPane.showMessageDialog(confirmFrame, error1.getMessage());
                        }
                    }
                });
                //  Screen set up
                confirmFrame.setDefaultCloseOperation(confirmFrame.HIDE_ON_CLOSE);
                confirmFrame.setTitle("User Create");
                confirmFrame.setVisible(true);
                confirmFrame.setSize(600, 300);
                confirmFrame.setResizable(false);
            }
        });

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                editFrame.setVisible(false);
                editUsers();

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
        editFrame.setLayout(new BorderLayout());
        //frame.add(new JLabel("Edit Users"), BorderLayout.NORTH);
        //frame.add(userTable, BorderLayout.CENTER);
        editFrame.getContentPane().add(BorderLayout.CENTER, panel2);
        editFrame.getContentPane().add(BorderLayout.SOUTH, panel);
        editFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editFrame.setSize(1000, 500);
        editFrame.setVisible(true);

        editFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        editFrame.setVisible(true);

    }

    private Boolean permCreate;
    private Boolean permEditBB;
    private Boolean permUsers;
    private Boolean permSchedule;

    public Boolean GetUserPerms() {
        try {
            Object[] loggedInUserPerms = ClientRequests.GetUserPermissions(username);

            permCreate = (Boolean) loggedInUserPerms[0];
            permEditBB = (Boolean) loggedInUserPerms[1];
            permUsers = (Boolean) loggedInUserPerms[2];
            permSchedule = (Boolean) loggedInUserPerms[3];
            return true;

        } catch (IOException | ClassNotFoundException e) {
            return false;
        }

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



