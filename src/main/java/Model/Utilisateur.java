package Model;

import com.mongodb.*;

import java.net.UnknownHostException;
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
    private ConnectionMDB connectionMDB;

    //id generé automatiquement par le document?   /*TO DO*/ Retirer le phone id ?
    public Utilisateur(int id, String mail, String password, String phoneId ,ArrayList<Utilisateur> friend,Position pos){
        this.id=id;
        this.mail=mail;
        this.password=password;
        //this.phoneId.put(phoneId,"false");
        this.phoneId=phoneId;
        this.friend=friend;
        this.pos=pos;
        this.connectionMDB=new ConnectionMDB();
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

        DBCollection dbCollection = connectionMDB.getConnectionUtilisateurs("utilisateurs");
        BasicDBObject oldUser= new BasicDBObject();
        oldUser.put("email",this.mail);
        DBObject oOldUser = dbCollection.findOne(oldUser);

        DBObject oNewUser = new BasicDBObject();

        oNewUser.put("id",this.id);
        oNewUser.put("mail",this.mail);
        oNewUser.put("password",this.password);
        oNewUser.put("phoneId",this.phoneId);
        oNewUser.put("friends",this.friend);
        oNewUser.put("pos",this.pos);

        dbCollection.update(oOldUser,oNewUser);


    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }
}
