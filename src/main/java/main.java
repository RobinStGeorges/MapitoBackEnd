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
        Utilisateur user = new Utilisateur(1,"premier.test@gmail.com","password","0670252682", listUtil,pos);
        DBCollection dbCollection = new ConnectionMDB().getConnection("utilisateurs");
        BasicDBObject dbo= new BasicDBObject();
        String mail = user.getMail();
        dbo.put("id",user.getId());
        dbo.put("mail",user.getMail());
        dbo.put("password",user.getPassword());
        dbo.put("phoneId",user.getPhoneId());
        dbo.put("friends",user.getFriend());
        //dbo.put("pos",user.getPos());
        dbCollection.insert(dbo);

        //AJOUT


        //RECUPERATION
    }
}
