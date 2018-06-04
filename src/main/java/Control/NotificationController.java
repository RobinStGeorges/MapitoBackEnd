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

    @GET
    @Path("/getFriendRequests")
    public ArrayList<Notification> getFriendRequest(@Context HttpHeaders headers){
            String token = headers.getRequestHeader("Authorization").get(0);
            Utilisateur fetchedUser = userDAO.getByToken(token);
            ArrayList<Notification> friendRequest = new ArrayList<Notification>();
            for(Notification not : fetchedUser.getListeNotifications()){
                if(not.getType()==3){
                    friendRequest.add(not);
                }
            }
        return friendRequest;
    }

    @GET
    @Path("/getNotifsNoFriendReques")
    public ArrayList<Notification> getNotifsNoFriendRequest(@Context HttpHeaders headers){
        String token = headers.getRequestHeader("Authorization").get(0);


        Utilisateur fetchedUser = userDAO.getByToken(token);

        ArrayList<Notification> NotifsNoFriend = fetchedUser.getNotifNoFriend();

        return NotifsNoFriend;
    }
    /**
     * R
     */
    @GET
    @Path("/getNotifications")
    public ArrayList<Notification> getUserNotification(@Context HttpHeaders headers){
        String token = headers.getRequestHeader("Authorization").get(0);
        Utilisateur fetchedUser = userDAO.getByToken(token);

        System.out.println(fetchedUser.getListeNotifications());
        return fetchedUser.getListeNotifications();

    }

    @POST
    @Path("/addNotification")
    public String addUserNotification(UserDTO userDTO) throws UnknownHostException {

        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);

        if(fetchedUser.getListeNotifications() == null){
        }
        ArrayList<Notification> listeNotifs;
        listeNotifs=fetchedUser.getListeNotifications();


        Notification notif = new Notification(3,"---"+userDTO.mail+"--- want to add you ! ",fetchedUser.getMail());

        listeNotifs.add(notif);
        userDAO.updateNotifsByToken(userDTO.token,listeNotifs);
        return "200";
    }


    @PUT
    @Path("acceptNotification/{token}")
    public void acceptNotification(TokenDTO tokenDTO){
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


    }
    @PUT
    @Path("/delete")
    public Response delete(UpdateNotificationsDTO notifDTO){
        Utilisateur fetchedUser = userDAO.getByToken(notifDTO.token);

        ArrayList<Notification> listNotifs = fetchedUser.getListeNotifications();
        int acc=0;
        for (Iterator<Notification> iter = listNotifs.listIterator(); iter.hasNext(); ) {
            Notification notif = iter.next();
            if (notif.getType()==3 & notif.getMail().equals(notifDTO.mail)) {
                iter.remove();
                acc++;
            }
        }
        if(acc==0){
            return Response.status(404).build();
        }
        userDAO.updateNotifsByToken(notifDTO.token,listNotifs);
        return Response.ok().build();
    }

}
