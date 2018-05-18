package Model;

import conf.ConnectionMDB;

import java.util.ArrayList;

public class Utilisateur {
    private int id;
    private String mail;
    private String password;
    //private Map<String,String> phoneId;
//    private ArrayList<Utilisateur> friends;
    private ArrayList<Friend> friends;
    private Position pos;
    private String phoneId;
    private ConnectionMDB connectionMDB;

    //id generé automatiquement par le document?
    public Utilisateur(int id, String mail, String password, String phoneId ,ArrayList<Friend> friends,Position pos){
        this.id=id;
        this.mail=mail;
        this.password=password;
        //this.phoneId.put(phoneId,"false");
        this.phoneId=phoneId;
        this.friends=friends;
        this.pos=pos;
        this.connectionMDB=new ConnectionMDB();
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public String getPhoneId() { return phoneId;}

    public void setPhoneId(String phoneId) { this.phoneId = phoneId;}
}
