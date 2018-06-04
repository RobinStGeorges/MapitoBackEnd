package Model.dto;

public class UpdateNotificationsDTO {


    public String token, mail, type;

    public UpdateNotificationsDTO() {
    }

    public UpdateNotificationsDTO(String token,String type, String mail) {
        this.token = token;
        this.mail = mail;
        this.type = type;
    }
}