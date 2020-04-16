public class Billboard {
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

}
