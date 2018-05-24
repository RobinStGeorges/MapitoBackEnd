package conf;

import Model.Friend;
import Model.Position;
import Model.Utilisateur;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;



public class ConnectionMDB {

    private MongoClient mongo;
    private DBCollection dbCollection;
    public ConnectionMDB(){
        this.mongo = new MongoClient( "localhost" , 27017 );
    }

    public DBCollection getConnectionUtilisateurs(String collection){
        DB db = mongo.getDB("mapito");
        dbCollection = db.getCollection("utilisateurs");
        return dbCollection;
    }

    /**
     *
     * @param user
     */
    public void saveUser (Utilisateur user){
        dbCollection = new ConnectionMDB().getConnectionUtilisateurs("Utilisateurs");
        BasicDBObject dbo= new BasicDBObject();

        dbo.put("mail",user.getMail());
        dbo.put("password",user.getPassword());
        dbo.put("phoneId",user.getPhoneId());
        dbo.put("friends",user.getFriends());
        dbo.put("pos",user.getPos().getPosString());

        dbCollection.insert(dbo);
    }

    public Utilisateur getUser(String fieldName,String value) {
        dbCollection = new ConnectionMDB().getConnectionUtilisateurs("Utilisateurs");
        BasicDBObject dbo = new BasicDBObject();
        List<DBObject> myList = null;

        dbo.put(fieldName, value);
        DBCursor dbCursor = dbCollection.find(dbo);
        myList = dbCursor.toArray();


        int id = (Integer) myList.get(0).get("id");
        String mail = (String) myList.get(0).get("mail");
        String password = (String) myList.get(0).get("password");
        String phoneId = (String) myList.get(0).get("phoneId");
        ArrayList<Friend> friends =(ArrayList<Friend>)  myList.get(0).get("friends");
        String posString = (String) myList.get(0).get("pos");
        String[] split = posString.split("-");
        Position pos = new Position(Float.parseFloat(split[0]), Float.parseFloat(split[1]));

        return new Utilisateur(mail, password, phoneId, friends, pos);
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("id",id);
//        jsonObj.put("mail",mail);
//        jsonObj.put("password",password);
//        jsonObj.put("phoneId",phoneId);
//        jsonObj.put("friends",friends);
//        jsonObj.put("pos",pos);
//
//        return jsonObj;

    }

}
