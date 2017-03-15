package models;

import com.avaje.ebean.Model;
import play.Logger;
import play.data.Form;

import javax.persistence.*;
import java.util.List;

/**
 * Created by User on 2/25/2016.
 */
@Entity
public class Paket extends Model {

    @Id
    public Integer id;
    public String name;
    public Integer cost;

    @Column(columnDefinition = "TEXT")
    public String description;
    public Integer duration;

    @ManyToOne
    public Apartment apartment;

    @OneToMany
    public Paket paket;

    public Paket() {
    }

    public Paket(String name, Integer cost, String description, Integer duration, Apartment apartment) {
        this.name = name;
        this.cost = cost;
        this.description = description;
        this.duration = duration;
        this.apartment = apartment;
    }

    private static Form<Paket> form = Form.form(Paket.class);
    private static Model.Finder<String, Paket> finder = new Model.Finder<>(Paket.class);

    public static void createPackage(String name, Integer cost, String description, Integer duration, Integer apartmentId) {
        Apartment apartment = Apartment.getApartmentById(apartmentId);

        Paket package1 = new Paket();

        package1.name = name;
        package1.cost = cost;
        package1.description =description;
        package1.duration = duration;
        package1.apartment = apartment;

        package1.save();
    }

    public static Paket getPackageById(Integer packageId){
        return finder.where().eq("id", packageId).findUnique();
    }

    public static Integer updatePackage(Integer packageId,String name, Integer cost, String description, Integer duration ) {
        Paket p = getPackageById(packageId);
        try {
            p.name = name;
            p.cost = cost;
            p.description  = description;
            p.duration = duration;

            p.update();

        } catch (Exception e) {
            Logger.debug("Nisam uspio uraditi update paketa :(");
        }
        return p.apartment.id;
    }

    public static Integer deletePackage(Integer packageId) {
        Paket p = getPackageById(packageId);

        List<Reservation> reservations = Reservation.getAllPackageReservations(packageId);

        for (int i = 0; i < reservations.size(); i++) {
            reservations.get(i).delete();
        }

        Integer apartmentId = p.apartment.id;
        p.delete();
        return apartmentId;
    }

    public static List<Paket> getPackageByApartmentId(Integer apartmentId) {
        return finder.where().eq("apartment_id", apartmentId).findList();
    }
}
