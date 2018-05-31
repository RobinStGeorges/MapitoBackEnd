package Model;


import org.mongodb.morphia.annotations.Entity;
@Entity("Friend")
public class Friend {

    private String mail;
    private boolean lastInArea = false;
    private boolean inTheArea = false;

    public Friend(String mail, boolean inTheArea,boolean lastInArea) {
        this.mail = mail;
        this.inTheArea = inTheArea;
        this.lastInArea = lastInArea;

    }

    public Friend() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isLastInArea() {
        return lastInArea;
    }

    public void setLastInArea(boolean lastInArea) {
        this.lastInArea = lastInArea;
    }

    public boolean isInTheArea() {
        return inTheArea;
    }

    public void setInTheArea(boolean inTheArea) {
        this.inTheArea = inTheArea;
    }
}
