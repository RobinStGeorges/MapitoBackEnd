package Model.dto;

public class UserDTO {

    public String token,mail, password, nom, prenom,field,rgbProfil;

    public UserDTO() {
    }

    public UserDTO(String token,String mail,String password,String nom ,String prenom,String rgbProfil) {
        this.token=token;
        this.mail = mail;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom ;
        this.rgbProfil=rgbProfil;
    }


}
