import Model.ConnectionMDB;
import Model.Position;
import Model.Utilisateur;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.util.ArrayList;

public class main {

    public static void main(String args[]){
        ArrayList listUtil =new ArrayList();
        Position pos = new Position(2,3);
        Utilisateur user = new Utilisateur(1,"second.test@gmail.com","password","0670252682", listUtil,pos);

        ConnectionMDB connectionMDB= new ConnectionMDB();


        //AJOUT USER
        connectionMDB.saveUser(user);

        //RECUPERATION USER
        Utilisateur userRecup = connectionMDB.getUser("mail","second.test@gmail.com");
        //System.out.println(userRecup.getPhoneId());
    }
}
