package Model;


import org.mongodb.morphia.annotations.Entity;
@Entity("Friend")
public class Friend {
    private Position pos;
    private double lon;
    private double lat;
    private String mail;


    public Friend(String mail, Position pos){
        this.pos=pos;
        this.mail=mail;
    }

    public void calculateLL(){
        this.lon=pos.getLongitude();
        this.lat=pos.getLatitude();
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
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
