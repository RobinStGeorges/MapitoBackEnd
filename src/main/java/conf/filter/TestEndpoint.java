package conf.filter;


import Model.Utilisateur;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by pitton on 2017-02-20.
 */
@Path("/api/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestEndpoint {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String helloWorld() {
        return "Hello World";
    }

    @GET
    @Path("/list")
    public List<String> getListInParams(@QueryParam("ids") List<String> ids) {
        System.out.println(ids);
        return ids;
    }

    @POST
    @Path("/entity")
    public Utilisateur getUser(Utilisateur User) {
        System.out.println("Received account " + User);
//        account.setUpdated(System.currentTimeMillis());
        return User;
    }

    @GET
    @Path("/exception")
    public Response exception() {
        throw new RuntimeException("Mon erreur");
    }

}
