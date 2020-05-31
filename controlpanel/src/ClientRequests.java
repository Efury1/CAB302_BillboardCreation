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
    public static void Login(String username, String password) throws IOException {
        Object[] token = SendReceive.SendReceive(1, "", new Object[] {username, password});
        sessionToken = token[0].toString();
    }

    /**
     *
     * @return
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

    public static Object[] GetBillboardInfo(String billboardName) throws IOException {
        Object[] billboardInfo = SendReceive.SendReceive(3, sessionToken, new Object[] {billboardName});
        return billboardInfo;
    }

    public static void CreateEditBillboard(String billboardName, String title, String description, byte[] pictureData, String bgColour, String titleColour, String descColour, String creatorUsername) throws IOException {
        SendReceive.SendReceive(4, sessionToken, new Object[] {billboardName, title, description, pictureData, bgColour, titleColour, descColour, creatorUsername});
    }

    public static void DeleteBillboard(String billboardName) throws IOException {
        SendReceive.SendReceive(5, sessionToken, new Object[] {billboardName});
    }

    public static Object[] ViewSchedule() throws IOException {
        Object[] schedule = SendReceive.SendReceive(6, sessionToken, new Object[] {});
        return schedule;
    }

    public static void ScheduleBillboard(String billboardName, String startTime, Integer duration, Boolean repeat, Integer repeatFreq, String startDate, String endDate) throws IOException {
        SendReceive.SendReceive(7, sessionToken, new Object[] {billboardName, startTime, duration, repeat, repeatFreq, startDate, endDate});
    }

    public static void RemoveSchedule(String billboardName, String startDate, String startTime) throws IOException {
        SendReceive.SendReceive(8, sessionToken, new Object[] {billboardName, startDate, startTime});
    }

    public static Object[] ListUsers() throws IOException {
        Object[] userList = SendReceive.SendReceive(9, sessionToken, new Object[] {});
        return userList;
    }

    public static void CreateUser(String username, Boolean perm1, Boolean perm2, Boolean perm3, Boolean perm4, String password) throws IOException {
        SendReceive.SendReceive(10, sessionToken, new Object[] {username, perm1, perm2, perm3, perm4, password});
    }

    public static Object[] GetUserPermissions(String username) throws IOException {
        Object[] userPerms = SendReceive.SendReceive(11, sessionToken, new Object[] {username});
        return userPerms;
    }

    public static void SetUserPermissions(String username, Boolean perm1, Boolean perm2, Boolean perm3, Boolean perm4) throws IOException {
        SendReceive.SendReceive(12, sessionToken, new Object[] {username, perm1, perm2, perm3, perm4});
    }

    public static void SetUserPassword(String username, String password) throws IOException {
        SendReceive.SendReceive(13, sessionToken, new Object[] {username, password});
    }

    public static void DeleteUser(String username) throws IOException {
        SendReceive.SendReceive(14, sessionToken, new Object[] {username});
    }

    public static void LogOut() throws IOException {
        SendReceive.SendReceive(15, sessionToken, new Object[] {});
    }

//    /**
//     *
//     * @param token
//     * @throws IOException
//     */
//    private static Boolean ValidateReply(Object[] token) throws IOException {
//        if (!((boolean) token[0]))
//        {
//            //  invalid credentials
//            if (token.length > 1)
//            {
//                throw new IOException((String)token[1]);
//            }
//            return false;
//        }
//        return true;
//    }
}
