package Model;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("Friend")
public class Friend {

    @Id
    private ObjectId _id; // always required
    private String mail;
    private boolean lastInArea = false;
    private boolean inTheArea = false;
    private String rgbProfil;

    public Friend(String mail, boolean inTheArea,boolean lastInArea) {
        this.mail = mail;
        this.inTheArea = inTheArea;
        this.lastInArea = lastInArea;

    }

    public Friend(String mail, boolean inTheArea,boolean lastInArea,String rgbProfil) {
        this.mail = mail;
        this.inTheArea = inTheArea;
        this.lastInArea = lastInArea;
        this.rgbProfil=rgbProfil;

    }

    public Friend() {
    }

    public Friend(String mail){
        this.mail=mail;
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
