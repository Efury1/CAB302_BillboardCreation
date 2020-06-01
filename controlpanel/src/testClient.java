import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
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
            System.out.println("Token: " + ClientRequests.GetSessionToken());   //   TODO remove this
        } catch (ClassNotFoundException | IOException e) {
            System.err.println(e);
            System.err.println("\nLogin failed.");
        }

//        System.out.println("=========================================================");
//        try {
//            ClientRequests.ListBillboards();
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//            System.err.println("\nList billboards failed.");
//        }

//        System.out.println("=========================================================");
//        try {
//            ClientRequests.GetBillboardInfo("bill");
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//            System.err.println("\nGetBillboardInfo failed.");
//        }

//        System.out.println("=========================================================");
//        try {
//            ClientRequests.CreateEditBillboard("billboard1", "title","blah blah", adda, "", "", "", "");
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//            System.err.println("\nCreateEditBillboard failed.");
//        }

        //  Delete billboard - function 5
//        System.out.println("=========================================================");
//        try {
//            ClientRequests.DeleteBillboard("howdy");
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//            System.err.println("\nDeleteBillboard failed.");
//        }

//        //  View Schedule - function 6
//        System.out.println("=========================================================");
//        try {
//            ClientRequests.ViewSchedule();
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//        }

//        //  Schedule Billboard - function 7
//        System.out.println("=========================================================");
//        try {
//            ClientRequests.ScheduleBillboard("billboard3", "10:30:00", 60, true, 3600, "2020-05-31", "2020-06-01");
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//        }

        //  Remove Schedule - function 8
//        System.out.println("=========================================================");
//        try {
//            ClientRequests.RemoveSchedule("b1","1999-01-01", "06:40:00");
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//        }

        //  List Users - function 9
//        System.out.println("=========================================================");
//        try {
//            ClientRequests.ListUsers();
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//        }

//        //  Create user - function 10
//        System.out.println("=========================================================");
//        try {
//            ClientRequests.CreateUser("bob", true, false, true, false, "jimmy");
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//        }

        //  Get user permission - function 11
//        System.out.println("=========================================================");
//        try {
//            Object[] perms = ClientRequests.GetUserPermissions("staff");
//            String[] permNo = {"1 (Create)", "2 (Edit All Billboards)", "3 (Edit Users)", "3 (Schedule Billboards [that are not yours])"};
//            int index = 0;
//            for (Object perm: perms) {
//                System.out.println("Staff permission" + permNo[index] + " " + perms[index++]);
//            }
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//        }

////        //  Set User Perms - function 12
//        System.out.println("=========================================================");
//        try {
//            ClientRequests.SetUserPermissions("bob", false, true, false, true);
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//

//        //  Set User Password - function 13
//        System.out.println("=========================================================");
//
//        try {
//            ClientRequests.SetUserPassword("bob", "bobb");
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//        }
//
//        try {
//            ClientRequests.Login("bob", "bobb");
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//        }

//        //  Delete User - function 14
//        System.out.println("=========================================================");
//        try {
//            ClientRequests.DeleteUser("bob");
//        } catch (ClassNotFoundException | IOException e) {
//            System.err.println(e);
//        }

        //TODO
//        //  Log Out - function 15
        System.out.println("=========================================================");
        try {
            Object[] userList = ClientRequests.ListUsers();
            for (Object user: userList) {
                System.out.println(user.toString());
            }
        } catch (ClassNotFoundException | IOException e) {
            System.err.println(e);
        }

        try {
            ClientRequests.LogOut();
        } catch (ClassNotFoundException | IOException e) {
            System.err.println(e);
        }

        try {
            Object[] userList = ClientRequests.ListUsers();
            for (Object user: userList) {
                System.out.println(user.toString());
            }
        } catch (ClassNotFoundException | IOException e) {
            System.err.println(e);
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