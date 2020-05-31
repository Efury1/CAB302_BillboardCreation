import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;


class Schedule
{

    public Schedule()
    {

        JFrame scheduleFrame;
        JPanel daysPanel, buttonsPanel, eventsPanel;
        JTextArea events;

        scheduleFrame = new JFrame();
        scheduleFrame.setTitle("Schedule Billboard");
        scheduleFrame.setSize(350, 430);
        scheduleFrame.setLayout(new BorderLayout());
        scheduleFrame.setResizable(false);
        scheduleFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //JPanels
        daysPanel = new JPanel();
        daysPanel.setLayout(new GridLayout(1,2));
        daysPanel.setBorder(BorderFactory.createTitledBorder("Schedule Billboard"));
        buttonsPanel = new JPanel();

        eventsPanel = new JPanel();

        JPanel btn = new JPanel(new GridLayout(1, 1, 1, 1));
        btn.add(new JLabel("Mon"));
        btn.add(new JLabel("Tues"));
        btn.add(new JLabel("Wed"));
        btn.add(new JLabel("Thur"));
        btn.add(new JLabel("Fri"));
        btn.add(new JLabel("Sat"));
        btn.add(new JLabel("Sun"));
        daysPanel.add(btn);

        JPanel btnPanel = new JPanel(new GridLayout(1, 1, 2, 2));
        btnPanel.add(new JButton("   "));
        btnPanel.add(new JButton("   "));
        btnPanel.add(new JButton("   "));
        btnPanel.add(new JButton("   "));
        btnPanel.add(new JButton("   "));
        btnPanel.add(new JButton("   "));
        btnPanel.add(new JButton("   "));
        buttonsPanel.add(btnPanel);

        events = new JTextArea(10,10);
        events.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(events);
         eventsPanel = new JPanel(new GridLayout(2, 5, 0, 0));
         eventsPanel.add(events);


        scheduleFrame.add(daysPanel, BorderLayout.NORTH);
        scheduleFrame.add(buttonsPanel, BorderLayout.CENTER);
        scheduleFrame.add(eventsPanel, BorderLayout.SOUTH);
        scheduleFrame.setVisible(true);
    }
    /*public static void main (String[] args)
    {
        Schedule jc = new Schedule();

    }*/

}