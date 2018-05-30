package Model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("Notification")
public class Notification {
    @Id
    private ObjectId _id; // always required

    private String titre;
    private String contenue;

    public Notification(String titre, String contenue) {
        this.titre = titre;
        this.contenue = contenue;
    }

    public Notification() {
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenue() {
        return contenue;
    }

    public void setContenue(String contenue) {
        this.contenue = contenue;
    }
}
