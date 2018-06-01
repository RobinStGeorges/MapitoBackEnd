package Model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("Notification")
@Indexes(@Index(fields = { @Field("ObjectId") }, options = @IndexOptions(unique = true)))
public class Notification {

    @Id
    private ObjectId _id; // always required

    private int type;
    private String message;

    public Notification(int titre, String contenue) {
        this.type = titre;
        this.message = contenue;
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
}
