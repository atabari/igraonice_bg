package helpers;

import play.Play;

/**
 * Created by User on 1/8/2016.
 */
public class ConfigProvider {

    // Configurations for email
    public static final String SMTP_HOST = Play.application().configuration().getString("smtp.host");
    public static final String SMTP_PORT = Play.application().configuration().getString("smtp.port");
    public static final String SMTP_USER = Play.application().configuration().getString("mail.smtp.user");
    public static final String SMTP_PASS = Play.application().configuration().getString("mail.smtp.pass");
    public static final String MAIL_FROM = Play.application().configuration().getString("mailFrom");
    public static final String MAIL_FROM_PASS = Play.application().configuration().getString("mailFromPass");
    public static final String UPLOAD_IMAGES_FOLDER = Play.application().configuration().getString("upload.images");

}
