import com.cloudinary.Cloudinary;
import models.Image;
import play.Application;
import play.GlobalSettings;
import play.Play;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;


public class Global extends GlobalSettings {
    @Override
    public  void onStart(Application application) {

        /**
         * Calls a thread that will check reservations every hour.
         * If reservation checkoutDate passed currentDate they will
         * be set as completed.
         */

       Image.cloudinary = new Cloudinary("cloudinary://" + Play.application().configuration().getString("cloudinary.string"));
    }
}

