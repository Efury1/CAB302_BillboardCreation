// java Program to create a simple JList
import javax.swing.*;
public class Billboard
{

    Billboard() {
        //create a new frame
        JFrame billboardframe = new JFrame("frame");
        JPanel listPanel =new JPanel();
        JLabel billboardList = new JLabel("select a test");
        String test[]= { "1","2"};
        JList listInstance= new JList(test);
        listInstance.setSelectedIndex(2);
        listPanel.add(listInstance);
        billboardframe.add(listPanel);
        billboardframe.setSize(400,400);
        billboardframe.setVisible(true);
    }

    //main class
    public static void main(String[] args) {
        new Billboard();
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
