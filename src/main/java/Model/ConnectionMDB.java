package Model;

import com.mongodb.*;

import java.net.UnknownHostException;

public class ConnectionMDB {

    MongoClient mongo;
    DBCollection dbCollection;
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

    public void getUser(String fieldName,String value){
        dbCollection = new ConnectionMDB().getConnectionUtilisateurs("utilisateurs");
        BasicDBObject dbo= new BasicDBObject();
        dbo.put(fieldName,value);
        DBCursor dbCursor = dbCollection.find(dbo);
        while(dbCursor.hasNext()){
            System.out.println(dbCursor.next());
        }
    }

}
