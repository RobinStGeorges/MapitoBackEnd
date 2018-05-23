
import Model.Friend;
import Model.Position;
import Model.Utilisateur;
import com.mongodb.*;
import org.mongodb.morphia.*;
import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;


import java.net.UnknownHostException;
import java.util.ArrayList;


public class Testing {
    public static void main(String[] args) throws UnknownHostException {
//        Morphia m = new Morphia();
//
//        // create the Datastore connecting to the default port on the local host
//        final Datastore ds = m.createDatastore(new MongoClient("localhost" , 27017 ), "mapito");
//        ds.ensureIndexes();
//
//        m.map(Utilisateur.class);
////        m.map(Position.class);
////        m.map(Friend.class);
//
//        ds.ensureIndexes(); //creates all defined with @Indexed
//        ds.ensureCaps(); //creates all collections for @Entity(cap=@CappedAt(...))
//
//        Utilisateur userRecup = new Utilisateur("mail.gmail@gmail.com","motdepasse","0670252682",new ArrayList(),new Position(2,1));
//        ds.save(userRecup);



         MorphiaService morphiaService;
         UserDAO userDAO;

        morphiaService = new MorphiaService();
        userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur user = new Utilisateur("mail.gmail@gmail.com","motdepasse","0670252682",new ArrayList(),new Position(2,1));
        userDAO.save(user);

         Utilisateur  fetchedUser = userDAO.getByEmail("mail.gmail@gmail.com");
        System.out.println(fetchedUser.getPhoneId());
    }
}
