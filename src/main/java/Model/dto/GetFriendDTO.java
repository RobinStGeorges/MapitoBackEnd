package Model.dto;

public class GetFriendDTO {
    String id;
    double latitude;
    double longitude;
    double lastlatitude;
    double lastlongitude;

    public GetFriendDTO(String id, double latitude, double longitude, double lastlatitude, double lastlongitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastlatitude = lastlatitude;
        this.lastlongitude = lastlongitude;
    }

}
