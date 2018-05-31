package Model;


import org.mongodb.morphia.annotations.Entity;
@Entity("Friend")
public class Friend {

    private String mail;
    private boolean lastInArea = false;

    public Friend(String mail, boolean lastInArea) {
        this.mail = mail;
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
}
