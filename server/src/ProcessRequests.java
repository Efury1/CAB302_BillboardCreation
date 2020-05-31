import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;

public class ProcessRequests {
    /**
     * Public function to help choose methods within this class. Options are specified with funcion_id.
     * @param function_id ID of the function (from 1-15) which determines how the input data is processed.
     * @param tokenCache Session tokens stored on the server.
     * @param vars Data received from the client to be processed.
     * @return An array of relevant data according to the function_id used.
     * @throws SQLException
     */
    public static Object[] ProcessRequest(Integer function_id, TokenHandler tokenCache, Object[] vars) throws SQLException {
        Object[] dataToSendBack = new Object[]{};
        Connection myConnection = DBConnection.getInstance();

        switch (function_id){
            case 1: //  Login request
                dataToSendBack = Login(vars[0].toString(), vars[1].toString(), myConnection, tokenCache);
                break;
            case 2: //  List billboards
                dataToSendBack = ListBillboards(myConnection);
                break;
            case 3: //  Get billboard information
                dataToSendBack = GetBillboardInfo(vars[0].toString(), myConnection);
                break;
            case 4: //  Create/edit billboard
                dataToSendBack = CreateEditBillboard(vars[0].toString(), vars[1].toString(), vars[2].toString(), (Blob)vars[3], vars[4].toString(), vars[5].toString(), vars[6].toString(), vars[7].toString(), myConnection);
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

        return dataToSendBack;
    }   //  end ProcessRequest


    /**
     * Checks if the username+password match a record on the database. Returns a token and creates a
     * session stored on the server.
     * @param user Input username
     * @param pass Input password
     * @param myConnection A connection/session with a specific database.
     * @param tokenCache Array of tokens
     * @return A token if the login is valid
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
    private static Object[] Login(String user, String pass, Connection myConnection, TokenHandler tokenCache) throws SQLException {
        Object[] loginStatus = new Object[3];
        loginStatus[1] = 1; //length is always 1 (for consequent data)
        //process Username and password from database

        PreparedStatement getUserQuery = myConnection.prepareStatement("SELECT password_hash, password_salt FROM users WHERE username = ?");
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
        if(clientSaltedHash.compareTo(serverPasswordHash) == 0){
            loginStatus[0] = true;
            //Return a valid token
            SaltHandler tokenGen = new SaltHandler();
            String token = tokenGen.GetSalt();  //Add token to valid token cache
            tokenCache.AddToken(token);
            loginStatus[2] = token;             //To send back to client
        } else {
            loginStatus = RelayError("The username or password is invalid.");
        }
        getUserQuery.close();
        return loginStatus;
    }
    

    /**
     * Queries a database and gets the names of all billboards and their creators.
     * @param myConnection A connection/session with a specific database.
     * @return An object array of billboard names and their creators
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
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
            return billboardData;
        }


        //Read in the data from the temp array to the main object array (to send the list of billboards back to the client)
        for (int i = 2; i < billboardData.length; i += 2) {
            billboardData[i] = temp_array.get(i-2);
            billboardData[i+1] = temp_array.get(i-1);
        }
        billboardQuery.close();
        return billboardData;
    }


    /**
     * Searches over a database for a billboard and gives back its details.
     * @param billboard_name Name of the queried billboard.
     * @param myConnection A connection/session with a specific database.
     * @return A billboard's components in an object array
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
    private static Object[] GetBillboardInfo(String billboard_name, Connection myConnection) throws SQLException {
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
            billboardData[4] = billboardInfo.getBlob(3);   //Picture
            billboardData[5] = billboardInfo.getString(4);  //Background colour
            billboardData[6] = billboardInfo.getString(5);  //Title colour
            billboardData[7] = billboardInfo.getString(6);  //Description colour

        } else {
            billboardData = RelayError("Billboard not found.");
        }
        billboardQuery.close();
        return billboardData;
    }


    /**
     * Creates a new billboard in the database OR updates the details of an existing billboard.
     * @param billboardName Name of the queried/new billboard.
     * @param title Title of the queried/new billboard.
     * @param description Description of the queried/new billboard.
     * @param picture Picture of the queried/new billboard.
     * @param backgroundColour Background colour of the queried/new billboard.
     * @param titleColour Title color of the queried/new billboard.
     * @param descriptionColour Description colour of the queried/new billboard.
     * @param creatorUsername Creator username of the queried/new billboard.
     * @param myConnection A connection/session with a specific database.
     * @return An object array of the successful transaction of creating/editing a billboard
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
    private static Object[] CreateEditBillboard(String billboardName, String title, String description, Blob picture, String backgroundColour, String titleColour, String descriptionColour, String creatorUsername, Connection myConnection) throws SQLException {
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
                updateBillboard.setBlob(3, picture);
                updateBillboard.setString(4, backgroundColour);
                updateBillboard.setString(5, titleColour);
                updateBillboard.setString(6, descriptionColour);
                updateBillboard.setString(7, billboardName);

                if(updateBillboard.executeUpdate() > 0){
                    serverReply = RelayValidResponse();
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
                createNewBillboard.setBlob(4, picture);
                createNewBillboard.setString(5, backgroundColour);
                createNewBillboard.setString(6, titleColour);
                createNewBillboard.setString(7, descriptionColour);

                PreparedStatement createUserBillboardEntry = myConnection.prepareStatement("INSERT INTO user_billboards (billboard_name, username) VALUES (?, ?)");
                createUserBillboardEntry.setString(1, billboardName);
                createUserBillboardEntry.setString(2, creatorUsername);

                //Only commit to database if both queries work
                if(createNewBillboard.executeUpdate() > 0){
                    if(createUserBillboardEntry.executeUpdate() > 0){
                        serverReply = RelayValidResponse();
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


    /**
     * Removes the billboard from a database with the given billboardName.
     * @param billboardName Name of the billboard to be removed.
     * @param myConnection A connection/session with a specific database.
     * @return A confirmation of deletion in an object array
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
    private static Object[] DeleteBillboard(String billboardName, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Prepare the statement for the database
        Statement commit = myConnection.createStatement();
        commit.execute("BEGIN WORK");

        //Make SQL statement to delete a billboard
        PreparedStatement deleteUserBillboard = myConnection.prepareStatement("DELETE FROM user_billboards WHERE billboard_name = ?");
        deleteUserBillboard.setString(1, billboardName);
        PreparedStatement deleteBillboard = myConnection.prepareStatement("DELETE FROM billboards WHERE billboard_name = ?");
        deleteBillboard.setString(1, billboardName);

        if(deleteUserBillboard.executeUpdate() > 0){
            if (deleteBillboard.executeUpdate() > 0)
            {
                commit.execute("COMMIT");
                serverReply = RelayValidResponse();
            }
            else
            {
                commit.execute("ROLLBACK");
                serverReply = RelayError("Failed to delete billboard.");
            }
        } else
        {
            serverReply = RelayError("Could not find the billboard to delete.");
        }
        deleteBillboard.close();
        return serverReply;
    }


    /**
     * Sends a request to a database and gets back all schedule data for all scheduled billboards.
     * @param myConnection A connection/session with a specific database.
     * @return Every scheduled billboard's details
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
    private static Object[] ViewSchedule(Connection myConnection) throws SQLException {
        Object[] scheduleData;
        ArrayList<Object> tempArray = new ArrayList<Object>();
        int length = 0;

        //Query database for the schedule of all billboards
        PreparedStatement billboardScheduleList = myConnection.prepareStatement("SELECT billboard_schedule.billboard_name, schedule_ts, start_date, end_date, start_time, duration, repeats, repeat_frequency FROM schedules INNER JOIN billboard_schedule ON schedules.schedule_ID = billboard_schedule.schedule_ID");
        ResultSet scheduledBillboards = billboardScheduleList.executeQuery();
        billboardScheduleList.close();

        if(scheduledBillboards.next()){
            do {
                tempArray.add(scheduledBillboards.getString(1));  //Billboard name
                tempArray.add(scheduledBillboards.getString(2));  //Last updated timestamp
                tempArray.add(scheduledBillboards.getString(3));  //Start date
                tempArray.add(scheduledBillboards.getString(4));  //End date
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

        for(int i = 2; i < length; i++){
            scheduleData[i] = tempArray.get(i-2);
        }

        return scheduleData;
    }


    /**
     * Schedules the billboard to display at that time and returns an acknowledgement.
     * @param billboardName Name of the billboard to be scheduled.
     * @param startTime Start time of the schedule. (eg. HH:MM:SS)
     * @param duration Duration (in minutes) the billboard is to be scheduled for.
     * @param repeats Boolean: Whether the schedule repeats or not
     * @param repeatFrequency The frequency (in minutes) that the schedule repeats.
     * @param startDate Start date of the schedule (eg. YYYY-MM-DD)
     * @param endDate End date of the schedule (eg. YYYY-MM-DD)
     * @param myConnection A connection/session with a specific database.
     * @return An object array of the successful transaction of scheduling a billboard
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
    private static Object[] ScheduleBillboard(String billboardName, String startTime, Integer duration, Boolean repeats, Integer repeatFrequency, String startDate, String endDate, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Prepare the statement for the database
        Statement commit = myConnection.createStatement();
        commit.execute("BEGIN WORK");

        PreparedStatement scheduleCreate = myConnection.prepareStatement("INSERT INTO schedules (start_date, end_date, start_time, duration, repeats, repeat_frequency) VALUES (?, ?, ?, ?, ?, ?)");
        scheduleCreate.setString(1, startDate);
        scheduleCreate.setString(2, endDate);
        scheduleCreate.setString(3, startTime);
        scheduleCreate.setInt(4, duration);
        scheduleCreate.setBoolean(5, repeats);
        scheduleCreate.setInt(6, repeatFrequency);

        //Find the scheduleID for the latest entry
        PreparedStatement scheduleGet = myConnection.prepareStatement("SELECT schedule_ID FROM schedules WHERE schedule_ts = (SELECT MAX(schedule_ts) FROM schedules)");
        PreparedStatement scheduleAssignToBillboard = myConnection.prepareStatement("INSERT INTO billboard_schedule (schedule_ID, billboard_name) VALUES (?, ?)");

        if(scheduleCreate.executeUpdate() > 0) {
            //Get the scheduleID of the new Schedule
            ResultSet recentSchedule = scheduleGet.executeQuery();
            recentSchedule.next();
            int scheduleID = recentSchedule.getInt(1);

            scheduleAssignToBillboard.setInt(1, scheduleID);
            scheduleAssignToBillboard.setString(2, billboardName);

            if(scheduleAssignToBillboard.executeUpdate() > 0){
                commit.execute("COMMIT");
                serverReply = RelayValidResponse();
            } else {
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


    /**
     * Removes the billboard from a given schedule.
     * @param billboardName Name of the billboard that's scheduled.
     * @param startDate Start date of the billboard to be removed. (eg. YYYY-MM-DD)
     * @param startTime Start time of the billboard to be removed. (eg. HH:MM:SS)
     * @param myConnection A connection/session with a specific database.
     * @return An object array of the successful transaction of removing the schedule of a billboard.
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
    private static Object[] RemoveSchedule(String billboardName, String startDate, String startTime, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Prepare the query to find the schedule and delete it
        PreparedStatement removeSchedule = myConnection.prepareStatement("DELETE FROM schedule_billboard WHERE schedule_billboard.billboard_name = ? AND schedules.start_date = ? AND schedules.start_time = ? INNER JOIN schedules ON schedule_billboard.schedule_ID = schedules.schedule_ID");
        removeSchedule.setString(1, billboardName);
        removeSchedule.setString(2, startDate);
        removeSchedule.setString(3, startTime);

        if(removeSchedule.executeUpdate() > 0){
            serverReply = RelayValidResponse();
        } else {
            serverReply = RelayError("Could not remove schedule (not found)");
        }
        removeSchedule.close();
        return serverReply;
    }


    /**
     * Returns all the usernames from a database.
     * @param myConnection A connection/session with a specific database.
     * @return Object array of all usernames
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
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
            for(int i = 2; i < length; i++){ //Copy across all the usernames to an object array to send to the client
                userList[i] = tempArray.get(i-2);
            }
        } else {
            userList = RelayError("No users found.");
        }
        userListQuery.close();
        return userList;
    }


    /**
     * Creates a user with the input parameters in the database.
     * @param username The username of the created user.
     * @param permCreate Boolean: Permission to create billboards.
     * @param permEditAllBillboards Boolean: Permission to edit all billboards.
     * @param permEditUsers Boolean: Permission to edit all users.
     * @param permSchedule Boolean: Permission to schedule billboards
     * @param password_hash Prehashed password of the user.
     * @param myConnection A connection/session with a specific database.
     * @return Returns an acknowledgement of the successful transaction
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
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
            serverReply = RelayValidResponse();
        } else {
            serverReply = RelayError("Could not create user" + username);
        }
        createNewUser.close();
        return serverReply;
    }
    

    /**
     * Retrieves the permissions of the specified user from a database.
     * @param username The user query.
     * @param myConnection A connection/session with a specific database.
     * @return An array of the queried user's permissions
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
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


    /**
     * Sets the queried user's permissions to the input parameters.
     * @param username The username of th queried user to set permissions.
     * @param permCreate Boolean: Permission to create billboards.
     * @param permEditAllBillboards Boolean: Permission to edit all billboards.
     * @param permEditUsers Boolean: Permission to edit all users.
     * @param permSchedule Boolean: Permission to schedule billboards
     * @param myConnection A connection/session with a specific database.
     * @return An acknowledgement of the successful transaction in the database
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
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
            serverReply = RelayValidResponse();
        } else {
            serverReply = RelayError("Could not find or update user permissions.");
        }

        setUserPerms.close();
        return serverReply;
    }


    /**
     * Stores the user's password in a database securely.
     * @param username Username of the user to set the password.
     * @param password Specified password from the user.
     * @param myConnection A connection/session with a specific database.
     * @return 
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
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


    /**
     * Deletes the specified user from the database and transfers any owned billboards to the admin
     * @param username Username of the user to be deleted from a database.
     * @param myConnection A connection/session with a specific database.
     * @return An acknowledgement of the successful transaction on the database.
     * OR an array object to handle errors from the server easily.
     * @throws SQLException
     */
    private static Object[] DeleteUser(String username, Connection myConnection) throws SQLException {
        Object[] serverReply;

        //Try to delete a user and make the admin the default billboard owner

        PreparedStatement deleteUserLinkingTable = myConnection.prepareStatement("UPDATE user_billboards SET username = ? WHERE username = ?");
        deleteUserLinkingTable.setString(1, InitDatabase.admin_username); //replace with admin as the billboard owner
        deleteUserLinkingTable.setString(2, username);

        PreparedStatement deleteUserRequest = myConnection.prepareStatement("DELETE FROM users WHERE username = ?");

        deleteUserLinkingTable.executeUpdate(); //Does not need to be rolled back (non-harmful command)
        if(deleteUserRequest.executeUpdate() > 0){
            serverReply = RelayValidResponse();
        } else {
            serverReply = RelayError("Could not find user.");
        }
        deleteUserLinkingTable.close();
        deleteUserRequest.close();
        return serverReply;
    }
















    //  Helper functions:   ///////////////////////


    /**
     * Creates and array object to handle a successful transaction.
     * @return An array in the format: acknowledgement, length.
     */
    public static Object[] RelayValidResponse()
    {
        Object[] validArray = new Object[2];
        validArray[0] = true;               //  acknowledgement
        validArray[1] = 0;                  //  length
        return validArray;
    }


    /**
     * Creates an array object to handle errors from the server easily.
     * @param errorMessage Error message to store in the error array.
     * @return An array in the format: acknowledgement, length, errorMessage.
     */
    public static Object[] RelayError(String errorMessage)
    {
        Object[] errorArray = new Object[3];
        errorArray[0] = false;         //  False (acknowledgement of action failed)
        errorArray[1] = 1;             //  length
        errorArray[2] = errorMessage;  //  Error message
        return errorArray;
    }
}
