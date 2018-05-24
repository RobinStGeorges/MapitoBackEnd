
import Model.Friend;
import Model.Position;
import Model.Utilisateur;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import org.mongodb.morphia.*;
import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;


import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Testing {
    public static void main(String[] args) throws UnknownHostException {

        try{
            Utilisateur test = new Utilisateur("mail.gmail@gmail.com","motdepasse","0670252682",new ArrayList(),new Position(2,1));
            ObjectMapper mapation = new ObjectMapper();
            String jsonette = mapation.writeValueAsString(test);
            System.out.println(jsonette);
        }catch(IOException e){}
//
//         MorphiaService morphiaService;
//         UserDAO userDAO;
//
//        morphiaService = new MorphiaService();
//        userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
//        Utilisateur user = new Utilisateur("mail.gmail@gmail.com","motdepasse","0670252682",new ArrayList(),new Position(2,1));
//        userDAO.save(user);
//
//        Utilisateur  fetchedUser = userDAO.getByEmail("mail.gmail@gmail.com");
//        System.out.println(fetchedUser.getPhoneId());
//        userDAO.updateByEmail("mail.gmail@gmail.com","phoneId","0102030405");
//        fetchedUser = userDAO.getByEmail("mail.gmail@gmail.com");
//        System.out.println(fetchedUser.getPhoneId());
    }
}
