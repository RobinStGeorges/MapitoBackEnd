package Control;

import conf.ConnectionMDB;
import Model.Position;
import Model.Utilisateur;
import com.mongodb.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/users")
public class UserController {


    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test(){
        return "Hello world";
    }

    @POST
    @Path("/authenticate")
    public boolean connectionUser(String mail, String password){
        ConnectionMDB connectionMDB= new ConnectionMDB();
        Utilisateur userRecup = connectionMDB.getUser("mail" , mail);
        return userRecup.getPassword().equals(password);
    }

    @POST
    public void newUser(Utilisateur user){

        ConnectionMDB connectionMDB= new ConnectionMDB();
        connectionMDB.saveUser(user);
    }

    @GET
    public void getUser(@QueryParam("email") String mail){
        ConnectionMDB connectionMDB= new ConnectionMDB();
        Utilisateur userRecup = connectionMDB.getUser("mail",mail);
        System.out.println(userRecup.getPhoneId());
    }


    @PUT
    public void updateUser(String mail){
        ConnectionMDB connectionMDB= new ConnectionMDB();
        try {
            //UPDATE USER
            DBCollection dbCollection = connectionMDB.getConnectionUtilisateurs("utilisateurs");
            BasicDBObject oldUser = new BasicDBObject();
            oldUser.put("mail", mail);
            DBObject oOldUser = dbCollection.findOne(oldUser);

            DBObject oNewUser = new BasicDBObject();

            oNewUser.put("id", oOldUser.get("id"));
            oNewUser.put("mail", "updatedMail@gmail.com");
            oNewUser.put("password", oOldUser.get("password"));
            oNewUser.put("phoneId", oOldUser.get("phoneId"));
            oNewUser.put("friends", oOldUser.get("friends"));
            oNewUser.put("pos", oOldUser.get("pos"));

            dbCollection.update(oOldUser, oNewUser);
        }catch(NullPointerException e){
            System.out.println(e);
        }
    }
}
