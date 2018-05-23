package Model;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("Position")

public class Position {
    @Id
    private ObjectId _id; // always required
    private double latitude;
    private double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getPosString(){
        return getLatitude()+"-"+getLongitude() ;
    }

    public void refresh(double latitude,double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }
}
