package service;

import java.util.List;

import Model.Position;
import Model.Utilisateur;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

/**
 * Our own Data Access Object interface for the User entity.
 *
 *
 * Note that we extend org.mongodb.morphia.dao.DAO<T,K>
 * which uses primitives :
 * T is the Type to persist, User in this case
 * K is the type of the entity's unique
 * identifier, ObjectId in this case
 * ( == The type of the field that we annotated with
 * @Id in our User class)
 *
 * @author Alex
 *
 */
public interface UserDAO extends DAO<Utilisateur, ObjectId>{



    /**
     * get a user using its first name and last name
     *
     * (note: don't do this in the real world as
     * multiple users might have the
     * same first name and last name ... :)
     *
     * @return
     */
    public Utilisateur getByEmail(String mail);

    public void updateByEmail(String mail, String field, String value);

    public void updateByToken(String token, String field, String value);

    public void updatePosByToken(String token,Position pos);

//    public void updatePosByEmail(String mail, Position position); //remplaced by token

    Utilisateur getByToken(String token);
}