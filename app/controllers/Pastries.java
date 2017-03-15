package controllers;

import helpers.Authenticator;
import helpers.UserAccessLevel;
import models.AppUser;
import models.Cake;
import models.Image;
import models.Pastry;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.adminPastries;

import java.io.File;
import java.util.List;

/**
 * Created by User on 5/31/2016.
 */
public class Pastries extends Controller {
    //final Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;
                /* --------------- create pastry render ---------------*/

    public Result createPastryRender(Integer userId) {
        return ok(views.html.pastry.createPastry.render(userId));
    }

            /* --------------- create pastry ---------------*/

    public Result createPastry(Integer userId) {
        DynamicForm form  = Form.form().bindFromRequest();

        String name = form.field("name").value();
        String location = form.field("location").value();
        String address = form.field("address").value();
        String workingHours = form.field("workingHours").value();
        String lat = form.field("lat").value();
        String lng = form.field("lng").value();

        Pastry pastry = Pastry.createPastry(name, location, address, workingHours, lat, lng, userId);

        Http.MultipartFormData body1 = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> fileParts = body1.getFiles();
        if (fileParts != null) {
            for (Http.MultipartFormData.FilePart filePart1 : fileParts) {
                File file = filePart1.getFile();
                Image image = Image.createPastryImage(file, pastry.id);
                pastry.images.add(image);
            }
        }
        pastry.update();

    return redirect(routes.Pastries.listOfUserPastries(userId));
    }

    /* --------------- users stores list render ---------------*/
    @Security.Authenticated(Authenticator.TortePokloniFilter.class)

    public Result listOfUserPastries(Integer userId) {
        List<Pastry> pastries = Pastry.userPastries(userId);
        AppUser user = AppUser.findUserById(userId);
        return ok(views.html.pastry.listOfPastries.render(pastries, user));
    }

    /* --------------- update pastry render ---------------*/

    public Result updatePastryRender(Integer pastryId) {
        Pastry pastry = Pastry.findPastryById(pastryId);

        Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;
        return ok(views.html.pastry.updatePastry.render(pastry, userId));
    }

        /* --------------- update pastry ---------------*/

    public Result updatePastry(Integer pastryId) {
        DynamicForm form = Form.form().bindFromRequest();

        String name = form.field("name").value();
        String location = form.field("location").value();
        String address = form.field("address").value();
        String workingHours = form.field("workingHours").value();
        String lat = form.field("lat").value();
        String lng = form.field("lng").value();

        Integer userId = Pastry.updatePastry(name, location, address, workingHours, lat, lng, pastryId);

        return redirect(routes.Pastries.listOfUserPastries(userId));
    }


            /* --------------- delete pastry ---------------*/

    public Result deletePastry(Integer pastryId) {
        Integer userId = Pastry.deletePastry(pastryId);
        return redirect(routes.Pastries.listOfUserPastries(userId));
    }


    /* --------------- Pastry visibility on homepage ---------------*/

    public Result showPastryOnHomePage(Integer pastryId) {
        Pastry.isVisible(pastryId);
        List<Pastry> pastries = Pastry.getAllPastries();

        Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;
        return ok(adminPastries.render(pastries, userId));
    }


    /* --------------- pastry main render ---------------*/

    public Result pastryMainRender() {
        List<Pastry> pastries = Pastry.getAllPastries();
        return ok(views.html.pastry.pastryPanel.render(pastries));
    }



        /* --------------- pastry  render ---------------*/

    public Result pastry(Integer pastryId) {
        Pastry pastry = Pastry.findPastryById(pastryId);
        AppUser currentUser = UserAccessLevel.getCurrentUser(ctx());
        List<Cake> cakes = Cake.findAllCakesByPastryId(pastryId);
        return ok(views.html.pastry.pastry.render(pastry,currentUser,cakes));

    }

}
