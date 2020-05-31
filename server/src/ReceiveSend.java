import java.io.*;
import java.net.Socket;
import java.sql.Blob;
import java.sql.SQLException;

public class ReceiveSend {
    public static void ReceiveSend(Socket socket, TokenHandler tokenCache) throws IOException {
        //  Get the connection info
        InputStream input = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(input);

        // TODO
        while (objectInputStream.available() == 0)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Integer functionID = objectInputStream.readInt();
        String token = objectInputStream.readUTF();
        Object[] clientData;
        Object[] replyData;

        if(functionID == 15){                   // Log Out (only have to remove the session token)
            tokenCache.RemoveToken(token);
            replyData = ProcessRequests.RelayValidResponse();
        }
        else if (functionID == 1)               //The login request (bypass token validation)
        {
            clientData = ReceiveData(objectInputStream, functionID);
            try {
                replyData = ProcessRequests.ProcessRequest(functionID, tokenCache, clientData);
            } catch (SQLException e) {
                replyData = ProcessRequests.RelayError("Invalid database action.");
                System.err.println(e);
            }
        }
        else                                    //Every other request
        {
            if (tokenCache.ValidateToken(token)) //Validate their token
            {
                clientData = ReceiveData(objectInputStream, functionID);
                try {
                    replyData = ProcessRequests.ProcessRequest(functionID, tokenCache, clientData);
                } catch (SQLException e) {
                    replyData = ProcessRequests.RelayError("Invalid database action: ");
                    System.err.println(e);
                }
            }
            else
            {
                replyData = ProcessRequests.RelayError("Invalid session token.");
            }
        }

        //  TODO remove this debug code
        System.out.println("Request ID: " + functionID);
        System.out.println("Token string: " + token);
        System.out.println("Reply: ");
        for (Object yeehaw:replyData)
        {
            System.out.println(yeehaw.toString());
        }
        System.out.println("=========================================================");

        //Send a reply (based on info)
        OutputStream output = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
        SendData(objectOutputStream, functionID, replyData);
        objectOutputStream.flush();
        objectInputStream.close();
        objectOutputStream.close();
    }

    public static Object[] ReceiveData(ObjectInputStream objectInputStream, Integer functionID) throws IOException {
        Integer length = GetDataLength(functionID);
        Object[] incomingData = new Object[length];
        switch (functionID)
        {
            case 1:     //  Login request
            case 13:    //  Set user password
                incomingData[0] = objectInputStream.readUTF();  //  username
                incomingData[1] = objectInputStream.readUTF();  //  hashed password
                break;
            case 2: //  List billboards
            case 6: //  View schedule
            case 9: //  List users
            case 15:    //  Log out (aka delete session token) (handled earlier)
                //  nothing required
                break;
            case 3: //  Get billboard information
            case 5: //  Delete billboard
                incomingData[0] = objectInputStream.readUTF();  //  billboard name
                break;
            case 4: //  Create/edit billboard
                incomingData[0] = objectInputStream.readUTF();  //  billboard name
                incomingData[1] = objectInputStream.readUTF();  //  title
                incomingData[2] = objectInputStream.readUTF();  //  description
                incomingData[3] = objectInputStream.read();     //  picture data
                incomingData[4] = objectInputStream.readUTF();  //  background colour
                incomingData[5] = objectInputStream.readUTF();  //  title colour
                incomingData[6] = objectInputStream.readUTF();  //  description colour
                incomingData[7] = objectInputStream.readUTF();  // creator username
                break;
            case 7: //  Schedule billboard
                incomingData[0] = objectInputStream.readUTF();      //  billboard name
                incomingData[1] = objectInputStream.readUTF();      //  start time
                incomingData[2] = objectInputStream.readInt();      //  duration
                incomingData[3] = objectInputStream.readBoolean();  //  repeat
                incomingData[4] = objectInputStream.readInt();      //  repeat freq
                incomingData[5] = objectInputStream.readUTF();      //  start date
                incomingData[6] = objectInputStream.readUTF();      //  end date
                break;
            case 8: //  Remove billboard from schedule
                incomingData[0] = objectInputStream.readUTF();  //  billboard name
                incomingData[1] = objectInputStream.readUTF();  //  start date
                incomingData[2] = objectInputStream.readUTF();  //  start time
                break;
            case 10:    //  Create user
                incomingData[0] = objectInputStream.readUTF();      //  username
                incomingData[1] = objectInputStream.readBoolean();  //  permission 1 (perm_create)
                incomingData[2] = objectInputStream.readBoolean();  //  permission 2 (perm_edit_all_billboards billboards)
                incomingData[3] = objectInputStream.readBoolean();  //  permission 3 (perm_edit_users)
                incomingData[4] = objectInputStream.readBoolean();  //  permission 4 (perm_schedule)
                incomingData[5] = objectInputStream.readUTF();      //  hashed password
                break;
            case 11:    //  Get user permissions
            case 14:    //  Delete user
                incomingData[0] = objectInputStream.readUTF();  //  username
                break;
            case 12:   //  Set user permissions
                incomingData[0] = objectInputStream.readUTF();      //  username
                incomingData[1] = objectInputStream.readBoolean();  //  permission 1 (perm_create)
                incomingData[2] = objectInputStream.readBoolean();  //  permission 2 (perm_edit_all_billboards billboards)
                incomingData[3] = objectInputStream.readBoolean();  //  permission 3 (perm_edit_users)
                incomingData[4] = objectInputStream.readBoolean();  //  permission 4 (perm_schedule)
                break;
            default:
                break;
        }
        return incomingData;
    }

    private static void SendData(ObjectOutputStream objectOutputStream, Integer functionID, Object[] packedDataToSend) throws IOException {
        Boolean validReply = (Boolean)packedDataToSend[0];
        Integer length = (Integer)packedDataToSend[1];

        //  Write these to the stream (included in every case)
        objectOutputStream.writeBoolean(validReply);
        objectOutputStream.writeInt(length);

        //  Check if the packed data is an "error array"
        if (!validReply)
        {
            //  Write the error message to the output stream
            objectOutputStream.writeUTF((String)packedDataToSend[2]);
            return;
        }

        switch (functionID)
        {
            case 1: //  Login request
                objectOutputStream.writeUTF((String)packedDataToSend[2]);   //  token
                break;
            case 2: //  List billboards
                for (int i = 2; i < length + 2; i+=2)
                {
                    objectOutputStream.writeUTF((String)packedDataToSend[i]); //  billboard name
                    objectOutputStream.writeUTF((String)packedDataToSend[i+1]); //  creator name
                }
                break;
            case 3: //  Get billboard information
                objectOutputStream.writeUTF((String)packedDataToSend[2]);   //  title
                objectOutputStream.writeUTF((String)packedDataToSend[3]);   //  description
                objectOutputStream.writeObject((Blob)packedDataToSend[4]);  //  picture
                objectOutputStream.writeUTF((String)packedDataToSend[5]);   //  background colour
                objectOutputStream.writeUTF((String)packedDataToSend[6]);   //  title colour
                objectOutputStream.writeUTF((String)packedDataToSend[7]);   //  description colour
                break;
            case 4: //  Create/edit billboard
            case 5: //  Delete billboard
            case 7: //  Schedule billboard
            case 8: //  Remove billboard from schedule
            case 10:    //  Create user
            case 12:    //  Set user permissions
            case 13:    //  Set user password
            case 14:    //  Delete user
            case 15:    //  Log out (aka delete session token)
                //  nothing required
                break;
            case 6: //  View schedule
                for (int i = 2; i < length + 2; i+=9)
                {
                    objectOutputStream.writeUTF((String)packedDataToSend[i]);       //  billboard name
                    objectOutputStream.writeUTF((String)packedDataToSend[i+1]);         //  creator name
                    objectOutputStream.writeUTF((String)packedDataToSend[i+2]);         //  time stamp
                    objectOutputStream.writeUTF((String)packedDataToSend[i+3]);         //  start date
                    objectOutputStream.writeUTF((String)packedDataToSend[i+4]);         //  end date
                    objectOutputStream.writeUTF((String)packedDataToSend[i+5]);         //  time start
                    objectOutputStream.writeInt((Integer)packedDataToSend[i+6]);        //  duration
                    objectOutputStream.writeBoolean((Boolean)packedDataToSend[i+7]);    //  repeats
                    objectOutputStream.writeInt((Integer)packedDataToSend[i+8]);        //  repeat frequency
                }
                break;
            case 9: //  List users
                for (int i = 2; i < length + 2; i++)
                {
                    objectOutputStream.writeUTF((String)packedDataToSend[i]);  //  username (eg. "user1", "user2", "user3")
                }
                break;
            case 11:    //  Get user permissions
                //  TODO can remove but kept just in case
                // receivedData[0] = true; //
                objectOutputStream.writeBoolean((Boolean)packedDataToSend[2]);  //  permission 1 (perm_create)
                objectOutputStream.writeBoolean((Boolean)packedDataToSend[3]);  //  permission 2 (perm_edit_all_billboards billboards)
                objectOutputStream.writeBoolean((Boolean)packedDataToSend[4]);  //  permission 3 (perm_edit_users)
                objectOutputStream.writeBoolean((Boolean)packedDataToSend[5]);  //  permission 4 (perm_schedule)
                break;
            default:
                break;
        }
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
