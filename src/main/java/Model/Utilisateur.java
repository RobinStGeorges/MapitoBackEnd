package Model;

//import conf.ConnectionMDB;
import io.jsonwebtoken.Jwt;
import org.mongodb.morphia.annotations.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import java.util.ArrayList;
@Entity("Utilisateur")

@Indexes(@Index(fields = { @Field("mail") }, options = @IndexOptions(unique = true)))
public class Utilisateur {

    @Id
    private ObjectId _id; // always required


    private String mail;

    private String token;
    private String password;

private double latitude;
private double longitude;


    private ArrayList<Utilisateur> friends = new ArrayList<Utilisateur>();
    private Position pos;
    private String phoneId;


    private int cptWrongtoken;
    private String nom;
    private String prenom;
    private String[] listeAttente; /*TODO faire les methodes correspondantes */

    public Utilisateur(){

    }

    //id generé automatiquement par le document?
    public Utilisateur(String mail, String password,String nom,String prenom){
        this.mail=mail;
        this.password=password;
        this.cptWrongtoken=0;
        this.nom=nom;
        this.prenom=prenom;

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

    //public Map<String,String> getPhoneId() {
 //       return phoneId;
   // }

    //public void setPhoneId(String phoneId) {
    //    this.phoneId.put(phoneId,"false");
 //   }

    public ArrayList<Utilisateur> getFriends() {
        return friends;
    }

    public void addFriends(Utilisateur friend) {
        this.friends.add(friend);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String[] getListeAttente() {
        return listeAttente;
    }

    public void setListeAttente(String[] listeAttente) {
        this.listeAttente = listeAttente;
    }


}
