// java Program to create a simple JList
import javax.swing.*;
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
} 







/*public class Billboard {
    private String BName;
    private String BOwner;
    private boolean scheduled;

    public Billboard(String name, String owner){
        BName = name;
        BOwner = owner;
        scheduled = false;
    }

    //for the setters will have to do something to change it in DB

    public String getBName(){
        return BName;
    }

    public void setBName(String name){
        BName = name;
    }

    public String getBOwner(){
        return BOwner;
    }

    public void setBOwner(String owner){
        BOwner = owner;
    }

    public boolean getScheduled(){
        return scheduled;
    }

    public void setScheduled(boolean s){
        scheduled = s;
    }

}*/
