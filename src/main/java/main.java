import Model.ConnectionMDB;
import Model.Utilisateur;
import com.mongodb.MongoClient;

public class main {

    public static void main(String args[]){
        Utilisateur user = new Utilisateur();
        ConnectionMDB cmdb = new ConnectionMDB();
        MongoClient db = cmdb.getConnection();
    }
}
