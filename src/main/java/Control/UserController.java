package Control;

import Model.*;
import Model.dto.*;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import service.MorphiaService;
import service.SendMail;
import service.UserDAO;
import service.UserDaoImpl;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/users")
public class UserController {
    private final UserDAO userDAO = new UserDaoImpl(Utilisateur.class, new MorphiaService().getDatastore());

    /**
     * Authenticate the user using mail and password
     * @param dto
     * @return
     */
    @PUT
    @Path("/authenticate")
    public Response authenticate(UserDTO dto) {

        Utilisateur userRecup = userDAO.getByEmail(dto.mail);

        if( userRecup == null ){
            return Response.status(404).
                    entity("L'utilisateur n'a pas été trouvé. Merci de ré-essayer.").
                    build();
        }
        if (!userRecup.getPassword().equals(dto.password) || !userRecup.getMail().equals(dto.mail) ){
            return Response.status(403)
                    .entity("Le mail ou le mot de passe ne correspondent pas. Merci de réessayer.")
                    .build();
        }

        /* gives the connected user a new token */
        TokenTournament tekken = new TokenTournament();
        String token = tekken.generateToken();
        userDAO.updateByEmail(dto.mail,"token", token);

        return Response.ok(new Gson().toJson(new TokenDTO(token))).build();
    }

    /**
     * Create a new user
     * @param dto
     * @return
     */
    @POST
    public Response create(UserDTO dto) {

        Utilisateur fetchedUser = userDAO.getByEmail(dto.mail);

        /* Check if email is well constructed */
        EmailValidator emailValidator = EmailValidator.getInstance();
        boolean isvalid = emailValidator.isValid(dto.mail);
        if(!isvalid){
            return Response.status(403).entity("Le mail reçu n'est pas valide. Merci de vérifier la syntaxe.").build();
        }

        if (fetchedUser != null){
            return Response.status(403).entity("Un compte utilisant cet email existe déjà. Merci d'utiliser un autre email").build();
        }

        Utilisateur user = new Utilisateur(dto);
        userDAO.save(user);

        return Response.ok().entity("Bienvenue sur Mapito ! Vous pouvez maintenant vous connecter !").build();
    }


    /**
     * Update the provided field with the provided value
     * @param updateUserDTO
     * @return
     */
    @PUT
    public Response updateUser(UpdateUserDTO updateUserDTO) {

        Utilisateur fetchedUser = userDAO.getByToken(updateUserDTO.token);

        if (fetchedUser == null) {
            return Response.status(403).entity("L'utilisateur n'a pas été trouvé ! ").build();
        }
        else{

            if(updateUserDTO.field.equals("mail")){

                /* the provided mai already exist */
                if(userDAO.getByEmail(updateUserDTO.value) != null){
                    return Response.status(403).entity("Ce mail existe déjà !").build();
                }

                EmailValidator emailValidator = EmailValidator.getInstance();
                boolean isvalid = emailValidator.isValid(updateUserDTO.value);
                if(!isvalid){
                    return Response.status(403).entity("Le mail reçu n'est pas valide. Merci de vérifier la syntaxe.").build();
                }
            }

            userDAO.updateByToken(updateUserDTO.token, updateUserDTO.field, updateUserDTO.value);

            return Response.ok().entity("Vous avez bien mis à jour vos données !").build();
        }
    }

    /**
     * Update user's position
     * @param updatePosDTO
     * @return
     */
    @PUT
    @Path("/position")
    public Response updateUserPos(UpdatePosDTO updatePosDTO) {
        System.out.println("started updating pos");//debug console

        Utilisateur fetchedUser = userDAO.getByToken(updatePosDTO.token);

        if(fetchedUser == null){
            return Response.status(403).entity("L'utilisateur n'a pas été trouvé").build();
        }

        //last latitude
        double last = fetchedUser.getPos().getLatitude();
        if(last != fetchedUser.getPos().getLastlatitude()){
            fetchedUser.getPos().setLastlatitude(last);
        }


        //last longitude
        last = fetchedUser.getPos().getLongitude();
        fetchedUser.getPos().setLastlongitude(last);

        // new latitude et longitude
        fetchedUser.getPos().setLatitude(updatePosDTO.lat);
        fetchedUser.getPos().setLongitude(updatePosDTO.lon);

        //updating with new values
        userDAO.updatePosByToken(updatePosDTO.token,fetchedUser.getPos());

        System.out.println("updated succesfuly");//debug console

        return Response.ok().entity("Position bien mise à jour").build();

    }

    /**
     * Return the asked field's value
     * @param userDto
     * @return
     */
    @GET
    @Path("/field")
    public Response getUserField (UserDTO userDto) {
        String result;

        Utilisateur fetchedUser = userDAO.getByToken(userDto.token);

        if (fetchedUser == null) {
            return Response.status(404)
                    .entity(" L'utilisateur n'a pas été trouvé ")
                    .build();
        }

        /* Check what field is asked */
        switch (userDto.field)
        {
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
                return Response.status(404)
                        .entity("Le champ demandé n'a pas été trouvé ")
                        .build();
        }
        return Response.ok(new Gson().toJson(new UpdateUserDTO(userDto.token, userDto.field, result))).build();
    }


    /**
     * Send a notif to the user when one of is friend is near (500meters)
     * @param sendNotifDTO
     * @return
     */
    @PUT
    @Path("/sendProxNotif")
    public Response sendUserProxNotif(SendNotifDTO sendNotifDTO){

        Utilisateur fetchedUser = userDAO.getByToken(sendNotifDTO.token);

        if(fetchedUser == null){
            return Response.status(404).entity(" L'utilisateur n'a pas été trouvé ").build();
        }

        ArrayList<Notification> listeNotifs = fetchedUser.getListeNotifications();

        ArrayList<Friend> listeFriends = fetchedUser.getFriends();

        Notification notif = new Notification(1,
                "L'utilisateur "+sendNotifDTO.mail+" "+sendNotifDTO.contenu,fetchedUser.getMail());

        listeNotifs.add(notif);

        userDAO.updateNotifsByToken(sendNotifDTO.token,listeNotifs);

        return Response.ok().build();
    }

    /**
     * Delete the selected notification
     * @param token
     * @param titre
     * @return
     */
    @PUT
    @Path("/deleteNotif/{token}/{titre}")
    public Response deleteUserNotif(@PathParam("token")String token,@PathParam("titre")int titre){

        Utilisateur fetchedUser = userDAO.getByToken(token);
        if(fetchedUser == null){
            return Response.status(404)
                    .entity(" L'utilisateur n'a pas été trouvé ")
                    .build();
        }

        /* delete from the notification list */
        ArrayList<Notification> listeNotifs = fetchedUser.getListeNotifications();
        for(Notification not : listeNotifs){
            if(not.getType()==(titre)){
                listeNotifs.remove(not);
                userDAO.updateNotifsByToken(token,listeNotifs);
                return Response.ok().build();
            }
        }
        return Response.status(404).entity(" la notification n'est plus présente ").build();
    }

    /**
     * delete the user's account
     * @param tokenDto
     * @return
     */
    @PUT
    @Path("/deleteUser")
    public Response deleteUser(TokenDTO tokenDto){

        Utilisateur fetchedUser = userDAO.getByToken(tokenDto.token);

        if(fetchedUser == null){
            return Response.status(404)
                    .entity(" L'utilisateur n'a pas été trouvé ")
                    .build();
        }

        userDAO.deleteByToken(tokenDto.token);

        return Response.ok().entity(" L'utilisateur a bien été supprimé ").build();
    }

    /**
     * Reset the user's password and send it through email
     * @param userDTO
     * @return
     */
    @PUT
    @Path("/resetUserMdp")
    public Response resetmdp(UserDTO userDTO){

        Utilisateur fetchedUser = userDAO.getByEmail(userDTO.mail);

        if(fetchedUser == null){
            return Response.status(404).entity("Utilisateur non trouvé !").build();
        }

        String tablounet [] =  {"Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G",
                "H","J","K","L","Z","X","C","V","B","N","M","1","2","3","4","5","6","7","8",
                "9","0","q","w","e","r","t","y","u","i","o","p","a","s","d","f","g","h","j",
                "k","l","z","x","c","v","b","n"};

        String newpassword = "";

        /* Construc a new random password */
        for(int i=0 ;i < 16 ; i ++){
            newpassword = newpassword + tablounet[(int)(Math.random() * ( tablounet.length ) ) ];
        }

        userDAO.updateByEmail(userDTO.mail,"password",newpassword);

        SendMail.sendMessage("reset password","Voici votre nouveau " +
                "mot de passe :"+newpassword,fetchedUser.getMail(),"mapitoLerance@gmail.com");
        
        return Response.ok().entity("Votre mot de passe a bien été mis à jour. Pensez" +
                " à vérifier votre boite de messagerie !").build();
    }

    /**
     *get users potential friend from users repertory
     * @param ligne
     * @return
     */
    @GET
    @Path("/getFriendByNumber")
    public Response getFriendByNumber(@HeaderParam("Authorization") String ligne){

        String[] listeNum = ligne.split(";");
        List<GetFriendDTO> userList= new ArrayList<>();

        for (String numero : listeNum) {
            Utilisateur user = userDAO.getByNum(numero);
            if (user != null){
                Friend friend = new Friend(user.getMail());
                GetFriendDTO dtoF = new GetFriendDTO(
                        friend.getMail(),
                        user.getPrenom(),
                        false,
                        false,
                        user.getPos().getLatitude(),
                        user.getPos().getLongitude(),
                        user.getPos().getLastlatitude(),
                        user.getPos().getLastlongitude()
                );
                userList.add(dtoF);
            }
        }

        return Response.ok(userList)
                .build();
    }

}
