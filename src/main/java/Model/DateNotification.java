package Model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;


@Entity("DateNotification")
public class DateNotification {
    @Id
    private ObjectId _id; // always required
    private String an;
    private String mois;
    private String jour;
    private String heure;
    private String minutes;
    private String secondes;

    public DateNotification() {
        String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        Calendar cal = Calendar.getInstance();
        String date = sdf.format(cal.getTime());
        String[] tabDate = date.split("-");

        this.an = tabDate[0];
        this.mois = tabDate[1];

        String hoursP = tabDate[2];
        String[] deuxieme = hoursP.split(" ");
        this.jour = deuxieme[0];

        String reste = deuxieme[1];
        String[] enfin = reste.split(":");
        this.heure=enfin[0];
        this.minutes=enfin[1];
        this.secondes=enfin[2];

    }


}
