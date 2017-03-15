package helpers;

import models.AppUser;

import static play.mvc.Controller.session;

/**
 * Created by User on 1/11/2016.
 */
public class Session {

    public static void setUserSessionData(AppUser user) {
        session("email", user.email);
        session("userAccessLevel", user.userAccessLevel.toString());
        session("userId", user.id.toString());
    }

    /**
     * Clears user data from the session.
     * Should be used within logout function.
     */
    public static void clearUserSessionData() {
        session().clear();
    }
}
