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
        this.jour = tabDate[2];
        this.heure = tabDate[3];
        this.minutes = tabDate[4];
        this.secondes = tabDate[5];
    }


}
