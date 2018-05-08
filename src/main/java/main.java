import Model.ConnectionMDB;
import Model.Position;
import Model.Utilisateur;
import com.mongodb.*;

import java.util.ArrayList;

public class main {

    public static void main(String args[]){
        ArrayList listUtil =new ArrayList();
        Position pos = new Position(2,3);
        Utilisateur user = new Utilisateur(1,"second.test@gmail.com","password","0670252682", listUtil,pos);

        ConnectionMDB connectionMDB= new ConnectionMDB();


        //AJOUT USER
        //connectionMDB.saveUser(user);

        //RECUPERATION USER
        //Utilisateur userRecup = connectionMDB.getUser("mail","second.test@gmail.com");
        //System.out.println(userRecup.getPhoneId());

        //UPDATE USER
        DBCollection dbCollection = connectionMDB.getConnectionUtilisateurs("utilisateurs");
        BasicDBObject oldUser= new BasicDBObject();
        oldUser.put("mail","premier.test@gmail.com");
        DBObject oOldUser = dbCollection.findOne(oldUser);

        DBObject oNewUser = new BasicDBObject();

        oNewUser.put("id",oOldUser.get("id"));
        oNewUser.put("mail","updatedMail@gmail.com");
        oNewUser.put("password",oOldUser.get("password"));
        oNewUser.put("phoneId",oOldUser.get("phoneId"));
        oNewUser.put("friends",oOldUser.get("friends"));
        oNewUser.put("pos",oOldUser.get("pos"));

        dbCollection.update(oOldUser,oNewUser);

    }
}
