package models;

import com.avaje.ebean.Model;
import controllers.CakeReservations;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * Created by User on 6/21/2016.
 */
@Entity
public class CakeReservation extends Model {

    @Id
    public Integer id;

    public String date;
    public String customerName;
    public String customerPhone;
    public String customerEmail;
    @Column(columnDefinition = "TEXT")
    public String comment;

    public Integer cost;

    @ManyToOne
    public Pastry pastry;

    @ManyToOne
    public Cake cake;

    public CakeReservation() {}

    private static Model.Finder<String, CakeReservation> finder = new Model.Finder<>(CakeReservation.class);

    /* --------------- save cake reservation ---------------*/
    public static void cakeReservation(String date, String customerName, String customerPhone, String customerEmail, String comment, Integer cost, Integer cakeId){
        CakeReservation reservation = new CakeReservation();
        Cake cake = Cake.findCakeById(cakeId);
        Pastry pastry = cake.pastry;

        reservation.date = date;
        reservation.customerName = customerName;
        reservation.customerPhone = customerPhone;
        reservation.customerEmail = customerEmail;
        reservation.comment = comment;
        reservation.cost = cost;
        reservation.cake = cake;
        reservation.pastry = pastry;

        reservation.save();
    }

    /* --------------- find cake reservations by pastry id ---------------*/
    public static List<CakeReservation> findCakeReservationByPastryId(Integer pastryId) {
        return finder.where().eq("pastry_id", pastryId).findList();
    }

      /* --------------- find cake reservation by reservation id ---------------*/
    public static CakeReservation findCakeReservationById(Integer reservationId) {
        return finder.where().eq("id", reservationId).findUnique();
    }

     /* --------------- find cake reservations by cake id ---------------*/
    public static List<CakeReservation> findCakeReservationsByCakeId(Integer cakeId) {
        return finder.where().eq("cake_id", cakeId).findList();
    }

    /* --------------- delete cake reservations by reservation id ---------------*/
    public static Integer deleteCakeReservationByReservationId(Integer reservationId) {
        CakeReservation reservation = findCakeReservationById(reservationId);
        Integer pastryId = reservation.pastry.id;
        reservation.delete();
        return pastryId;
    }

    /* --------------- delete reservations by cake id ---------------*/
    public static Integer deleteCakeReservationsByCakeId(Integer cakeId) {
        List<CakeReservation> cakeReservations = findCakeReservationsByCakeId(cakeId);
        for (CakeReservation cr : cakeReservations) {
            cr.delete();
        }
        return cakeId;
    }

        /* --------------- delete all pastry reservations ---------------*/

    public static void deleteAllPastryReservations(Integer pastryId) {
        List<CakeReservation> reservations = findCakeReservationByPastryId(pastryId);
        for(int i = 0; i < reservations.size(); i ++) {
            reservations.get(i).delete();
        }
    }

}
