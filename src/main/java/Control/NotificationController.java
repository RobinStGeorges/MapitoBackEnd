package Control;

import Model.Friend;
import Model.Notification;
import Model.Utilisateur;
import Model.dto.TokenDTO;
import Model.dto.UpdateNotificationsDTO;
import Model.dto.UserDTO;
import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/notifications")
public class NotificationController {

    private final UserDAO userDAO = new UserDaoImpl(Utilisateur.class, new MorphiaService().getDatastore());

    /**
     * return the user s list of friendrequests notification
     * @param headers
     * @return
     */
    @GET
    @Path("/getFriendRequests")
    public ArrayList<Notification> getFriendRequest(@Context HttpHeaders headers){

            String token = headers.getRequestHeader("Authorization").get(0);

            Utilisateur fetchedUser = userDAO.getByToken(token);

            ArrayList<Notification> friendRequest = new ArrayList<Notification>();
            for(Notification not : fetchedUser.getListeNotifications()){ //si la notification est de type 3
                if(not.getType()==3){                                    // -> demande d'ami
                    friendRequest.add(not);
                }
            }

        return friendRequest;
    }

    /**
     * get all notifications that are NOT friend requests
     * @param headers
     * @return
     */
    @GET
    @Path("/getNotifsNoFriendReques")
    public ArrayList<Notification> getNotifsNoFriendRequest(@Context HttpHeaders headers){

        String token = headers.getRequestHeader("Authorization").get(0);

        Utilisateur fetchedUser = userDAO.getByToken(token);

        return fetchedUser.getNotifNoFriend();
    }

    /**
     * return all the user s notifications
     * @param headers
     * @return
     */
    @GET
    @Path("/getNotifications")
    public Response getUserNotification(@Context HttpHeaders headers){


        String token = headers.getRequestHeader("Authorization").get(0);

        Utilisateur fetchedUser = userDAO.getByToken(token);

        return Response.ok(fetchedUser.getListeNotifications()).build();

    }

    /**
     *
     * @param userDTO
     * @return
     * @throws UnknownHostException
     */
    @POST
    @Path("/addNotification")

    public Response addUserNotification(UserDTO userDTO) throws UnknownHostException {

        ArrayList<Notification> listeNotifs;

        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);

        listeNotifs=fetchedUser.getListeNotifications();

        Notification notif = new Notification(3,"---"+userDTO.mail+"--- want to add you ! ",fetchedUser.getMail());

        listeNotifs.add(notif);

        userDAO.updateNotifsByToken(userDTO.token,listeNotifs);

        return Response.ok().build();
    }

//fonction de test à supprimer
    @PUT
    @Path("acceptNotification/{token}")
    public Response acceptNotification(TokenDTO tokenDTO){
        Utilisateur fetchedUser = userDAO.getByToken(tokenDTO.token);

        ArrayList<Notification> listeNotification = fetchedUser.getListeNotifications();

        for(Notification notif : listeNotification){
            switch (notif.getType()) {
                case 3:
                    String body = notif.getMessage();
                    String[] tabBody = body.split("---");
                    String mailAAccepter= tabBody[1];

                    Utilisateur userRequesting = userDAO.getByEmail(mailAAccepter);

                    Friend fFetched = new Friend(fetchedUser.getMail(),false,false);
                    Friend fUR = new Friend(userRequesting.getMail(),false,false);
                    userRequesting.getFriends().add(fFetched);
                    fetchedUser.getFriends().add(fUR);

                    Notification notification = new Notification(2,"---"+fetchedUser.getMail()+"--- just accepted your invitation !",fetchedUser.getMail());
                    userRequesting.getListeNotifications().add(notification);
                    //sauvegarder utilisateur
                    break;
            }
        }
    return Response.ok().entity("vous avez bien envoyé vos demande d'ami !").build();
    }

    /**
     * DElete a notification from the user s notification s list
     * @param notifDTO
     * @return
     */
    @PUT
    @Path("/delete")
    public Response delete(UpdateNotificationsDTO notifDTO){

        int acc=0;

        Utilisateur fetchedUser = userDAO.getByToken(notifDTO.token);

        ArrayList<Notification> listNotifs = fetchedUser.getListeNotifications();

        for (Iterator<Notification> iter = listNotifs.listIterator(); iter.hasNext(); ) {
            Notification notif = iter.next();
            if (notif.getType()==3 & notif.getMail().equals(notifDTO.mail)) {
                iter.remove();
                acc++;
            }
        }

        if(acc==0){ //never entered the iteration
            return Response.status(404).entity("la notification n'a pas été trouvé !").build();
        }

        userDAO.updateNotifsByToken(notifDTO.token,listNotifs);

        return Response.ok().entity("Vous avez refusé la demande d'ami de "+notifDTO.mail).build();
    }

}
