package Model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("Notification")
@Indexes(@Index(fields = { @Field("ObjectId") }, options = @IndexOptions(unique = true)))
public class Notification {

    @Id
    private ObjectId _id; // always required
    private String mail;
    private int type;
    private String message;
    private DateNotification dateNotif;

    public Notification(int type, String contenue,String mail) {
        this.mail=mail;
        this.type = type;
        this.message = contenue;
        this.dateNotif=new DateNotification();
    }

    public Notification() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public DateNotification getDateNotif() {
        return dateNotif;
    }

    public void setDateNotif(DateNotification dateNotif) {
        this.dateNotif = dateNotif;
    }
}
