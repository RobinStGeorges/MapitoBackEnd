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
//    /**
//     * R
//     * TODO refaire avec token
//     */
//    @GET
//    @Produces("text/plain")
//    @Path("/get/{mail}")
//    public Position getPosUser(@PathParam("mail") String mail) throws UnknownHostException {
//
//        MorphiaService morphiaService;
//        UserDAO userDAO;
//
//        morphiaService = new MorphiaService();
//        userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
//        Utilisateur fetchedUser = userDAO.getByEmail("mail.gmail@gmail.com");
//        fetchedUser.setPos(new Position(5,5));// pour les tests
//        Position pos = fetchedUser.getPos();
//        return pos;
//
//    }

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
     * HeaderAuthentification
     */
    @PUT//get
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
    public int newUser(@PathParam("mail") String mail, @PathParam("mdp") String mdp, @PathParam("nom") String nom,@PathParam("prenom") String prenom ) throws UnknownHostException {
        MorphiaService morphiaService;
         UserDAO userDAO;

        morphiaService = new MorphiaService();
        userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        /*verification si un utilisateur possède dejà ce mail */
        Utilisateur fetchedUser = userDAO.getByEmail(mail);
        if (fetchedUser != null){
            return 500;
        }

        Utilisateur user = new Utilisateur(mail,mdp,nom,prenom);
        userDAO.save(user);
        return 200;
        /*TODO gerer les code erreurs400   */


    }


    /*TODO generer methode qui reçoit email, creer un mdp, le set dans le user puis envois email + mdp a l'email */



    /**
     * R
     * refaire avec token field value
     */
    @PUT
    @Path("/update/{token}/{field}/{value}")
    @Consumes("text/plain")
    public String updateUser(@PathParam("token")String token,@PathParam("field") String field,
                             @PathParam("value") String value ) throws UnknownHostException {

        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        /*parse du token received*/
        String segmentsReceived[] =token.split(":");;
        String mailReceived = segmentsReceived[0];
        String cleTokenReceived = segmentsReceived[1];

        /*get user token & parsed it */
        Utilisateur fetchedUser = userDAO.getByEmail(mailReceived);
        String segments[] = fetchedUser.getToken().split(":");
        String cleToken= segments[1];

        System.out.println(cleToken);
        if (cleTokenReceived.equals(cleToken)){
            userDAO.updateByToken(mailReceived,field,value);

            return "completed";
        }
        else{
            throw new RuntimeException("Mon erreur");
        }


        /*DANS LE FRONT : si "" -> mettre la valeur a celle deja presente dans le user*/
    }

    /**
     * R
     */
    @PUT
    @Path("/updatePos/{mail}/{lon}/{lat}/{token}")
    @Consumes("text/plain")
    public String updateUserPos(@PathParam("mail") String mail,@PathParam("lon") double lon,
                             @PathParam("lat") double lat,@PathParam("token") String token) throws UnknownHostException {

        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur fetchedUser = userDAO.getByEmail(mail);
        String segments[] = fetchedUser.getToken().split(":");
        String segments2[] =token.split(":");
        String cleToken= segments[1];
        String cleToken2= segments[1];
//        System.out.println(cleToken);
        if (cleToken2.equals(cleToken)){
            Position position = new Position(lat,lon);
            userDAO.updatePosByEmail(mail,position);

            return "completed";
        }
        else{
            throw new RuntimeException("Mon erreur");
        }
    }


//    @GET
//    @Path("/getFriendsPosition/{token}")
//    public ArrayList<Position> getFriendsDistance(@PathParam("token") String token) throws UnknownHostException {
//        ArrayList<Position> listePosition = new ArrayList<>();
//        MorphiaService morphiaService= new MorphiaService();
//
//        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
//        String mail = token.split(":");
//        Utilisateur fetchedUser = userDAO.getByEmail(mail);
//
//
//        return listePosition;
//    }


    /*TODO */
    @GET
    @Path("/getField/{token}/{field}")
    public String getUserField (@PathParam("token") String token, @PathParam("field") String field)
            throws UnknownHostException {
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);
        String result;
        switch (field)
        {
            case "token":
                result=fetchedUser.getToken();
                break;
            case"mail":
                result=fetchedUser.getMail();
                break;
            case"nom":
                result=fetchedUser.getNom();
                break;
            case"prenom":
                result=fetchedUser.getPrenom();
                break;
            default:
                result="error";
                break;
        }
        return result;

    }

}
