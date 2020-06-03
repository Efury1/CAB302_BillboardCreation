import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.Properties;

public class ServerListen {
    private static Integer RetrievePort(){
        //Get the network info from the 'network.props' file
        Properties properties = new Properties();
        FileInputStream in = null;
        String port = null;
        Integer testInt = 0;
        try{
            in = new FileInputStream("./network.props");
            properties.load(in);
            in.close();

            //Extract the port
            port = properties.getProperty("network.port");
            testInt = Integer.parseInt(port);
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Could not find port.");
            System.exit(1);
        }

        //Return the port (if all is well)
        return testInt;
    }

    public static void main(String[] args){

        try {
            InitDatabase.InitialiseDB();
        } catch (SQLException throwables) {
            System.err.println(throwables);
            System.err.println("Could not initialise database.");
            System.exit(1);
        }

        TokenHandler tokenCache = new TokenHandler();
        Integer port = RetrievePort();

        //Catch that the assigned port is not valid
        if(!(port == null)){
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException | SecurityException | IllegalArgumentException e) {
                System.err.println("Could not open socket.");
                return;
            }

            //  Loop indefinitely, listening for a connection to accept
            for(;;) {
                for(;;) {
                    System.err.println("Server ready...");
                    Socket socket = null;

                    try {   //  Try to accept an incoming connection..
                        socket = serverSocket.accept();

                        try {
                            ReceiveSend.ReceiveSend(socket, tokenCache);
                        } catch (IOException e) {
                            System.err.println("Data transmission failed.");
                            break;
                        }
//                        catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
                        socket.close();
                    } catch (IOException e) {
                        System.err.println("Could not accept connection.");
                        break;
                    }
                }
            }
        }
    }   //  end Main()
}
