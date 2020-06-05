import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import javax.swing.event.*;
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
 * Worked on designing GUI() screen.
 * Implemented action listeners that allows screen to pick up text
 * and change color of background.
 * Linked functionality to billboard (@author Lauren).
 * Renamed functions to understandable names (@author Lachie).
 * Implemented Logout function and revised login function (@author Lachie).
 * @author Eliza Fury
 * @author Lauren Howlett
 * @author Lachlan Munt
 */

/*Note: we may need threading when it comes to refreshing the ratios.
* So, it doesn't interfere with the programming logic. */
    //TODO Need to scale image so it's not going over into other buttons
    //TODO Schedule needs drop down menu
    //TODO Add and Delete user screens
public class GUI extends Component {

    private String username;

    /**
     * <p>Constructor for the GUI. Initialises based on the supplied username, changing the access permissions on Init.</p>
     * <p>The GUI functionality is dependent on the database stored permissions of the user.</p>
     * @param username The username of the logged in user.
     */
    public GUI (String username) {
        this.username = username;

        JTextArea jt;

        //Create the frame elements
        JFrame frame = new JFrame("Control Panel Review");
        frame.setBounds(30, 30, 700, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();

        JPanel infoPanel = new JPanel();

        jt = new JTextArea("Notes: ", 80, 20);
        infoPanel.add(jt);


        //Try to get the user's permissions, if they couldn't be retrieved, exit the GUI and prompt for login
        if (!GetUserPerms()) {
            JOptionPane.showMessageDialog(frame, "Could not retrieve your user permissions, please log-out and try again.");
            frame.dispose();
            Login returnToLogin = new Login();
        }

        //Add the rest of the GUI elements
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JButton viewBillboards = new JButton("View Billboards");
        JButton scheduleBillboards = new JButton("Schedule Billboards");
        JMenu menuLogout = new JMenu("Logout");

        /*Adding menu */
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewBillboards);
        menuBar.add(scheduleBillboards);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuLogout);
        /*Under file */
        JMenuItem importMenu = new JMenuItem("Import");
        JMenuItem exportMenu = new JMenuItem("Export");

        fileMenu.add(importMenu);
        fileMenu.add(exportMenu);

        scheduleBillboards.setEnabled(permSchedule);

        /*Under edit */
        JMenuItem editUserPermission = new JMenuItem("Edit Users");
        JMenuItem changePassMenu = new JMenuItem("Change Password");
        editUserPermission.setEnabled(permUsers);
        editMenu.add(editUserPermission);
        editMenu.add(changePassMenu);

        /* actionPerformed */
        /**
         * The user can interact with GUI to create their billboard.
         * There's also, the ability to go to other frames like View Billboards, schedule etc.
         * @param aEvent from these buttons: 1.Upload Image 2.Edit Users 3.Schedule 4. Typing text etc
         */

        scheduleBillboards.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Schedule schedule = new Schedule();
            }
        });

        editUserPermission.addActionListener(e -> {
            editUsers();
        });

        changePassMenu.addActionListener(e -> {
            ChangePassword(username);
        });

        menuLogout.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                try {
                    ClientRequests.LogOut();
                } catch (IOException | ClassNotFoundException e1){
                    JOptionPane.showMessageDialog(frame, e1.getMessage());
                } finally {
                    JOptionPane.showMessageDialog(frame, "You have successfully logged out.");
                    frame.dispose();
                    Login returnToLogin = new Login();
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });

        //View Billboards
        viewBillboards.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                SelectBillboard billboardSelector = new SelectBillboard(username, permCreate, permEditBB);
                billboardSelector.showSelectionScreen();
            }
        });


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
        JButton changePerms = new JButton("Change Permissions");
        JPanel panel = new JPanel();
        panel.add(addUser);
        panel.add(changePass);
        panel.add(deleteBtn);
        panel.add(refresh);
        panel.add(changePerms);

        //GetUserPerms(frame)

        //String data1[][];

        final Object[][][] allUsersArray = {new Object[][]{}};

        try {
            Object[] allUsers = ClientRequests.ListUsers();
            allUsersArray[0] = new Object[allUsers.length][5];

            for (int i = 0; i < allUsers.length; i++) {
                Object[] userPerms = ClientRequests.GetUserPermissions(allUsers[i].toString());
                allUsersArray[0][i][0] = (String) allUsers[i].toString();
                allUsersArray[0][i][1] = (Boolean) userPerms[0];
                allUsersArray[0][i][2] = (Boolean) userPerms[1];
                allUsersArray[0][i][3] = (Boolean) userPerms[2];
                allUsersArray[0][i][4] = (Boolean) userPerms[3];

            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(editFrame, e.getMessage());
            return;
        }

        String column[]={"User", "Create", "Edit All Billboards", "Edit Users", "Schedule Billboards"};

        DefaultTableModel model = new DefaultTableModel(allUsersArray[0], column);
        JTable userTable = new JTable(model);

        ListSelectionModel select = userTable.getSelectionModel();

        changePass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userToChange = "";

                for( int i = 0; i < userTable.getRowCount(); i++)
                {
                    if (userTable.isRowSelected(i)){
                        userToChange = (String)userTable.getValueAt(i, 0);
                    }
                }

                if (userToChange == "") {
                    JOptionPane.showMessageDialog(editFrame, "Unable to change password: No user selected.");
                    return;
                }
                ChangePassword(userToChange);
            }
        });


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
                        String pass = passwordField.getText();  //  TODO getText() is deprecated for JPasswordField (find other method)
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

        //TODO YOU'Re MUM //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        changePerms.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String userToChange = "";
                for( int i = 0; i < userTable.getRowCount(); i++)
                {
                    if (userTable.isRowSelected(i)){
                        userToChange = (String)userTable.getValueAt(i, 0);
                    }
                }



                if (userToChange == "") {
                    JOptionPane.showMessageDialog(editFrame, "Cannot change permissions: No user selected.");
                    return;
                }
                else if (userToChange.equals(username)){
                    JOptionPane.showMessageDialog(editFrame, "Cannot change permissions: Cannot change permissions of self.");
                    return;
                }

                JFrame confirmFrame = new JFrame("Control Panel Review");
                confirmFrame.setDefaultCloseOperation(confirmFrame.EXIT_ON_CLOSE);
                confirmFrame.setBounds(100, 100, 500, 400);
                confirmFrame.setResizable(false);
                JPanel contentPane = new JPanel();
                confirmFrame.setContentPane(contentPane);
                contentPane.setLayout(null);

                JLabel createUserLabel = new JLabel("Change Permissions... " + userToChange);
                Font font = createUserLabel.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                createUserLabel.setFont(font.deriveFont(attributes));
                createUserLabel.setBounds(50, 30, 500, 40);
                contentPane.add(createUserLabel);

                Object[] perms = new Object[4];

                try {
                    perms = ClientRequests.GetUserPermissions(userToChange);
                } catch (IOException | ClassNotFoundException error) {
                    JOptionPane.showMessageDialog(confirmFrame, error.getMessage());
                    confirmFrame.setVisible(false);
                    confirmFrame.dispose();
                    return;
                }

                //RADIO LABELS
                JRadioButton permCreate = new JRadioButton("Create Billboards", (Boolean)perms[0]);
                JRadioButton permEditBB = new JRadioButton("Edit Billboards", (Boolean)perms[1]);
                JRadioButton permUsers = new JRadioButton("Edit Users", (Boolean)perms[2]);
                JRadioButton permSchedule = new JRadioButton("Schedule Billboards", (Boolean)perms[3]);

                //RADIO PANEL
                JPanel radioPanel = new JPanel();
                radioPanel.setLayout(new GridLayout(4, 1));
                radioPanel.add(permCreate);
                radioPanel.add(permEditBB);
                radioPanel.add(permUsers);
                radioPanel.add(permSchedule);

                radioPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "Permissions:"));

                radioPanel.setBounds(50, 70, 150, 150);
                contentPane.add(radioPanel);


                JButton confirmButton = new JButton("Confirm");
                confirmButton.setBounds(50, 230, 100, 30);
                contentPane.add(confirmButton);
                confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                String finalUserToChange = userToChange;
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            ClientRequests.SetUserPermissions(finalUserToChange, permCreate.isSelected(), permEditBB.isSelected(), permUsers.isSelected(), permSchedule.isSelected());
                            JOptionPane.showMessageDialog(confirmFrame, "You have successfully changed the permissions of: " + finalUserToChange + ".");
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
                confirmFrame.setTitle("Change User Permissions");
                confirmFrame.setVisible(true);
                confirmFrame.setSize(300, 320);
                confirmFrame.setResizable(false);
            }
        });

        /*You're able to change table value */
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        select.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Object Data = null;
                int[] row = userTable.getSelectedRows();
                int[] columns = userTable.getSelectedColumns();
                JButton deleteBtn = new JButton("Delete User");
                for (int i = 0; i < row.length; i++)
                {
                    for (int j = 0; j < columns.length; j++)
                    {
                        Data = userTable.getValueAt(row[i], columns[j]);
                    }
                    //Tells you want has been selected, this is more for testing
                    System.out.println("The selected table element is: " + Data.toString());
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

    public void ChangePassword(String userToChange) {

        JFrame changePassFrame = new JFrame("Change Password");
        changePassFrame.setDefaultCloseOperation(changePassFrame.EXIT_ON_CLOSE);
        changePassFrame.setBounds(100, 100, 500, 400);
        changePassFrame.setResizable(false);
        JPanel contentPane = new JPanel();
        changePassFrame.setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel createUserLabel = new JLabel("Change Pass...");
        Font font = createUserLabel.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        createUserLabel.setFont(font.deriveFont(attributes));
        createUserLabel.setBounds(50, 20, 120, 40);
        contentPane.add(createUserLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 45, 193, 52);
        contentPane.add(userLabel);

        JTextField textField = new JTextField(userToChange);
        //Username text field
        textField.setBounds(50, 80, 200, 20);
        contentPane.add(textField);
        textField.setColumns(10);
        textField.setEditable(false);

        JLabel PassLabel = new JLabel("New Password:");
        PassLabel.setBounds(50, 90, 193, 52);
        contentPane.add(PassLabel);

        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(50, 130, 193, 52);
        contentPane.add(confirmPassLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 125, 200, 20);
        contentPane.add(passwordField);

        JPasswordField confpasswordField = new JPasswordField();
        confpasswordField.setBounds(50, 165, 200, 20);
        contentPane.add(confpasswordField);

        JButton confirmButton = new JButton("Change");
        confirmButton.setBounds(50, 210, 100, 30);
        contentPane.add(confirmButton);
        confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        String finalUserToChange = userToChange;
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uName = textField.getText();
                String pass = passwordField.getText();  //  TODO getText() is deprecated for JPasswordField (find other method)
                String confpass = confpasswordField.getText();  //  TODO getText() is deprecated for JPasswordField (find other method)
                try {
                    if (!pass.equals("")) {
                        if (confpass.equals(pass)) {
                            ClientRequests.SetUserPassword(finalUserToChange, pass);
                            JOptionPane.showMessageDialog(changePassFrame, "Password change successful.");
                        }
                        else {
                            JOptionPane.showMessageDialog(changePassFrame, "Unable to change password: Passwords do not match.");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(changePassFrame, "Unable to change password: Password cannot be blank.");
                    }
                } catch (IOException | ClassNotFoundException changePassError){
                    JOptionPane.showMessageDialog(changePassFrame, changePassError);
                }

            }
        });
        //  Screen set up
        changePassFrame.setDefaultCloseOperation(changePassFrame.HIDE_ON_CLOSE);
        changePassFrame.setTitle("Change Pass");
        changePassFrame.setVisible(true);
        changePassFrame.setSize(600, 300);
        changePassFrame.setResizable(false);
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



