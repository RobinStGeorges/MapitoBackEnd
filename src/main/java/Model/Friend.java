package Model;
import org.mongodb.morphia.annotations.*;

@Embedded
public class Friend {
    private Position pos;
    private int id;
    private int distance;
    public Friend(int id, Position pos, int distance){
        this.pos=pos;
        this.id=id;
    }
}
