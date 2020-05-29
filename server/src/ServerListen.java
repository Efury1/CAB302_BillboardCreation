import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
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
            //TODO get ride of ex.printStackTrace, In lec3 he says it's not acceptable error handling.
            //  e.printStackTrace();
            System.err.println("Could not find port.");
            System.exit(1);
        }

        //Return the port (if all is well)
        return testInt;
    }

    public static void main(String[] args) throws IOException, SQLException{
        TokenHandling tokenCache = new TokenHandling();
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

            //loop indefinitely, listening for a connection to accept
            for(;;) {
                Socket socket = serverSocket.accept();

                ReceiveSend.ReceiveSend(socket, tokenCache);

                //close the connection
                socket.close();
            }
        }
    }   //  end Main()
}
