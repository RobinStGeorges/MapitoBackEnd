package Control;

import Model.Notification;
import Model.Utilisateur;
import Model.dto.GetFriendDTO;
import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/friends/")
public class FriendsController {
    /**
     * A
     */
    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test(){
        return "Hello world";
    }

    @POST
    @Path("friendRequest/{token}/{mail}")
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

                    Notification notification = new Notification("accepted","---"+fetchedUser.getMail()+"--- just accepted your invitation !");
                    userRequesting.getListeNotifications().add(notification);
                    //sauvegarder utilisateur
                    break;
            }
        }


    }
    @PUT
    @Path("deleteFriend/{token}/{mail}")
    /**
     *
     */
    public void suppFriend(@PathParam("token")String token,@PathParam("mail")String mail) throws UnknownHostException {
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        System.out.println("1");
/*Current user*/
        Utilisateur fetchedUser = userDAO.getByToken(token);
        ArrayList<Utilisateur> listeFriends = fetchedUser.getFriends();

        Iterator<Utilisateur> iterator = listeFriends.iterator();
        while ( iterator.hasNext() ) {
            Utilisateur user = iterator.next();
            if(user.getMail().equals(mail)){
                iterator.remove();
                System.out.println("2");
            }
        }
        userDAO.updateFriendsByToken(token,listeFriends);
        System.out.println("3");//last seen
        /*deleted user*/
        Utilisateur requestingUser = userDAO.getByEmail(mail);
        ArrayList<Utilisateur> listeRequestedUserFriends = requestingUser.getFriends();
        Iterator<Utilisateur> iterator2 = listeRequestedUserFriends.iterator();
        System.out.println("3.1");
        while ( iterator2.hasNext() ) {
            System.out.println("3.2");//last seen
            Utilisateur user = iterator2.next();
            if(user.getMail().equals(fetchedUser.getMail())){
                System.out.println("3.3");
                iterator2.remove();
                System.out.println("4");
            }
        }
        userDAO.updateFriendsByEmail(mail,listeRequestedUserFriends);
        System.out.println("5");
    }
}
