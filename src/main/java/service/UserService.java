//package service;
//
//import Model.Friend;
//import conf.ConnectionMDB;
//import Model.Utilisateur;
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBCollection;
//import com.mongodb.DBObject;
//
//public class UserService {
//
//
//    public void AddFriend(Utilisateur user, Friend friend){
//        ConnectionMDB connectionMDB= new ConnectionMDB();
//
//        //ajout base de donnée;
//        user.getFriends().add(friend);
//
//
//
//        DBCollection dbCollection = connectionMDB.getConnectionUtilisateurs("utilisateurs");
//
//        BasicDBObject oldUser= new BasicDBObject();
//        oldUser.put("email",user.getMail());
//        DBObject oOldUser = dbCollection.findOne(oldUser);
//
//        DBObject oNewUser = new BasicDBObject();
//
//
//        oNewUser.put("mail",user.getMail());
//        oNewUser.put("password",user.getPassword());
//        oNewUser.put("phoneId",user.getPhoneId());
//        oNewUser.put("friends",user.getFriends());
//        oNewUser.put("pos",user.getPos());
//
//        dbCollection.update(oOldUser,oNewUser);
//
//
//    }
//}
