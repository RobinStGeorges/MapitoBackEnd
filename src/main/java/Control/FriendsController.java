package Control;

import Model.Friend;
import Model.Notification;
import Model.Utilisateur;
import Model.dto.GetFriendDTO;
import service.MorphiaService;
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

    @PUT
    @Path("friendRequest/{token}/{mail}")
    /**
     * R
     * send a friend resquest using the mail of the friend
     */
    public Response newFriendRequest(@PathParam("token") String token, @PathParam("mail") String mail) throws UnknownHostException {

        MorphiaService morphiaService= new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());

        Utilisateur fetchedUser = userDAO.getByToken(token);

        Utilisateur receivingUser = userDAO.getByEmail(mail);
        System.out.println("pouet");
        ArrayList<Friend> poto = fetchedUser.getFriends();
        System.out.println("pouet2");
        Iterator<Friend> iterator = poto.iterator();

        while ( iterator.hasNext() ) {
            Friend user = iterator.next();
            if (user.getMail().equals(mail)) {

                return Response.status(400).build();

            }
        }
        System.out.println("pouet3");
        String mailUser= fetchedUser.getMail();
        System.out.println("pouet3.5");

        Notification notif = new Notification(3,"---"+mailUser+"--- want to add you ! ");

        System.out.println("pouet4");
        receivingUser.getListeNotifications().add(notif);


        return Response.ok().build();

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
                case 3:
                    String body = notif.getMessage();
                    String[] tabBody = body.split("---");
                    String mailAAccepter= tabBody[1];

                    Utilisateur userRequesting = userDAO.getByEmail(mailAAccepter);

                    Friend fFetched = new Friend(fetchedUser.getMail(),false,false);
                    Friend fUR = new Friend(userRequesting.getMail(),false,false);
                    userRequesting.getFriends().add(fFetched);
                    fetchedUser.getFriends().add(fUR);

                    Notification notification = new Notification(2,"---"+fetchedUser.getMail()+"--- just accepted your invitation !");
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

        ArrayList<Friend> listeFriends=fetchedUser.getFriends();
        boolean tempLITA;
        if(listeFriends != null){
            for (Friend friend : listeFriends){
                Utilisateur poto = userDAO.getByEmail(friend.getMail());
                double distance = poto.getPos().distance(
                        fetchedUser.getPos().getLatitude(),
                        fetchedUser.getPos().getLongitude(),
                        poto.getPos().getLatitude(),
                        poto.getPos().getLongitude(),
                        "K"
                );
                boolean inTheArea;
                if(distance < 0.5){
                    inTheArea=true;
                    tempLITA=true;
                }
                else{
                    inTheArea=false;
                    tempLITA=false;
                }
                boolean lastInTheArea = friend.isLastInArea();

                GetFriendDTO dtoF = new GetFriendDTO(
                        friend.getMail(),
                        poto.getPrenom(),
                        inTheArea,
                        lastInTheArea,
                        poto.getPos().getLatitude(),
                        poto.getPos().getLongitude(),
                        poto.getPos().getLastlatitude(),
                        poto.getPos().getLastlongitude()
                );
                friend.setLastInArea(tempLITA);
                friend.setInTheArea(inTheArea);

                friends.add(dtoF);
            }
        }
        userDAO.updateFriendsByToken(token,listeFriends);
        /**
         * TODO enregistrer inthearea et lastinthearea !!
         */
        return friends;
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

        ArrayList<Friend> AL;
        AL=fetchedUser.getFriends();

        Friend newFriend =new Friend(mail,false,false);

        if (user2Add != null){
            Iterator<Friend> iteratorF = AL.iterator();
            boolean trouve = false;
            while ( iteratorF.hasNext() ) {

                Friend friend = iteratorF.next();

                if(friend.getMail().equals(mail)){

                    iteratorF.remove();
                    trouve=true;

                }
            }
            AL.add(newFriend);
            userDAO.updateFriendsByToken(token, AL );
            return "200";
        }
        else{
            return "400";
        }


    }


}
