package controllers;

import helpers.UserAccessLevel;
import models.Apartment;
import models.Cake;
import models.Item;
import play.mvc.Controller;

import java.io.File;
import java.util.List;

import models.Image;
import play.Logger;
import play.mvc.*;
import views.html.imagesUpload;

/**
 * Created by Ajla on 5.5.2016.
 */
public class Images extends Controller {

    /* ------------------- images upload render ------------------ */
    public Result imagesUploadRender(Integer apartmentId){
        Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;
        return ok(imagesUpload.render(apartmentId, userId));
    }

    /* ------------------- images upload  ------------------ */
//        @BodyParser.Of(value = BodyParser.MultipartFormData.class, maxLength = 1000 * 1024)
    public Result imagesUpload(Integer apartmentId) {
        Apartment apartment = Apartment.getApartmentById(apartmentId);

        Http.MultipartFormData body1 = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> fileParts = body1.getFiles();
        if (fileParts != null) {
            for (Http.MultipartFormData.FilePart filePart1 : fileParts) {
                File file = filePart1.getFile();
                Image image = Image.create(file, apartment.id);
                apartment.images.add(image);
            }
        }

        apartment.update();
        return redirect(routes.Images.listOfPicturesRender(apartmentId));
    }

    /* ------------------- list of images render ------------------ */
    public Result listOfPicturesRender(Integer apartmentId) {
        List<Image> images = Image.findApartmentImages(apartmentId);
        Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;
        return ok(views.html.Apartments.listOfImages.render(images, apartmentId, userId));
    }

    /* ------------------- delete image ------------------ */
    public Result deleteImage(String image_public_id) {
        Image image = Image.findImageById(image_public_id);
        Integer parentId;
        if (image.apartment != null) {
            parentId = image.apartment.id;
        } else if (image.item != null) {
            parentId = image.item.id;
        } else if (image.cake != null) {
            parentId = image.cake.id;
        } else {
            parentId = image.pastry.id;
        }

        image.delete();
        return redirect(routes.Images.listOfPicturesRender(parentId));
    }

        /* ------------------- add item image ------------------ */
    public Result imagesItemUpload(Integer itemId) {
        Item item = Item.findItemById(itemId);

        Http.MultipartFormData body1 = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> fileParts = body1.getFiles();
        if (fileParts != null) {
            for (Http.MultipartFormData.FilePart filePart1 : fileParts) {
                File file = filePart1.getFile();
                Image image = Image.createItemImage(file, item.id);
                item.images.add(image);
            }
        }

        item.update();
        return redirect(routes.Items.listOfItemImages(itemId));
    }




       /* ------------------- add cake image ------------------ */
    public Result imagesCakeUpload(Integer cakeId) {
        Cake  cake = Cake.findCakeById(cakeId);

        Http.MultipartFormData body1 = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> fileParts = body1.getFiles();
        if (fileParts != null) {
            for (Http.MultipartFormData.FilePart filePart1 : fileParts) {
                File file = filePart1.getFile();
                Image image = Image.createCakeImage(file, cake.id);
                cake.images.add(image);
            }
        }

        cake.update();
        return redirect(routes.Cakes.listOfCakeImages(cakeId));
    }
}
