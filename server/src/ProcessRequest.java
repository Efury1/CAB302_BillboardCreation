import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;

public class ProcessRequest {

    /**
     * Creates an array object to handle errors from the server easily.
     * @param errorMessage Error message to store in the error array
     * @return An array in the format: acknowledgement, length, errorMessage
     */
    public static Object[] RelayError(String errorMessage)
    {
        Object[] errorArray = new Object[3];
        errorArray[0] = false;         //  False (acknowledgement of action failed)
        errorArray[1] = 1;             //  length
        errorArray[2] = errorMessage;  //  Error message
        return errorArray;
    }


    private static Object[] Login(String user, String pass, Connection myConnection, TokenHandler tokenCache) throws SQLException {
        Object[] loginStatus = new Object[3];
        loginStatus[1] = 1; //length is always 1 (for consequent data)
        //process Username and password from database

        PreparedStatement getUserQuery = myConnection.prepareStatement("SELECT password_hash, password_salt FROM users WHERE user = ?");
        getUserQuery.setString(1, user);
        ResultSet result = getUserQuery.executeQuery();
        String serverPasswordHash;
        String serverPasswordSalt;

        //Check that the username is in the database
        if(result.next()) {
            serverPasswordHash = result.getString(1);
            serverPasswordSalt = result.getString(2);
        } else {
            loginStatus = RelayError("The username or password is invalid.");
            return loginStatus;
        }

        SaltHandler saltHandler = new SaltHandler();

        //Code to compare the hashed passwords
        String clientSaltedHash = saltHandler.HashString(serverPasswordSalt + pass); //Client's password

        //Check that the passwords match
        if(clientSaltedHash == serverPasswordHash){
            loginStatus[0] = true;
            //Return a valid token
            SaltHandler tokenGen = new SaltHandler();
            String token = tokenGen.GetSalt();  //Add token to valid token cache
            tokenCache.AddToken(token);
            loginStatus[2] = token;             //To send back to client
        } else {
            loginStatus = RelayError("The username or password is invalid");
        }
        getUserQuery.close();
        return loginStatus;
    }

    private static Object[] ListBillboards(Connection myConnection) throws SQLException {
        Object[] billboardData;
        ArrayList<String> temp_array = new ArrayList<String>();
        int length = 0;

        PreparedStatement billboardQuery = myConnection.prepareStatement("SELECT billboards.billboard_name, user_billboards.username FROM billboards INNER JOIN user_billboards ON billboards.billboard_name = user_billboards.billboard_name");
        ResultSet billboardList = billboardQuery.executeQuery();

        boolean hasBillboards = false;
        while(billboardList.next()){
            hasBillboards = true;
            temp_array.add(billboardList.getString(1)); //Billboard name
            temp_array.add(billboardList.getString(2)); //Creator / username
            length += 2;
        }

        //Check if any billboard was returned
        if(hasBillboards) {
            billboardData = new Object[length+2];
            billboardData[0] = true;                //return true to signal billboard was found
            billboardData[1] = length;              //return the length of billboard data to read in
        } else {
            billboardData = RelayError("No billboards were found.");
        }


        //Read in the data from the temp array to the main object array (to send the list of billboards back to the client)
        for (int i = 2; i < billboardData.length+2; i += 2) {
            billboardData[i] = temp_array.get(i-2);
            billboardData[i+1] = temp_array.get(i-1);
        }
        billboardQuery.close();
        return billboardData;
    }

    private static Object[] GetBillboardInformation(String billboard_name, Connection myConnection) throws SQLException {
        Object[] billboardData;

        //Query database for the billboard
        PreparedStatement billboardQuery = myConnection.prepareStatement("SELECT title, description, picture, background_colour, title_colour, description_colour FROM billboards WHERE billboard_name = ?");
        billboardQuery.setString(1, billboard_name);
        ResultSet billboardInfo = billboardQuery.executeQuery();

        if(billboardInfo.next()){
            billboardData = new Object[8];
            billboardData[0] = true;
            billboardData[1] = 6;
            billboardData[2] = billboardInfo.getString(1);  //Title
            billboardData[3] = billboardInfo.getString(2);  //Description
            billboardData[4] = billboardInfo.getBytes(3);   //Picture
            billboardData[5] = billboardInfo.getString(4);  //Background colour
            billboardData[6] = billboardInfo.getString(5);  //Title colour
            billboardData[7] = billboardInfo.getString(6);  //Description colour

        } else {
            billboardData = RelayError("Billboard not found.");
        }
        billboardQuery.close();
        return billboardData;
    }

    private static Object[] CreateEditBillboard(String billboardName, String title, String description, byte[] picture, String backgroundColour, String titleColour, String descriptionColour, String creatorUsername, Connection myConnection) throws SQLException {
        Object[] serverReply;
        //Query the database for the billboard
        PreparedStatement billboardQuery = myConnection.prepareStatement("SELECT COUNT(billboard_name) FROM billboards WHERE billboard_name = ?");
        billboardQuery.setString(1, billboardName);
        ResultSet billboardResult = billboardQuery.executeQuery();
        billboardQuery.close();

        if(billboardResult.next()){
            if(billboardResult.getInt(1) == 1){     //Billboard already exists
                //Update the billboard's information
                PreparedStatement updateBillboard = myConnection.prepareStatement("UPDATE billboards SET title = ?, description = ?, picture = ?, background_colour = ?, title_colour = ?, description_colour = ? WHERE billboard_name = ?");
                updateBillboard.setString(1, title);
                updateBillboard.setString(2, description);
                updateBillboard.setBytes(3, picture);
                updateBillboard.setString(4, backgroundColour);
                updateBillboard.setString(5, titleColour);
                updateBillboard.setString(6, descriptionColour);
                updateBillboard.setString(7, billboardName);

                if(updateBillboard.executeUpdate() > 0){
                    serverReply = new Object[2];
                    serverReply[0] = true;
                    serverReply[1] = 0;
                } else {
                    serverReply = RelayError("Could not update the billboard information");
                }
                //Close the statement
                updateBillboard.close();
            } else {
                Statement commit = myConnection.createStatement();
                commit.execute("BEGIN WORK");
                //Create a new billboard in the database
                PreparedStatement createNewBillboard = myConnection.prepareStatement("INSERT INTO billboards (billboard_name, title, description, picture, background_colour, title_colour, description_colour) VALUES (?, ?, ?, ?, ?, ?, ?)");
                createNewBillboard.setString(1, billboardName);
                createNewBillboard.setString(2, title);
                createNewBillboard.setString(3, description);
                createNewBillboard.setBytes(4, picture);
                createNewBillboard.setString(5, backgroundColour);
                createNewBillboard.setString(6, titleColour);
                createNewBillboard.setString(7, descriptionColour);

                PreparedStatement createUserBillboardEntry = myConnection.prepareStatement("INSERT INTO user_billboards (billboard_name, username) VALUES (?, ?)");
                createUserBillboardEntry.setString(1, billboardName);
                createUserBillboardEntry.setString(2, creatorUsername);

                //Only commit to database if both queries work
                if(createNewBillboard.executeUpdate() > 0){
                    if(createUserBillboardEntry.executeUpdate() > 0){
                        serverReply = new Object[2];
                        serverReply[0] = true;
                        serverReply[1] = 0;
                        commit.execute("COMMIT");
                    } else {
                        serverReply = RelayError("The billboard could not be created.");
                        commit.execute("ROLLBACK");
                    }
                } else {
                    serverReply = RelayError("The billboard could not be created.");
                }
                commit.execute("COMMIT");

                //Close the statements
                commit.close();
                createNewBillboard.close();
                createUserBillboardEntry.close();
            }
        } else {
            serverReply = RelayError("SQL error. Could not search for billboard.");
        }
        return serverReply;
    }

    private static Object[] DeleteBillboard(String billboardName, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Make SQL statement to delete a billboard
        PreparedStatement deleteBillboard = myConnection.prepareStatement("DELETE FROM billboards WHERE billboard_name = ?");

        if(deleteBillboard.executeUpdate() > 0){
            serverReply = new Object[2];
            serverReply[0] = true;
            serverReply[1] = 0;
        } else {
            serverReply = new Object[3];
            serverReply[0] = false;
            serverReply[1] = 1;
            serverReply[2] = "Could not find the billboard to delete";
        }
        deleteBillboard.close();
        return serverReply;
    }

    private static Object[] ViewSchedule(Connection myConnection) throws SQLException {
        Object[] scheduleData;
        ArrayList<Object> tempArray = new ArrayList<Object>();
        int length = 0;

        //Query database for the schedule of all billboards
        PreparedStatement billboardScheduleList = myConnection.prepareStatement("SELECT billboard_schedule.billboard_name, schedule_ts, start_date, end_date, start_time, duration, repeats, repeat_frequency FROM schedules INNER JOIN billboard_schedule ON schedules.scheduleID = billboard_schedule.scheduleID");
        ResultSet scheduledBillboards = billboardScheduleList.executeQuery();
        billboardScheduleList.close();

        if(scheduledBillboards.next()){
            do {
                tempArray.add(scheduledBillboards.getString(1)); //Billboard name
                tempArray.add(scheduledBillboards.getString(2)); //Last updated timestamp
                tempArray.add(scheduledBillboards.getString(3)); //Start date
                tempArray.add(scheduledBillboards.getString(4)); //End date
                tempArray.add(scheduledBillboards.getInt(5));     //Duration
                tempArray.add(scheduledBillboards.getBoolean(6)); //Repeats
                tempArray.add(scheduledBillboards.getInt(7));     //Repeat frequency
                length += 7;
            } while(scheduledBillboards.next());
        } else {
            scheduleData = RelayError("No scheduled billboards found.");
            return scheduleData;
        }

        scheduleData = new Object[length+2];
        scheduleData[0] = true;
        scheduleData[1] = length;

        for(int i = 2; i < length+2; i++){
            scheduleData[i] = tempArray.get(i-2);
        }

        return scheduleData;
    }

    private static Object[] ScheduleBillboard(String billboardName, String startTime, Integer duration, Boolean repeats, Integer repeatFrequency, String startDate, String endDate, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Prepare the statement for the database
        Statement commit = myConnection.createStatement();
        commit.execute("BEGIN WORK");

        PreparedStatement scheduleCreate = myConnection.prepareStatement("INSERT INTO schedules (start_date, end_date, start_time, duration, repeats, repeat_frequency) VALUES (?, ?, ?, ?, ?, ?");
        scheduleCreate.setString(1, startDate);
        scheduleCreate.setString(2, endDate);
        scheduleCreate.setString(3, startTime);
        scheduleCreate.setInt(4, duration);
        scheduleCreate.setBoolean(5, repeats);
        scheduleCreate.setInt(6, repeatFrequency);

        //Find the scheduleID for the latest entry
        PreparedStatement scheduleGet = myConnection.prepareStatement("SELECT schedule_ID FROM schedules WHERE schedule_ts = (SELECT MAX(schedule_ts) FROM schedules)");

        PreparedStatement scheduleAssignToBillboard = myConnection.prepareStatement("INSERT INTO billboard_schedule (schedule_ID, billboard_name) VALUES (?, ?)");

        if(scheduleCreate.executeUpdate() > 0){
            //Get the scheduleID of the new Schedule
            ResultSet recentSchedule = scheduleGet.executeQuery();
            recentSchedule.next();
            int scheduleID = recentSchedule.getInt(1);

            scheduleAssignToBillboard.setInt(1, scheduleID);
            scheduleAssignToBillboard.setString(2, billboardName);

            if(scheduleAssignToBillboard.executeUpdate() > 0){
                commit.execute("COMMIT");
                serverReply = new Object[2];
                serverReply[0] = true;
                serverReply[1] = 0;
            } else{
                commit.execute("ROLLBACK");
                serverReply = RelayError("Could not schedule billboard");
            }
        } else {
            serverReply = RelayError("Could not schedule billboard");
        }


        //close the statements
        commit.close();
        scheduleAssignToBillboard.close();
        scheduleCreate.close();
        scheduleGet.close();
        return serverReply;
    }

    private static Object[] RemoveSchedule(String billboardName, String startDate, String startTime, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Prepare the query to find the schedule and delete it
        PreparedStatement removeSchedule = myConnection.prepareStatement("DELETE FROM schedule_billboard WHERE schedule_billboard.billboard_name = ? AND schedules.start_date = ? AND schedules.start_time = ? INNER JOIN schedules ON schedule_billboard.schedule_ID = schedules.schedule_ID");
        removeSchedule.setString(1, billboardName);
        removeSchedule.setString(2, startDate);
        removeSchedule.setString(3, startTime);

        if(removeSchedule.executeUpdate() > 0){
            serverReply = new Object[2];
            serverReply[0] = true;
            serverReply[1] = 0;
        } else {
            serverReply = RelayError("Could not remove schedule (not found)");
        }
        removeSchedule.close();
        return serverReply;
    }

    private static Object[] ListUsers(Connection myConnection) throws SQLException {
        Object[] userList;
        ArrayList<String> tempArray = new ArrayList<String>();
        int length = 0;

        //Query for all users
        PreparedStatement userListQuery = myConnection.prepareStatement("SELECT username FROM users");
        ResultSet userListSet = userListQuery.executeQuery();

        boolean hasUsers = false;
        while(userListSet.next()){
            hasUsers = true;
            tempArray.add(userListSet.getString(1));
            length++;
        }

        if(hasUsers){
            userList = new Object[length+2];
            userList[0] = true;
            userList[1] = length;
            for(int i = 2; i < length+2; i++){ //Copy across all the usernames to an object array to send to the client
                userList[i] = tempArray.get(i-2);
            }
        } else {
            userList = RelayError("No users found.");
        }
        userListQuery.close();
        return userList;
    }

    private static Object[] CreateUser(String username, Boolean permCreate, Boolean permEditAllBillboards, Boolean permEditUsers, Boolean permSchedule, String password_hash, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Make a salt
        SaltHandler saltHandler = new SaltHandler();
        String newSalt = saltHandler.GetSalt();

        String saltedPassword = newSalt + password_hash;
        String saltedHash = saltHandler.HashString(saltedPassword);

        PreparedStatement createNewUser = myConnection.prepareStatement("INSERT INTO users (username, password_hash, password_salt, perm_create, perm_edit_all_billboards, perm_edit_users, perm_schedule) VALUES (?, ?, ?, ?, ?, ?, ?)");
        createNewUser.setString(1, username);
        createNewUser.setString(2, saltedHash);
        createNewUser.setString(3, newSalt);
        createNewUser.setBoolean(4, permCreate);
        createNewUser.setBoolean(5, permEditAllBillboards);
        createNewUser.setBoolean(6, permEditUsers);
        createNewUser.setBoolean(7, permSchedule);

        if(createNewUser.executeUpdate() > 0){
            serverReply = new Object[2];
            serverReply[0] = true;
            serverReply[1] = 0;
        } else {
            serverReply = RelayError("Could not create user" + username);
        }
        createNewUser.close();
        return serverReply;
    }

    private static Object[] GetUserPermissions(String username, Connection myConnection) throws SQLException {
        Object[] userPermissions;

        //Query DB for user perms
        PreparedStatement userPermQuery = myConnection.prepareStatement("SELECT perm_create, perm_edit_all_billboards, perm_edit_users, perm_schedule FROM users WHERE username = ?");
        userPermQuery.setString(1, username);

        ResultSet perms = userPermQuery.executeQuery();
        if(perms.next()){
            userPermissions = new Object[6];
            userPermissions[0] = true;
            userPermissions[1] = 4;
            for(int i = 2; i < 6; i++){
                userPermissions[i] = perms.getBoolean(i-1);
            }

        } else {
            userPermissions = RelayError("User not found.");
        }

        userPermQuery.close();
        return userPermissions;
    }

    private static Object[] SetUserPermissions(String username, Boolean permCreate, Boolean permEditAllBillboards, Boolean permEditUsers, Boolean permSchedule, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Try to set the perms of an existing user
        PreparedStatement setUserPerms = myConnection.prepareStatement("UPDATE users SET perm_create = ?, perm_edit_all_billboards = ?, perm_edit_users = ?, perm_schedule = ? WHERE username = ?");
        setUserPerms.setBoolean(1, permCreate);
        setUserPerms.setBoolean(2, permEditAllBillboards);
        setUserPerms.setBoolean(3, permEditUsers);
        setUserPerms.setBoolean(4, permSchedule);
        setUserPerms.setString(5, username);

        if(setUserPerms.executeUpdate() > 0){
            serverReply = new Object[2];
            serverReply[0] = true;
            serverReply[1] = 0;
        } else {
            serverReply = RelayError("Could not find or update user permissions.");
        }

        setUserPerms.close();
        return serverReply;
    }

    private static Object[] SetUserPassword(String username, String password, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Make a Salt Hashing instance
        SaltHandler saltHandler = new SaltHandler();

        //Try to change the user's password
        PreparedStatement getServerSalt = myConnection.prepareStatement("SELECT password_salt FROM users WHERE username = ?");
        getServerSalt.setString(1, username);

        PreparedStatement setPassword = myConnection.prepareStatement("UPDATE users SET password_hash WHERE username = ?");

        ResultSet serverSalt = getServerSalt.executeQuery();
        if(serverSalt.next()){
            String serverPasswordSalt = serverSalt.getString(1);
            String saltedPassword = serverPasswordSalt + password;
            String saltedHashedPassword = saltHandler.HashString(saltedPassword);
            if(setPassword.executeUpdate() > 0){
                serverReply = new Object[2];
                serverReply[0] = true;
                serverReply[1] = 0;
            } else {
                serverReply = RelayError("Could not update password.");
            }
        } else {
            serverReply = RelayError("Could not retrieve user information. Check this user exists.");
        }

        getServerSalt.close();
        setPassword.close();

        return serverReply;

    }

    private static Object[] DeleteUser(String username, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Try to delete a user and make the admin the default billboard owner

        PreparedStatement deleteUserLinkingTable = myConnection.prepareStatement("UPDATE user_billboards SET username = ? WHERE username = ?");
        deleteUserLinkingTable.setString(1, InitDatabase.admin_username); //replace with admin as the billboard owner
        deleteUserLinkingTable.setString(2, username);

        PreparedStatement deleteUserRequest = myConnection.prepareStatement("DELETE FROM users WHERE username = ?");

        deleteUserLinkingTable.executeUpdate(); //Does not need to be rolled back (non-harmful command)
        if(deleteUserRequest.executeUpdate() > 0){
            serverReply = new Object[2];
            serverReply[0] = true;
            serverReply[1] = 0;
        } else {
            serverReply = RelayError("Could not find user.");
        }
        deleteUserLinkingTable.close();
        deleteUserRequest.close();
        return serverReply;
    }

    public static Object[] ProcessRequest(Integer function_id, TokenHandler tokenCache, Object[] vars) throws SQLException {
        Object[] dataToSendBack = new Object[]{};
        Connection myConnection = DBConnection.getInstance();

        switch (function_id){
            case 1: //Login request
                dataToSendBack = Login(vars[0].toString(), vars[1].toString(), myConnection, tokenCache);
                break;
            case 2: //  List billboards
                dataToSendBack = ListBillboards(myConnection);
                break;
            case 3: //  Get billboard information
                dataToSendBack = GetBillboardInformation(vars[0].toString(), myConnection);
                break;
            case 4: //  Create/edit billboard
                dataToSendBack = CreateEditBillboard(vars[0].toString(), vars[1].toString(), vars[2].toString(), (byte[])vars[3], vars[4].toString(), vars[5].toString(), vars[6].toString(), vars[7].toString(), myConnection);
                break;
            case 5: //  Delete billboard
                dataToSendBack = DeleteBillboard(vars[0].toString(), myConnection);
                break;
            case 6: //  View schedule
                dataToSendBack = ViewSchedule(myConnection);
                break;
            case 7: //  Schedule billboard
                dataToSendBack = ScheduleBillboard(vars[0].toString(), vars[1].toString(), (Integer)vars[2], (Boolean)vars[3], (Integer)vars[4], vars[5].toString(), vars[6].toString(), myConnection);
                break;
            case 8: //  Remove billboard from schedule
                dataToSendBack = RemoveSchedule(vars[0].toString(), vars[1].toString(), vars[2].toString(), myConnection);
                break;
            case 9: //  List users
                dataToSendBack = ListUsers(myConnection);
                break;
            case 10: //  Create User
                dataToSendBack = CreateUser(vars[0].toString(), (Boolean)vars[1], (Boolean)vars[2], (Boolean)vars[3], (Boolean)vars[4], vars[5].toString(), myConnection);
                break;
            case 11:    //  Get user permissions
                dataToSendBack = GetUserPermissions(vars[0].toString(), myConnection);
                break;
            case 12:   //  Set user permissions
                dataToSendBack = SetUserPermissions(vars[0].toString(), (Boolean)vars[1], (Boolean)vars[2], (Boolean)vars[3], (Boolean)vars[4], myConnection);
                break;
            case 13:    //  Set user password
                dataToSendBack = SetUserPassword(vars[0].toString(), vars[1].toString(), myConnection);
                break;
            case 14:    //  Delete user
                dataToSendBack = DeleteUser(vars[0].toString(), myConnection);
                break;
            /**
             * @see ReceiveSend
             */
            case 15:    //Handled in ReceiveSend class
                //do not need to do anything, it will never enter here
                break;
        }

        //TODO Remove this
        return  dataToSendBack;
    }
}
