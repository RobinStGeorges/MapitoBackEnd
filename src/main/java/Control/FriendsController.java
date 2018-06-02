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

    @PUT
    @Path("/friendRequest")
    public Response newFriendRequest(UserDTO userDTO){
        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);
        Utilisateur receivingUser = userDAO.getByEmail(userDTO.mail);
        if(fetchedUser == null || receivingUser == null){
            return Response.status(401).build();
        }
        ArrayList<Friend> poto = fetchedUser.getFriends();


        for(Friend sauce : poto){
            if(sauce.getMail().equals(userDTO.mail) ){
                return Response.status(403).build();
            }
        }

        String mailUser= fetchedUser.getMail();

        Notification notif = new Notification(3,"---"+mailUser+"--- want to add you ! ");

        receivingUser.getListeNotifications().add(notif);
        userDAO.updateNotifsByToken(userDAO.getByEmail(receivingUser.getMail()).getToken(),receivingUser.getListeNotifications());
        return Response.ok().build();

    }

    @GET
    @Path("/whereAreYou")
    public String inTheArea(UserDTO userDTO) {
        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);

        String response = "{\"area\": %s}";

        ArrayList<Friend> poto = fetchedUser.getFriends();
        Iterator<Friend> iterator = poto.iterator();
        while (iterator.hasNext()) {
            Friend user = iterator.next();
            if (user.getMail().equals(userDTO.mail)) {
                return String.format(response, Boolean.toString(user.isLastInArea()));
            }
        }
        return String.format(response, Boolean.toString(false));
    }


    /**
     *R
     * when a user delete e friend, delete himself from the friend list too
     */
    @PUT
    @Path("/deleteFriend")
    public Response suppFriend(UserDTO userDTO){
        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);
        ArrayList<Friend> listeFriends = fetchedUser.getFriends();
        boolean trouve1=false,trouve2=false;
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

            return  Response.status(401).build();
        }
        else{
            return Response.ok().build();
        }
    }
    /**
     * A
     * return a json with positions of friends & distance from the user
     */
    @GET
    @Path("/getFriends")
    public ArrayList<GetFriendDTO> getUserFriends(@Context HttpHeaders headers){
        System.out.println("pouet");
        String token = headers.getRequestHeader("Authorization").get(0);
        System.out.println(token);

        Utilisateur fetchedUser = userDAO.getByToken(token);

        ArrayList<GetFriendDTO> friends = new ArrayList<>();//ok

        ArrayList<Friend> listeFriends=fetchedUser.getFriends();
        System.out.println(listeFriends.get(0).getMail());
        boolean tempLITA;
        if(listeFriends != null){
            for (Friend friend : listeFriends){
                Utilisateur poto = userDAO.getByEmail(friend.getMail());
                System.out.println(poto.getMail());
                double distance = poto.getPos().distance(
                        fetchedUser.getPos().getLatitude(),
                        fetchedUser.getPos().getLongitude(),
                        poto.getPos().getLatitude(),
                        poto.getPos().getLongitude(),
                        "K"
                );
                System.out.println("distance");
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
        userDAO.updateFriendsByToken(token,listeFriends);
        /**
         * TODO enregistrer inthearea et lastinthearea !!
         */
        return friends;
    }
    /**
     * R
     * add the friend using is mail to the user list
     */
    @PUT
    @Path("/addFriend")
    public Response addFriend(UserDTO userDTO){
        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);
        Utilisateur friend = userDAO.getByEmail(userDTO.mail);
        if(friend == null || fetchedUser == null){
            return Response.status(401).build();
        }
        ArrayList<Friend> FriendListUser = fetchedUser.getFriends();
        ArrayList<Friend> FriendListMail = friend.getFriends();
        Friend newFriendUser = new Friend(friend.getMail(),false,false);
        Friend newFriendMail = new Friend(fetchedUser.getMail(),false,false);
        FriendListMail.add(newFriendMail);
        FriendListUser.add(newFriendUser);
        userDAO.updateFriendsByEmail(friend.getMail(),FriendListMail);
        userDAO.updateFriendsByEmail(fetchedUser.getMail(), FriendListUser );
        return Response.ok().build();
    }


}
