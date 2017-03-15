package models;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by ajla on 26-Dec-15.
 */
@Entity
public class Reservation extends Model {
    @Id
    public Integer id;

    public String dateFrom;

    public String timeFrom;
    public String timeTo;

    public String visitorName;
    public String visitorLastname;
    public String visitorEmail;
    public String capacity;
    public String phone;
    @Column(columnDefinition = "TEXT")
    public String comment;
    public Integer cost;
    @ManyToOne
    public Apartment apartment;
    public Boolean confirmed;
    @ManyToOne
    public Paket paket;


    public Reservation() {
    }
    public Reservation(Apartment apartment, String dateFrom,String timeFrom,String timeTo, String visitorName, String visitorLastname, String visitorEmail,String capacity, String phone, String comment, Integer cost, Boolean confirmed, Paket paket) {
        this.apartment = apartment;
        this.dateFrom = dateFrom;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.visitorName = visitorName;
        this.visitorLastname = visitorLastname;
        this.visitorEmail = visitorEmail;
        this.capacity = capacity;
        this.phone = phone;
        this.comment = comment;
        this.cost = cost;
        this.confirmed = confirmed;
        this.paket = paket;
    }


    public static void saveReservation(Integer apartmentId, String name, String email, String phone, String checkInDate, String timeFrom, String timeTo, String comment, Integer paketId) {

        Apartment apartment = Apartment.getApartmentById(apartmentId);

        Reservation reservation = new Reservation();
        reservation.apartment = apartment;
        reservation.comment = comment;
        reservation.phone = phone;
        reservation.visitorEmail = email;
        reservation.visitorName = name;

        if(reservation.visitorName.contains(" ")) {
            reservation.visitorName = name.split(" ")[0];
            reservation.visitorLastname = name.split(" ")[1];
        } else {
            reservation.visitorName = name;
            reservation.visitorLastname = " ";
        }
        reservation.dateFrom = checkInDate;
        reservation.timeFrom = timeFrom;
        reservation.timeTo = timeTo;
        reservation.confirmed = false;

        Paket paket = Paket.getPackageById(paketId);
        reservation.paket = paket;

        reservation.save();
    }

    public static List<String> reservationTimes(Integer apartmentId, String dateToCheck) {
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);
        List<Reservation> reservations = finder.where().eq("apartment_id", apartmentId).findList();
        List<String> times = new ArrayList<>();

        for (int i=0; i < reservations.size(); i ++){
            if(dateToCheck.equals(reservations.get(i).dateFrom)) {
                times.add(reservations.get(i).timeFrom +":00 h " + " - " + reservations.get(i).timeTo +":00 h" );
            }
        }
        return times;
    }

    public static HashMap<String, List<String>> getReservationsByApartmentId(Integer apartmentId, String dateToCheck) {
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);
        List <Reservation> confirmedReservations = new ArrayList<>();
        List<Reservation> reservations = finder.where().eq("apartment_id", apartmentId).findList();
        for(int e = 0; e < reservations.size(); e++) {
            if(reservations.get(e).confirmed == true) {
                confirmedReservations.add(reservations.get(e));
            }
        }
        HashMap<String, List<String>> hashMap = new HashMap<>();

        for (int i=0; i < confirmedReservations.size(); i ++){
           hashMap.put(confirmedReservations.get(i).dateFrom, reservationTimes(apartmentId, dateToCheck));

        }
        return hashMap;
    }

    public static List<Reservation> getAllPackageReservations(Integer paketid) {
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);
        return finder.where().eq("paket_id", paketid).findList();
    }

    public static List<String> getReservations(String datum) {

        String dateToCheck = datum.split("-")[0];
        Integer apartmentId =Integer.parseInt(datum.split("-")[1]) ;

        HashMap<String, List<String>> hashMap = getReservationsByApartmentId(apartmentId, dateToCheck);

        return hashMap.get(dateToCheck);

    }

    public static List<Reservation> getApartmentReservations(Integer apartmentId) {
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);
        return finder.where().eq("apartment_id", apartmentId).findList();
    }

    public static Reservation getReservationById(Integer reservationId){
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);
        return finder.where().eq("id", reservationId).findUnique();
    }

      /* --------------- confirm reservation ---------------*/

    public static void confirmReservation(Integer reservationId){
        Reservation reservation = getReservationById(reservationId);
        if(reservation.confirmed == false){
            reservation.confirmed = true;
        }else if (reservation.confirmed == true){
            reservation.confirmed = false;
        }
        reservation.update();
    }

}
