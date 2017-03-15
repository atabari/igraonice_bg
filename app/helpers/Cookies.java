package helpers;

import models.Apartment;
import models.AppUser;
import play.mvc.Security;

import static play.mvc.Controller.response;

/**
 * Created by User on 1/6/2016.
 */
public class Cookies extends Security.Authenticator {

    public static void setCookies(Integer apartmentId) {
        Apartment apartment = Apartment.getApartmentById(apartmentId);
        if (apartment.name.contains(" ")) {
            String apartmentName = apartment.name.split(" ")[1] + apartmentId;
            response().setCookie(apartmentName, apartmentId.toString() + "");
        } else {
            response().setCookie(apartment.name, apartmentId.toString() + "");
        }
    }

    public static void setUserCookies(AppUser user){
        response().setCookie("email", user.email);
        response().setCookie("userAccessLevel", user.userAccessLevel.toString());
        response().setCookie("userId", user.id.toString());
    }

    /**
     * Clears user data from the cookies.
     * Should be used within logout function.
     */
    public static void clearCookies() {
        response().discardCookie("email");
        response().discardCookie("userAccessLevel");
        response().discardCookie("userId");
    }

}

