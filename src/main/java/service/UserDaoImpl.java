package service;

import java.util.List;

import Model.Utilisateur;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;


/**
 * implementation of
 * @author Alex
 *
 */
public class UserDaoImpl extends BasicDAO<Utilisateur, ObjectId> implements UserDAO {

    public UserDaoImpl(Class<Utilisateur> entityClass, Datastore ds) {
        super(entityClass, ds);
    }


//
//    public List<Utilisateur> getByFirstName(String firstName) {
//
//        Query<Utilisateur> query = createQuery().
//                field("firstName").equal(firstName);
//
//        return query.asList();
//    }

    @Override
    public Utilisateur getByEmail(String mail) {
        Query<Utilisateur> query = createQuery().
                field("email").equal(mail);

        return query.get();
    }
}