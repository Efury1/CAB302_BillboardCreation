import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class testClient{

    //  This method is copied to SendReceive
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

    //test client class
    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {
        //  This is to be used after server ReceiveSend(); is created
        //  SendReceive.SendReceive(1, "token", new Object[]{"testuser", "testpassword"});

        try {
            ClientRequests.Login("staff", "today123");
            System.out.println(ClientRequests.GetSessionToken());
        } catch (ClassNotFoundException e) {
            System.err.println("Login failed.");
        }
//        System.out.println("=========================================================");
//        try {
//            ClientRequests.ListBillboards();
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println("List billboards failed.");
//        }
        System.out.println("=========================================================");
        try {
            ClientRequests.GetBillboardInfo("name");
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("List billboards failed.");
        }

        /*String host = null;
        Integer port = null;
        Socket socket;
        String[] connectionProps = RetrieveConnectionProps();

            //  Check if valid connection string
        if (connectionProps[0] == null || connectionProps[1] == null) {
            //  Don't connect if there is no valid host/port
            System.err.println("A valid hostname couldn't be found. No connection was established");
            return;
        }

        //  Start the connection
        host = connectionProps[0];
        port = Integer.parseInt(connectionProps[1]);
        socket = new Socket(host, port);

        //prepare to send something
        OutputStream output = socket.getOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(output);
        ///send info
        objectOutput.writeInt(2);
        objectOutput.writeUTF("tokenString");

        objectOutput.flush();


        GetServerReply(socket);


        socket.close();*/
    }

    private static void GetServerReply(Socket socket) throws IOException, InterruptedException {
        //get the reply from the server
        int myInt = 0;
        String myString = "";
        InputStream input = socket.getInputStream();
        ObjectInputStream objectInput = new ObjectInputStream(input);

        //  TODO make sure that every piece of data is received before assigning the variables
        while(objectInput.available() == 0)
        {
            Thread.sleep(1);
        }
        myInt = objectInput.readInt();
        myString = objectInput.readUTF();

//        restart:
//        if(objectInput.available() > 5000000){
//            myInt = objectInput.readInt();
//            myString = objectInput.readUTF();
//        } else {
//            Thread.sleep(1);
//            break restart;
//        }

        //System.out.println("Received byte " + myInt);
        System.out.println("Message from server: " + myString);
        objectInput.close();
    }
}