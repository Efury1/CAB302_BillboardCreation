import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ReceiveSend {
    public static void ReceiveSend(Socket socket, TokenHandler tokenCache) throws IOException {
        //  Get the connection info
        InputStream input = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(input);
        Integer functionID = objectInputStream.readInt();
        String token = objectInputStream.readUTF();
        Object[] clientData;

        if(functionID == 15){                   // Log Out (only have to remove the session token)
            tokenCache.RemoveToken(token);
            clientData = ProcessRequests.RelayValidResponse();
        }
        else if (functionID == 1)               //The login request (bypass token validation)
        {
            clientData = ReceiveData(objectInputStream, functionID);
            clientData = ReceiveData(objectInputStream, functionID);
            //    for (Object yeeyha:clientData) {
        //        System.out.println("Incoming Client Data: " + yeeyha);
        //    }
        }
        else                                    //Every other request
        {
            if (tokenCache.ValidateToken(token)) //Validate their token
            {
                clientData = ReceiveData(objectInputStream, functionID);
                try {
                    ProcessRequests.ProcessRequest(functionID, tokenCache, clientData);
                } catch (SQLException e) {
                    clientData = ProcessRequests.RelayError("Invalid database action.");
                }
            }
            else
            {
                clientData = ProcessRequests.RelayError("Invalid session token.");
            }
        }

        //System.out.println("Read byte " + myInt);
        System.out.println("Request ID: " + functionID);
        System.out.println("Token string: " + token);


        //Send a reply (based on info)
        OutputStream output = socket.getOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(output);
        objectOutput.writeInt(functionID * 10);
        objectOutput.writeUTF("Received request ID: " + functionID + " and token: " + token);
        objectOutput.flush();
    }

    public static Object[] ReceiveData(ObjectInputStream objectInputStream, Integer functionID) throws IOException {
        Integer length = GetDataLength(functionID);
        Object[] incomingData = new Object[length];
        switch (functionID)
        {
            case 1: //  Login request
            case 13:    //  Set user password
                incomingData[0] = objectInputStream.readUTF();    //  username
                incomingData[1] = objectInputStream.readUTF();    //  hashed password
                break;
            case 2: //  List billboards
            case 6: //  View schedule
            case 9: //  List users
            case 15:    //  Log out (aka delete session token) (handled earlier)
                //  nothing required
                break;
            case 3: //  Get billboard information
            case 5: //  Delete billboard
                incomingData[0] = objectInputStream.readUTF();    //  billboard name
                break;
            case 4: //  Create/edit billboard
                incomingData[0] = objectInputStream.readUTF();    //  billboard name
                incomingData[1] = objectInputStream.readUTF();    //  title
                incomingData[2] = objectInputStream.readUTF();    //  description
                incomingData[3] = objectInputStream.read();   //  picture data
                incomingData[4] = objectInputStream.readUTF();    //  background colour
                incomingData[5] = objectInputStream.readUTF();    //  title colour
                incomingData[6] = objectInputStream.readUTF();    //  description colour
                incomingData[7] = objectInputStream.readUTF();     // creator username
                break;
            case 7: //  Schedule billboard
                incomingData[0] = objectInputStream.readUTF();    //  billboard name
                incomingData[1] = objectInputStream.readUTF();    //  start time
                incomingData[2] = objectInputStream.readInt();   //  duration
                incomingData[3] = objectInputStream.readBoolean();    //  repeat
                incomingData[4] = objectInputStream.readInt();    //  repeat freq
                incomingData[5] = objectInputStream.readUTF();    //  start date
                incomingData[6] = objectInputStream.readUTF();    //  end date
                break;
            case 8: //  Remove billboard from schedule
                incomingData[0] = objectInputStream.readUTF();  //  billboard name
                incomingData[1] = objectInputStream.readUTF();  //  start date
                incomingData[2] = objectInputStream.readUTF();  //  start time
                break;
            case 10:    //  Create User
                incomingData[0] = objectInputStream.readUTF();  //  username
                incomingData[1] = objectInputStream.readBoolean();    //  permission 1 (perm_create)
                incomingData[2] = objectInputStream.readBoolean();    //  permission 2 (perm_edit_all_billboards billboards)
                incomingData[3] = objectInputStream.readBoolean();    //  permission 3 (perm_edit_users)
                incomingData[4] = objectInputStream.readBoolean();    //  permission 4 (perm_schedule)
                incomingData[5] = objectInputStream.readUTF();  //  hashed password
                break;
            case 11:    //  Get user permissions
            case 14:    //  Delete user
                incomingData[0] = objectInputStream.readUTF();  //  username
                break;
            case 12:   //  Set user permissions
                incomingData[0] = objectInputStream.readUTF();    //  username
                incomingData[1] = objectInputStream.readBoolean();    //  permission 1 (perm_create)
                incomingData[2] = objectInputStream.readBoolean();    //  permission 2 (perm_edit_all_billboards billboards)
                incomingData[3] = objectInputStream.readBoolean();    //  permission 3 (perm_edit_users)
                incomingData[4] = objectInputStream.readBoolean();    //  permission 4 (perm_schedule)
                break;
            default:
                break;
        }
        return incomingData;
    }

    /**
     * Get the length of the data to be sent to the server. Based on function ID standards.
     * @param functionID
     * @return
     */
    private static Integer GetDataLength(Integer functionID)
    {
        Integer length = 0;
        switch (functionID)
        {
            case 1:
            case 13:
                length = 2;
                break;
            case 2:
            case 6:
            case 9:
                length = 0;
                break;
            case 3:
            case 5:
            case 15:
            case 11:
            case 14:
                length = 1;
                break;
            case 4:
                length = 8;
                break;
            case 7:
                length = 7;
                break;
            case 8:
                length = 3;
                break;
            case 10:
                length = 6;
                break;
            case 12:
                length = 5;
                break;
            default:
                break;
        }
        return length;
    }
}
