import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


class MainCalendarScene extends JFrame {
    private ScheduleModel calModel;
    //private EventDateModel events;
    private int chosenButton;
    private LocalDate dateChosen;

    private JPanel calendarPanel;
    private JButton[] titleButtons;
    private JButton[] daybuttons;
    private JLabel monthYearLabel;
    private JPanel buttonPanel;



    private JPanel eventPanel;
    private JTextArea eventArea;
    private JLabel dayLabel;



    //public MainCalendarScene(CalendarModel model, EventDateModel model2)
    public MainCalendarScene(ScheduleModel model){
        calModel = model;
        //events = model2;
        dateChosen = calModel.getDateNow();

        //////////////////////////////
        //Setting up navigation panel


        //////////////////////////////////
        //setting up event panel
        eventArea = new JTextArea(20,20);
        eventArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(eventArea);
        dayLabel = new JLabel(this.getDateString(calModel.getDateNow()));

        eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout (eventPanel, BoxLayout.Y_AXIS));
        eventPanel.add(dayLabel);
        eventPanel.add(scrollPane);

        this.add(eventPanel, BorderLayout.EAST);

        ///////////////////////////////////////////


        monthYearLabel = new JLabel(this.getMonthYear(calModel.getDateNow()));

        //set up button panel and attach listener
        this.setupButtonPanel();

        calendarPanel = new JPanel();
        calendarPanel.setLayout(new BoxLayout (calendarPanel, BoxLayout.Y_AXIS));

        calendarPanel.add(monthYearLabel);
        calendarPanel.add(buttonPanel);


        this.add(calendarPanel, BorderLayout.WEST);

        //general frame setting

        this.pack();

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public String getDateString(LocalDate date){
        String line = date.getDayOfWeek() + " "+date.getMonthValue()+"/"+date.getDayOfMonth();
        return line;
    }

    public String getMonthYear(LocalDate date){
        String line = date.getMonth()+" "+date.getYear();
        return line;
    }

    private void setupButtonPanel(){
        String[] row = calModel.getRowData();

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 7));

        titleButtons = new JButton[7];

        //drawing titles
        String[] title = new String[]{"S","M","T","W","T","F","S"};
        for(int i = 0; i < titleButtons.length; i++){
            titleButtons[i] = new JButton(title[i]);
            buttonPanel.add(titleButtons[i]);
        }


        daybuttons = new JButton[row.length];
        for(int i = 0; i < row.length; i++){
            daybuttons[i] = new JButton(row[i]);
            buttonPanel.add(daybuttons[i]);
            String currentDate = calModel.getDateNow().getDayOfMonth()+"";
            daybuttons[i].addActionListener(dayButtonClicked(i));
            if(row[i].equals(currentDate)){
                daybuttons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                chosenButton = i;
            }
        }


    }

    public ActionListener dayButtonClicked(int i){
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate day = calModel.getRequestedDay();
                String dayNumber = daybuttons[i].getText();
                if(!dayNumber.trim().equals("")){
                    LocalDate date = LocalDate.of(day.getYear(),
                            day.getMonthValue(), Integer.parseInt(dayNumber));
                    dateChosen = date;

                    System.out.println("Button clicked "+dateChosen);


                }
            }

        };

    }


}




/*import javax.swing.JFrame;
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
        f.setDefaultCloseOperation(f.HIDE_ON_CLOSE);

    }

}*/