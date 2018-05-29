package Model;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("Position")
public class Position {
    @Id
    private ObjectId _id; // always required
    private double latitude=0;
    private double longitude=0;
    private double lastlatitude=0;
    private double lastlongitude=0;
    private double distance;

    public Position(){
    }
    public Position(double latitude, double longitude) {
        this._id = _id;
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

    public double getDistance(double lat1, double lat2 ,double lon1, double lon2){
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        distance = Math.pow(distance, 2) ;
        return distance;
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

    public double getLastlatitude() { return lastlatitude; }

    public void setLastlatitude(double lastlatitude) { this.lastlatitude = lastlatitude; }

    public double getLastlongitude() { return lastlongitude; }

    public void setLastlongitude(double lastlongitude) { this.lastlongitude = lastlongitude; }

    public double getDistance() { return distance; }

    public void setDistance(double distance) { this.distance = distance; }
}
