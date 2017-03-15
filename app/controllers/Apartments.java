package controllers;

import helpers.Authenticator;
import helpers.ConfigProvider;
import helpers.Cookies;
import helpers.UserAccessLevel;
import models.Apartment;
import models.AppUser;
import models.Paket;
import org.omg.DynamicAny.DynAny;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.concurrent.java8.FuturesConvertersImpl;
import views.html.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 12/29/2015.
 */
public class Apartments extends Controller {
    //final Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;


    // Apartment details
    public Result apartment(Integer apartmentId) {
        Apartment apart = Apartment.getApartmentById(apartmentId);
        AppUser currentUser = UserAccessLevel.getCurrentUser(ctx());
        List<Apartment> apartments = Apartment.apartmentsToRecommend(apartmentId);
        List<Paket> paketi = Paket.getPackageByApartmentId(apartmentId);


            List<String> results = new ArrayList<>();

            String folderName = apart.name + apart.id;
            String location = ConfigProvider.UPLOAD_IMAGES_FOLDER + folderName;

            File[] files = new File(location).listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        results.add("/assets/images/" + folderName + "/" + file.getName());
                    }
                }
            }

            if (results.size() > 0) {
                results.remove(0);
            }

        return ok(apartment.render(apart, currentUser, apartments, paketi, results));
    }

    // Create apartment
    @Security.Authenticated(Authenticator.AdminUserFilter.class)
    public Result renderApartment(Integer userId) {
        return ok(createapartment.render(userId));
    }
    @Security.Authenticated(Authenticator.AdminUserFilter.class)
    public Result createApartment(Integer userId) {
        DynamicForm form = Form.form().bindFromRequest();

        String name = form.field("name").value();
        String location = form.field("location").value();
        Logger.info("location  " + location);
        String address = form.field("address").value();
        Integer price = Integer.parseInt(form.field("price").value());
        Integer capacity = Integer.parseInt(form.field("capacity").value());
        String timeFrom = form.field("timeFrom").value();
        String timeTo = form.field("timeTo").value();
        String description = form.field("description").value();
        String lat = form.field("lat").value();
        String lng = form.field("lng").value();

        Apartment apart = Apartment.createApartment(name, location, address, price, capacity,timeFrom,timeTo, description, lat, lng, userId);

        if (apart != null) {
            flash("success", "Uspješno ste kreirali objekat.");
            return redirect(routes.Apartments.apartment(apart.id));
        } else {
            flash("error", "Desila se greška, objekat nije kreiran.");
            return status(404, createapartment.render(userId));
        }
    }

    // Update apartment
    @Security.Authenticated(Authenticator.AdminUserFilter.class)
    public Result renderUpdateApartment(Integer apartmentId) {
        Apartment apart = Apartment.getApartmentById(apartmentId);

        Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;
        return ok(updateapartment.render(apart, userId));
    }

    @Security.Authenticated(Authenticator.AdminUserFilter.class)
    public Result updateApartment(Integer apartmentId) {
        DynamicForm form = Form.form().bindFromRequest();

        List<Apartment> apartments = Apartment.apartmentsToRecommend(apartmentId);
        List<Paket> paketi = Paket.getPackageByApartmentId(apartmentId);

        String name = form.field("name").value();
        String location = form.field("location").value();
        String address = form.field("address").value();
        Integer price = Integer.parseInt(form.field("price").value());
        Integer capacity = Integer.parseInt(form.field("capacity").value());
        String timeFrom = form.field("timeFrom").value();
        String timeTo = form.field("timeTo").value();
        String description = form.field("description").value();
        String lat = form.field("lat").value();
        String lng = form.field("lng").value();

        Apartment apart = Apartment.updateApartment(apartmentId, name, location, address, price, capacity,timeFrom, timeTo, description, lat, lng);


        List<String> results = new ArrayList<>();

        String folderName = apart.name + apart.id;
        String location2 = ConfigProvider.UPLOAD_IMAGES_FOLDER + folderName;
        File[] files = new File(location2).listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    results.add("/assets/images/" + folderName + "/" + file.getName());
                }
            }
        }
        AppUser currentUser = UserAccessLevel.getCurrentUser(ctx());
        if (apart != null) {
            flash("success", "Uspješno ste ažurirali podatke o objektu.");
            return ok(apartment.render(apart, currentUser, apartments, paketi, results ));
        } else {
            flash("error", "Desila se greška, podaci o objektu nisu ažurirani.");
            return ok(createapartment.render(apart.userId));
        }
    }
    public Result cookies(Integer apartmentId){
        Cookies.setCookies(apartmentId);
        return redirect(routes.Apartments.apartment(apartmentId));
    }




     /* --------------- apartments with location Sarajevo ---------------*/

    public Result sarajevoApartments(){
        List<Apartment> apartments = Apartment.apartmentsSarajevo();
        return ok(searchApartments.render(apartments));
    }
      /* --------------- apartments with location Banja Luka ---------------*/

    public Result banjalukaApartments(){
        List<Apartment> apartments = Apartment.apartmentsBanjaLuka();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments with location Mostar ---------------*/

    public Result mostarApartments(){
        List<Apartment> apartments = Apartment.apartmentsMostar();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments with location Zenica ---------------*/

    public Result zenicaApartments(){
        List<Apartment> apartments = Apartment.apartmentsZenica();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments with location Tuzla ---------------*/

    public Result tuzlaApartments(){
        List<Apartment> apartments = Apartment.apartmentsTuzla();
        return ok(searchApartments.render(apartments));
    }

     /* --------------- apartments with location Brcko ---------------*/

    public Result brckoApartments(){
        List<Apartment> apartments = Apartment.apartmentsBrcko();
        return ok(searchApartments.render(apartments));
    }


        /* --------------- delete apartment ---------------*/

    public Result deleteApartment(Integer apartmentId){
        Apartment.deleteApartment(apartmentId);
        List<Apartment> apartments = Apartment.getAllApartments();

        Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;
        return status(200, adminpage.render(apartments, userId));
    }



     /* --------------- show apartment on homepage ---------------*/

    public Result showOnHomepage(Integer apartmentId){
        Apartment.isVisible(apartmentId);
        return redirect(routes.Login.apartmentsList());
    }
}
