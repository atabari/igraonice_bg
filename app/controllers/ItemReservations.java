package controllers;

import models.ItemReservation;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by User on 5/23/2016.
 */
public class ItemReservations extends Controller {

    public Result storeReservationsRender(Integer storeId) {
        List<ItemReservation> reservations = ItemReservation.findItemReservationsByStoreId(storeId);
        return ok(views.html.store.storeReservations.render(reservations));
    }

    public Result deleteItemReservation(Integer reservationId) {
        Integer storeId = ItemReservation.deleteItemReservation(reservationId);
        return redirect(routes.ItemReservations.storeReservationsRender(storeId));
    }
}
