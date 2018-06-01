package Model;

import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;

import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.UUID;

public class TokenTournament {

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public int checkToken(String token, Utilisateur user) {
        if(token == user.getToken())
            return 1;

        if(user.getCptWrongtoken()>=10) {
            return 3;
        }
        MorphiaService morphiaService = new MorphiaService();
        UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
        userDAO.updateByEmail(user.getMail(),"cptWrongtoken",Integer.toString(user.getCptWrongtoken()+1 ) );
        return 2;

    }
}