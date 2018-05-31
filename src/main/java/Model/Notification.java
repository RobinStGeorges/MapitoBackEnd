package Model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("Notification")
public class Notification {
    @Id
    private ObjectId _id; // always required

    private String titre;
    private String message;

    public Notification(String titre, String contenue) {
        this.titre = titre;
        this.message = contenue;
    }

    public Notification() {
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
