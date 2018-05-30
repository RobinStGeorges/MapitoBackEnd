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

    public  double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }
        System.out.println(dist+"KM ");
        return (dist);
    }
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
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


}
