package helpers;

import models.AppUser;
import play.mvc.Http;
import play.mvc.Security;

/**
 * Created by User on 1/10/2016.
 */
public class Authenticator {

    public static class AdminFilter extends Security.Authenticator {

        @Override
        public String getUsername(Http.Context ctx) {
            if (!ctx.session().containsKey("email"))
                return null;
            String email = ctx.session().get("email");
            AppUser user = AppUser.findUserByEmail(email);
            if (user != null && user.userAccessLevel == UserAccessLevel.ADMIN)
                return user.email;
            return null;
        }

    }

    public static class IgraonicaFilter extends Security.Authenticator {

        @Override
        public String getUsername(Http.Context ctx) {
            if (!ctx.session().containsKey("email"))
                return null;
            String email = ctx.session().get("email");
            AppUser user = AppUser.findUserByEmail(email);
            if (user != null && user.userAccessLevel == UserAccessLevel.IGRAONICA)
                return user.email;
            return null;
        }
    }

    public static class TorteFilter extends Security.Authenticator {

        @Override
        public String getUsername(Http.Context ctx) {
            if (!ctx.session().containsKey("email"))
                return null;
            String email = ctx.session().get("email");
            AppUser user = AppUser.findUserByEmail(email);
            if (user != null && user.userAccessLevel == UserAccessLevel.TORTE)
                return user.email;
            return null;
        }
    }

    public static class AnimatoriFilter extends Security.Authenticator {

        @Override
        public String getUsername(Http.Context ctx) {
            if (!ctx.session().containsKey("email"))
                return null;
            String email = ctx.session().get("email");
            AppUser user = AppUser.findUserByEmail(email);
            if (user != null && user.userAccessLevel == UserAccessLevel.ANIMATORI)
                return user.email;
            return null;
        }
    }

    public static class PokloniFilter extends Security.Authenticator {

        @Override
        public String getUsername(Http.Context ctx) {
            if (!ctx.session().containsKey("email"))
                return null;
            String email = ctx.session().get("email");
            AppUser user = AppUser.findUserByEmail(email);
            if (user != null && user.userAccessLevel == UserAccessLevel.POKLONI)
                return user.email;
            return null;
        }
    }

    public static class AdminUserFilter extends Security.Authenticator {

        @Override
        public String getUsername(Http.Context ctx) {
            if (!ctx.session().containsKey("email"))
                return null;
            String email = ctx.session().get("email");
            AppUser user = AppUser.findUserByEmail(email);
            if (user != null && user.userAccessLevel == UserAccessLevel.IGRAONICA
                    || user.userAccessLevel == UserAccessLevel.TORTE
                    || user.userAccessLevel == UserAccessLevel.ANIMATORI
                    || user.userAccessLevel == UserAccessLevel.POKLONI
                    || user.userAccessLevel == UserAccessLevel.ADMIN)
                return user.email;
            return null;
        }
    }

    public static class TortePokloniFilter extends Security.Authenticator {

        @Override
        public String getUsername(Http.Context ctx) {
            if (!ctx.session().containsKey("email"))
                return null;
            String email = ctx.session().get("email");
            AppUser user = AppUser.findUserByEmail(email);
            if (user != null && user.userAccessLevel == UserAccessLevel.TORTE
                    || user.userAccessLevel == UserAccessLevel.POKLONI)
                return user.email;
            return null;
        }
    }
}
