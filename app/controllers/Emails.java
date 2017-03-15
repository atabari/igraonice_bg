package controllers;

import helpers.ConfigProvider;
import models.Email;
import models.Reservation;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.createapartment;
import views.html.createuser;

/**
 * Created by User on 1/8/2016.
 */
public class Emails extends Controller {
    public Result sendMail(Integer apartmentId) {

        //taking values from input fields
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.field("name").value();
        String mail = form.field("mail").value();
        String phone = form.field("phone").value();
        String checkIndate = form.field("checkIndate").value();
        String timeFrom = form.field("timeFrom").value();
        String timeTo = form.field("timeTo").value();
        String comment = form.field("comment").value();
        Integer paketId = Integer.parseInt(form.field("paketId").value());

            Email.sendMailReservation(name, mail, phone, checkIndate,timeFrom, timeTo, comment, apartmentId, paketId);
            /*If mail is sent flash appears and user is redirected to index page */
            flash("success", "Vaša poruka je poslana. Potrudit ćemo se da odgovorimo u najkraćem mogućem roku. Zahvaljujemo!");
            return redirect(routes.Apartments.apartment(apartmentId));
    }


     /* ---------------  item reservation and sending mail to user  ---------------*/
    public Result sendMailItem(Integer itemId) {
        //taking values from input fields
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.field("name").value();
        String mail = form.field("mail").value();
        String phone = form.field("phone").value();
        String checkIndate = form.field("checkIndate").value();
        String comment = form.field("comment").value();

        Email.itemReservation(name, mail, phone, checkIndate, comment, itemId);
        flash("success", "Vaša poruka je poslana. Potrudit ćemo se da odgovorimo u najkraćem mogućem roku. Zahvaljujemo!");
        return redirect(routes.Items.item(itemId));
    }



    /* ---------------  cake reservation and sending mail to user  ---------------*/
    public Result sendMailCake(Integer cakeId) {

        //taking values from input fields
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.field("name").value();
        String mail = form.field("mail").value();
        String phone = form.field("phone").value();
        String checkIndate = form.field("checkIndate").value();
        String comment = form.field("comment").value();

        Integer pastryId = Email.cakeReservation(name, mail, phone, checkIndate, comment, cakeId);
        flash("success", "Vaša poruka je poslana. Potrudit ćemo se da odgovorimo u najkraćem mogućem roku. Zahvaljujemo!");
        return redirect(routes.Pastries.pastry(pastryId));
    }
}
