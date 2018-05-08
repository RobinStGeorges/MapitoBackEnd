package Model;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

public class ConnectionMDB {

    MongoClient mongo;
    public ConnectionMDB(){
        try {
            this.mongo = new MongoClient( "localhost" , 27017 );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public DBCollection getConnection(String collection){
        DB db = mongo.getDB("mapito");
        DBCollection dbCollection = db.getCollection("collection");
        return dbCollection;
    }

}
