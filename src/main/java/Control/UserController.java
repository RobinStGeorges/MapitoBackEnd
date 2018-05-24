package Control;

import com.sun.org.apache.bcel.internal.classfile.Constant;
import conf.ConnectionMDB;
import Model.Position;
import Model.Utilisateur;
import com.mongodb.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Key;
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
    public String connectionUser(String mail, String password){



//        try {
//            Boolean valid = false;
//            ConnectionMDB connectionMDB= new ConnectionMDB();
//            Utilisateur userRecup = connectionMDB.getUser("mail" , mail);
//            if( userRecup.getPassword().equals(password)){
//                valid=true;
//            }
//            else valid=false;
//
//
//            // Issue a token for the user
//            String token = issueToken(mail);
//
//            // Return the token on the response
//            return Response.ok(token).build();
//
//        } catch (Exception e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
        return "hello";

    }

    @POST
//    @JWTTokenNeeded
    public void newUser(@PathParam("user") Utilisateur user){

        ConnectionMDB connectionMDB= new ConnectionMDB();
        connectionMDB.saveUser(user);
    }

    //RESTEASY003065: Cannot consume content type !!!!!!
    @GET
    @Consumes(MediaType.TEXT_HTML)
    @Produces("text/plain")
//    @JWTTokenNeeded
    public String getUser(@PathParam("mail")String mail){
        ConnectionMDB connectionMDB= new ConnectionMDB();
        Utilisateur userRecup = connectionMDB.getUser("mail",mail);
        System.out.println(userRecup.getPhoneId());
        return userRecup.getPhoneId();
    }


    @PUT
//    @JWTTokenNeeded
    public void updateUser(@PathParam("mail") String mail){
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
