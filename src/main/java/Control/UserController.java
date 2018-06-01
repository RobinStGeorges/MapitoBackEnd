package Control;

import Model.*;
import Model.dto.*;

import com.google.gson.Gson;
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

/*TODO faire les codes dans header */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/users")
public class UserController {

    private final UserDAO userDAO = new UserDaoImpl(Utilisateur.class, new MorphiaService().getDatastore());

    /**
     * R
     * HeaderAuthentification
     */
    @PUT
    @Path("/authenticate")
    public Response authenticate(UserDTO dto) {
        Utilisateur userRecup = userDAO.getByEmail(dto.mail);

        if (!userRecup.getPassword().equals(dto.password)) {
            return Response.status(403).build();
        }

        TokenTournament tekken = new TokenTournament();
        String token = tekken.generateToken();
        userDAO.updateByEmail(dto.mail,"token", token);
        return Response.ok(new Gson().toJson(new TokenDTO(token))).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(UserDTO dto) {
        /*verification si un utilisateur possède dejà ce mail */
        Utilisateur fetchedUser = userDAO.getByEmail(dto.mail);
        if (fetchedUser != null){
            return Response.status(403).build();
        }

        Utilisateur user = new Utilisateur(dto);
        userDAO.save(user);
        return Response.ok().build();
    }


    /*TODO generer methode qui reçoit email, creer un mdp, le set dans le user puis envois email + mdp a l'email */

    /**
     * R
     * refaire avec token field value
     */
    @PUT
    public Response updateUser(UpdateUserDTO updateUserDTO) {
        Utilisateur fetchedUser = userDAO.getByToken(updateUserDTO.token);
        if (fetchedUser != null) {
            userDAO.updateByToken(updateUserDTO.token, updateUserDTO.field, updateUserDTO.value);
            return Response.ok().build();
        }
        return Response.status(401).build();
    }

    /**
     * R
     */
    @PUT
    @Path("/position")
    public Response updateUserPos(UpdatePosDTO updatePosDTO) {
        Utilisateur fetchedUser = userDAO.getByToken(updatePosDTO.token);

        if(fetchedUser == null){
            return Response.status(401).build();
        }

        //last latitude
        double last = fetchedUser.getPos().getLatitude();
        fetchedUser.getPos().setLastlatitude(last);

        //last longitude
        last = fetchedUser.getPos().getLongitude();
        fetchedUser.getPos().setLastlongitude(last);

        // new latitude et longitude
        fetchedUser.getPos().setLatitude(updatePosDTO.lat);
        fetchedUser.getPos().setLongitude(updatePosDTO.lon);
        System.out.println("last");
        //mise a jours du users
        userDAO.updatePosByToken(updatePosDTO.token,fetchedUser.getPos());

        return Response.ok().build();

    }

    /**
     * return the value fro the field requested of the user using token
     */
    @GET
    @Path("/field")
    public Response getUserField (UserDTO userDto) {
        Utilisateur fetchedUser = userDAO.getByToken(userDto.token);
        if (fetchedUser == null) {
            return Response.status(401).build();
        }
        String result;
        switch (userDto.field)
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
            case"password":
                result=fetchedUser.getPassword();
                break;
            default:
                return Response.status(404).build();
        }
        return Response.ok(new Gson().toJson(new UpdateUserDTO(userDto.token, userDto.field, result))).build();
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

    @PUT
    @Path("addFriend/{token}/{mail}")
    /**
     * R
     * add the friend using its mail to the user list
     */
    public String addFriend(@PathParam("token")String token,@PathParam("mail")String mail) throws UnknownHostException {
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        Utilisateur user2Add = userDAO.getByEmail(mail);

        ArrayList<Friend> AL;
        AL = fetchedUser.getFriends();

        Friend newFriend = new Friend(mail, false, false);

        if (user2Add != null) {
            Iterator<Friend> iteratorF = AL.iterator();
            boolean trouve = false;
            while (iteratorF.hasNext()) {

                Friend friend = iteratorF.next();

                if (friend.getMail().equals(mail)) {

                    iteratorF.remove();
                    trouve = true;

                }
            }
            AL.add(newFriend);
            userDAO.updateFriendsByToken(token, AL);
            return "200";
        } else {
            return "400";
        }

    }







    /**
     * R
     * gerer les codes erreurs
     */
    @PUT
    @Path("/sendProxNotif")
    public Response sendUserProxNotif(SendNotifDTO sendNotifDTO){
        Utilisateur fetchedUser = userDAO.getByToken(sendNotifDTO.token);
        if(fetchedUser == null){
            return Response.status(401).build();
        }
        ArrayList<Notification> listeNotifs = fetchedUser.getListeNotifications();
        ArrayList<Friend> listeFriends = fetchedUser.getFriends();
        Notification notif = new Notification(1,"L'utilisateur "+sendNotifDTO.mail+" "+sendNotifDTO.contenu);

        listeNotifs.add(notif);
        userDAO.updateNotifsByToken(sendNotifDTO.token,listeNotifs);
        return Response.ok().build();
    }
    /**
     * R
     * delete notif with titre   gerer les codes erreurs
     */
    @PUT
    @Path("/deleteNotif/{token}/{titre}")
    public Response deleteUserNotif(@PathParam("token")String token,@PathParam("titre")int titre){
        Utilisateur fetchedUser = userDAO.getByToken(token);
        if(fetchedUser == null){
            return Response.status(401).build();
        }
        ArrayList<Notification> listeNotifs = fetchedUser.getListeNotifications();
        Iterator<Notification> iterator = listeNotifs.iterator();

        for(Notification not : listeNotifs){
            if(not.getType()==(titre)){
                listeNotifs.remove(not);
                userDAO.updateNotifsByToken(token,listeNotifs);
                return Response.ok().build();
            }
        }
        return Response.status(404).build();
    }



    @PUT
    @Path("deleteUser/{token}")
    public Response deleteUser(TokenDTO tokenDto) throws UnknownHostException {
        Utilisateur fetchedUser = userDAO.getByToken(tokenDto.token);
        if(fetchedUser == null){
            return Response.status(401).build();
        }
        userDAO.deleteByToken(tokenDto.token);
        return Response.ok().build();
    }

    @PUT
    @Path("/resetUserMdp/{mail}")
    public Response resetmdp(@PathParam("mail") String mail)throws UnknownHostException{
        Utilisateur fetchedUser = userDAO.getByEmail(mail);
        if(fetchedUser == null){
            return Response.status(401).build();
        }

        String tablounet [] =  {"Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M","1","2","3","4","5","6","7","8","9","0","q","w","e","r","t","y","u","i","o","p","a","s","d","f","g","h","j","k","l","z","x","c","v","b","n"};
        String newpassword = "";
        for(int i=0 ;i < 16 ; i ++){
            newpassword = newpassword + tablounet[(int)(Math.random() * ( tablounet.length ) ) ];
        }
        userDAO.updateByEmail(mail,"password",newpassword);
        SendMail.sendMessage("reset password","Voici votre nouveau mot de passe :"+newpassword,fetchedUser.getMail(),"mapitoLerance@gmail.com");
        return Response.ok().build();
    }

}
