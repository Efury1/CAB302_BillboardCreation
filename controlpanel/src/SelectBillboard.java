import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class SelectBillboard {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        showSelectionScreen();
    }

    /**
     * Opens a new window showing a JList of billboards that can be viewed, edited, or deleted
     * @throws IOException
     */
    public static void showSelectionScreen() throws IOException, ClassNotFoundException {
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
        Object[] billboardData = ClientRequests.ListBillboards();

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

        buttonPanel.add(BView);
        buttonPanel.add(BEdit);
        buttonPanel.add(BDelete);

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
               /* try {
                    ClientRequests.DeleteBillboard(selectedBillboard.getBName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }*/
            }
        });

        frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
        frame.getContentPane().add(BorderLayout.CENTER, billboardList);
        frame.getContentPane().add(BorderLayout.EAST, labelPanel);
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

}
