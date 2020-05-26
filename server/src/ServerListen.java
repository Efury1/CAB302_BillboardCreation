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
        try{
            in = new FileInputStream("./network.props");
            properties.load(in);
            in.close();

            //Extract the port
            port = properties.getProperty("network.port");
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException e) {
            //TODO get ride of ex.printStackTrace, In lec3 he says it's not acceptable error handling.
            e.printStackTrace();
            System.exit(1);
        }

        //Return the port (if all is well)
        return Integer.parseInt(port);
    }

    public static void main(String[] args) throws IOException, SQLException {

        Integer port = RetrievePort();

        //Catch that the assigned port is not valid
        if(!(port == null)){
            ServerSocket serverSocket = new ServerSocket(port);

            //loop indefinitely, listening for a connection to accept
            for(;;) {
                Socket socket = serverSocket.accept();

                //Get the connection info
                InputStream input = socket.getInputStream();
                ObjectInputStream objectInput = new ObjectInputStream(input);
                int myInt = objectInput.readInt();
                String token = objectInput.readUTF();
                //System.out.println("Read byte " + myInt);
                System.out.println("Request ID: " + myInt);
                System.out.println("Token string: " + token);

                //
                ProcessRequest.ProcessRequest(myInt, token);

                //Send a reply (based on info)
                OutputStream output = socket.getOutputStream();
                ObjectOutputStream objectOutput = new ObjectOutputStream(output);
                objectOutput.writeInt(myInt*10);
                objectOutput.writeUTF("Received request ID: " + myInt + " and token: " + token);
                objectOutput.flush();


                //close the connection
                socket.close();
            }
        }



    }


}
