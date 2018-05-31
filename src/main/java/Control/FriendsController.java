package Control;

import Model.Friend;
import Model.Notification;
import Model.Utilisateur;
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
    /**
     * R
     * send a friend resquest using the mail a the friend
     */
    public String newFriendRequest(@PathParam("token") String token,@PathParam("mail") String mail) throws UnknownHostException {

        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        Utilisateur receivingUser = userDAO.getByEmail(mail);

        ArrayList<Friend> poto = fetchedUser.getFriends();
        Iterator<Friend> iterator = poto.iterator();
        while ( iterator.hasNext() ) {
            Friend user = iterator.next();
            if (user.getMail().equals(mail)) {
                return "400";
            }
        }

        String mailUser= fetchedUser.getMail();
        Notification notif = new Notification("addFriend","---"+mailUser+"--- want to add you ! ");

        receivingUser.getListeNotifications().add(notif);

        return "200";
    }
    @GET
    @Path("whereAreYou/{token}/{mail}")
    public boolean whereAreYou(@PathParam("token")String token,@PathParam("mail")String mail)throws UnknownHostException{
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        Utilisateur fetchedUser = userDAO.getByToken(token);

        ArrayList<Friend> poto = fetchedUser.getFriends();
        Iterator<Friend> iterator = poto.iterator();
        while ( iterator.hasNext() ) {
            Friend user = iterator.next();
            if (user.getMail().equals(mail)) {
                return user.isLastInArea();
            }
        }return false;
    }

    @PUT
    @Path("acceptNotification/{token}")
    /**
     * R
     * When user clic on accept the invitation
     */
    public void acceptNotification(@PathParam("token")String token) throws UnknownHostException {
        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        ArrayList<Notification> listeNotification = fetchedUser.getListeNotifications();

        for(Notification notif : listeNotification){
            switch (notif.getType()) {
                case "addFriend":
                    String body = notif.getMessage();
                    String[] tabBody = body.split("---");
                    String mailAAccepter= tabBody[1];

                    Utilisateur userRequesting = userDAO.getByEmail(mailAAccepter);

                    Friend fFetched = new Friend(fetchedUser.getMail(),false,false);
                    Friend fUR = new Friend(userRequesting.getMail(),false,false);
                    userRequesting.getFriends().add(fFetched);
                    fetchedUser.getFriends().add(fUR);

                    Notification notification = new Notification("accepted","---"+fetchedUser.getMail()+"--- just accepted your invitation !");
                    userRequesting.getListeNotifications().add(notification);
                    //sauvegarder utilisateur
                    break;
            }
        }


    }

    @PUT
    @Path("/updateLIA/{token}/{mail}")
    public void updateLastInArea(@PathParam("token")String token,@PathParam("mail")String mail) throws UnknownHostException {
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        /*Current user*/
        Utilisateur fetchedUser = userDAO.getByToken(token);
        ArrayList<Friend> listeFriends = fetchedUser.getFriends();


//        Iterator<Friend> iterator = listeFriends.iterator();
//        boolean trouve = false;
//        while ( iterator.hasNext() ) {
//            Friend user = iterator.next();
//            if(user.getMail().equals(mail)){
//                trouve=true;
//                switch(""+user.isInTheArea()){
//                    case "true":
//                        user.setLastInArea(true);
//                        break;
//                    case "false":
//                        user.setLastInArea(true);
//                        break;
//                }
//
//            }
//        }




    }

    @PUT
    @Path("deleteFriend/{token}/{mail}")
    /**
     *R
     * when a user delete e friend, delete himself from the friend list too
     */
    public String suppFriend(@PathParam("token")String token,@PathParam("mail")String mail) throws UnknownHostException {
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

/*Current user*/
        Utilisateur fetchedUser = userDAO.getByToken(token);
        ArrayList<Friend> listeFriends = fetchedUser.getFriends();

        Iterator<Friend> iterator = listeFriends.iterator();
        boolean trouve2 = false;
        while ( iterator.hasNext() ) {
            Friend user = iterator.next();
            if(user.getMail().equals(mail)){
                iterator.remove();
                System.out.println("2");
                trouve2=true;
            }
        }
        userDAO.updateFriendsByToken(token,listeFriends);
        System.out.println("3");//last seen
        /*deleted user*/
        Utilisateur requestingUser = userDAO.getByEmail(mail);
        ArrayList<Friend> listeRequestedUserFriends = requestingUser.getFriends();
        Iterator<Friend> iterator2 = listeRequestedUserFriends.iterator();
        boolean trouve = false;
        while ( iterator2.hasNext() ) {
            Friend user = iterator2.next();
            if(user.getMail().equals(fetchedUser.getMail())){
                iterator2.remove();
                trouve=true;
            }
        }

        userDAO.updateFriendsByEmail(mail,listeRequestedUserFriends);
        if (!trouve || !trouve2) {

            return "400";
        }
        else{
            return "200";
        }
    }
}
