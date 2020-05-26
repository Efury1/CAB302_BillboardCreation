/*This is a way to handle exception in the program.
* That way in functions we will be able to go throw ProgramExceptions.
* We need to be able to handle particular exceptions */

public class ProgramExceptions extends RuntimeException {
    public ProgramExceptions(String message)
    {
        super (message);
    }

    /*Normal exception that doesn't take anything. */
    public ProgramExceptions() {
        super();
    }
}
