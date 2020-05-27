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
    public static Object[] SendReceive(Integer functionID, String token, Object[] dataToSend) throws IOException, NoSuchAlgorithmException {
        Socket mySocket;
        try {
            mySocket = SocketConnect();
        } catch (IOException e) {
            System.err.println(e);
            throw e;
        }
        OutputStream output = mySocket.getOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(output);

        objectOutput.writeInt(functionID);
        objectOutput.writeUTF(token);

        SendData(objectOutput, functionID, dataToSend);

        for (Object data:dataToSend) {
            objectOutput.writeUTF((data.toString()));
        }
        return new Object[]{};
    }

    private static String HashPassword(Object password) throws NoSuchAlgorithmException {
        String inputPassword = password.toString();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = messageDigest.digest(inputPassword.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b: hashedPassword)
        {
            stringBuffer.append(String.format("%1x", b & 255));
        }
        return stringBuffer.toString();
    }



    private static void SendData(ObjectOutputStream objectOutput, int functionID, Object[] dataToSend) throws IOException, NoSuchAlgorithmException {
        //  List of things the client sends to the server (comes after the functionID and token)
        switch (functionID)
        {
            case 1: //  Login request
            case 13:    //  Set user password
                objectOutput.writeUTF((String)dataToSend[0]);   //  username
                objectOutput.writeUTF(HashPassword(dataToSend[1])); //  hashed password
                break;
            case 2: //  List billboards
                //  nothing required
                break;
            case 3: //  Get billboard information
            case 5: //  Delete billboard
                objectOutput.writeUTF((String)dataToSend[0]);   //  billboard name
                break;
            case 4: //  Create/edit billboard
                objectOutput.writeUTF((String)dataToSend[0]);   //  billboard name
                objectOutput.writeUTF((String)dataToSend[1]);   //  title
                objectOutput.writeUTF((String)dataToSend[2]);   //  description
                objectOutput.write((byte[])dataToSend[3]);  //  picture data
                objectOutput.writeUTF((String)dataToSend[4]);   //  background colour   //TODO check if the cast is to string(instead of something like (Int)dataToSend
                objectOutput.writeUTF((String)dataToSend[5]);   //  title colour
                objectOutput.writeUTF((String)dataToSend[6]);   //  description colour
                break;
            case 6: //  View schedule
                //  nothing required
                break;
            case 7: //  Schedule billboard
                objectOutput.writeUTF((String)dataToSend[0]);   //  billboard name
                objectOutput.writeUTF((String)dataToSend[1]);   //  start time  //TODO check cast type (time datatype?)
                objectOutput.writeInt((Integer)dataToSend[2]);  //  duration
                objectOutput.writeBoolean((Boolean)dataToSend[3]);  //  repeat bool
                objectOutput.writeInt((Integer)dataToSend[4]);  //  repeat freq
                objectOutput.writeUTF((String)dataToSend[5]);   //  start date
                objectOutput.writeUTF((String)dataToSend[6]);   //  end date
                break;
            case 8: //  Remove billboard from schedule
                objectOutput.writeUTF((String)dataToSend[0]);   //  billboard name
                objectOutput.writeUTF((String)dataToSend[1]);   //  start date
                objectOutput.writeUTF((String)dataToSend[2]);   //  start time   //TODO check casting
                break;
            case 9: //  List users
                //  nothing required
                break;
            case 10:    //  Create User
                objectOutput.writeUTF((String)dataToSend[0]);   //  username
                objectOutput.writeBoolean((Boolean)dataToSend[1]);  //  permission 1 (perm_create)
                objectOutput.writeBoolean((Boolean)dataToSend[2]);  //  permission 2 (perm_edit_all_billboards billboards)
                objectOutput.writeBoolean((Boolean)dataToSend[3]);  //  permission 3 (perm_edit_users)
                objectOutput.writeBoolean((Boolean)dataToSend[4]);  //  permission 4 (perm_schedule)
                objectOutput.writeUTF(HashPassword(dataToSend[5])); //  hashed password
                break;
            case 11:    //  Get user permissions
            case 14:    //  Delete user
                objectOutput.writeUTF(dataToSend[0].toString());   //  username
                break;
            case 12:   //  Set user permissions
                objectOutput.writeUTF((String)dataToSend[0]);   //  username
                objectOutput.writeBoolean((Boolean)dataToSend[1]);   //  permission 1 (perm_create)
                objectOutput.writeBoolean((Boolean)dataToSend[2]);   //  permission 2 (perm_edit_all_billboards billboards)
                objectOutput.writeBoolean((Boolean)dataToSend[3]);   //  permission 3 (perm_edit_users)
                objectOutput.writeBoolean((Boolean)dataToSend[4]);   //  permission 4 (perm_schedule)
                break;
            case 15:    //  Log out (aka delete session token)
                //  nothing required
                break;
            default:
                break;
        }
    }

    /**
     *
     */
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