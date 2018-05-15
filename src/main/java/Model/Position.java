package Model;

public class Position {
    private double latitude;
    private double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getPosString(){
        return getLatitude()+"-"+getLongitude() ;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public void refresh(double latitude,double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
