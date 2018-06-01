package Model;

import service.MorphiaService;
import service.UserDAO;
import service.UserDaoImpl;

import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.UUID;

public class TokenTournament {

    protected  SecureRandom random = new SecureRandom();

    public synchronized String generateToken( String username ) {
        String suuid = UUID.randomUUID().toString();
        return suuid;
    }
    public int checkToken(String token,Utilisateur user) {
        if(token == user.getToken())
            return 1;
        else {
            if(user.getCptWrongtoken()>=10) {
                return 3;
            }else {
                try {
                    MorphiaService morphiaService = new MorphiaService();
                    UserDAO userDAO = new UserDaoImpl(Utilisateur.class, morphiaService.getDatastore());
                    userDAO.updateByEmail(user.getMail(),"cptWrongtoken",Integer.toString(user.getCptWrongtoken()+1 ) );
                    return 2;
                }catch(UnknownHostException e){return 0;}
            }
        }
    }
}