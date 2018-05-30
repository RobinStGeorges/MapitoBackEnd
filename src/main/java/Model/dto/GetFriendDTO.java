package Model.dto;

public class GetFriendDTO {
    private String mail;
    private String prenom;
    private double latitude;
    private double longitude;
    private double lastlatitude;
    private double lastlongitude;
    private boolean inTheArea;

    public GetFriendDTO(String mail,String prenom,boolean inTheArea, double latitude, double longitude, double lastlatitude, double lastlongitude) {
        this.mail = mail;
        this.prenom = prenom;
        this.inTheArea = inTheArea;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastlatitude = lastlatitude;
        this.lastlongitude = lastlongitude;
    }

}
