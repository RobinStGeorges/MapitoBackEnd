package Control;
import Model.Position;
import conf.ConnectionMDB;
import Model.*;
import Model.Utilisateur;
import com.mongodb.*;
import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.UnknownHostException;
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
    public String connectionUser(String mail, String password)throws UnknownHostException {
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());;
        Utilisateur userRecup = userDAO.getByEmail( mail);
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
//    @JWTTokenNeeded
    public void newUser( Utilisateur user)throws UnknownHostException{
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());;
        userDAO.save(user);
    }

    @POST
    public void addUser(@QueryParam("mail") String mail, String token,Friend friend)throws UnknownHostException{
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());;
        Utilisateur user =  userDAO.getByEmail(mail);
        TokenTournament toktok = new TokenTournament();
        if(toktok.checkToken(token,user)){
            user.getFriends().add(friend);
            userDAO.updateByEmail(mail,"Friends",user.getFriends());
        }
    }

    @GET
    @Consumes(MediaType.TEXT_HTML)
    @Produces("text/plain")
//    @JWTTokenNeeded
    public Utilisateur getUser(@QueryParam("mail") String mail,String token)throws UnknownHostException{
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur user =  userDAO.getByEmail(mail);
        return user;
    }


    @PUT
//    @JWTTokenNeeded
    public void updateUser(String mail,String field, String value)throws UnknownHostException{
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        userDAO.updateByEmail(mail,field,value);
    }
}
