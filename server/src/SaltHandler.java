import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SaltHandler {
    private String salt;

    /**
     * Hashes an input OBJECT (after casting it to a string) with SHA-256
     * @param inputString
     * @return
     */
    public static String HashString(String inputString) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            //  do nothing (this will never be caught)
        }
        byte[] hashedString = messageDigest.digest(inputString.getBytes());
        return BytesToString(hashedString);
    }

    /**
     * Converts an array of bytes to a string
     * @param byteArray
     * @return The string equivalent
     */
    private static String BytesToString(byte[] byteArray){
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b: byteArray)
        {
            //  "%1x" formats it as a hashed string without the spaces included (could also use "%02x")
            stringBuffer.append(String.format("%1x", b & 255));
        }
        return stringBuffer.toString();
    }

    /**
     * Constructor for a new Salt
     */
    public SaltHandler(){
        Random random = new Random();
        byte[] saltBytes = new byte[32];
        random.nextBytes(saltBytes);
        salt = BytesToString(saltBytes);
    }

    public String GetSalt(){
        return salt;
    }
}
