import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** GUI view for the schedule */
public class Schedule extends JPanel  {
    /** The month choice */
    private JComboBox monthChoice;


    DefaultTableModel cTable;
    DefaultTableModel dTable;
    int today;
    int someRow;
    int someCol;
    int hour;
    JTable table;
    JTextArea events;
    public Calendar myCal = new GregorianCalendar();

    /** View events that are relevant to schedule*/
    public Schedule() {
        JFrame scheduleframe = new JFrame();
        events = new JTextArea();
        scheduleframe.setSize(900,700);
        String[] cols = {"Sun", "Mon", "Tues", "Wed", "Thu", "Fri", "Sat"};

        JButton createButton = new JButton("Create");
        createButton.addActionListener(event->{
            //TODO Show scheduled billboard
        });

        JPanel dayOfWeek = new JPanel();
        dayOfWeek.setLayout(new GridLayout(1, 2));
        JButton[][] cats = new JButton[6][7]; // first row is days

        dayOfWeek.add(new JButton("S"));
        dayOfWeek.add(new JButton("M"));
        dayOfWeek.add(new JButton("T"));
        dayOfWeek.add(new JButton("W"));
        dayOfWeek.add(new JButton("R"));
        dayOfWeek.add(new JButton("F"));
        dayOfWeek.add(new JButton("S"));

        JPanel times = new JPanel();
        times.setLayout(new GridLayout(6, 6));
        JButton[][] timeButtons = new JButton[7][7];
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 7; j++) {
                times.add(timeButtons[i][j] = new JButton(""));
            }
        Container container = scheduleframe.getContentPane();

        scheduleframe.add(dayOfWeek);
        scheduleframe.add(times);
        scheduleframe.add(events);

        container.add(dayOfWeek, BorderLayout.NORTH);
        container.add(times, BorderLayout.CENTER);
        container.add(events, BorderLayout.SOUTH);


        scheduleframe.setVisible(true);

    }

    /*public static void main(String[] args) {
        Schedule testing = new Schedule();
    }*/
}

