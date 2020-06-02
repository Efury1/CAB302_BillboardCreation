import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.*;

import java.io.IOException;

/*All possible scenarios
* 1. Client tries to connect to database. Success is printed
* to terminal.
* 2. Client tries to connect to database but server failed.
* An error is received and user is prompted to try again.
* 1.Username and password fields are empty, "Enter" is click.
* and an error dialog appears
* 2.If username and password fields are invalid, the 'Enter' is pressed.
* An error dialog appears.
*
* ------------------Calculating Testing Effectiveness------------------------
* */

public class UnitTestingLogin{
    @Test
    public void testConnectInvalid() throws IOException, ClassNotFoundException {
        String[] processRequest = {};
        String token = "";
        ;
        //Create FileNoteFoundException: When system cannot
        //IOException: When a valid host name can't be found
        Assertions.assertThrows(IOException.class, ()->{
            ClientRequests.Login("","");
        });
    }

    @Test
    public void testUserPasswordValid() throws IOException, ClassNotFoundException {
        //Login using default user
        //should return correct value
        //assertNotNull();
        ClientRequests.Login("staff", "today123"); //Login with the correct admin user details
        Assertions.assertNotNull(ClientRequests.GetSessionToken()); //Check that a valid session token is set

    }

    @Test
    public void testUserPasswordEmpty()
    {
        Assertions.assertThrows(IOException.class, () -> {
            ClientRequests.Login("staff", "");
        });
    }

    @Test
    public void testUserPasswordInvalid()
    {
        //Login using default user
        //should return correct value
        //assertNotNull();
        Assertions.assertThrows(IOException.class, () -> {
            ClientRequests.Login("staff", "theWrongPassword");
        });

    }
}