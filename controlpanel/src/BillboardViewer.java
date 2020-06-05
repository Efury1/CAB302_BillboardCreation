import javax.swing.*;
import java.io.IOException;
import java.net.Inet4Address;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BillboardViewer {
    public static void main(String[] args){
        BillboardViewer instance = new BillboardViewer();
    }
    public BillboardViewer(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        Calendar timeToCheck;
        Calendar timeToCheck2;

        HashMap<String, Integer> dayMap = new HashMap<String, Integer>();
        dayMap.put("Monday", 2);
        dayMap.put("Tuesday", 3);
        dayMap.put("Wednesday", 4);
        dayMap.put("Thursday", 5);
        dayMap.put("Friday", 6);


        Long startupTime = System.currentTimeMillis();
        try {
            ClientRequests.Login("staff", "today123");
            System.out.println("Connected to server");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Could not connect to server, retrying in 15 seconds");
            //wait and try again
            ShowDefaultBB();
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                System.err.println("Thread timeout error");
            }

            System.err.println("Retrying...");
            BillboardViewer again = new BillboardViewer();
        }

        while(true){

            System.out.println("Checking for a new billboard to display...");

            //TODO delete this paragraph of code below (hardcodes date)
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            String dateInString = "04-06-2020 11:32:56";
            try {
                date = sdf2.parse(dateInString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.setTime(date);
            timeToCheck = Calendar.getInstance();
            timeToCheck.setTime(date);
            timeToCheck2 = Calendar.getInstance();
            timeToCheck2.setTime(date);

            //TODO uncomment after deleting the above
//            calendar = Calendar.getInstance();
//            timeToCheck = Calendar.getInstance();
//            timeToCheck2 = Calendar.getInstance();

            //Get the new schedule data
            Object[] scheduleData = new Object[]{};
            try {
                scheduleData = ClientRequests.ViewSchedule();
            } catch (IOException | ClassNotFoundException e) {
                ShowDefaultBB();
            }
            String candidateBoardName = "";
            String candidateBoardOwner = "";
            for(int i = 0; i < scheduleData.length; i+=9){
                //Read in all the necessary data
                String bbName = (String)scheduleData[i];
                String bbOwner = (String)scheduleData[i+1];
                String startDay = (String)scheduleData[i+3];
                String endDay = (String)scheduleData[i+4];
                String  startTime = (String)scheduleData[i+5];
                Integer duration = (Integer)scheduleData[i+6];
                Boolean repeats = (Boolean)scheduleData[i+7];
                Integer repeatFreq = (Integer)scheduleData[i+8];
                try {
                    date = sdf.parse(startTime);
                } catch (ParseException e) {
                    ShowDefaultBB();
                }

                if(calendar.get(Calendar.DAY_OF_WEEK) >= dayMap.get(startDay)){
                    if(calendar.get((Calendar.DAY_OF_WEEK)) <= dayMap.get(endDay)){ //Check that the day is in range
                        timeToCheck.set(Calendar.HOUR_OF_DAY, date.getHours());
                        timeToCheck.set(Calendar.MINUTE, date.getMinutes());

                        timeToCheck2.set(Calendar.HOUR_OF_DAY, date.getHours() + (date.getMinutes()+duration)/60);
                        timeToCheck2.set(Calendar.MINUTE, (date.getMinutes()+duration)%60);


                        if(!timeToCheck.after(calendar) && !timeToCheck2.before(calendar)){
                            candidateBoardName = bbName;
                            candidateBoardOwner = bbOwner;
                        } else if (repeats){
                            while(timeToCheck.get(Calendar.HOUR_OF_DAY) < 17){
                                //Set the next start interval time as the previous time + the repeat freq
                                timeToCheck.set(Calendar.HOUR_OF_DAY, timeToCheck.get(Calendar.HOUR_OF_DAY) + repeatFreq/60);
                                if(timeToCheck.get(Calendar.MINUTE) + repeatFreq%60 > 60){
                                    timeToCheck.set(Calendar.HOUR_OF_DAY, timeToCheck.get(Calendar.HOUR_OF_DAY) + 1);
                                }
                                timeToCheck.set(Calendar.MINUTE, (timeToCheck.get(Calendar.MINUTE) + repeatFreq)%60);

                                //Set the next end interval time as the previous time + the repeat freq + the duration
                                timeToCheck2.set(Calendar.HOUR_OF_DAY, timeToCheck.get(Calendar.HOUR_OF_DAY) + (repeatFreq/60) + (duration/60));
                                if(timeToCheck2.get(Calendar.MINUTE) + duration%60 + repeatFreq%60 > 60) {
                                    timeToCheck2.set(Calendar.HOUR_OF_DAY, timeToCheck.get(Calendar.HOUR_OF_DAY) + (timeToCheck2.get(Calendar.MINUTE) + duration % 60 + repeatFreq % 60) / 60);
                                }
                                timeToCheck2.set(Calendar.MINUTE, (timeToCheck2.get(Calendar.MINUTE) + duration%60 + repeatFreq%60)%60);
                                if(!timeToCheck.after(calendar) && !timeToCheck2.before(calendar)) {
                                    candidateBoardName = bbName;
                                    candidateBoardOwner = bbOwner;
                                }
                            }
                        }
//                        if(calendar.get(Calendar.HOUR_OF_DAY) > date.getHours()){
//                            candidateBoardName = bbName;
//                            candidateBoardOwner = bbOwner;
//                        } else if (calendar.get(Calendar.HOUR_OF_DAY) == date.getHours()){
//                            if(calendar.get(Calendar.MINUTE) >= date.getMinutes() && calendar.get(Calendar.MINUTE) <= (date.getMinutes() + duration)){
//                                candidateBoardName = bbName;
//                                candidateBoardOwner = bbOwner;
//                            }
//                        }
                    }
                }
            }
            if(candidateBoardName == "" || candidateBoardOwner == ""){
                ShowDefaultBB();
            } else {
                Billboard billboardToShow = new Billboard(candidateBoardName, candidateBoardOwner);
                try {
                    billboardToShow.importAllInfo();
                    ViewBillboard.showBillboard(billboardToShow);
                } catch (IOException | ClassNotFoundException | SQLException e) {
                    ShowDefaultBB();
                }
            }

            //check that the billboard has displayed for at least 15 seconds
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                System.err.println("Thread timeout error");
            }
        }

    }
    private void ShowDefaultBB(){
        Billboard defaultBB = new Billboard("default", "default");
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.DAY_OF_WEEK) == 7 || calendar.get((Calendar.DAY_OF_WEEK)) == 1){
            defaultBB.setBMessage("It's the weekend");
            defaultBB.setBBackgroundColour("FFFF00");
        } else {
            defaultBB.setBDescription("This is a placeholder board");
            defaultBB.setBMessage("We'll be right back");
            defaultBB.setBBackgroundColour("FF0000");
        }
        defaultBB.setBDescriptionColour("FFFFFF");
        defaultBB.setBMessageColour("0000FF");
        try {
            ViewBillboard.showBillboard(defaultBB);
        } catch (IOException | SQLException e) {
            System.err.println("Could not display default Billboard: " + e);
        }
        try {
            Thread.sleep(14800);
        } catch (InterruptedException e) {
            System.err.println("Thread timeout error: " + e);
        }
        ViewBillboard.frame.setVisible(false);
        ViewBillboard.frame.dispose();
    }
}
