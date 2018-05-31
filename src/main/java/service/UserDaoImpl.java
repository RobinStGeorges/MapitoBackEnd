package service;

import java.util.ArrayList;
import java.util.List;

import Model.Friend;
import Model.Notification;
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
    public void updateNotifsByToken(String token, ArrayList<Notification> notif) {
        Query<Utilisateur> query = ds.createQuery(Utilisateur.class).field("token").equal(token);
        UpdateOperations<Utilisateur> ops = ds.createUpdateOperations(Utilisateur.class).set("listeNotifications", notif);

        ds.update(query, ops);
    }

    @Override
    public void updateFriendsByToken(String token,ArrayList<Friend> value){
        Query<Utilisateur> query = ds.createQuery(Utilisateur.class).field("token").equal(token);
        UpdateOperations<Utilisateur> ops = ds.createUpdateOperations(Utilisateur.class).set("friends", value);

        ds.update(query, ops);
    }

    @Override
    public void updateFriendsByEmail(String mail,ArrayList<Friend> value){
        Query<Utilisateur> query = ds.createQuery(Utilisateur.class).field("mail").equal(mail);
        UpdateOperations<Utilisateur> ops = ds.createUpdateOperations(Utilisateur.class).set("friends", value);

        ds.update(query, ops);

    }


    @Override
    public void updateByEmail(String mail, String field, String value){

        Query<Utilisateur> query = ds.createQuery(Utilisateur.class).field("mail").equal(mail);
        UpdateOperations<Utilisateur> ops = ds.createUpdateOperations(Utilisateur.class).set(field, value);

        ds.update(query, ops);

    }

    @Override
    public void deleteByToken(String token){
        Query<Utilisateur> query = ds.createQuery(Utilisateur.class).field("token").equal(token);
        ds.delete(query);
    }

    @Override
    public void updatePosByToken(String token, Position position) {
        Query<Utilisateur> query = ds.createQuery(Utilisateur.class).field("token").equal(token);
        UpdateOperations<Utilisateur> ops = ds.createUpdateOperations(Utilisateur.class).set("pos", position);

        ds.update(query, ops);
    }
//    @Override
//    public void updatePosByEmail(String mail, Position position) {
//        Query<Utilisateur> query = ds.createQuery(Utilisateur.class).field("mail").equal(mail);
//        UpdateOperations<Utilisateur> ops = ds.createUpdateOperations(Utilisateur.class).set("pos", position);
//
//        ds.update(query, ops);
//    }

    @Override
    public Utilisateur getByToken(String token) {
        Query<Utilisateur> query = createQuery().
                field("token").equal(token);

        return query.get();
    }

}