import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class TokenHandling {
    private HashMap<String, Timestamp> sessionToken;

    public TokenHandling()
    {
        sessionToken = new HashMap<String, Timestamp>();
    }

    public void AddToken(String token)
    {
        sessionToken.put(token, GetTime());
    }

    /**
     * Validates the session token AND updates (using UpdateToken())
     * @param token The token to be validated
     * @return True if the token is valid
     */
    public Boolean ValidateToken(String token)
    {
        if (sessionToken.containsKey(token))
        {
            //  Assign the token to a workable variable
            Timestamp timestamp = sessionToken.get(token);
            Calendar tokenTime = Calendar.getInstance();
            tokenTime.setTime(timestamp);

            //  Add 24 hours to variable
            tokenTime.add(Calendar.DAY_OF_WEEK, 1);
            timestamp.setTime(tokenTime.getTime().getTime());


            //  Check if token+24h is before now
            if (timestamp.before(GetTime())) {
                //  has expired
                //  Token expired - can be deleted
                RemoveToken(token);
                return false;
            }
            else {
                //  hasn't expired
                UpdateToken(token);
                return true;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Updates the input token to the current time, refreshing it
     * @param token
     */
    private void UpdateToken(String token)
    {
        if (sessionToken.containsKey(token)) {
            //  Update the token's timestamp
            sessionToken.replace(token, GetTime());
        }
    }

    public void RemoveToken(String token)
    {
        if (sessionToken.containsKey(token))
        {
            sessionToken.remove(token);
        }
    }

    private Timestamp GetTime()
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp;
    }
}