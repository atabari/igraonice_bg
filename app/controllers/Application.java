package controllers;

import models.Apartment;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.index2;

import java.util.List;

public class Application extends Controller {

    public Result index() {

        return ok(index.render());
    }
    public Result index2() {
        List<Apartment> apartments = Apartment.apartmentsSarajevo();
        return ok(index2.render(apartments));
    }

}
