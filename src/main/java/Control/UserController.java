package Control;

import Model.TokenTournament;
import com.google.gson.Gson;

import Model.Position;
import Model.Utilisateur;

import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.UnknownHostException;
import java.util.ArrayList;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/users")
public class UserController {


    //RESTEASY003065: Cannot consume content type !!!!!!
    /**
     * R
     */
    @GET
    @Produces("text/plain")
    @Path("/get/{mail}")
    public String getPosUser(@PathParam("mail") String mail) throws UnknownHostException {

        MorphiaService morphiaService;
        UserDAO userDAO;

        morphiaService = new MorphiaService();
        userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur fetchedUser = userDAO.getByEmail("mail.gmail@gmail.com");
        fetchedUser.setPos(new Position(5,5));// pour les tests
        Gson gson = new Gson();
        String json = gson.toJson(fetchedUser.getPos());
        return json;

    }

    /**
     * A
     */
    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test(){
        return "Hello world";
    }

    /**
     * R
     */
    @GET
    @Path("/authenticate/{mail}/{mdp}")
    public String connectionUser(@PathParam("mail") String mail,@PathParam("mdp") String password) throws UnknownHostException {

        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur userRecup = userDAO.getByEmail(mail);

        if (userRecup.getPassword().equals(password)) {
            TokenTournament tekken = new TokenTournament();
            String token = tekken.generateToken(mail);
            userDAO.updateByEmail(mail,"token",token);
            return token;
        }else{
            return null;//si mauvaise authentification
        }

    }

    /**
     * R
     */
    @POST
    @Path("/new/{mail}/{mdp}/{nom}/{prenom}")
    public void newUser(@PathParam("mail") String mail, @PathParam("mdp") String mdp, @PathParam("nom") String nom,@PathParam("prenom") String prenom ) throws UnknownHostException {
        MorphiaService morphiaService;
         UserDAO userDAO;

        morphiaService = new MorphiaService();
        userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur user = new Utilisateur(mail,mdp,nom,prenom);
        userDAO.save(user);
    }


    /**
     * R
     */
    @PUT
    @Path("/update/{mail}/{password}/{nom}/{prenom}/{token}")
    @Consumes("text/plain")
    public String updateUser(@PathParam("mail") String mail,@PathParam("password") String mdp,
                             @PathParam("nom") String nom ,@PathParam("prenom") String prenom,
                             @PathParam("token")String token) throws UnknownHostException {

         MorphiaService morphiaService= new MorphiaService();
         UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur fetchedUser = userDAO.getByEmail(mail);
        String segments[] = fetchedUser.getToken().split(":");
        String segments2[] =token.split(":");
        String cleToken= segments[1];
        String cleToken2= segments[1];
        System.out.println(cleToken);
         if (cleToken2.equals(cleToken)){
             userDAO.updateByEmail(mail,"password",mdp);
             userDAO.updateByEmail(mail,"nom",nom);
             userDAO.updateByEmail(mail,"prenom",prenom);
             return "completed";
         }
         else{
             throw new RuntimeException("Mon erreur");
         }


        /*DANS LE FRONT : si "" -> mettre la valeur a celle deja presente dans le user*/
    }

    @GET
    @Path("/getFriendsPosition/{mail}")
    public ArrayList<Position> getFriendsDistance(@PathParam("mail") String mail) throws UnknownHostException {
        ArrayList<Position> listePosition = new ArrayList<>();
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur fetchedUser = userDAO.getByEmail(mail);


        return listePosition;
    }

    @GET
    @Path("/exception")
    /**
     * R
     */
    public Response exception() {
        throw new RuntimeException("Mon erreur");
    }
}
