import java.io.IOException;
import java.sql.Blob;

/**
 * Used to create an instance of a billboard, and allows access to each element of it.
 */
public class Billboard {
    private String BName;
    private String BOwner;
    private String BMessage;
    private String BDescription;
    private String BBackgroundColour;
    private String BMessageColour;
    private String BDescriptionColour;
    private String BImageLink;
    private Blob BBlobData;
    private boolean scheduled;

    /**
     * Constructor for billboard
     * @param name Name of billboard
     * @param owner Owner/creator of billboard
     */
    public Billboard(String name, String owner){ //Billboard new Billboard = Billboard("BB1", "staff");
        BName = name;
        BOwner = owner;
        scheduled = false;
    }

    /**
     * Returns whether the billboard contains a message
     * @return true if message is not blank or null, otherwise false
     */
    public boolean hasMessage(){
        if(getBMessage() == "" || getBMessage() == null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Returns whether the billboard contains a description
     * @return true if description is not blank or null, otherwise false
     */
    public boolean hasDescription(){
        if(getBDescription() == "" || getBDescription() == null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Returns whether the billboard contains an image
     * @return true if image is present, otherwise false
     */
    public boolean hasImage(){
        if(getBBlobData() == null && (getBImageLink() == "" || getBImageLink() == null)){
            return false;
        }
        else{
            return true;
        }
    }

    //for the setters will have to do something to change it in DB

    /**
     * Returns name of billboard
     * @return the name of the billboard
     */
    public String getBName(){
        return BName;
    }

    /**
     * Changes name of billboard
     * @param name new name of billboard
     */
    public void setBName(String name){
        BName = name;
    }

    /**
     * Returns owner/creator of billboard
     * @return billboard owner
     */
    public String getBOwner(){
        return BOwner;
    }

    /**
     * Changes billboard owner
     * @param owner new owner
     */
    public void setBOwner(String owner){
        BOwner = owner;
    }

    /**
     * Returns whether billboard is scheduled
     * @return True if scheduled, False if not scheduled
     */
    public boolean getScheduled(){
        return scheduled;
    }

    /**
     * Changes whether billboard is recorded as scheduled
     * @param s True if billboard is scheduled, otherwise False
     */
    public void setScheduled(boolean s){
        scheduled = s;
    }

    /**
     * Returns message
     * @return optional message for billboard
     */
    public String getBMessage() {
        return BMessage;
    }

    /**
     * Adds or changes message
     * @param BMessage new message
     */
    public void setBMessage(String BMessage) {
        this.BMessage = BMessage;
    }

    /**
     * Returns description
     * @return optional description for billboard
     */
    public String getBDescription() {
        return BDescription;
    }

    /**
     * Adds or changes description
     * @param BDescription new message
     */
    public void setBDescription(String BDescription) {
        this.BDescription = BDescription;
    }

    /**
     * Returns billboard background colour
     * @return optional hexidecimal for billboard background colour
     */
    public String getBBackgroundColour() {
        return BBackgroundColour;
    }

    /**
     * Adds or changes billboard background colour
     * @param BBackgroundColour hexidecimal for billboard background colour
     */
    public void setBBackgroundColour(String BBackgroundColour) {
        this.BBackgroundColour = BBackgroundColour;
    }

    /**
     * Returns message text colour
     * @return optional hexidecimal for message text colour
     */
    public String getBMessageColour() {
        return BMessageColour;
    }

    /**
     * Adds or changes message text colour
     * @param BMessageColour hexidecimal for message text colour
     */
    public void setBMessageColour(String BMessageColour) {
        this.BMessageColour = BMessageColour;
    }

    /**
     * Returns description text colour
     * @return optional hexidecimal for description text colour
     */
    public String getBDescriptionColour() {
        return BDescriptionColour;
    }

    /**
     * Adds or changes description text colour
     * @param BDescriptionColour hexidecimal for description colour
     */
    public void setBDescriptionColour(String BDescriptionColour) {
        this.BDescriptionColour = BDescriptionColour;
    }

    /**
     * Returns url of image -- only 1 of two image types allowed
     * @return url for image
     */
    public String getBImageLink() {
        return BImageLink;
    }

    /**
     * Adds url image link and deletes any blob data
     * @param BImageLink url link for image
     */
    public void setBImageLink(String BImageLink) {
        this.BImageLink = BImageLink;
        this.BBlobData = null;
    }

    /**
     * Returns blob data of image -- only 1 of two image types allowed
     * @return blob data for image
     */
    public Blob getBBlobData() {
        return BBlobData;
    }

    /**
     * Adds blob data and deletes any url link
     * @param BBlobData blob data for image
     */
    public void setBBlobData(Blob BBlobData) {
        this.BBlobData = BBlobData;
    }

    /**
     * Requests and processes billboard dat from server
     * @throws IOException
     */
    public void importAllInfo() throws IOException, ClassNotFoundException {
        Object[] billboardData = ClientRequests.GetBillboardInfo(getBName());

        if (billboardData.length == 6) {
            setBMessage(billboardData[0].toString());               //Title
            setBDescription(billboardData[1].toString());           //Description
            setBBlobData((Blob) billboardData[2]);                   //Picture
            setBBackgroundColour(billboardData[3].toString());      //Background colour
            setBMessageColour(billboardData[4].toString());         //Title Colour
            setBDescriptionColour(billboardData[5].toString());     //Description colour
        }
    }
}