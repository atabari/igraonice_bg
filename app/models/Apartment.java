package models;

import com.avaje.ebean.Model;
import helpers.Authenticator;
import helpers.ConfigProvider;
import play.Logger;
import play.data.Form;
import play.mvc.Security;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static models.Image.findApartmentImages;

/**
 * Created by ajla on 22-Dec-15.
 */
@Entity
public class Apartment extends Model {
    @Id
    public Integer id;
    public String name;
    public String location;
    public String address;
    public Integer price;
    public Integer capacity;
    public String timeFrom;
    public String timeTo;
    @Column(columnDefinition = "TEXT")
    public String description;
    public String lat;
    public String lng;

    public Integer userId;

    @Column
    public Boolean isVisible;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "apartment")
    public List<Image> images;

    public Apartment() {

    }
    public Apartment(Integer id, String name,String location, String address, Integer price, Integer capacity,String timeFrom, String timeTo,
                      String description, String lat, String lng, Integer userId, Boolean isVisible) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.address = address;
        this.price = price;
        this.capacity = capacity;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.userId = userId;
        this.isVisible = isVisible;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", capacity=" + capacity +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", description=" + description +
                ", lat=" + lat +
                ", lng=" + lng +
                ",userId=" + userId +
                ",isVisible=" + isVisible +
                '}';
    }

    private static Form<Apartment> form = Form.form(Apartment.class);
    private static Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);

    /**
     * Creates an Apartment and saves it in the DB
     * @return
     */
    /* --------------- create apartment ---------------*/
    @Security.Authenticated(Authenticator.AdminFilter.class)
    public static Apartment createApartment(String name, String location, String address, Integer price, Integer capacity,String timeFrom, String timeTo, String description, String lat, String lng, Integer userId) {

        Apartment apartment = new Apartment();
        try {
            apartment.name = name;
            apartment.location = location;
            apartment.address = address;
            apartment.price = price;
            apartment.capacity = capacity;
            apartment.timeFrom = timeFrom;
            apartment.timeTo = timeTo;
            apartment.description = description;
            apartment.lat = lat;
            apartment.lng = lng;
            apartment.userId = userId;
            apartment.isVisible = false;

            apartment.save();

            return apartment;
        } catch (Exception e) {
            Logger.debug("Nisam uspio spasiti apartman :(");
            return apartment;
        }
    }
    /* --------------- update apartment ---------------*/
    @Security.Authenticated(Authenticator.AdminFilter.class)
    public static Apartment updateApartment(Integer apartmentId, String name,String location, String address, Integer price, Integer capacity,String timeFrom, String timeTo, String description, String lat, String lng) {

        Apartment apartment = Apartment.getApartmentById(apartmentId);
        try {
            apartment.name = name;
            apartment.location = location;
            apartment.address = address;
            apartment.price = price;
            apartment.capacity = capacity;
            apartment.timeFrom = timeFrom;
            apartment.timeTo = timeTo;
            apartment.description = description;
            apartment.lat = lat;
            apartment.lng = lng;

            apartment.update();

            return apartment;
        } catch (Exception e) {
            Logger.debug("Nisam uspio spasiti apartman :(");
            return apartment;
        }
    }
    /* --------------- delete apartment ---------------*/
    @Security.Authenticated(Authenticator.AdminFilter.class)
    public static void deleteApartment(Integer apartmentId){
        Apartment apartment = finder.where().eq("id", apartmentId).findUnique();
        List<Reservation> reservations = Reservation.getApartmentReservations(apartmentId);
        List<Paket> paketi = Paket.getPackageByApartmentId(apartmentId);
        for(Reservation r: reservations){
            r.delete();
        }
        for(Paket p: paketi){
            p.delete();
        }

        List<Image> itemImages = findApartmentImages(apartmentId);

        for(int i = 0; i < itemImages.size(); i ++){
            Image.deleteImage(itemImages.get(i));
        }

        apartment.delete();
    }

        /* --------------- retrieves all apartments ---------------*/
    public static List<Apartment> getAllApartments(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.all();
        return apartments;
    }

    /**
     * Retrieves an apartment by provided id.
     * @param apartmentId
     * @return
     */
    public static Apartment getApartmentById(Integer apartmentId) {
        return finder.where().eq("id", apartmentId).findUnique();
    }


            /* --------------- retrieves apartments with location Sarajevo ---------------*/

    public static List<Apartment> apartmentsSarajevo(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartmentsList = finder.where().eq("location", "Sarajevo").findList();
        List<Apartment> apartments = new ArrayList<>();
        for(int i = 0; i < apartmentsList.size(); i ++){
            if(apartmentsList.get(i).isVisible){
                apartments.add(apartmentsList.get(i));
            }
        }
        return apartments;
    }
       /* --------------- retrieves apartments with location Mostar ---------------*/

    public static List<Apartment> apartmentsMostar(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("location", "Mostar").findList();

        return apartments;
    }

            /* --------------- retrieves apartments with location Banja Luka ---------------*/

    public static List<Apartment> apartmentsBanjaLuka(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("location", "Banja Luka").findList();

        return apartments;
    }

            /* --------------- retrieves apartments with location Zenica ---------------*/

    public static List<Apartment> apartmentsZenica(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("location", "Zenica").findList();

        return apartments;
    }

            /* --------------- retrieves apartments with location Tuzla ---------------*/

    public static List<Apartment> apartmentsTuzla(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("location", "Tuzla").findList();

        return apartments;
    }


            /* --------------- retrieves apartments with location Brcko ---------------*/

    public static List<Apartment> apartmentsBrcko(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("location", "BrÄ�ko").findList();

        return apartments;
    }

    /* --------------- retrieves list of images names for the current apartment ---------------*/

    public static List<String> getListOfApartmentImages(Apartment apartment) {
        List<String> results = new ArrayList<>();

        String folderName = apartment.name + apartment.id;
        String location = ConfigProvider.UPLOAD_IMAGES_FOLDER + folderName;
        File[] files = new File(location).listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    results.add("/assets/images/" + folderName + "/" + file.getName());
                }
            }
        }
        return results;
    }

    /* --------------- retrieves first picture name for the current apartment ---------------*/
    public static String getFirstImage(Apartment apartment) {
        List<String> images = getListOfApartmentImages(apartment);

        return (images.size() > 0) ? images.get(0) : "/assets/images/pocetna.jpg";
    }

    /* --------------- retrieves first picture name for the current apartment ---------------*/
    public static String getFirstImageSmall(Apartment apartment) {
        List<Image> images = findApartmentImages(apartment.id);
        return (images.size() > 0) ? images.get(0).getImageForPreview() : "/assets/images/pocetna-mala.jpg";
    }

    /* --------------- Checks if images list for the current apartement is empty ---------------*/
    public static Boolean imagesListIsEmpty(Apartment apartment) {
        List<String> images = getListOfApartmentImages(apartment);
        return (images.size() > 0) ? false : true;
    }

        /* --------------- Finds all users apartments ---------------*/

    public static List<Apartment> userApartments(Integer userId){

        List<Apartment> apartments = finder.where().eq("user_id", userId).findList();

        return apartments;
    }

            /* --------------- Apartments to recommend ---------------*/

    public static List<Apartment> apartmentsToRecommend(Integer apartmentId){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);

        List<Apartment> recommendedApartments = new ArrayList<>();

        Apartment apartment = finder.where().eq("id", apartmentId).findUnique();

        Integer price = apartment.price;

        List<Apartment> apartments = finder.where().eq("location", apartment.location).findList();
        List<Integer> prices = new ArrayList<>();

        for(int k = 0; k < apartments.size(); k++) {
            prices.add(apartments.get(k).price);
        }

        for(int i=0; i < prices.size(); i++) {
            for (int j = price - 10; j <= price + 10; j++) {

                if (apartments.size() != 0 && apartments.get(i).price == j) {
                    recommendedApartments.add(apartments.get(i));
                }

            }
        }
        return recommendedApartments;
    }

            /* --------------- Apartment visibility on homepage ---------------*/

    public static void isVisible(Integer apartmentId){
        Apartment apartment = Apartment.getApartmentById(apartmentId);
        if(apartment.isVisible == false) {
            apartment.isVisible = true;
        }else if(apartment.isVisible == true){
            apartment.isVisible = false;
        }
        apartment.update();
    }

              /* --------------- List of apartments for homepage ---------------*/

    public static List<Apartment> apartmentsForHomepage(){
        List<Apartment> apartments = finder.where().eq("isVisible", true).findList();
        return apartments;
    }

        /* ------------------- get first image if item has images, else return default image ------------------ */

    public static Image getFirstItemImage(Integer apartmentId) {
        Apartment apartment = getApartmentById(apartmentId);
        if (apartment.images.size() > 0) {
            return apartment.images.get(0);
        } else {
            return null;
        }
    }
}

