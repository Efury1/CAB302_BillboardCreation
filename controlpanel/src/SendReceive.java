import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;


public class SendReceive {
    /**
     * Sends all the data and receives all the data
     * @param functionID
     * @param token A session token
     * @param dataToSend
     * @return  The data that it gets from the server
     * @throws IOException
     */
    public static Object[] SendReceive(Integer functionID, String token, Object[] dataToSend) throws IOException, ClassNotFoundException {
        //  Prepare to connect..
        Socket mySocket;
        try {
            mySocket = SocketConnect();
        } catch (IOException e) {
            System.err.println(e);
            throw e;
        }

        //  Prepare to send..
        OutputStream outputStream = mySocket.getOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
        objectOutput.writeInt(functionID);
        objectOutput.writeUTF(token);

        SendData(objectOutput, functionID, dataToSend);

//        for (Object data:dataToSend) {
//            objectOutput.writeUTF((data.toString()));
//        }
        objectOutput.flush();

        //  prepare to receive..
        InputStream inputStream = mySocket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return ReceiveData(objectInputStream, functionID);
    }

    /**
     * Hashes an input password with SHA-256
     * @param password
     * @return
     */
    private static String HashPassword(String password) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            //  do nothing (this will never be caught)
        }
        byte[] hashedPassword = messageDigest.digest(password.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b: hashedPassword)
        {
            //  "%1x" formats it as a hashed string without the spaces included (could also use "%02x")
            stringBuffer.append(String.format("%1x", b & 255));
        }
        return stringBuffer.toString();
    }

    /**
     * A collection of all the different "options" that a client can send to the server.
     * @param objectOutputStream
     * @param functionID
     * @param dataToSend
     * @throws IOException
     */
    private static void SendData(ObjectOutputStream objectOutputStream, int functionID, Object[] dataToSend) throws IOException {
        //  List of things the client sends to the server (comes after the functionID and token)
        switch (functionID)
        {
            case 1: //  Login request
            case 13:    //  Set user password
                objectOutputStream.writeUTF((String)dataToSend[0]);   //  username
                objectOutputStream.writeUTF(HashPassword((String)dataToSend[1])); //  hashed password
                break;
            case 2: //  List billboards
            case 6: //  View schedule
            case 9: //  List users
            case 15:    //  Log out (aka delete session token)
                //  nothing required
                break;
            case 3: //  Get billboard information
            case 5: //  Delete billboard
                objectOutputStream.writeUTF((String)dataToSend[0]);   //  billboard name
                break;
            case 4: //  Create/edit billboard
                objectOutputStream.writeUTF((String)dataToSend[0]);   //  billboard name
                objectOutputStream.writeUTF((String)dataToSend[1]);   //  title
                objectOutputStream.writeUTF((String)dataToSend[2]);   //  description
                //if ((Blob)dataToSend[3] == null) {
                //    dataToSend[3] = "";
                //}
                objectOutputStream.writeObject((Blob)dataToSend[3]);  //  picture data
                objectOutputStream.writeUTF((String)dataToSend[4]);   //  background colour
                objectOutputStream.writeUTF((String)dataToSend[5]);   //  title colour
                objectOutputStream.writeUTF((String)dataToSend[6]);   //  description colour
                objectOutputStream.writeUTF((String)dataToSend[7]);   //  creator username
                break;
            case 7: //  Schedule billboard
                objectOutputStream.writeUTF((String)dataToSend[0]);   //  billboard name
                objectOutputStream.writeUTF((String)dataToSend[1]);   //  start time
                objectOutputStream.writeInt((Integer)dataToSend[2]);  //  duration
                objectOutputStream.writeBoolean((Boolean)dataToSend[3]);  //  repeat
                objectOutputStream.writeInt((Integer)dataToSend[4]);  //  repeat freq
                objectOutputStream.writeUTF((String)dataToSend[5]);   //  start date
                objectOutputStream.writeUTF((String)dataToSend[6]);   //  end date
                break;
            case 8: //  Remove billboard from schedule
                objectOutputStream.writeUTF((String)dataToSend[0]);   //  billboard name
                objectOutputStream.writeUTF((String)dataToSend[1]);   //  start date
                objectOutputStream.writeUTF((String)dataToSend[2]);   //  start time
                break;
            case 10:    //  Create user
                objectOutputStream.writeUTF((String)dataToSend[0]);   //  username
                objectOutputStream.writeBoolean((Boolean)dataToSend[1]);  //  permission 1 (perm_create)
                objectOutputStream.writeBoolean((Boolean)dataToSend[2]);  //  permission 2 (perm_edit_all_billboards billboards)
                objectOutputStream.writeBoolean((Boolean)dataToSend[3]);  //  permission 3 (perm_edit_users)
                objectOutputStream.writeBoolean((Boolean)dataToSend[4]);  //  permission 4 (perm_schedule)
                objectOutputStream.writeUTF(HashPassword((String)dataToSend[5])); //  hashed password
                break;
            case 11:    //  Get user permissions
            case 14:    //  Delete user
                objectOutputStream.writeUTF(dataToSend[0].toString());   //  username
                break;
            case 12:   //  Set user permissions
                objectOutputStream.writeUTF((String)dataToSend[0]);   //  username
                objectOutputStream.writeBoolean((Boolean)dataToSend[1]);   //  permission 1 (perm_create)
                objectOutputStream.writeBoolean((Boolean)dataToSend[2]);   //  permission 2 (perm_edit_all_billboards billboards)
                objectOutputStream.writeBoolean((Boolean)dataToSend[3]);   //  permission 3 (perm_edit_users)
                objectOutputStream.writeBoolean((Boolean)dataToSend[4]);   //  permission 4 (perm_schedule)
                break;
            default:
                break;
        }
    }

    /**
     * A collection of all the different "options" that a client can receive from the server.
     * @param objectInputStream
     * @param functionID
     * @return
     */
    public static Object[] ReceiveData(ObjectInputStream objectInputStream, Integer functionID) throws IOException, ClassNotFoundException {
        while (objectInputStream.available() == 0)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Boolean reply = objectInputStream.readBoolean();
        Integer length = objectInputStream.readInt();
        if (!reply)
        {
            if (length == 0)
            {
                functionID = -1;
                throw new IOException("Unexpected error occurred.");
            }
            else
            {
                //  Return with error message
                throw new IOException(objectInputStream.readUTF());
                //  The Format of the error that's sent from the server:
                //  array[] = {reply(bool), length(int), errorMessage(String)}
            }
        }
        Object[] receivedData = new Object[length]; //  This must be run after the check (in case length == 0)
        switch (functionID)
        {
            case 1: //  Login request
                receivedData[0] = objectInputStream.readUTF();  //  token
                break;
            case 2: //  List billboards
                for (int i = 0; i < length; i+=2)
                {
                    receivedData[i] = objectInputStream.readUTF();  //  billboard name
                    receivedData[i+1] = objectInputStream.readUTF();    //  creator name
                }
                break;
            case 3: //  Get billboard information
                receivedData[0] = objectInputStream.readUTF();  //  title
                receivedData[1] = objectInputStream.readUTF();  //  description
                receivedData[2] = objectInputStream.readObject();  //  picture
                receivedData[3] = objectInputStream.readUTF();  //  background colour
                receivedData[4] = objectInputStream.readUTF();  //  title colour
                receivedData[5] = objectInputStream.readUTF();  //  description colour
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
                return new Object[] {true}; //  Creation/Edit/Delete/Schedule successful
            case 6: //  View schedule
                for (int i = 0; i < length; i+=9)
                {
                    receivedData[i] = objectInputStream.readUTF();  //  billboard name
                    receivedData[i+1] = objectInputStream.readUTF();    //  creator name
                    receivedData[i+2] = objectInputStream.readUTF();    //  time stamp
                    receivedData[i+3] = objectInputStream.readUTF();    //  start date
                    receivedData[i+4] = objectInputStream.readUTF();    //  end date
                    receivedData[i+5] = objectInputStream.readUTF();    //  time start
                    receivedData[i+6] = objectInputStream.readInt();    //  duration
                    receivedData[i+7] = objectInputStream.readBoolean();    //  repeats
                    receivedData[i+8] = objectInputStream.readInt();    //  repeat frequency
                }
                break;
            case 9: //  List users
                for (int i = 0; i < length; i++)
                {
                    receivedData[i] = objectInputStream.readUTF();  //  username (eg. "user1", "user2", "user3")
                }
                break;
            case 11:    //  Get user permissions
                receivedData[0] = objectInputStream.readBoolean();  //  permission 1 (perm_create)
                receivedData[1] = objectInputStream.readBoolean();  //  permission 2 (perm_edit_all_billboards billboards)
                receivedData[2] = objectInputStream.readBoolean();  //  permission 3 (perm_edit_users)
                receivedData[3] = objectInputStream.readBoolean();  //  permission 4 (perm_schedule)
                break;
            default:
                break;
        }   //  end switch

        return receivedData;
    }

    private static Socket SocketConnect() throws IOException {
        String host = null;
        Integer port = null;
        Socket socket;
        String[] connectionProps = RetrieveConnectionProps();

        //  Check if valid connection string
        if (connectionProps[0] == null || connectionProps[1] == null) {
            //  Don't connect if there is no valid host/port
            throw new IOException("A valid hostname couldn't be found. No connection was established");
        }
        else
        {
            //  Start the connection
            host = connectionProps[0];
            port = Integer.parseInt(connectionProps[1]);
            socket = new Socket(host, port);

            return socket;
        }
    }

    /**
     *
     * @return
     */
    private static String[] RetrieveConnectionProps(){
        //Get the network info from the 'network.props' file
        Properties properties = new Properties();
        FileInputStream in = null;
        //  I'm assuming we want to be reading from local host
        String host = null;
        String port = null;
        //Connects through socket object
        try{
            in = new FileInputStream("./network.props");
            properties.load(in);
            in.close();

            //Extract the connection properties
            host = properties.getProperty("network.host");
            port = properties.getProperty("network.port");
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Return the host and port as string
        return new String[]{host, port};
    }
}