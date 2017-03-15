package models;

import com.avaje.ebean.Model;
import helpers.ConfigProvider;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * Created by User on 2/1/2016.
 */
public class Email extends Model {
     /* ------------------- send mail to user when his account is created ------------------ */

    public static void sendMail(String mail, String password){


        SimpleEmail email = new SimpleEmail();
        email.setHostName(ConfigProvider.SMTP_HOST);
        email.setSmtpPort(Integer.parseInt(ConfigProvider.SMTP_PORT));
        try {
                /*Configuring mail*/
            email.setAuthentication(ConfigProvider.MAIL_FROM, ConfigProvider.MAIL_FROM_PASS);
            email.setFrom(ConfigProvider.MAIL_FROM);
            email.setStartTLSEnabled(true);
            email.addTo(mail);
            email.setSubject("Uspjesno kreiran profil");
            email.setMsg("Postovani, " + "\n" +
                    "Želimo Vas obavijestiti da je Vaš profil napravljen." + "\n" +
                    "Profilu možete pristupiti uz sljedeće podatke:" + "\n" +
                    "Username: " + mail + "\n" +
                    "Password: " + password + "\n" +

                    "Želimo Vam puno uspjeha." + "\n" +
                    "Vaš," + "\n" +
                    "Rodjendan.ba");
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

         /* ------------------- send mail reservation ------------------ */

    public static void sendMailReservation(String name, String mail, String phone, String checkInDate,String timeFrom,String timeTo, String comment, Integer apartmentId, Integer paketId){

        Apartment apartment = Apartment.getApartmentById(apartmentId);
        AppUser user = AppUser.findUserById(apartment.userId);
        String userMail = user.email;

        /* sending an email*/
        MultiPartEmail multiPartEmail = new MultiPartEmail();
        multiPartEmail.setHostName(ConfigProvider.SMTP_HOST);
        multiPartEmail.setSmtpPort(Integer.parseInt(ConfigProvider.SMTP_PORT));
        try {
                /*Configuring mail*/
            multiPartEmail.setAuthentication(ConfigProvider.MAIL_FROM, ConfigProvider.MAIL_FROM_PASS);
            multiPartEmail.setFrom(ConfigProvider.MAIL_FROM);
            multiPartEmail.setStartTLSEnabled(true);
            multiPartEmail.addBcc(userMail);
            multiPartEmail.addBcc(mail);
            multiPartEmail.setSubject("Rezervacija");
            multiPartEmail.setMsg("Ime i prezime:  " + name + "\n" +
                    "Email:  " + mail + "\n" +
                    "Telefon:  " + phone + "\n" +
                    "Datum proslave:  " + checkInDate + "\n" +
                    "Od:  " + timeFrom + " sati " + " do " + timeTo + " sati" + "\n" +
                    "Komentar:  " + comment);

            Reservation.saveReservation(apartmentId, name, mail, phone, checkInDate, timeFrom, timeTo, comment, paketId);
            multiPartEmail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }



         /* ------------------- item reservation ------------------ */

    public static void itemReservation(String name, String mail, String phone, String checkInDate,String comment, Integer itemId){
        Item item = Item.findItemById(itemId);
        AppUser user = AppUser.findUserById(item.store.userId);
        String userMail = user.email;

        /* sending an email*/
        MultiPartEmail multiPartEmail = new MultiPartEmail();
        multiPartEmail.setHostName(ConfigProvider.SMTP_HOST);
        multiPartEmail.setSmtpPort(Integer.parseInt(ConfigProvider.SMTP_PORT));
        try {
                /*Configuring mail*/
            multiPartEmail.setAuthentication(ConfigProvider.MAIL_FROM, ConfigProvider.MAIL_FROM_PASS);
            multiPartEmail.setFrom(ConfigProvider.MAIL_FROM);
            multiPartEmail.setStartTLSEnabled(true);
            multiPartEmail.addBcc(userMail);
            multiPartEmail.addBcc(mail);
            multiPartEmail.setSubject("Rezervacija");
            multiPartEmail.setMsg("Ime i prezime:  " + name + "\n" +
                    "Email:  " + mail + "\n" +
                    "Telefon:  " + phone + "\n" +
                    "Proizvod:  " + item.name + "\n" +
                    "Cijena:  " + item.price + "\n" +
                    "Datum rezervacije proizvoda:  " + checkInDate + "\n" +
                    "Komentar:  " + comment);


            ItemReservation.itemReservation(checkInDate, name, phone, mail, comment, item.price, itemId);
            multiPartEmail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }


     /* ------------------- cake reservation ------------------ */

    public static Integer cakeReservation(String name, String mail, String phone, String checkInDate,String comment, Integer cakeId){
        Cake cake = Cake.findCakeById(cakeId);
        AppUser user = AppUser.findUserById(cake.pastry.userId);
        String userMail = user.email;

        /* sending an email*/
        MultiPartEmail multiPartEmail = new MultiPartEmail();
        multiPartEmail.setHostName(ConfigProvider.SMTP_HOST);
        multiPartEmail.setSmtpPort(Integer.parseInt(ConfigProvider.SMTP_PORT));
        try {
                /*Configuring mail*/
            multiPartEmail.setAuthentication(ConfigProvider.MAIL_FROM, ConfigProvider.MAIL_FROM_PASS);
            multiPartEmail.setFrom(ConfigProvider.MAIL_FROM);
            multiPartEmail.setStartTLSEnabled(true);
            multiPartEmail.addBcc(userMail);
            multiPartEmail.addBcc(mail);
            multiPartEmail.setSubject("Rezervacija");
            multiPartEmail.setMsg("Ime i prezime:  " + name + "\n" +
                    "Email:  " + mail + "\n" +
                    "Telefon:  " + phone + "\n" +
                    "Torta:  " + cake.name + "\n" +
                    "Cijena:  " + cake.price + "\n" +
                    "Datum rezervacije torte:  " + checkInDate + "\n" +
                    "Komentar:  " + comment);


            CakeReservation.cakeReservation(checkInDate, name,phone, mail, comment, cake.price, cakeId );
            multiPartEmail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
        return cake.pastry.id;
    }
}
