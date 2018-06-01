package Control;

import Model.Notification;
import Model.Utilisateur;
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

    @GET@
    Path("/getFriendRequest/{token}")
    public ArrayList<Notification> getFriendRequest(@PathParam("token")String token) throws UnknownHostException {
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        ArrayList<Notification> listeFriendRequest = fetchedUser.getFriendNotif();

        return listeFriendRequest;
    }

    @GET
    @Path("/getNotifsNoFriendRequest/{token}")
    public ArrayList<Notification> getNotifsNoFriendRequest(@PathParam("token")String token) throws UnknownHostException {
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        ArrayList<Notification> NotifsNoFriend = fetchedUser.getNotifNoFriend();

        return NotifsNoFriend;
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
    @Path("/addNotification/{token}/{mail}")
    public String addUserNotification(@PathParam("token")String token,@PathParam("mail")String mail) throws UnknownHostException {
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        if(fetchedUser.getListeNotifications() == null){
            System.out.println("null");
        }
        ArrayList<Notification> listeNotifs;
        listeNotifs=fetchedUser.getListeNotifications();

        Notification notif = new Notification(3,"---"+"unMailrandom@gmail.com"+"--- want to add you ! ");
        listeNotifs.add(notif);
        userDAO.updateNotifsByToken(token,listeNotifs);
        return "200";
    }
}
