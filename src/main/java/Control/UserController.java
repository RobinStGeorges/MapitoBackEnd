package Control;

import Model.TokenTournament;
import com.google.gson.Gson;
import com.mongodb.util.JSON;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import conf.ConnectionMDB;
import Model.Position;
import Model.Utilisateur;
import com.mongodb.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.ArrayList;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/users")
public class UserController {


    //RESTEASY003065: Cannot consume content type !!!!!!
    @GET
    @Path("/get/{mail}/{token}")
    public String getPosUser(@PathParam("mail") String mail, @PathParam("token") String token) throws UnknownHostException {

        ConnectionMDB connectionMDB= new ConnectionMDB();
        MorphiaService morphiaService;
        UserDAO userDAO;

        morphiaService = new MorphiaService();
        userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur fetchedUser = userDAO.getByEmail("mail.gmail@gmail.com");
        if (fetchedUser.getToken()!= null){ // a verifier
            Gson gson = new Gson();
            String json = gson.toJson(fetchedUser.getPos());

            return json;
        }
        else{
            return null;
        }
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test(){
        return "Hello world";
    }

    @POST
    @Path("/authenticate")
    public String connectionUser(String mail, String password) throws UnknownHostException {

        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur userRecup = userDAO.getByEmail(mail);
        if (userRecup.getPassword().equals(password)) {
            TokenTournament tekken = new TokenTournament();
            String token = tekken.generateToken(mail);
            userDAO.updateByEmail(mail,"token",token);
            return token;
        }else{
            return "error 8012";
        }

    }

    @POST
    @Path("/new/{mail}/{mdp}/{nom}/{prenom}")
    public void newUser(@PathParam("mail") String mail, @PathParam("mdp") String mdp, String nom, String prenom ){
        ConnectionMDB connectionMDB= new ConnectionMDB();
        connectionMDB.saveUser(new Utilisateur(mail,mdp,nom,prenom));
    }



    @PUT
    @Consumes("text/plain")
    public void updateUser(String mail,String field, String value) throws UnknownHostException {
         MorphiaService morphiaService= new MorphiaService();
         UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        userDAO.updateByEmail(mail,field,value);
    }
}
