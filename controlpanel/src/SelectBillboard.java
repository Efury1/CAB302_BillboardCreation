import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class SelectBillboard {

    private static String username;
    private static boolean permCreate;
    private static boolean permEdit;


    static Blob imageBlob = null;

    public SelectBillboard(String username, Boolean permCreate, Boolean permEdit) {
        this.username = username;
        this.permCreate = permCreate;
        this.permEdit = permEdit;
    }

    private static Blob ReadImage(File selectedFile) throws IOException, SQLException {
        BufferedImage image = ImageIO.read(selectedFile);
        ByteArrayOutputStream Output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", Output);
        Blob myBlob = new SerialBlob(Output.toByteArray());
        return myBlob;
    }

    /**
     * Opens a new window showing a JList of billboards that can be viewed, edited, or deleted
     * @throws IOException
     */
    public static void showSelectionScreen() {

        String token = "";
        JFrame frame = new JFrame("Control Panel");
        int FRAME_WIDTH = 1000;
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Import billboards from database (placeholder code)

//        Billboard b1 = new Billboard("EOFYParty", "Sam");
//        Billboard b2 = new Billboard("May1Fundraiser", "Esther");
//        Billboard b3 = new Billboard("MyBillboard1", "Sam");
//        Billboard b4 = new Billboard("BirthdayMessage", "Dave");
        int totalBoards = 0;
        ArrayList<Billboard> billboards = new ArrayList<Billboard>();
//        billboards.add(b1);
//        billboards.add(b2);
//        billboards.add(b3);
//        billboards.add(b4);
        String[] requestData = {};
        //TODO Uncomment section when SendReceive is working
        Object[] billboardData = new Object[0];
        try {
            billboardData = ClientRequests.ListBillboards();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }

        //unpack data, create billboard from data, and append billboard to array of billboards
        for(int i = 0; i<billboardData.length; i++){
            String billboardName = billboardData[i].toString();
            i++;
            String billboardCreator = billboardData[i].toString();
            billboards.add(new Billboard(billboardName, billboardCreator));
            totalBoards++;
        }

        JPanel buttonPanel = new JPanel();

        //TODO "Add Billboard" Button
        JButton BView = new JButton("View");

        JButton BEdit = new JButton("Edit");

        JButton BDelete = new JButton("Delete");

        JButton BAdd = new JButton("Add");

        buttonPanel.add(BView);
        buttonPanel.add(BEdit);
        buttonPanel.add(BDelete);
        BAdd.setEnabled(permCreate);
        buttonPanel.add(BAdd);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setPreferredSize(new Dimension(250, 700));
        JLabel LCreator = new JLabel("Billboard Creator:");
        labelPanel.add(LCreator);


        DefaultListModel listModel = new DefaultListModel();
        for(int i=0; i<totalBoards; i++){
            listModel.addElement(billboards.get(i).getBName());
        }

        JList billboardList = new JList(listModel);
        billboardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        billboardList.setLayoutOrientation(JList.VERTICAL);
        billboardList.setSelectedIndex(0);
        billboardList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {

                    if (billboardList.getSelectedIndex() == -1) {
                        BView.setEnabled(false);
                        BEdit.setEnabled(false);
                        BDelete.setEnabled(false);

                    } else {
                        //Update text when billboard selected
                        LCreator.setText("Billboard Creator: " + billboards.get(billboardList.getSelectedIndex()).getBOwner());

                    }
                }
            }
        });

        BView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //View billboard
                Billboard selectedBillboard = billboards.get(billboardList.getSelectedIndex());
                //selectedBillboard.setBMessage("Testing...");
                //selectedBillboard.setBDescription("Test description");
                try {
                    selectedBillboard.importAllInfo();
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame,"Could not display billboard: " + selectedBillboard.getBName());
                }

                if(selectedBillboard.hasMessage()==false && selectedBillboard.hasDescription()==false && selectedBillboard.hasImage()==false){
                    //Does not show billboard with nothing on it
                    JOptionPane.showMessageDialog(frame,"There are no elements to display in this billboard");
                }
                else {
                    try {
                        ViewBillboard.showBillboard(selectedBillboard);
                    } catch (IOException | SQLException ex) {
                        JOptionPane.showMessageDialog(frame,"Could not display billboard: " + selectedBillboard.getBName()); //TODO check this works
                    }
                }
            }
        });

        BEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Edit billboard
                //TODO Get all data about billboard from database
                Billboard selectedBillboard = billboards.get(billboardList.getSelectedIndex());
                /*try {
                    selectedBillboard.importAllInfo();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }*/
            }
        });

        BDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO Delete billboard including from database
                Billboard selectedBillboard = billboards.get(billboardList.getSelectedIndex());

                if (selectedBillboard.getBOwner().equals(username)) {

                } else if (permEdit){

                } else {
                    JOptionPane.showMessageDialog(frame, "You do not have permissions to delete other users billboards.");
                    return;
                }


                try {
                    ClientRequests.DeleteBillboard(selectedBillboard.getBName());
                    frame.setVisible(false);
                    frame.dispose();
                    showSelectionScreen();
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame, "Could not delete billboard: " + ex.getMessage());
                }
            }
        });

        BEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Billboard selectedBillboard = billboards.get(billboardList.getSelectedIndex());
                try {
                    selectedBillboard.importAllInfo();
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame, "Could not edit billboard: " + ex.getMessage());
                    return;
                }
                Color defaultColour = new Color(Color.BLACK.getRGB());

                Color Background = defaultColour;
                Color Description = defaultColour;
                Color Title = defaultColour;

                JFrame confirmFrame = new JFrame("Edit Billboard");
                confirmFrame.setDefaultCloseOperation(confirmFrame.EXIT_ON_CLOSE);
                confirmFrame.setBounds(100, 100, 500, 400);
                confirmFrame.setResizable(false);
                JPanel contentPane = new JPanel();
                confirmFrame.setContentPane(contentPane);
                contentPane.setLayout(null);

                JLabel createUserLabel = new JLabel("Edit Billboard...");
                Font font = createUserLabel.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                createUserLabel.setFont(font.deriveFont(attributes));
                createUserLabel.setBounds(50, 20, 120, 40);
                contentPane.add(createUserLabel);

                JColorChooser ColorPanel = new JColorChooser(defaultColour);

                final int st = 35; //starting point
                final int sp = 35; //spacing
                final int x2 = 300; //second x

                JLabel BBNameLabel = new JLabel("Billboard Name:");
                BBNameLabel.setBounds(50, st, 193, 52);
                contentPane.add(BBNameLabel);

                JTextField BBNameField = new JTextField(selectedBillboard.getBName());                        //TODO FIELD//
                BBNameField.setBounds(50, st+sp, 200, 20);
                BBNameField.setEnabled(false);
                contentPane.add(BBNameField);
                BBNameField.setColumns(10);

                JLabel BBBackLabel = new JLabel("Background Colour:");
                BBBackLabel.setBounds(x2, st, 193, 52);
                contentPane.add(BBBackLabel);

                JButton BBBackField = new JButton();//TODO FIELD//
                BBBackField.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color Background = ColorPanel.showDialog(BBBackField, "Select Background Colour", defaultColour);
                    }
                });
                BBBackField.setBounds(x2, st+sp, 110, 20);
                contentPane.add(BBBackField);

                JLabel BBTitleLabel = new JLabel("Billboard Title");
                BBTitleLabel.setBounds(50, st+sp*2, 193, 52);
                contentPane.add(BBTitleLabel);

                String titleBool = "";
                if (selectedBillboard.hasMessage()) {
                    titleBool = selectedBillboard.getBMessage();
                }
                JTextField BBTitleField = new JTextField(titleBool);                       //TODO FIELD//
                BBTitleField.setBounds(50, st+sp*3, 200, 20);
                contentPane.add(BBTitleField);
                BBTitleField.setColumns(10);

                JLabel BBTitleColLabel = new JLabel("Title Colour:");
                BBTitleColLabel.setBounds(x2, st+sp*2, 193, 52);
                contentPane.add(BBTitleColLabel);

                JButton BBTitleColField = new JButton();                       //TODO FIELD//
                BBTitleColField.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Color Title = ColorPanel.showDialog(BBTitleColField, "Select Title Colour", defaultColour);
                    }
                });
                BBTitleColField.setBounds(x2, st+sp*3, 110, 20);
                contentPane.add(BBTitleColField);

                JLabel BBDescLabel = new JLabel("Description:");
                BBDescLabel.setBounds(50, st+sp*4, 193, 52);
                contentPane.add(BBDescLabel);

                String descBool = "";
                if (selectedBillboard.hasDescription()) {
                    descBool = selectedBillboard.getBDescription();
                }
                JTextField BBDescField = new JTextField(descBool);                      //TODO FIELD//
                BBDescField.setBounds(50, st+sp*5, 200, 20);
                contentPane.add(BBDescField);
                BBDescField.setColumns(10);

                JLabel BBDescColLabel = new JLabel("Description Colour:");
                BBDescColLabel.setBounds(x2, st+sp*4, 193, 52);
                contentPane.add(BBDescColLabel);

                JButton BBDescColField = new JButton();                      //TODO FIELD//
                BBDescColField.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color Description = ColorPanel.showDialog(BBDescColField, "Select Description Colour", defaultColour);
                    }
                });
                BBDescColField.setBounds(x2, st+sp*5, 110, 20);
                contentPane.add(BBDescColField);

                JLabel BBPicLabel = new JLabel("Picture:");
                BBPicLabel.setBounds(50, st+sp*6, 193, 52);
                contentPane.add(BBPicLabel);

                JButton BBPicField = new JButton();                      //TODO FIELD//
                BBPicField.setBounds(50, st+sp*7, 200, 20);
                contentPane.add(BBPicField);

                BBPicField.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
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
                                imageBlob = ReadImage(selectedFile);                                                                  //TODO /////////////////////////////////////////////////////////////////////////////////////////////////////////

                            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, ex); }
                        }
                    }
                });

                JButton confirmButton = new JButton("Confirm");
                confirmButton.setBounds(50, st+sp*8, 100, 30);
                contentPane.add(confirmButton);
                confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String hexBackground = Integer.toHexString(Background.getRGB() & 0xFFFFFF);
                        String hexTitle = Integer.toHexString(Title.getRGB() & 0xFFFFFF);
                        String hexDescription = Integer.toHexString(Description.getRGB() & 0xFFFFFF);
                        try {
                            ClientRequests.CreateEditBillboard(BBNameField.getText(), BBTitleField.getText(), BBDescField.getText(), imageBlob, hexBackground, hexTitle, hexDescription, username);
                            System.out.println(hexBackground + "\n" + hexDescription + "\n" + hexTitle);
                            JOptionPane.showMessageDialog(frame,"Billboard Replaced!");
                            frame.setVisible(false);
                            frame.dispose();
                            confirmFrame.setVisible(false);
                            confirmFrame.dispose();
                            showSelectionScreen();
                        } catch (IOException | ClassNotFoundException error){
                            JOptionPane.showMessageDialog(frame,error.getMessage());
                        }
                    }
                });
                //  Screen set up
                confirmFrame.setDefaultCloseOperation(confirmFrame.HIDE_ON_CLOSE);
                confirmFrame.setTitle("Billboard Create");
                confirmFrame.setVisible(true);
                confirmFrame.setSize(700, 500);
                confirmFrame.setResizable(false);
            }
        });

        BAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Color defaultColour = new Color(Color.BLACK.getRGB());

                Color Background = defaultColour;
                Color Description = defaultColour;
                Color Title = defaultColour;

                JFrame confirmFrame = new JFrame("Add Billboard");
                confirmFrame.setDefaultCloseOperation(confirmFrame.EXIT_ON_CLOSE);
                confirmFrame.setBounds(100, 100, 500, 400);
                confirmFrame.setResizable(false);
                JPanel contentPane = new JPanel();
                confirmFrame.setContentPane(contentPane);
                contentPane.setLayout(null);

                JLabel createUserLabel = new JLabel("Add Billboard...");
                Font font = createUserLabel.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                createUserLabel.setFont(font.deriveFont(attributes));
                createUserLabel.setBounds(50, 20, 120, 40);
                contentPane.add(createUserLabel);

                JColorChooser ColorPanel = new JColorChooser(defaultColour);

                final int st = 35; //starting point
                final int sp = 35; //spacing
                final int x2 = 300; //second x

                JLabel BBNameLabel = new JLabel("Billboard Name:");
                BBNameLabel.setBounds(50, st, 193, 52);
                contentPane.add(BBNameLabel);

                JTextField BBNameField = new JTextField();                        //TODO FIELD//
                BBNameField.setBounds(50, st+sp, 200, 20);
                contentPane.add(BBNameField);
                BBNameField.setColumns(10);

                JLabel BBBackLabel = new JLabel("Background Colour:");
                BBBackLabel.setBounds(x2, st, 193, 52);
                contentPane.add(BBBackLabel);

                JButton BBBackField = new JButton();//TODO FIELD//
                BBBackField.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color Background = ColorPanel.showDialog(BBBackField, "Select Background Colour", defaultColour);
                    }
                });
                BBBackField.setBounds(x2, st+sp, 110, 20);
                contentPane.add(BBBackField);

                JLabel BBTitleLabel = new JLabel("Billboard Title:");
                BBTitleLabel.setBounds(50, st+sp*2, 193, 52);
                contentPane.add(BBTitleLabel);

                JTextField BBTitleField = new JTextField();                       //TODO FIELD//
                BBTitleField.setBounds(50, st+sp*3, 200, 20);
                contentPane.add(BBTitleField);
                BBTitleField.setColumns(10);

                JLabel BBTitleColLabel = new JLabel("Title Colour:");
                BBTitleColLabel.setBounds(x2, st+sp*2, 193, 52);
                contentPane.add(BBTitleColLabel);

                JButton BBTitleColField = new JButton();                       //TODO FIELD//
                BBTitleColField.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Color Title = ColorPanel.showDialog(BBTitleColField, "Select Title Colour", defaultColour);
                    }
                });
                BBTitleColField.setBounds(x2, st+sp*3, 110, 20);
                contentPane.add(BBTitleColField);

                JLabel BBDescLabel = new JLabel("Description:");
                BBDescLabel.setBounds(50, st+sp*4, 193, 52);
                contentPane.add(BBDescLabel);

                JTextField BBDescField = new JTextField();                      //TODO FIELD//
                BBDescField.setBounds(50, st+sp*5, 200, 20);
                contentPane.add(BBDescField);
                BBDescField.setColumns(10);

                JLabel BBDescColLabel = new JLabel("Description Colour:");
                BBDescColLabel.setBounds(x2, st+sp*4, 193, 52);
                contentPane.add(BBDescColLabel);

                JButton BBDescColField = new JButton();                      //TODO FIELD//
                BBDescColField.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color Description = ColorPanel.showDialog(BBDescColField, "Select Description Colour", defaultColour);
                    }
                });
                BBDescColField.setBounds(x2, st+sp*5, 110, 20);
                contentPane.add(BBDescColField);

                JLabel BBPicLabel = new JLabel("Picture:");
                BBPicLabel.setBounds(50, st+sp*6, 193, 52);
                contentPane.add(BBPicLabel);

                JButton BBPicField = new JButton();                      //TODO FIELD//
                BBPicField.setBounds(50, st+sp*7, 200, 20);
                contentPane.add(BBPicField);

                BBPicField.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
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
                                imageBlob = ReadImage(selectedFile);                                                                  //TODO /////////////////////////////////////////////////////////////////////////////////////////////////////////

                            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, ex); }
                        }
                    }
                });

                JButton confirmButton = new JButton("Confirm");
                confirmButton.setBounds(50, st+sp*8, 100, 30);
                contentPane.add(confirmButton);
                confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String hexBackground = Integer.toHexString(Background.getRGB() & 0xFFFFFF);
                        String hexTitle = Integer.toHexString(Title.getRGB() & 0xFFFFFF);
                        String hexDescription = Integer.toHexString(Description.getRGB() & 0xFFFFFF);
                        try {
                            ClientRequests.CreateEditBillboard(BBNameField.getText(), BBTitleField.getText(), BBDescField.getText(), imageBlob, hexBackground, hexTitle, hexDescription, username);
                            System.out.println(hexBackground + "\n" + hexDescription + "\n" + hexTitle);
                            JOptionPane.showMessageDialog(frame,"Billboard Created!");
                            frame.setVisible(false);
                            frame.dispose();
                            showSelectionScreen();
                        } catch (IOException | ClassNotFoundException error){
                            JOptionPane.showMessageDialog(frame,error.getMessage());
                            }
                        }
                });
                //  Screen set up
                confirmFrame.setDefaultCloseOperation(confirmFrame.HIDE_ON_CLOSE);
                confirmFrame.setTitle("Billboard Create");
                confirmFrame.setVisible(true);
                confirmFrame.setSize(700, 500);
                confirmFrame.setResizable(false);
            }
        });
        frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
        frame.getContentPane().add(BorderLayout.CENTER, billboardList);
        frame.getContentPane().add(BorderLayout.EAST, labelPanel);
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }
}
