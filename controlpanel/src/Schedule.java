
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionListener;

public class Schedule extends JPanel {

    // frame
    JFrame f;
    // Table
    JTable j;
    Schedule() {
        f = new JFrame();
        f.setTitle("JTable Example");

        String[][] data = {
                { "5:00pm", " ", "  ", " ", " ", " ", " ", " "},
                { "6:00pm", " ", " ", " ", " ", " ", " ", " "},
                { "7:00pm", " ", "  ", " ", " ", " ", " ", " "},
                { "8:00pm", " ", " ", " ", " ", " ", " ", " "},
                { "9:00pm", " ", "  ", " ", " ", " ", " ", " "},
                { "10:00pm", " ", " ", " ", " ", " ", " ", " "},
                { "11:00pm", " ", "  ", " ", " ", " ", " ", " "},
                { "12:00pm", " ", " ", " ", " ", " ", " ", " "}
        };

        // Column Names
        String[] columnNames = { "Times", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

        j = new JTable(data, columnNames);
        j.setBounds(30, 40, 200, 300);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(j);
        f.add(sp);
        // Frame Size
        f.setSize(500, 200);
        // Frame Visible = true
        f.setVisible(true);

    }

}