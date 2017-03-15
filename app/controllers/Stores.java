package controllers;

import helpers.Authenticator;
import helpers.UserAccessLevel;
import models.AppUser;
import models.Store;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.adminStores;

import java.util.List;

/**
 * Created by User on 5/5/2016.
 */

public class Stores extends Controller {
    //final Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;


    /* --------------- users stores list render ---------------*/
    @Security.Authenticated(Authenticator.PokloniFilter.class)

    public Result listOfUserStores(Integer userId) {
        List<Store> stores = Store.userStores(userId);
        AppUser user = AppUser.findUserById(userId);
        return ok(views.html.store.listOfStores.render(stores, user));
    }


    /* --------------- create store render ---------------*/
    @Security.Authenticated(Authenticator.PokloniFilter.class)

    public Result createStoreRender(Integer userId) {
        return ok(views.html.store.createStore.render(userId));
    }


    /* --------------- create store  ---------------*/

    @Security.Authenticated(Authenticator.PokloniFilter.class)

    public Result createStore(Integer userId) {
        DynamicForm form  = Form.form().bindFromRequest();

        String name = form.field("name").value();
        String location = form.field("location").value();
        String address = form.field("address").value();
        String workingHours = form.field("workingHours").value();
        String lat = form.field("lat").value();
        String lng = form.field("lng").value();

        Store.createStore(name, location, address, workingHours, lat, lng, userId);
        return redirect(routes.Stores.listOfUserStores(userId));
    }


    /* --------------- update store render ---------------*/
    @Security.Authenticated(Authenticator.TortePokloniFilter.class)

    public Result updateStoreRender(Integer storeId) {
        Store store = Store.findStoreById(storeId);
        return ok(views.html.store.updateStore.render(store));
    }


    /* --------------- update store  ---------------*/
    @Security.Authenticated(Authenticator.PokloniFilter.class)

    public Result updateStore(Integer storeId) {
        DynamicForm form  = Form.form().bindFromRequest();

        String name = form.field("name").value();
        String location = form.field("location").value();
        String address = form.field("address").value();
        String workingHours = form.field("workingHours").value();
        String lat = form.field("lat").value();
        String lng = form.field("lng").value();

        Integer userId  = Store.updateStore(name, location, address, workingHours, lat, lng, storeId);
        return redirect(routes.Stores.listOfUserStores(userId));
    }

     /* --------------- delete store  ---------------*/
    @Security.Authenticated(Authenticator.AdminUserFilter.class)

    public Result deleteStore(Integer storeId) {
        Integer userId = Store.deleteStore(storeId);
        return redirect(routes.Stores.listOfUserStores(userId));
    }


        /* --------------- show store  on home page---------------*/

    public Result showOnHomePage(Integer storeId) {
        Store.isVisible(storeId);
        List<Store> stores = Store.getAllStores();

        Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;
        return ok(adminStores.render(stores, userId));
    }
}
