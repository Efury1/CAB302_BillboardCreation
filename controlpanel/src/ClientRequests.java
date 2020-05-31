import java.io.IOException;
import java.io.ObjectInput;

public class ClientRequests {
    private static String sessionToken;

    public String GetSessionToken() { return sessionToken; };
    public void SetSessionToken(String sessionToken) { this.sessionToken = sessionToken; };

    /**
     * Login request - functionID == 1
     * @param username
     * @param password
     * @throws IOException catch the error when this is run. If the server sends false, an exception is thrown
     */

    /**
     *Sends login request to server and retrieves token
     * @param username username used to login to the application
     * @param password password used to login to the application
     * @throws IOException
     */
    public static void Login(String username, String password) throws IOException {
        Object[] token = SendReceive.SendReceive(1, "", new Object[] {username, password});
        sessionToken = token[0].toString();
    }


    /**
     *Requests and returns the current list of billboards in the database
     * @return An object array of the name and creator of each billboard
     * @throws IOException
     */
    public static Object[] ListBillboards() throws IOException {
        Object[] listOfBillboards = SendReceive.SendReceive(2, sessionToken, new Object[] {});
        //  TODO for loop just for testing
        for(int i = 0; i < listOfBillboards.length; i+=2)
        {
            System.out.println(listOfBillboards[i]);    //Billboard name
            System.out.println(listOfBillboards[i+1]);  //Billboard creator
        }
        return listOfBillboards;
    }


    /**
     *Requests and returns the data of a particular billboard
     * @param billboardName the name of the billboard to return data of
     * @return An object array of billboard data
     * @throws IOException
     */
    public static Object[] GetBillboardInfo(String billboardName) throws IOException {
        Object[] billboardInfo = SendReceive.SendReceive(3, sessionToken, new Object[] {billboardName});
        return billboardInfo;
    }


    /**
     *Sends data to create or update a billboard
     * @param billboardName name of billboard to update or create
     * @param title new/updated title
     * @param description new/updated description
     * @param pictureData new/updated image
     * @param bgColour new/updated background color
     * @param titleColour new/updated title colour
     * @param descColour new/updated description colour
     * @param creatorUsername the user name of the creator
     * @throws IOException
     */
    public static void CreateEditBillboard(String billboardName, String title, String description, byte[] pictureData, String bgColour, String titleColour, String descColour, String creatorUsername) throws IOException {
        SendReceive.SendReceive(4, sessionToken, new Object[] {billboardName, title, description, pictureData, bgColour, titleColour, descColour, creatorUsername});
    }


    /**
     *Deletes the specified billboard
     * @param billboardName name of billboard to delete
     * @throws IOException
     */
    public static void DeleteBillboard(String billboardName) throws IOException {
        SendReceive.SendReceive(5, sessionToken, new Object[] {billboardName});
    }


    /**
     *Returns schedule of billboard showings
     * @return object array of schedule data
     * @throws IOException
     */
    public static Object[] ViewSchedule() throws IOException {
        Object[] schedule = SendReceive.SendReceive(6, sessionToken, new Object[] {});
        return schedule;
    }


    /**
     *Schedules the selected billboard
     * @param billboardName name of billboard to schedule
     * @param startTime start of the scheduled session
     * @param duration duration in minutes of the showing of the billboard
     * @param repeat whether the showing is repeated
     * @param repeatFreq the frequency of repeats
     * @param startDate the start date of the scheduling
     * @param endDate the end date of the scheduling
     * @throws IOException
     */
    public static void ScheduleBillboard(String billboardName, String startTime, Integer duration, Boolean repeat, Integer repeatFreq, String startDate, String endDate) throws IOException {
        SendReceive.SendReceive(7, sessionToken, new Object[] {billboardName, startTime, duration, repeat, repeatFreq, startDate, endDate});
    }


    /**
     *Removes a specific scheduling of a billboard
     * @param billboardName the name of the billboard to remove scheduling of
     * @param startDate the start date of the scheduling to be removed
     * @param startTime the start time of the scheduling to be removed
     * @throws IOException
     */
    public static void RemoveSchedule(String billboardName, String startDate, String startTime) throws IOException {
        SendReceive.SendReceive(8, sessionToken, new Object[] {billboardName, startDate, startTime});
    }


    /**
     *Returns a list of registered users
     * @return object array of usernames
     * @throws IOException
     */
    public static Object[] ListUsers() throws IOException {
        Object[] userList = SendReceive.SendReceive(9, sessionToken, new Object[] {});
        return userList;
    }


    /**
     *Creates a new user
     * @param username username of new user
     * @param perm1 whether has permission to create billboards
     * @param perm2 whether has permission to edit all billboards
     * @param perm3 whether has permission to edit other users
     * @param perm4 whether has permission to schedule billboards
     * @param password password of new user
     * @throws IOException
     */
    public static void CreateUser(String username, Boolean perm1, Boolean perm2, Boolean perm3, Boolean perm4, String password) throws IOException {
        SendReceive.SendReceive(10, sessionToken, new Object[] {username, perm1, perm2, perm3, perm4, password});
    }


    /**
     *Returns list of user permissions
     * @param username username of user to get permissions of
     * @return object array of permissions
     * @throws IOException
     */
    public static Object[] GetUserPermissions(String username) throws IOException {
        Object[] userPerms = SendReceive.SendReceive(11, sessionToken, new Object[] {username});
        return userPerms;
    }


    /**
     *Sets the permissions of a specific user
     * @param username username of user to set permissions of
     * @param perm1 whether has permission to create billboards
     * @param perm2 whether has permission to edit all billboards
     * @param perm3 whether has permission to edit other users
     * @param perm4 whether has permission to schedule billboards
     * @throws IOException
     */
    public static void SetUserPermissions(String username, Boolean perm1, Boolean perm2, Boolean perm3, Boolean perm4) throws IOException {
        SendReceive.SendReceive(12, sessionToken, new Object[] {username, perm1, perm2, perm3, perm4});
    }


    /**
     *Sets the password of a user
     * @param username username of user to set password of
     * @param password new password of user
     * @throws IOException
     */
    public static void SetUserPassword(String username, String password) throws IOException {
        SendReceive.SendReceive(13, sessionToken, new Object[] {username, password});
    }


    /**
     *Deletes specified user
     * @param username username of user to delete
     * @throws IOException
     */
    public static void DeleteUser(String username) throws IOException {
        SendReceive.SendReceive(14, sessionToken, new Object[] {username});
    }


    /**
     *Logs user out of the application
     * @throws IOException
     */
    public static void LogOut() throws IOException {
        SendReceive.SendReceive(15, sessionToken, new Object[] {});
    }
}
