package Control;

import Model.Notification;
import Model.Utilisateur;
import Model.dto.GetFriendDTO;
import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;

import javax.rmi.CORBA.Util;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.UnknownHostException;
import java.util.ArrayList;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/friends")
public class FriendsController {
    @POST
    @Path("/friendRequest/{token}/{mail}")
    public String newFriendRequest(@PathParam("token") String token,@PathParam("mail") String mail) throws UnknownHostException {

        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        Utilisateur receivingUser = userDAO.getByEmail(mail);

        String mailUser= fetchedUser.getMail();
        Notification notif = new Notification("addFriend","---"+mailUser+"--- want to add you ! ");

        receivingUser.getListeNotifications().add(notif);

        return "200";
    }

    @PUT
    @Path("acceptNotification/{token}")
    public void acceptNotification(@PathParam("token")String token) throws UnknownHostException {
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        ArrayList<Notification> listeNotification = fetchedUser.getListeNotifications();

        for(Notification notif : listeNotification){
            switch (notif.getTitre()) {
                case "addFriend":
                    String body = notif.getContenue();
                    String[] tabBody = body.split("---");
                    String mailAAccepter= tabBody[1];

                    Utilisateur userRequesting = userDAO.getByEmail(mailAAccepter);
                    userRequesting.getFriends().add(fetchedUser);
                    fetchedUser.getFriends().add(userRequesting);
                    break;
            }
        }


    }

}
