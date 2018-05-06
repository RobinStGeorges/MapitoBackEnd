package Model;

import com.mongodb.MongoClient;

import java.net.UnknownHostException;

public class connectionMDB {

    MongoClient mongo;
    public connectionMDB(){
        try {
            this.mongo = new MongoClient( "localhost" , 27017 );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public MongoClient getConnection(){
        return this.mongo;
    }

}
