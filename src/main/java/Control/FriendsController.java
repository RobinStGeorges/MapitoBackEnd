package Control;

import Model.Friend;
import Model.Notification;
import Model.Utilisateur;
import Model.dto.GetFriendDTO;
import Model.dto.TokenDTO;
import Model.dto.UserDTO;
import com.google.gson.Gson;
import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/friends/")
public class FriendsController {

    private final UserDAO userDAO = new UserDaoImpl(Utilisateur.class, new MorphiaService().getDatastore());

    @POST
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

                return Response.status(400).build();
            }
        }
        String mailUser= fetchedUser.getMail();

        Notification notif = new Notification(3,"---"+mailUser+"--- want to add you ! ");

        receivingUser.getListeNotifications().add(notif);
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
    @Path("deleteFriend")
    public Response suppFriend(UserDTO userDTO){
        Utilisateur fetchedUser = userDAO.getByToken(userDTO.token);
        ArrayList<Friend> listeFriends = fetchedUser.getFriends();
        boolean trouve1=false,trouve2=false;
        for(Friend poto : listeFriends){
            if(poto.getMail().equals(userDTO.mail)){
                listeFriends.remove(poto);
                trouve2 = true;
            }
        }
        userDAO.updateFriendsByToken(userDTO.token,listeFriends);

        Utilisateur requestingUser = userDAO.getByEmail(userDTO.mail);
        listeFriends = requestingUser.getFriends();
        for(Friend poto : listeFriends){
            if(poto.getMail().equals(requestingUser) ){
                listeFriends.remove(poto);
                trouve1 = true;
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

    @GET
    @Path("/getFriends")
    /**
     * A
     * return a json with positions of friends & distance from the user
     */
    public ArrayList<GetFriendDTO> getUserFriends(TokenDTO tokenDTO){
        Utilisateur fetchedUser = userDAO.getByToken(tokenDTO.token);

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
        userDAO.updateFriendsByToken(tokenDTO.token,listeFriends);
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

        ArrayList<Friend> FriendList;
        FriendList=fetchedUser.getFriends();

        Friend newFriend =new Friend(userDTO.mail,false,false);

        if (friend != null){
            for(Friend poto : FriendList){
                if(poto.getMail().equals(userDTO.mail)){
                    return Response.status(401).build();
                }
            }
            FriendList.add(newFriend);
            userDAO.updateFriendsByToken(userDTO.token, FriendList );
            return Response.ok().build();
        }
        return Response.status(403).build();
    }


}
