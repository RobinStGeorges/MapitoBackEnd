package Model;

import java.util.ArrayList;
import java.util.Map;

public class Utilisateur {
    private int id;
    private String mail;
    private String password;
    //private Map<String,String> phoneId;
    private ArrayList<Utilisateur> friend;
    private Position pos;
    private String phoneId;

    //id generé automatiquement par le document?
    public Utilisateur(int id, String mail, String password, String phoneId ,ArrayList<Utilisateur> friend,Position pos){
        this.id=id;
        this.mail=mail;
        this.password=password;
        //this.phoneId.put(phoneId,"false");
        this.phoneId=phoneId;
        this.friend=friend;
        this.pos=pos;
    }

   // public void setPhoneId(Map<String, String> phoneId) {
     //   this.phoneId = phoneId;
    //}

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

    public ArrayList<Utilisateur> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<Utilisateur> friend) {
        this.friend = friend;
    }
    public void AddFriend(Utilisateur friend){
        //ajout base de donnée;
        this.friend.add(friend);
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }
}
