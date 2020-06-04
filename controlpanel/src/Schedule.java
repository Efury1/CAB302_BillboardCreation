import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class handles the panel for scheduling billboards.
 * Displays the billboards and has options to manipulate their schedules.
 */
class Schedule {
    private static String startDay = "";
    private static String endDay = "";
    private static Integer duration = 0;
    private static Integer repeatFreq = 0;

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

        //  Times listed in schedule window (west side)
        JPanel times = new JPanel(new GridLayout(9, 1, 1, 1));
        times.add(new JLabel("10:00"));
        times.add(new JLabel("11:00"));
        times.add(new JLabel("12:00"));
        times.add(new JLabel("13:00"));
        times.add(new JLabel("14:00"));
        times.add(new JLabel("15:00"));
        times.add(new JLabel("16:00"));
        times.add(new JLabel("17:00"));
        times.add(new JLabel("18:00"));
        timesPanel.add(times, BorderLayout.PAGE_START);

        JPanel schedulePanel = new JPanel();
        schedulePanel.setBounds(3, 40, 478, 480);

        drawingComponent DC = new drawingComponent();
        scheduleFrame.add(DC);
        scheduleFrame.setVisible(true);

        scheduleFrame.add(schedulePanel);
        JButton schedule = new JButton("Schedule a Billboard");
        eventsPanel = new JPanel(new GridLayout(2, 5, 0, 0));
        eventsPanel.add(schedule);

        scheduleFrame.add(daysPanel, BorderLayout.NORTH);
        scheduleFrame.add(eventsPanel, BorderLayout.SOUTH);
        scheduleFrame.add(times, BorderLayout.WEST);
        scheduleFrame.setVisible(true);

        schedule.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

                //  Model for JComboBox: endDayCB
                ComboBoxModel[] models = new ComboBoxModel[5];
                //  from https://stackoverflow.com/a/3191882
                models[0] = new DefaultComboBoxModel(new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
                models[1] = new DefaultComboBoxModel(new String[] {"Tuesday", "Wednesday", "Thursday", "Friday"});
                models[2] = new DefaultComboBoxModel(new String[] {"Wednesday", "Thursday", "Friday"});
                models[3] = new DefaultComboBoxModel(new String[] {"Thursday", "Friday"});
                models[4] = new DefaultComboBoxModel(new String[] {"Friday"});

                //  Model for JSpinner: repeatFreqSpinner
                SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0, 0, 480, 1);
                //  Make a new frame/window
                JFrame scheduleNew = new JFrame();

                //  Declare swing items in JFrame
                JComboBox endDayCB = new JComboBox(days);   //  END day combo box
                JComboBox startDayCB = new JComboBox(days); //  START day combo box
                JSpinner durationSpinner = new JSpinner();  //  duration spinner
                JSpinner repeatFreqSpinner = new JSpinner(spinnerNumberModel);  //  repeat frequency spinner
                JLabel startTimeLabel = new JLabel("Start Time");   //  Start time label
                JTextField startTimeTextField = new JTextField("Start Time");   //  Start time text field
                JCheckBox repeatBool = new JCheckBox("Repeats");    //  Repeat check box

                //  Adding the elements to the JFrame..
                scheduleNew.add(startDayCB, BorderLayout.WEST);
                scheduleNew.add(endDayCB, BorderLayout.CENTER);
                scheduleNew.add(durationSpinner, BorderLayout.EAST);
                scheduleNew.add(repeatFreqSpinner, BorderLayout.EAST);
                scheduleNew.add(startTimeLabel, BorderLayout.EAST);
                scheduleNew.add(repeatBool, BorderLayout.EAST);

                //  Properties of the frame/window
                scheduleNew.setSize(500, 500);
                scheduleNew.setResizable(true);
                scheduleNew.setVisible(true);
                scheduleNew.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);


                startDayCB.setSelectedIndex(0);
                startDayCB.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        endDayCB.setModel(models[startDayCB.getSelectedIndex()]);
                        startDay = startDayCB.getSelectedItem().toString();
                    }
                });

                endDayCB.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        endDay = startDayCB.getSelectedItem().toString();
                    }
                });

                durationSpinner.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        //  Update the duration spinner (can never be smaller than duration + 1)
                        spinnerNumberModel.setMinimum((Integer) durationSpinner.getValue() + 1);
                        spinnerNumberModel.setValue((Integer) durationSpinner.getValue() + 1);
                        duration = (Integer) durationSpinner.getValue();
                    }
                });

                repeatFreqSpinner.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        repeatFreq = (Integer) repeatFreqSpinner.getValue();
                    }
                });




                /*JPanel datePanel = new JPanel();
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


                scheduleNew.add(titlePanel, BorderLayout.NORTH);
                scheduleNew.add(datePanel, BorderLayout.CENTER);
                scheduleNew.add(savePanel, BorderLayout.SOUTH);
                scheduleNew.add(times, BorderLayout.WEST);
                scheduleNew.setSize(300, 150);
                scheduleNew.setResizable(true);
                scheduleNew.setVisible(true);
                scheduleNew.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);*/
            }
        });
    }


    //  TODO remove debug code
    public static void main(String[] args) {
        Schedule jc = new Schedule();
    }
}