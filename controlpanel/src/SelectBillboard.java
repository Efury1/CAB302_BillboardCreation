import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectBillboard {
    public static void showSelectionScreen() {
        JFrame frame = new JFrame("Control Panel");
        int FRAME_WIDTH = 1000;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Import billboards from database (placeholder code)
        Billboard b1 = new Billboard("EOFYParty", "Sam");
        Billboard b2 = new Billboard("May1Fundraiser", "Esther");
        Billboard b3 = new Billboard("MyBillboard1", "Sam");
        Billboard b4 = new Billboard("BirthdayMessage", "Dave");
        int totalBoards = 4;
        Billboard[] billboards = new Billboard[totalBoards];
        billboards[0] = b1;
        billboards[1] = b2;
        billboards[2] = b3;
        billboards[3] = b4;

        JPanel buttonPanel = new JPanel();
        JButton BView = new JButton("View");

        JButton BEdit = new JButton("Edit");

        JButton BDelete = new JButton("Delete");

        buttonPanel.add(BView);
        buttonPanel.add(BEdit);
        buttonPanel.add(BDelete);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        JLabel LCreator = new JLabel("Billboard Creator:");
        JLabel LScheduled = new JLabel("Billboard Schedule Status:");
        labelPanel.add(LCreator);
        labelPanel.add(LScheduled);


        DefaultListModel listModel = new DefaultListModel();
        for(int i=0; i<totalBoards; i++){
            listModel.addElement(billboards[i].getBName());
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
                        //Selection, enable the fire button.
                        LCreator.setText("Billboard Creator: " + billboards[billboardList.getSelectedIndex()].getBOwner());
                        LScheduled.setText("Billboard Schedule Status: " + billboards[billboardList.getSelectedIndex()].getScheduled());
                    }
                }
            }
        });
        //frame.add(billboardList);

        /*BView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //View billboard
            }
        });

        BEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Edit billboard
            }
        });

        BDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Delete billboard
                
            }
        });*/

        frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
        frame.getContentPane().add(BorderLayout.CENTER, billboardList);
        frame.getContentPane().add(BorderLayout.EAST, labelPanel);
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

}
