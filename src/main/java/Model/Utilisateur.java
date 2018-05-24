package Model;
import org.mongodb.morphia.annotations.*;
import org.bson.types.ObjectId;
import java.util.ArrayList;
@Entity("Utilisateur")
@Indexes(
        @Index(value = "email", fields = @Field("email"), unique = true)
)
public class Utilisateur {

    @Id
    private ObjectId _id; // always required

    @Indexed
    private String mail;
    private String token;
    private int cptWrongtoken;
    private String password;
    @Embedded
    private ArrayList<Friend> friends;
    private Position pos;
    private String phoneId;

    public Utilisateur(){

    }
    //id generé automatiquement par le document?
    public Utilisateur(String mail, String password, String phoneId ,ArrayList<Friend> friends,Position pos){
        this.mail=mail;
        this.password=password;
        //this.phoneId.put(phoneId,"false");
        this.phoneId=phoneId;
        this.friends=friends;
        this.pos=pos;
    }


    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }



    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //public Map<String,String> getPhoneId() {
 //       return phoneId;
   // }

    //public void setPhoneId(String phoneId) {
    //    this.phoneId.put(phoneId,"false");
 //   }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public String getPhoneId() { return phoneId;}

    public void setPhoneId(String phoneId) { this.phoneId = phoneId;}

    public String getToken() { return token;}

    public void setToken(String token) { this.token = token;}

    public int getCptWrongtoken() {return cptWrongtoken;}

    public void setCptWrongtoken(int cptWrongtoken) {this.cptWrongtoken = cptWrongtoken;}
}
