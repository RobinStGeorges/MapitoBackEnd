package Model;


import org.mongodb.morphia.annotations.Entity;
@Entity("Friend")
public class Friend {

    private String mail;
    private Position pos;
    private boolean inTheArea;
    private boolean lastInTheArea;


    public Friend(String mail, double lat, double lon) {
        this.mail = mail;
        this.pos=new Position(lat,lon);
    }






    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }



    public boolean isInTheArea() {
        return inTheArea;
    }

    public void setInTheArea(boolean inTheArea) {
        this.inTheArea = inTheArea;
    }

    public boolean isLastInTheArea() {
        return lastInTheArea;
    }

    public void setLastInTheArea(boolean lastInTheArea) {
        this.lastInTheArea = lastInTheArea;
    }
}
