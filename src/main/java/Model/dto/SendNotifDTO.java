package Model.dto;



public class SendNotifDTO {
    public String token,mail,contenu;

    public SendNotifDTO(String token, String mail, String contenu) {
        this.token = token;
        this.mail = mail;
        this.contenu = contenu;
    }

    public SendNotifDTO() {
    }

}
