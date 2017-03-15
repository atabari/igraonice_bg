package helpers;

import models.AppUser;
import play.mvc.Http;

/**
 * Created by User on 1/10/2016.
 */
public class UserAccessLevel {
    public static final Integer ADMIN = 1;
    public static final Integer IGRAONICA = 2;
    public static final Integer POKLONI = 3;
    public static final Integer TORTE = 4;
    public static final Integer ANIMATORI = 5;



    public static AppUser getCurrentUser(Http.Context ctx) {
        String email = ctx.session().get("email");
        if (email == null)
            return null;
        return AppUser.findUserByEmail(email);
    }
}

