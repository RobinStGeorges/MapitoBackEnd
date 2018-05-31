package Model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("Notification")
public class Notification {
    @Id
    private ObjectId _id; // always required
    private String type;
    private String message;

    public Notification(String titre, String contenue) {
        this.type = titre;
        this.message = contenue;
    }

    public Notification() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
