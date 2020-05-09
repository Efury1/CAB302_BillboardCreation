// java Program to create a simple JList
/*import javax.swing.*;
import java.awt.*;
public class Billboard extends JFrame

{
    JScrollPane scrollpane;

    public Billboard()
    {
        super("Billboard Viewer");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        String categories[] = {"Example 1", "Example 2", "Example 3","Example 4", "Example 5", "Example 6", "Example 7", "Example 8", "Example 9" };
        JList billboardList = new JList(categories);
        scrollpane = new JScrollPane(billboardList);
        getContentPane().add(scrollpane, BorderLayout.CENTER);
    }

    // Main Method 
    public static void main(String args[])
    {
        Billboard session1 = new Billboard();
        session1.setVisible(true);
    }
} */







public class Billboard {
    private String BName;
    private String BOwner;
    private boolean scheduled;

    /**
     * Constructor for billboard
     * @param name Name of billboard
     * @param owner Owner/creator of billboard
     */
    public Billboard(String name, String owner){
        BName = name;
        BOwner = owner;
        scheduled = false;
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

}
