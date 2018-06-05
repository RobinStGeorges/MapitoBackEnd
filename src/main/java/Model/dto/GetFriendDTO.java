package Model.dto;

public class GetFriendDTO {
    private String mail;
    private String prenom;
    private double latitude;
    private double longitude;
    private double lastlatitude;
    private double lastlongitude;
    private boolean inTheArea;
    private boolean lastInTheArea;
    private String rgbProfil;

    public GetFriendDTO(String mail,String prenom,boolean inTheArea,boolean lastInTheArea,
                        double latitude, double longitude, double lastlatitude,
                        double lastlongitude, String rgbProfil) {
        this.mail = mail;
        this.prenom = prenom;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastlatitude = lastlatitude;
        this.lastlongitude = lastlongitude;
        this.inTheArea=inTheArea;
        this.lastInTheArea=lastInTheArea;
        this.rgbProfil=rgbProfil;

    }

}
