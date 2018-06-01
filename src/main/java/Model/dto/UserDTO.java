package Model.dto;

public class UserDTO {

    public String token,mail, password, nom, prenom,field;

    public UserDTO() {
    }

    public UserDTO(String mail,String password,String nom ,String prenom) {
        this.mail = mail;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom ;
    }
}
