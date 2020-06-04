import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class drawingComponent extends JComponent {
    final static int cellWidth = 70;
    final static int cellHeight = 60;
    final static int textHeight = 24;

    public drawingComponent() {
        repaint();
    }

    public void paintComponent(Graphics g) {
        Color[] colours = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.darkGray, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK};
        int currentColour = 0;

        Object[] scheduledBillboards = new Object[]{};
        try {
            scheduledBillboards = ClientRequests.ViewSchedule();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        HashMap<String, Integer> dayMap = new HashMap<String, Integer>();
        dayMap.put("Monday", 0);
        dayMap.put("Tuesday", 1);
        dayMap.put("Wednesday", 2);
        dayMap.put("Thursday", 3);
        dayMap.put("Friday", 4);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        int minutesFrom9 = 0;

        if(scheduledBillboards.length > 0){
            for(int i = 0; i < scheduledBillboards.length; i+=9){
                //For each billboard that is scheduled, get the starting times
                if(dayMap.get(scheduledBillboards[i+3]) != null){

                    //set the billboard colour
                    if(currentColour >= colours.length){
                        currentColour -= colours.length;
                    }
                    g.setColor(colours[currentColour]);
                    currentColour++;

                    //Get the start and end days
                    Integer startDay = dayMap.get(scheduledBillboards[i+3]);
                    Integer endDay = dayMap.get(scheduledBillboards[i+4]);

                    //Get the time of the schedules board
                    try {
                        date = sdf.parse((String)scheduledBillboards[i+5]); //"07:20:00"
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Integer duration = (Integer)scheduledBillboards[i+6];
                    Boolean repeats = (Boolean)scheduledBillboards[i+7];
                    Integer repeatFrequency = (Integer)scheduledBillboards[i+8];

                    //Find the time from 9am
                    //How many minutes from 9am
                    minutesFrom9 = (date.getHours() * 60 + date.getMinutes()) - 540;
                    Integer scheduleTimer = minutesFrom9;

                    if(minutesFrom9 >= 0) {
                        while (startDay <= endDay) {
                            //draw a bunch of rectangles
                            g.fillRect((startDay* cellWidth), minutesFrom9 + textHeight, cellWidth, duration);
                            if(repeats){
                                scheduleTimer += repeatFrequency;
                                while(scheduleTimer < 480) {
                                    g.fillRect((startDay* cellWidth), scheduleTimer + textHeight, cellWidth, duration);
                                    scheduleTimer += repeatFrequency;
                                }
                            }
                            startDay++;
                            scheduleTimer = minutesFrom9;
                        }
                    }
                }
            }
        }   //  end if

        //  Draw the grid on top of the schedules
        DrawGrid(g);
    }


    /**
     * Draws a grid of cells size: cellWidth x cellHeight.
     * @param g2 The graphics class called from Schedule.java to draw on the schedule window.
     */
    public static void DrawGrid(Graphics g2)
    {
        g2.setColor(Color.LIGHT_GRAY);
        //  vertical grid lines
        g2.drawLine(70, textHeight, 70, 540);
        g2.drawLine(70+ cellWidth, textHeight, 70+ cellWidth, 540);
        g2.drawLine(70+ cellWidth *2, textHeight, 70+ cellWidth *2, 540);
        g2.drawLine(70+ cellWidth *3, textHeight, 70+ cellWidth *3, 540);
        g2.drawLine(70+ cellWidth *4, textHeight, 70+ cellWidth *4, 540);
        g2.drawLine(70+ cellWidth *5, textHeight, 70+ cellWidth *5, 540);

        //  horizontal grid lines
        g2.drawLine(27, textHeight, 420, textHeight);
        g2.drawLine(27, textHeight + cellHeight, 420, textHeight + cellHeight);
        g2.drawLine(27, textHeight + cellHeight * 2, 420, textHeight + cellHeight * 2);
        g2.drawLine(27, textHeight + cellHeight * 3, 420, textHeight + cellHeight * 3);
        g2.drawLine(27, textHeight + cellHeight * 4, 420, textHeight + cellHeight * 4);
        g2.drawLine(27, textHeight + cellHeight * 5, 420, textHeight + cellHeight * 5);
        g2.drawLine(27, textHeight + cellHeight * 6, 420, textHeight + cellHeight * 6);
        g2.drawLine(27, textHeight + cellHeight * 7, 420, textHeight + cellHeight * 7);
        g2.drawLine(27, textHeight + cellHeight * 8, 420, textHeight + cellHeight * 8);
    }
}
