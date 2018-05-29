package service;

import java.util.List;

import Model.Position;
import Model.Utilisateur;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;


/**
 * implementation of
 *
 * @author Alex
 */
public class UserDaoImpl extends BasicDAO<Utilisateur, ObjectId> implements UserDAO {

    public UserDaoImpl(Class<Utilisateur> entityClass, Datastore ds) {
        super(entityClass, ds);
    }


    @Override
    public Utilisateur getByEmail(String mail) {
        Query<Utilisateur> query = createQuery().
                field("mail").equal(mail);

        return query.get();
    }

    @Override

    public void updateByToken(String token, String field, String value){
        Query<Utilisateur> query = ds.createQuery(Utilisateur.class).field("token").equal(token);
        UpdateOperations<Utilisateur> ops = ds.createUpdateOperations(Utilisateur.class).set(field, value);

        ds.update(query, ops);

    }
    @Override
    public void updateByEmail(String mail, String field, String value){

        Query<Utilisateur> query = ds.createQuery(Utilisateur.class).field("mail").equal(mail);
        UpdateOperations<Utilisateur> ops = ds.createUpdateOperations(Utilisateur.class).set(field, value);

        ds.update(query, ops);

    }

    @Override
    public void updatePosByEmail(String mail, Position position) {
        Query<Utilisateur> query = ds.createQuery(Utilisateur.class).field("mail").equal(mail);
        UpdateOperations<Utilisateur> ops = ds.createUpdateOperations(Utilisateur.class).set("pos", position);

        ds.update(query, ops);
    }

    @Override
    public Utilisateur getByToken(String token) {
        Query<Utilisateur> query = createQuery().
                field("token").equal(token);

        return query.get();
    }

}