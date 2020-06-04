import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * This class handles the panel for scheduling billboards.
 * Displays the billboards and has options to manipulate their schedules.
 */
class Schedule {

    public Schedule() {

        JFrame scheduleFrame;
        JPanel daysPanel, buttonsPanel, eventsPanel, timesPanel;
        JTextArea events;

        scheduleFrame = new JFrame();
        scheduleFrame.setTitle("Schedule Billboard");
        scheduleFrame.setSize(500, 640);
        scheduleFrame.setLayout(new BorderLayout());
        scheduleFrame.setResizable(false);
        scheduleFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //JPanels
        daysPanel = new JPanel();
        daysPanel.setLayout(new BorderLayout());
        //daysPanel.setBounds(0, 0, 50, 50);

        eventsPanel = new JPanel();

        JPanel days = new JPanel(new GridLayout(0, 7, 0, 0));
        days.add(new JLabel("   ", SwingConstants.CENTER));
        days.add(new JLabel("Mon", SwingConstants.CENTER));
        days.add(new JLabel("Tues", SwingConstants.CENTER));
        days.add(new JLabel("Wed", SwingConstants.CENTER));
        days.add(new JLabel("Thur", SwingConstants.CENTER));
        days.add(new JLabel("Fri", SwingConstants.CENTER));
        days.add(new JLabel("   ", SwingConstants.CENTER));
        //btn.add(new JLabel("Sat"));
        //btn.add(new JLabel("Sun"));
        daysPanel.add(days, BorderLayout.PAGE_START);

        timesPanel = new JPanel();
        timesPanel.setLayout(new GridLayout(9, 1));

        JPanel times = new JPanel(new GridLayout(9, 1, 1, 1));
        times.add(new JLabel("09:00"));
        times.add(new JLabel("10:00"));
        times.add(new JLabel("11:00"));
        times.add(new JLabel("12:00"));
        times.add(new JLabel("13:00"));
        times.add(new JLabel("14:00"));
        times.add(new JLabel("15:00"));
        times.add(new JLabel("16:00"));
        times.add(new JLabel("17:00"));
        timesPanel.add(times, BorderLayout.PAGE_START);

        //JPanel btnPanel = new JPanel(new GridLayout(1, 1, 2, 2));
        //btnPanel.add(new JButton("   "));
        //btnPanel.add(new JButton("   "));
        //btnPanel.add(new JButton("   "));
        //btnPanel.add(new JButton("   "));
        //btnPanel.add(new JButton("   "));
        //btnPanel.add(new JButton("   "));
        //btnPanel.add(new JButton("   "));
        //buttonsPanel.add(btnPanel);

        JPanel schedulePanel = new JPanel();
        schedulePanel.setBounds(3, 40, 478, 480);
        //schedulePanel.setSize(300, 480);
        //schedulePanel.setBackground(Color.LIGHT_GRAY);

        drawingComponent DC = new drawingComponent();
        scheduleFrame.add(DC);
        scheduleFrame.setVisible(true);

        scheduleFrame.add(schedulePanel);
        JButton create = new JButton("Create Billboard");
        eventsPanel = new JPanel(new GridLayout(2, 5, 0, 0));
        eventsPanel.add(create);

        scheduleFrame.add(daysPanel, BorderLayout.NORTH);
        scheduleFrame.add(eventsPanel, BorderLayout.SOUTH);
        scheduleFrame.add(times, BorderLayout.WEST);
        scheduleFrame.setVisible(true);

        create.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame CBillboard = new JFrame();
                JTextField endDate = new JTextField("00:00am");
                JTextField startDate = new JTextField("00:00am");

                JPanel datePanel = new JPanel();
                datePanel.setLayout(new GridLayout(2,2));

                JLabel frameTitle = new JLabel("Schedule Billboard");

                JPanel titlePanel = new JPanel();
                titlePanel.add(frameTitle);

                datePanel.add(new JLabel("Start Date: ", SwingConstants.RIGHT));

                JButton save = new JButton("Save");
                JPanel savePanel = new JPanel();
                savePanel.add(save);


                datePanel.add(endDate);
                datePanel.add(new JLabel("End Date: ", SwingConstants.RIGHT));
                datePanel.add(startDate);


                CBillboard.add(titlePanel, BorderLayout.NORTH);
                CBillboard.add(datePanel, BorderLayout.CENTER);
                CBillboard.add(savePanel, BorderLayout.SOUTH);
                CBillboard.add(times, BorderLayout.WEST);
                CBillboard.setSize(300, 150);
                CBillboard.setResizable(true);
                CBillboard.setVisible(true);
                CBillboard.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            }
        });
    }

    public static void main(String[] args) {
        Schedule jc = new Schedule();

    }
}