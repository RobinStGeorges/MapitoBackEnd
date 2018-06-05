package Model.dto;

public class UserDTO {

    public String token,mail, password, nom, prenom,field;

    public UserDTO() {
    }

    public UserDTO(String token,String mail,String password,String nom ,String prenom) {
        this.token=token;
        this.mail = mail;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom ;
    }


}
