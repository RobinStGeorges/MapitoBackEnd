package Control;

import Model.Notification;
import Model.TokenTournament;
import Model.dto.GetFriendDTO;
import com.google.gson.Gson;

import Model.Position;
import Model.Utilisateur;

import service.MorphiaService;
import service.SendMail;
import service.UserDAO;
import service.UserDaoImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/users")
public class UserController {

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
    /**
     *
     */
    public String updateUser(@PathParam("token")String token,@PathParam("field") String field,
                             @PathParam("value") String value ) throws UnknownHostException {

        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
            Utilisateur fetchedUser = userDAO.getByToken(token);
            if (fetchedUser!= null) {
                userDAO.updateByToken(token, field, value);

                return "200";
            }
            else{
                throw new RuntimeException("400");
            }

    }

    /**
     * R
     */
    @PUT
    @Path("/updatePos/{token}/{lon}/{lat}")
    @Consumes("text/plain")
    /**
     * update lat lon lastlat & lastlon of a user using token
     */
    public String updateUserPos(@PathParam("token") String token,@PathParam("lon") double lon,@PathParam("lat") double lat) throws UnknownHostException {
        System.out.println("in");
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur fetchedUser = userDAO.getByToken(token);

        if(fetchedUser == null){
            return "400";
        }

        //last latitude
        double last = fetchedUser.getPos().getLatitude();
        fetchedUser.getPos().setLastlatitude(last);

        //last longitude
        last = fetchedUser.getPos().getLongitude();
        fetchedUser.getPos().setLastlongitude(last);

        // new latitude et longitude
        fetchedUser.getPos().setLatitude(lat);
        fetchedUser.getPos().setLongitude(lon);
        System.out.println("last");
        //mise a jours du users
        userDAO.updatePosByToken(token,fetchedUser.getPos());

        return "200";

    }




    /*TODO */
    @GET
    @Path("/getField/{token}/{field}")
    /**
     * return the value fro the field requested of the user using token
     */
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

    @PUT
    @Path("addFriend/{token}/{mail}")
    /**
     * R
     * add the friend using is mail to the user list
     */
    public String addFriend(@PathParam("token")String token,@PathParam("mail")String mail) throws UnknownHostException {
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        Utilisateur user2Add = userDAO.getByEmail(mail);

        ArrayList<Utilisateur> AL;
        AL=fetchedUser.getFriends();

        if (user2Add != null){
            AL.add(user2Add);
            userDAO.updateFriendsByToken(token, AL );
            return "200";
        }
        else{
            return "400";
        }


    }

    @GET
    @Path("/getNotifications/{token}")
    /**
     * R
     */
    public ArrayList<Notification> getUserNotification(@PathParam("token")String token) throws UnknownHostException {
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);
        System.out.println(fetchedUser.getListeNotifications());
        return fetchedUser.getListeNotifications();

    }

    @POST
    @Path("/addNotification/{token}")
    public String addUserNotification(@PathParam("token")String token) throws UnknownHostException {
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        if(fetchedUser.getListeNotifications() == null){
            System.out.println("null");
        }
        ArrayList<Notification> listeNotifs;
        listeNotifs=fetchedUser.getListeNotifications();

        Notification notif = new Notification("addFriend","---"+"unMailrandom@gmail.com"+"--- want to add you ! ");
        listeNotifs.add(notif);
        userDAO.updateNotifsByToken(token,listeNotifs);
        return "200";
    }

    @PUT
    @Path("/sendProxNotif/{token}/{mail}")
    public void sendUserProxNotif(@PathParam("token")String token,@PathParam("mail")String mail){
        
    }

    @GET
    @Path("/getFriends/{token}")
    /**
     * A
     * return a json with positions of friends & distance from the user
     */
    public ArrayList<GetFriendDTO> getUserFriends(@PathParam("token") String token) throws UnknownHostException{

        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur fetchedUser = userDAO.getByToken(token);

        ArrayList<GetFriendDTO> friends = new ArrayList<>();//ok

        ArrayList<Utilisateur> listeFriends=fetchedUser.getFriends();

        if(listeFriends != null){
            for (Utilisateur friend : listeFriends){
                String mail=friend.getMail();
                String prenom=friend.getPrenom();
                Position pos = friend.getPos();
                double latitude =  friend.getPos().getLatitude();
                double longitude = friend.getPos().getLongitude();
                double lastlat =  friend.getPos().getLastlatitude();
                double lastlon =  friend.getPos().getLastlongitude();
                double distance = friend.getPos().distance(fetchedUser.getPos().getLatitude(),fetchedUser.getPos().getLongitude(),latitude,longitude,"K");
                boolean inTheArea = false;
                if(distance < 0.3){
                    inTheArea=true;
                }
                GetFriendDTO dtoF = new GetFriendDTO( mail, prenom, inTheArea,latitude,longitude,lastlat,lastlon);
                friends.add(dtoF);
            }

        }
            return friends;
    }
    @PUT
    @Path("/sendmail/{token}")
    public void resetmdp(@PathParam("token") String token)throws UnknownHostException{
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur fetchedUser = userDAO.getByToken(token);


        String tablounet [] =  {"Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M","1","2","3","4","5","6","7","8","9","0","q","w","e","r","t","y","u","i","o","p","a","s","d","f","g","h","j","k","l","z","x","c","v","b","n"};
        String newpassword = "";
        for(int i=0 ;i < 16 ; i ++){

            newpassword = newpassword + tablounet[(int)(Math.random() * ( tablounet.length ) ) ];
        }
        userDAO.updateByToken("token","password",newpassword);
        SendMail.sendMessage("reset password","Voici votre nouveau mot de passe :"+newpassword,fetchedUser.getMail(),"");
    }

}
