package Model;

//import conf.ConnectionMDB;
import Model.dto.UserDTO;
import io.jsonwebtoken.Jwt;
import org.mongodb.morphia.annotations.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import service.UserDAO;

import java.util.ArrayList;
@Entity("Utilisateur")
@Indexes(@Index(fields = { @Field("mail") }, options = @IndexOptions(unique = true)))
public class Utilisateur {

    @Id
    private ObjectId _id; // always required
    private String mail;
    private String token;
    private String password;
    private ArrayList<Friend> friends = new ArrayList<Friend>();
    @Embedded
    private Position pos;
    private int cptWrongtoken;
    private String nom;
    private String prenom;
    private ArrayList<Notification> listeNotifications = new ArrayList<Notification>(); /*TODO faire les methodes correspondantes */
    private String rgbProfil;

    public Utilisateur(){

    }

    public Utilisateur(UserDTO dto) {
        this.mail = dto.mail;
        this.prenom = dto.prenom;
        this.nom = dto.nom;
        this.password = dto.password;
        this.pos=new Position(0,0);
    }

    //id generé automatiquement par le document?
    public Utilisateur(String mail, String password,String nom,String prenom){
        this.mail=mail;
        this.password=password;
        this.cptWrongtoken=0;
        this.nom=nom;
        this.prenom=prenom;
        this.pos=new Position(0,0);
//        this.friends.add(new Friend("plopplop",false));
//        listeNotifications.add(new Notification(0,"testesttest"));

    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public int getCptWrongtoken() {
        return cptWrongtoken;
    }

    public void setCptWrongtoken(int cptWrongtoken) {
        this.cptWrongtoken = cptWrongtoken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public ArrayList<Friend> getFriends() {
        return friends;
    }


    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public ArrayList<Notification> getListeNotifications() {
        return listeNotifications;
    }

    public void setListeNotifications(ArrayList<Notification> listeNotifications) { this.listeNotifications = listeNotifications; }

    public String getRgbProfil() {
        return rgbProfil;
    }

    public void setRgbProfil(String rgbProfil) {
        this.rgbProfil = rgbProfil;
    }

    public ArrayList<Notification> getFriendNotif(){
        ArrayList<Notification> requestpoto = new ArrayList<Notification>();
        for (Notification i : listeNotifications){
            if( i.getType() == 3){
                requestpoto.add(i);
            }
        }
        return requestpoto;
    }

    public ArrayList<Notification> getNotifNoFriend(){
        ArrayList<Notification> requestpoto = new ArrayList<Notification>();
        for (Notification i : listeNotifications){
            if( i.getType() != 3){
                requestpoto.add(i);
            }
        }
        return requestpoto;
    }
}
