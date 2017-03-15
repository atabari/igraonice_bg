package controllers;

import helpers.UserAccessLevel;
import models.Apartment;
import models.Paket;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by User on 2/25/2016.
 */
public class Paketi extends Controller {

    final Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;

    public Result createPackageRender(Integer apartmentId) {
        Apartment apartment = Apartment.getApartmentById(apartmentId);
        return ok(views.html.Paketi.createPackage.render(apartment, userId));
    }

    public Result listOfPackages(Integer apartmentId){
        List<Paket> packages = Paket.getPackageByApartmentId(apartmentId);
        return ok(views.html.Paketi.listOfPackages.render(packages, apartmentId, userId));
    }

    public Result createPackage(Integer apartmentId) {
        DynamicForm form = Form.form().bindFromRequest();

        String name = form.field("name").value();
        Integer cost = Integer.parseInt(form.field("cost").value());
        String description = form.field("description").value();
        Integer duration = Integer.parseInt(form.field("duration").value());

        Paket.createPackage(name, cost, description, duration, apartmentId);
        return redirect(routes.Paketi.listOfPackages(apartmentId));

    }

    public Result updatePackageRender(Integer packageId) {
        Paket paket = Paket.getPackageById(packageId);
        return ok(views.html.Paketi.updatePackage.render(paket, userId));
    }

    public Result updatePackage(Integer packageId) {
        DynamicForm form = Form.form().bindFromRequest();

        String name = form.field("name").value();
        Integer cost = Integer.parseInt(form.field("cost").value());
        String description = form.field("description").value();
        Integer duration = Integer.parseInt(form.field("duration").value());

        Integer apartmentId =  Paket.updatePackage(packageId, name, cost, description, duration);
        return redirect(routes.Paketi.listOfPackages(apartmentId));
    }

    public Result deletePackage(Integer packageId) {
        Integer apartmentId = Paket.deletePackage(packageId);
        return redirect(routes.Paketi.listOfPackages(apartmentId));
    }
}
