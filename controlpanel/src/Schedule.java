import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;

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

        JPanel days = new JPanel(new GridLayout(0, 7, 0, 0));
        days.add(new JLabel("   ", SwingConstants.CENTER));
        days.add(new JLabel("Mon", SwingConstants.CENTER));
        days.add(new JLabel("Tues", SwingConstants.CENTER));
        days.add(new JLabel("Wed", SwingConstants.CENTER));
        days.add(new JLabel("Thur", SwingConstants.CENTER));
        days.add(new JLabel("Fri", SwingConstants.CENTER));
        days.add(new JLabel("   ", SwingConstants.CENTER));
        daysPanel.add(days, BorderLayout.PAGE_START);

        timesPanel = new JPanel();
        timesPanel.setLayout(new GridLayout(9, 1));

        //  Times listed in schedule window (west side)
        JPanel times = new JPanel(new GridLayout(9, 1, 1, 1));
        times.add(new JLabel("9:00"));
        times.add(new JLabel("10:00"));
        times.add(new JLabel("11:00"));
        times.add(new JLabel("12:00"));
        times.add(new JLabel("13:00"));
        times.add(new JLabel("14:00"));
        times.add(new JLabel("15:00"));
        times.add(new JLabel("16:00"));
        times.add(new JLabel("17:00"));
        //times.add(new JLabel("18:00"));
        timesPanel.add(times, BorderLayout.PAGE_START);

        JPanel schedulePanel = new JPanel();
        schedulePanel.setBounds(3, 40, 478, 480);

        drawingComponent DC = new drawingComponent();
        scheduleFrame.add(DC);
        scheduleFrame.setVisible(true);

        scheduleFrame.add(schedulePanel);
        JButton schedule = new JButton("Schedule a Billboard");
        JButton deleteFromSchedule = new JButton("Remove Billboard from schedule");
        eventsPanel = new JPanel(new GridLayout(2, 5, 0, 0));
        eventsPanel.add(schedule);
        eventsPanel.add(deleteFromSchedule);

        scheduleFrame.add(daysPanel, BorderLayout.NORTH);
        scheduleFrame.add(eventsPanel, BorderLayout.SOUTH);
        scheduleFrame.add(times, BorderLayout.WEST);
        scheduleFrame.setVisible(true);

        deleteFromSchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame scheduleDelete = new JFrame("Delete from schedule");
                scheduleDelete.setLayout(new GridLayout(0, 3));

                String[] billboardName = new String[0];
                String[] billboardsDisplay = new String[0];
                String[] billboardStartDay = new String[0];
                String[] billboardStartTime = new String[0];
                int billboardListLength = 0;
                Object[] billboardData = new Object[0];
                try {
                    billboardData = ClientRequests.ViewSchedule();
                } catch (IOException | ClassNotFoundException err) {
                    JOptionPane.showMessageDialog(scheduleFrame, err.getMessage());
                }

                for(int i = 0; i < billboardData.length; i+=9){
                    billboardListLength++;
                }
                billboardName = new String[billboardListLength];
                billboardsDisplay = new String[billboardListLength];
                billboardStartDay = new String[billboardListLength];
                billboardStartTime = new String[billboardListLength];
                for(int i = 0, j = 0; i < billboardData.length; i+=9, j++){
                    billboardsDisplay[j] = billboardData[i].toString() + " Day: " + billboardData[i+3].toString() + " Time: " + billboardData[i+5].toString();
                    billboardStartDay[j] = billboardData[i+3].toString();
                    billboardName[j] = billboardData[i].toString();
                    billboardStartTime[j] = billboardData[i+5].toString();
                }

                JLabel selectBillboard = new JLabel("Select billboard to remove:");
                JComboBox scheduledBillboardsList = new JComboBox(billboardsDisplay);
                JComboBox scheduledBillboardsStart = new JComboBox(billboardStartDay);
                JComboBox scheduledBillboardsTime= new JComboBox(billboardStartTime);
                JComboBox scheduledBillboardName = new JComboBox(billboardName);
                scheduledBillboardsList.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        scheduledBillboardsStart.setSelectedIndex(scheduledBillboardsList.getSelectedIndex());
                        scheduledBillboardsTime.setSelectedIndex(scheduledBillboardsList.getSelectedIndex());
                        scheduledBillboardName.setSelectedIndex(scheduledBillboardsList.getSelectedIndex());
                    }
                });
                JButton confirmButton = new JButton("Confirm Remove");

                scheduleDelete.setSize(1000, 100);
                scheduleDelete.setResizable(false);
                scheduleDelete.add(selectBillboard);
                scheduleDelete.add(scheduledBillboardsList);
                scheduleDelete.add(confirmButton);

                scheduleDelete.setVisible(true);
                scheduleDelete.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Billboard to delete from schedule
                        String billboardToDelete = (String)scheduledBillboardName.getSelectedItem();
                        String startDate = (String)scheduledBillboardsStart.getSelectedItem();
                        String startTime = (String)scheduledBillboardsTime.getSelectedItem();

                        try {
                            ClientRequests.RemoveSchedule(billboardToDelete, startDate, startTime);
                            JOptionPane.showMessageDialog(scheduleFrame, "Billboard schedule removed.");
                            scheduleDelete.setVisible(false);
                            scheduleDelete.dispose();
                            scheduleFrame.setVisible(false);
                            scheduleFrame.dispose();

                            Schedule schedule1 = new Schedule();
                        } catch (IOException | ClassNotFoundException exError) {
                            JOptionPane.showMessageDialog(scheduleFrame, "Could not delete the billboard");
                        }
                    }
                });
            }
        });

        schedule.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                //  Make a new frame/window
                JFrame scheduleNew = new JFrame("Add a billboard to schedule");

                //  Model for JComboBox: endDayCB and JSpinners: durationSpinner & repeatFreqSpinner
                SpinnerNumberModel defaultSpinnerModel = new SpinnerNumberModel(1, 1, 480, 1);
                SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(2, 2, 480, 1);
                ComboBoxModel[] models = new ComboBoxModel[5];
                for (int i = 0; i < days.length; i++)
                {
                    //  from https://stackoverflow.com/a/3191882
                    models[i] = new DefaultComboBoxModel(Arrays.copyOfRange(days, i, days.length));
                }

                //  Declare swing items in JFrame
                JComboBox endDayCB = new JComboBox(days);   //  END day combo box
                JComboBox startDayCB = new JComboBox(days); //  START day combo box
                JSpinner durationSpinner = new JSpinner(defaultSpinnerModel);  //  duration spinner
                JSpinner repeatFreqSpinner = new JSpinner(spinnerNumberModel);  //  repeat frequency spinner
                JLabel startTimeLabel = new JLabel("Start Time");   //  Start time label
                //JTextField startTimeTextField = new JTextField("00:00:00");   //  Start time text field

                /*
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
                spinnerDateModel.setValue(calendar.getTime());

                //spinnerDateModel.setStart(calendar.getTime());
                //calendar.set(Calendar.HOUR_OF_DAY, 17);
                //spinnerDateModel.setEnd(calendar.getTime());

                JSpinner startTimeSpinner = new JSpinner(spinnerDateModel);

                JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm:ss");
                DateFormatter formatter = (DateFormatter)dateEditor.getTextField().getFormatter();
                formatter.setAllowsInvalid(false);
                formatter.setOverwriteMode(true);
                startTimeSpinner.setEditor(dateEditor);
                */
                //Dropdown menu for the billboards


                String[] billboardsNames = new String[0];
                int billboardListLength = 0;
                Object[] billboardData = new Object[0];
                ArrayList<Billboard> billboards = new ArrayList<Billboard>();
                try {
                    billboardData = ClientRequests.ListBillboards();
                } catch (IOException | ClassNotFoundException err) {
                    JOptionPane.showMessageDialog(scheduleFrame, err.getMessage());
                }

                //unpack data, create billboard from data, and append billboard to array of billboards
                for(int i = 0; i<billboardData.length; i++){
                    String billboardName = billboardData[i].toString();
                    i++;
                    String billboardCreator = billboardData[i].toString();
                    billboards.add(new Billboard(billboardName, billboardCreator));
                }
                //Check every billboard to see if it has been scheduled already
                try{
                    for (Billboard bb: billboards) {
                        bb.importAllInfo();
                        if(!bb.getScheduled()){
                            billboardListLength++;
                        }
                    }
                    billboardsNames = new String[billboardListLength];
                    Integer counter = 0;
                    for (Billboard bb: billboards) {
                        if(!bb.getScheduled()){
                            billboardsNames[counter] = bb.getBName();
                            counter++;
                        }
                    }
                } catch(IOException | ClassNotFoundException exc) {
                    JOptionPane.showMessageDialog(scheduleFrame, exc.getMessage());
                }


                JComboBox billboardList = new JComboBox(billboardsNames);

                //New time format
                JPanel startTimePanel = new JPanel();
                startTimePanel.setLayout(new GridLayout(0, 2));

                SpinnerNumberModel hoursModel = new SpinnerNumberModel(9, 9, 16, 1);
                SpinnerNumberModel minutesModel = new SpinnerNumberModel(0, 0, 59, 1);
                JSpinner hoursSpinner = new JSpinner(hoursModel);
                JSpinner minutesSpinner = new JSpinner(minutesModel);

                hoursSpinner.setEditor(new JSpinner.DefaultEditor(hoursSpinner));
                minutesSpinner.setEditor(new JSpinner.DefaultEditor(minutesSpinner));

                startTimePanel.add(hoursSpinner);
                startTimePanel.add(minutesSpinner);


                JCheckBox repeatBool = new JCheckBox("Repeats");    //  Repeat check box
                JLabel startDayLabel = new JLabel("Start Day:");
                JLabel endDayLabel = new JLabel("End Day:");
                JLabel durationLabel = new JLabel("Duration:");
                JLabel frequencyLabel = new JLabel("Repeat Frequency:");
                JLabel blank = new JLabel("");
                JLabel blank2 = new JLabel("");
                JLabel blank3 = new JLabel("");
                JLabel blank4 = new JLabel("");
                JLabel selectBillboard = new JLabel("Select billboard");
                JButton confirmButton = new JButton("Confirm");

                //  Adding the elements to the JPanel..
                JPanel optionsPanel = new JPanel();
                optionsPanel.setLayout(new GridLayout(3, 6, 5, 0));
                //startDayCB.setBounds(50, 200, 100, 100);
                //endDayCB.setBounds(150, 100, 100, 100);
                //durationSpinner.setBounds(50, 200, 100, 100);
                //repeatFreqSpinner.setBounds(50, 250, 100, 100);
                //startTimeLabel.setBounds(50, 300, 100, 100);
                //repeatBool.setBounds(50, 350, 100, 100);

                optionsPanel.add(startTimeLabel);
                optionsPanel.add(startDayLabel);
                optionsPanel.add(endDayLabel);
                optionsPanel.add(durationLabel);
                optionsPanel.add(frequencyLabel);
                //optionsPanel.add(blank);
                optionsPanel.add(selectBillboard);

                optionsPanel.add(startTimePanel);
                optionsPanel.add(startDayCB);
                optionsPanel.add(endDayCB);
                optionsPanel.add(durationSpinner);
                optionsPanel.add(repeatFreqSpinner);
                optionsPanel.add(billboardList);
                optionsPanel.add(blank);
                optionsPanel.add(blank2);
                optionsPanel.add(blank3);
                optionsPanel.add(blank4);
                optionsPanel.add(repeatBool);

                optionsPanel.add(confirmButton);


                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String billboardName = (String) billboardList.getSelectedItem();
                        String startTime = "";
//                        if((Integer)hoursSpinner.getValue() == 9){
//                            startTime = "09:" + String.valueOf(minutesSpinner.getValue()) + ":00";
//                        } else {
//                            startTime = String.valueOf(hoursSpinner.getValue()) + ":" + String.valueOf(minutesSpinner.getValue()) + ":00";
//                        }
                        startTime = String.format("%02d:%02d:00", hoursSpinner.getValue(), minutesSpinner.getValue());
                        Integer duration = (Integer)durationSpinner.getValue();
                        Boolean repeat = repeatBool.isSelected();
                        Integer repeatFrequency = (Integer)repeatFreqSpinner.getValue();
                        String startDate = (String)startDayCB.getSelectedItem();
                        String endDate = (String)endDayCB.getSelectedItem();
                        try {
                            ClientRequests.ScheduleBillboard(billboardName, startTime, duration, repeat, repeatFrequency, startDate, endDate);
                            JOptionPane.showMessageDialog(scheduleNew, "Billboard schedule success");
                            scheduleNew.setVisible(false);
                            scheduleNew.dispose();
                            scheduleFrame.setVisible(false);
                            scheduleFrame.dispose();

                            Schedule schedule1 = new Schedule();
                        } catch (IOException | ClassNotFoundException schedError) {
                            JOptionPane.showMessageDialog(scheduleNew, "The billboard could not be scheduled: " + schedError);
                            return;
                        }
                    }
                });

                scheduleNew.add(optionsPanel);

                //  Properties of the frame/window
                scheduleNew.setSize(700, 100);
                scheduleNew.setResizable(false);
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


//    //  TODO remove debug code
//    public static void main(String[] args) {
//        Schedule jc = new Schedule();
//    }
}