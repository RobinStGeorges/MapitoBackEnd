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
}
