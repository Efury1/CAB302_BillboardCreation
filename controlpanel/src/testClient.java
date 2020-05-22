import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class testClient{

    //
    private static String[] RetrieveConnectionProps(){
        //Get the network info from the 'network.props' file
        Properties properties = new Properties();
        FileInputStream in = null;

        String host = null;
        String port = null;

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
    public static void main(String[] args) throws IOException, InterruptedException {
        String host = null;
        Integer port = null;
        Socket socket;

        String[] connectionProps = RetrieveConnectionProps();

        if(connectionProps[0] == null){
            //invalid host, treat as localhost
            host = "localhost";
        } else {
            host = connectionProps[0];
        }
        //Start the connection
        if(!(connectionProps[1] == null)){
            port = Integer.parseInt(connectionProps[1]);
            socket = new Socket(host, port);

            //prepare to send something
            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutput = new ObjectOutputStream(output);
            ///send info
            objectOutput.writeInt(9);
            objectOutput.flush();


            //get the reply from the server

            InputStream input = socket.getInputStream();
            ObjectInputStream objectInput = new ObjectInputStream(input);
            int myInt = 0;
            if(objectInput.available() > 0){
                myInt = objectInput.readInt();
            } else {
                Thread.sleep(500);
            }
            System.out.println("Received byte " + myInt);
            objectInput.close();
            socket.close();
        }


    }
}