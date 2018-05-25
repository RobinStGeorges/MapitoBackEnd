package Model;


import org.mongodb.morphia.annotations.Entity;
@Entity("Friend")
public class Friend {

    private String mail;
    private double lat;
    private double lon;
    private double oldLat;
    private double oldLon;


    public Friend(String mail, double lat, double lon) {
        this.mail = mail;
        this.lat = lat;
        this.lon = lon;
    }



    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
