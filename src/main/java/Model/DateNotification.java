package Model;

import org.mongodb.morphia.annotations.*;


@Entity("DateNotification")
public class DateNotification {
    private String an;
    private String mois;
    private String jour;
    private String heure;
    private String minutes;
    private String secondes;

}
