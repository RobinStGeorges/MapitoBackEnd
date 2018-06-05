package Control;

import Model.Friend;
import Model.Notification;
import Model.Utilisateur;
import Model.dto.GetFriendDTO;
import Model.dto.TokenDTO;
import Model.dto.UserDTO;
import com.google.gson.Gson;
import org.jboss.resteasy.util.HttpServletRequestDelegate;
import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/friends")
public class FriendsController {

    private final UserDAO userDAO = new UserDaoImpl(Utilisateur.class, new MorphiaService().getDatastore());

    /**
     * send a friend request
     * @param userDTO
     * @return
     */
    @PUT
    @Path("/friendRequest")
    public Response newFriendRequest(UserDTO userDTO){
        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);
        Utilisateur receivingUser = userDAO.getByEmail(userDTO.mail);

        if(fetchedUser == null || receivingUser == null){
            return Response.status(404).entity("L'ami est introuvable").build();
        }
        if(fetchedUser.getMail().equals(receivingUser.getMail())){
            return  Response.status(403).entity("Vous ne pouvez pas vous ajouter vous même").build();
        }
        ArrayList<Friend> poto = fetchedUser.getFriends();

        for(Friend sauce : poto){
            if(sauce.getMail().equals(userDTO.mail) ){
                return Response.status(403).entity("Vous avez déjà ajouté cet ami !").build();
            }
        }
        ArrayList<Notification> listNotif  = receivingUser.getNotifNoFriend();
        for (Notification notif : listNotif) {
            if (notif.getMessage().equals("---"+fetchedUser.getMail()+"--- want to add you ! ")) {
                return Response.status(403).entity("vous avez déja fait une demande a cet ami, en attente de réponse").build();
            }
        }

        String mailUser= fetchedUser.getMail();

        Notification notif = new Notification(3,"---"+mailUser+"--- want to add you ! ",
                fetchedUser.getMail());

        receivingUser.getListeNotifications().add(notif);

        userDAO.updateNotifsByToken(userDAO.getByEmail(receivingUser.getMail()).getToken(),
                receivingUser.getListeNotifications());

        return Response.ok().entity("La demande d'ami/e a bien été envoyé !").build();

    }

    /**
     * delete a friend from both one's list and the friend's list
     * @param userDTO
     * @return
     */
    @PUT
    @Path("/deleteFriend")
    public Response suppFriend(UserDTO userDTO){

        boolean trouve1=false,trouve2=false;

        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);

        ArrayList<Friend> listeFriends = fetchedUser.getFriends();
        for(Friend poto : listeFriends){
            if(poto.getMail().equals(userDTO.mail)){
                listeFriends.remove(poto);
                trouve2 = true;
                break;
            }
        }

        userDAO.updateFriendsByToken(userDTO.token,listeFriends);

        Utilisateur requestingUser = userDAO.getByEmail(userDTO.mail);
        listeFriends = requestingUser.getFriends();
        for(Friend poto : listeFriends){
            if(poto.getMail().equals(fetchedUser.getMail()) ){
                listeFriends.remove(poto);
                trouve1 = true;
                break;
            }
        }

        userDAO.updateFriendsByEmail(userDTO.mail,listeFriends);

        if (!trouve1 || !trouve2) {
            return  Response.status(404).entity("Vous n'avez pas cet ami dans votre liste d'ami").build();
        }
        else{
            return Response.ok().entity("Vous avez bien supprimé "+
                    userDTO.mail +
                    " de votre liste d'amis ").build();
        }
    }


    /**
     *return liste of user's friend with distances
     * @param headers
     * @return
     */
    @GET
    @Path("/getFriends")
    public Response getUserFriends(@Context HttpHeaders headers){
        String token = headers.getRequestHeader("Authorization").get(0);

        Utilisateur fetchedUser = userDAO.getByToken(token);
        ArrayList<GetFriendDTO> friends = new ArrayList<>();

        ArrayList<Friend> listeFriends=fetchedUser.getFriends();

        boolean tempLITA;

        if(listeFriends != null){
            for (Friend friend : listeFriends){

                Utilisateur poto = userDAO.getByEmail(friend.getMail());

                System.out.println(poto.getMail());//debug

                double distance = poto.getPos().distance(
                        fetchedUser.getPos().getLatitude(),
                        fetchedUser.getPos().getLongitude(),
                        poto.getPos().getLatitude(),
                        poto.getPos().getLongitude(),
                        "K"
                );
                System.out.println("distance ");
                System.out.println(distance);
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
        else{
            return Response.status(404)
                    .entity("Vous n'avez pas encore d'ami ! Pensez à faire de nouvelles rencontres ;) !")
                    .build();
        }
        userDAO.updateFriendsByToken(token,listeFriends);

        return Response.ok(friends).build();
    }
    /**
     * R
     * add the friend using is mail to the user list
     */
    @PUT
    @Path("/addFriend")
    public Response addFriend(UserDTO userDTO){
        System.out.println("testation");
        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);
        Utilisateur friend = userDAO.getByEmail(userDTO.mail);

        if(friend == null || fetchedUser == null){
            return Response.status(403).entity("L'ami n'existe pas ." +
                    " Veuillez contacter un administrateur si le problème persiste.").build();
        }
        if(fetchedUser.getMail().equals(friend.getMail())){
            return Response.status(403).entity("vous ne pouvez pas vous ajouter vous même !" +
                    " Sortez faire de nouvelles rencontre ! ;)").build();
        }

        ArrayList<Friend> FriendListUser = fetchedUser.getFriends();
        ArrayList<Friend> FriendListMail = friend.getFriends();

        Friend newFriendUser = new Friend(friend.getMail(),false,false);
        Friend newFriendMail = new Friend(fetchedUser.getMail(),false,false);

        FriendListMail.add(newFriendMail);
        FriendListUser.add(newFriendUser);

        userDAO.updateFriendsByEmail(friend.getMail(),FriendListMail);
        userDAO.updateFriendsByEmail(fetchedUser.getMail(), FriendListUser );

        ArrayList<Notification> listNotif = fetchedUser.getListeNotifications();
        for (Notification notif : listNotif) {
            if (notif.getMessage().equals("---"+friend.getMail()+"--- want to add you ! ")) {
                listNotif.remove(notif);
                userDAO.updateNotifsByToken(userDTO.token,listNotif);
                System.out.println("testation del friend");
                return Response.ok().entity("Vous avez bien ajouté "+userDTO.mail+ " !").build();
            }
        }
            return Response.status(403).entity("Vous n'avez pas reçuc cette notification !").build();
    }


}
