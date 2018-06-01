package Control;

import Model.Notification;
import Model.Utilisateur;
import Model.dto.TokenDTO;
import Model.dto.UserDTO;
import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.UnknownHostException;
import java.util.ArrayList;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/notification")
public class NotificationController {

    private final UserDAO userDAO = new UserDaoImpl(Utilisateur.class, new MorphiaService().getDatastore());

    @GET@
    Path("/getFriendRequest")
    public ArrayList<Notification> getFriendRequest(TokenDTO tokenDTO) {

        Utilisateur fetchedUser = userDAO.getByToken(tokenDTO.token);

        ArrayList<Notification> listeFriendRequest = fetchedUser.getFriendNotif();

        return listeFriendRequest;
    }

    @GET
    @Path("/getNotifsNoFriendRequest/{token}")
    public ArrayList<Notification> getNotifsNoFriendRequest(TokenDTO tokenDTO) {


        Utilisateur fetchedUser = userDAO.getByToken(tokenDTO.token);

        ArrayList<Notification> NotifsNoFriend = fetchedUser.getNotifNoFriend();

        return NotifsNoFriend;
    }

    @GET
    @Path("/getNotifications")
    /**
     * R
     */
    public ArrayList<Notification> getUserNotification(UserDTO userDTO) throws UnknownHostException {

        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);
        System.out.println(fetchedUser.getListeNotifications());
        return fetchedUser.getListeNotifications();

    }

    @POST
    @Path("/addNotification/")
    public String addUserNotification(UserDTO userDTO) throws UnknownHostException {
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);

        if(fetchedUser.getListeNotifications() == null){
            System.out.println("null");
        }
        ArrayList<Notification> listeNotifs;
        listeNotifs=fetchedUser.getListeNotifications();

        Notification notif = new Notification(3,"---"+userDTO.mail+"--- want to add you ! ");
        listeNotifs.add(notif);
        userDAO.updateNotifsByToken(userDTO.token,listeNotifs);
        return "200";
    }
}
