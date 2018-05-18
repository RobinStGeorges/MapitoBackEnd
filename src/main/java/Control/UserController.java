package Control;

import conf.ConnectionMDB;
import Model.Position;
import Model.Utilisateur;
import com.mongodb.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

public class UserController {

    public static void main(String args[]){
        ArrayList listUtil =new ArrayList();
        ArrayList friends = new ArrayList();
        Position pos = new Position(2,3);
        Utilisateur user = new Utilisateur(1,"second.test@gmail.com","password","0670252682", friends,pos);

        ConnectionMDB connectionMDB= new ConnectionMDB();


//        //AJOUT USER
//        connectionMDB.saveUser(user);

//        //RECUPERATION USER
//        Utilisateur userRecup = connectionMDB.getUser("mail","second.test@gmail.com");
//        System.out.println(userRecup.getPhoneId());

//        try {
//            //UPDATE USER
//            DBCollection dbCollection = connectionMDB.getConnectionUtilisateurs("utilisateurs");
//            BasicDBObject oldUser = new BasicDBObject();
//            oldUser.put("mail", "second.test@gmail.com");
//            DBObject oOldUser = dbCollection.findOne(oldUser);
//
//            DBObject oNewUser = new BasicDBObject();
//
//            oNewUser.put("id", oOldUser.get("id"));
//            oNewUser.put("mail", "updatedMail@gmail.com");
//            oNewUser.put("password", oOldUser.get("password"));
//            oNewUser.put("phoneId", oOldUser.get("phoneId"));
//            oNewUser.put("friends", oOldUser.get("friends"));
//            oNewUser.put("pos", oOldUser.get("pos"));
//
//            dbCollection.update(oOldUser, oNewUser);
//        }catch(NullPointerException e){
//            System.out.println(e);
//        }

    }

    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean connectionUser(String mail, String password){

        ConnectionMDB connectionMDB= new ConnectionMDB();
        Utilisateur userRecup = connectionMDB.getUser("mail" , mail);
        if (userRecup.getPassword().equals(password)){
            return true;
        }
    }

    @Path("/newUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void newUser(Utilisateur user){

        ConnectionMDB connectionMDB= new ConnectionMDB();
        connectionMDB.saveUser(user);
    }

    @Path("/getUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void getUser(String mail){
        ConnectionMDB connectionMDB= new ConnectionMDB();
        Utilisateur userRecup = connectionMDB.getUser("mail",mail);
        System.out.println(userRecup.getPhoneId());
    }

    @Path("/api/updateUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateUser(String mail){
        ConnectionMDB connectionMDB= new ConnectionMDB();
        try {
            //UPDATE USER
            DBCollection dbCollection = connectionMDB.getConnectionUtilisateurs("utilisateurs");
            BasicDBObject oldUser = new BasicDBObject();
            oldUser.put("mail", mail);
            DBObject oOldUser = dbCollection.findOne(oldUser);

            DBObject oNewUser = new BasicDBObject();

            oNewUser.put("id", oOldUser.get("id"));
            oNewUser.put("mail", "updatedMail@gmail.com");
            oNewUser.put("password", oOldUser.get("password"));
            oNewUser.put("phoneId", oOldUser.get("phoneId"));
            oNewUser.put("friends", oOldUser.get("friends"));
            oNewUser.put("pos", oOldUser.get("pos"));

            dbCollection.update(oOldUser, oNewUser);
        }catch(NullPointerException e){
            System.out.println(e);
        }
    }
}
