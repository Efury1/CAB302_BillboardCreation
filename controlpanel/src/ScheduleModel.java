import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * An instance of the calendar containing all scheduled billboards for the week.
 */
public class ScheduleModel {
    private Calendar cal;
    private LocalDate now;
    private String[] rowData;

    public ScheduleModel() {
        cal = Calendar.getInstance();
        now = LocalDate.of(cal.get(Calendar.YEAR),
                cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.HOUR_OF_DAY));
        setRowData();
        JFrame f = new JFrame();
        ScheduleModel calModel = null;
        JLabel dayLabel = new JLabel(String.valueOf(cal.get(Calendar.DAY_OF_WEEK)));

        ///////////
        String[] title = new String[]{"S","M","T","W","T","F","S"};
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 7));
        JButton[] titleButtons = new JButton[7];
        for(int i = 0; i < titleButtons.length; i++){
            titleButtons[i] = new JButton(title[i]);
            buttonPanel.add(titleButtons[i]);
        }
        /////////////////


        ////////////////

        JPanel eventPanel = new JPanel();
        eventPanel.add(buttonPanel);
        eventPanel.setLayout(new BoxLayout (eventPanel, BoxLayout.Y_AXIS));

        f.add(eventPanel, BorderLayout.NORTH);
        f.setSize(500,500);
        f.setVisible(true);

    }




    public LocalDate getRequestedDay() {
        int requestedYear = cal.get(Calendar.YEAR);
        int requestedDay = cal.get(Calendar.DAY_OF_WEEK);
        int requestedHour = cal.get(Calendar.HOUR_OF_DAY);
        return LocalDate.of(requestedYear, requestedHour, requestedHour);
    }

    private void setRowData() {


        String[] rowData = new String[7];


    }



    public LocalDate getDateNow() {
        return now;
    }

    public String[] getRowData() {
        return rowData.clone();
    }

}

