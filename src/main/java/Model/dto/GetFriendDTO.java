package Model.dto;

public class GetFriendDTO {
    private String mail;
    private String prenom;
    private double latitude;
    private double longitude;
    private double lastlatitude;
    private double lastlongitude;

    public GetFriendDTO(String mail,String prenom, double latitude, double longitude, double lastlatitude, double lastlongitude) {
        this.mail = mail;
        this.prenom=prenom;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastlatitude = lastlatitude;
        this.lastlongitude = lastlongitude;
    }

}
