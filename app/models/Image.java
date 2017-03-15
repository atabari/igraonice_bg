package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import play.Logger;

/**
 * Created by Ajla on 5.5.2016.
 */
@Entity
public class Image extends Model {

    @Id
    public Integer id;
    public String public_id;
    public String image_url;
    public String secret_image_url;

    @ManyToOne
    @JsonBackReference
    public Apartment apartment;

    @ManyToOne
    @JsonBackReference
    public Item item;

    @ManyToOne
    @JsonBackReference
    public Cake cake;

    @ManyToOne
    @JsonBackReference
    public Pastry pastry;

    public static Cloudinary cloudinary;


    public static Finder<Integer, Image> finder = new Finder<Integer, Image>(Image.class);

    public static Image createImage(String public_id, String image_url, String secret_image_url, Apartment apartment) {
        Image image = new Image();
        image.public_id = public_id;
        image.image_url = image_url;
        image.secret_image_url = secret_image_url;
        image.apartment = apartment;
        image.save();
        return image;
    }

    public static Image create(String public_id, String image_url, String secret_image_url) {
        Image i = new Image();
        i.public_id = public_id;
        i.image_url = image_url;
        i.secret_image_url = secret_image_url;
        i.save();
        return i;
    }

    public static Image create(File image, Integer itemId) {
        Map result;
        try {
            result = cloudinary.uploader().upload(image, null);
            return create(result, itemId);

        } catch (IOException e) {
            Logger.debug("Failed to save image.", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Image create(Map uploadResult, Integer apartmentId) {
        Image image = new Image();

        image.public_id = (String) uploadResult.get("public_id");
        image.image_url = (String) uploadResult.get("url");
        image.secret_image_url = (String) uploadResult.get("secure_url");
        if(apartmentId != null) {
            image.apartment = Apartment.getApartmentById(apartmentId);
        }
        image.save();
        return image;
    }

    /* ------------------- all images ------------------ */

    public static List<Image> all() {
        return finder.all();
    }

    /* ------------------- get image size ------------------ */

    public String getSize(int width, int height) {
        try {
            String url;
            url = cloudinary.url().format("jpg")
                    .transformation(new Transformation().width(width).height(height)).generate(public_id);
            return url;
        }catch (NullPointerException e){
            Logger.debug("Failed to receive image url.", e.getMessage());
            return "null";
        }
    }

    public String getApartmentImageProfile() {
        try {
            String url;
            url = cloudinary.url()
                    .transformation(new Transformation().width(450).height(250).crop("limit")).generate(public_id);
            return url;
        }catch (NullPointerException e){
            Logger.debug("Failed to receive image url.", e.getMessage());
            return "null";
        }
    }

    public String getApartmentImageProfileThumbnail() {
        try {
            String url;
            url = cloudinary.url()
                    .transformation(new Transformation().width(80).height(80).crop("thumb")).generate(public_id);
            return url;
        }catch (NullPointerException e){
            Logger.debug("Failed to receive image url.", e.getMessage());
            return "null";
        }
    }

    public String getImageForPreview() {
        try {
            String url;
            url = cloudinary.url()
                    .transformation(new Transformation().width(253).height(253).crop("fill")).generate(public_id);
            return url;
        }catch (NullPointerException e){
            Logger.debug("Failed to receive image url.", e.getMessage());
            return "null";
        }
    }

    public String getListImagesThumbnail() {
        try {
            String url;
            url = cloudinary.url()
                    .transformation(new Transformation().width(100).height(100).crop("fill")).generate(public_id);
            return url;
        }catch (NullPointerException e){
            Logger.debug("Failed to receive image url.", e.getMessage());
            return "null";
        }
    }

    public String getItemImage() {
        try {
            String url;
            url = cloudinary.url()
                    .transformation(new Transformation().width(500).height(300).crop("fill")).generate(public_id);
            return url;
        }catch (NullPointerException e){
            Logger.debug("Failed to receive image url.", e.getMessage());
            return "null";
        }
    }

    public String getItemImageZoom() {
        try {
            String url;
            url = cloudinary.url()
                    .transformation(new Transformation().width(800).height(600)).generate(public_id);
            return url;
        }catch (NullPointerException e){
            Logger.debug("Failed to receive image url.", e.getMessage());
            return "null";
        }
    }


    /* ------------------- delete image ------------------ */

    public static Integer deleteImage(Image image) {

        deleteImgFromCloudinary(image);

        if (image.apartment != null) {
            return image.apartment.id;
        }
        if (image.item != null) {
            return image.item.id;
        }
        if (image.cake != null) {
            return image.cake.id;
        }
        else {
            return image.pastry.id;
        }
    }

    private static void deleteImgFromCloudinary(Image image) {
        try {
            cloudinary.uploader().destroy(image.public_id, null);
        } catch (IOException e) {
            Logger.debug("Failed to delete image.", e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        image.delete();
    }

    /* ------------------- find images by apartment id ------------------ */

    public static List<Image> findApartmentImages(Integer apartmentId) {
        return finder.where().eq("apartment_id", apartmentId).findList();
    }

    /* ------------------- find images by item id ------------------ */

    public static List<Image> findItemImages(Integer itemId) {
        return finder.where().eq("item_id", itemId).findList();
    }

        /* ------------------- find images by item id ------------------ */

    public static List<Image> findPastryImages(Integer itemId) {
        return finder.where().eq("pastry_id", itemId).findList();
    }

        /* ------------------- find cake images ------------------ */
    public static List<Image> findCakeImages(Integer cakeId) {
        return finder.where().eq("cake_id", cakeId).findList();
    }



    /* ------------------- find images by id ------------------ */
    public static Image findImageById(String public_id){
        return finder.where().eq("public_id", public_id).findUnique();
    }


    /* ------------------- create image for items ------------------ */
    public static Image createItemImage(File image, Integer itemId) {
        Map result;

        try {
            result = cloudinary.uploader().upload(image, null);
            return createItemImage(result, itemId);

        } catch (IOException e) {
            Logger.debug("Failed to save image.", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Image createItemImage(Map uploadResult, Integer itemId) {
        Image image = new Image();

        image.public_id = (String) uploadResult.get("public_id");
        Logger.debug(image.public_id);
        image.image_url = (String) uploadResult.get("url");
        Logger.debug(image.image_url);
        image.secret_image_url = (String) uploadResult.get("secure_url");
        Logger.debug(image.secret_image_url);
        if(itemId != null) {
            image.item = Item.findItemById(itemId);
        }
        image.save();
        return image;
    }



    /* ------------------- create image for cakes ------------------ */

    public static Image createCakeImage(File image, Integer cakeId) {
        Map result;

        try {
            result = cloudinary.uploader().upload(image, null);
            return createCakeImage(result, cakeId);

        } catch (IOException e) {
            Logger.debug("Failed to save image.", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Image createCakeImage(Map uploadResult, Integer cakeId) {
        Image image = new Image();

        image.public_id = (String) uploadResult.get("public_id");
        Logger.debug(image.public_id);
        image.image_url = (String) uploadResult.get("url");
        Logger.debug(image.image_url);
        image.secret_image_url = (String) uploadResult.get("secure_url");
        Logger.debug(image.secret_image_url);
        if(cakeId != null) {
            image.cake = Cake.findCakeById(cakeId);
        }
        image.save();
        return image;
    }


    /* ------------------- create image for pastries ------------------ */

    public static Image createPastryImage(File image, Integer pastryId) {
        Map result;

        try {
            result = cloudinary.uploader().upload(image, null);
            return createPastryImage(result, pastryId);

        } catch (IOException e) {
            Logger.debug("Failed to save image.", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Image createPastryImage(Map uploadResult, Integer pastryId) {
        Image image = new Image();

        image.public_id = (String) uploadResult.get("public_id");
        Logger.debug(image.public_id);
        image.image_url = (String) uploadResult.get("url");
        Logger.debug(image.image_url);
        image.secret_image_url = (String) uploadResult.get("secure_url");
        Logger.debug(image.secret_image_url);
        if(pastryId != null) {
            image.pastry = Pastry.findPastryById(pastryId);
        }
        image.save();
        return image;
    }
}
