package Model;

import com.mongodb.*;
import com.mongodb.util.JSON;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;



public class ConnectionMDB {

    private MongoClient mongo;
    private DBCollection dbCollection;
    public ConnectionMDB(){
        try {
            this.mongo = new MongoClient( "localhost" , 27017 );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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
        dbCollection = new ConnectionMDB().getConnectionUtilisateurs("utilisateurs");
        BasicDBObject dbo= new BasicDBObject();

        dbo.put("id",user.getId());
        dbo.put("mail",user.getMail());
        dbo.put("password",user.getPassword());
        dbo.put("phoneId",user.getPhoneId());
        dbo.put("friends",user.getFriend());
        dbo.put("pos",user.getPos().getPosString());

        dbCollection.insert(dbo);
    }

    public Utilisateur getUser(String fieldName,String value){
        dbCollection = new ConnectionMDB().getConnectionUtilisateurs("utilisateurs");
        BasicDBObject dbo= new BasicDBObject();
        List<DBObject> myList = null;

        dbo.put(fieldName,value);
        DBCursor dbCursor = dbCollection.find(dbo);
        myList = dbCursor.toArray();


        int id = (Integer)myList.get(0).get("id");
        String mail =(String)myList.get(0).get("mail");
        String password =(String)myList.get(0).get("password");
        String phoneId =(String)myList.get(0).get("phoneId");
        ArrayList friends =(ArrayList) myList.get(0).get("friends");
        String posString =(String)myList.get(0).get("pos");
        String[] split = posString.split("-");
        Position pos = new Position(Float.parseFloat(split[0]),Float.parseFloat(split[1]));

      Utilisateur user =new Utilisateur(id,mail,password,phoneId,friends,pos);


        JSON json = new JSON();
    }



}
