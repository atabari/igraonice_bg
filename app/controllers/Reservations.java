package controllers;

import helpers.UserAccessLevel;
import models.Apartment;
import models.Reservation;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.reports;

import java.util.List;

/**
 * Created by User on 2/25/2016.
 */
public class Reservations extends Controller {
    final Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;


    public Result listOfReservationTimes(String datum) {
        List<String> times =  Reservation.getReservations(datum);
        return ok(String.valueOf(times));
    }

    public Result saveReservation(Integer apartmentId){
        DynamicForm form = Form.form().bindFromRequest();
        String date = form.field("checkIndate").value();
        String timeFrom = form.field("timeFrom").value();
        String timeTo = form.field("timeTo").value();
        String name = form.field("name").value();
        String phone = form.field("phone").value();
        String paket = form.field("paketId").value();

        Reservation.saveReservation(apartmentId, name, null, phone, date, timeFrom, timeTo, null, Integer.parseInt(paket));
        return redirect(routes.Paketi.listOfPackages(apartmentId));
    }

    public Result allReservations(Integer apartmentId){
        List<Reservation> reservations = Reservation.getApartmentReservations(apartmentId);
        return ok(reports.render(reservations, userId));
    }

      /* --------------- confirm reservation ---------------*/

    public Result confirmReservation(Integer reservationId){
        Reservation.confirmReservation(reservationId);
        Reservation reservation = Reservation.getReservationById(reservationId);
        Integer apartmentId = reservation.apartment.id;
        return redirect(routes.Reservations.allReservations(apartmentId));
    }
}
