package Model;

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

    public MongoClient getConnection(){
        return this.mongo;
    }

}
