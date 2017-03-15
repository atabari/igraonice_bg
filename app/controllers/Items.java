package controllers;

import com.sun.org.apache.regexp.internal.REDebugCompiler;
import helpers.Authenticator;
import helpers.UserAccessLevel;
import models.AppUser;
import models.Image;
import models.Item;
import models.Store;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.io.File;
import java.util.List;

/**
 * Created by User on 5/5/2016.
 */
public class Items extends Controller {



          /* --------------- item render  ---------------*/

    public Result item(Integer itemId) {
        Item item = Item.findItemById(itemId);
        AppUser currentUser = UserAccessLevel.getCurrentUser(ctx());
        List<Item> items = Item.itemsToRecommend(itemId);
        return ok(views.html.item.item.render(item,currentUser,items));
    }

    /* --------------- list of items ---------------*/
        @Security.Authenticated(Authenticator.PokloniFilter.class)

        public Result listOfStoreItems(Integer storeId) {
        List<Item> items  = Item.findStoreItems(storeId);
        return ok(views.html.item.listOfStoreItems.render(items, storeId));
    }



    /* --------------- create item render ---------------*/
    @Security.Authenticated(Authenticator.PokloniFilter.class)

    public Result createItemRender(Integer storeId) {
        return ok(views.html.item.createItem.render(storeId));
    }


    /* --------------- create item ---------------*/
    @Security.Authenticated(Authenticator.PokloniFilter.class)

    public Result createItem(Integer storeId) {

        DynamicForm form = Form.form().bindFromRequest();

        String name = form.field("name").value();
        Integer price = Integer.parseInt(form.field("price").value());
        String description = form.field("description").value();
        String size = form.field("size").value();
        Integer itemCategory =Integer.parseInt(form.field("itemCategory").value());
        Store store = Store.findStoreById(storeId);

        Item item = Item.createItem(name, price, description, size, store, itemCategory);

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

        return redirect(routes.Items.listOfStoreItems(storeId));
    }

    /* --------------- update item render ---------------*/

    public Result updateItemRender(Integer itemId) {
        Item item = Item.findItemById(itemId);
        return ok(views.html.item.updateItem.render(item));
    }

    /* --------------- update item  ---------------*/
    @Security.Authenticated(Authenticator.PokloniFilter.class)

    public Result updateItem(Integer itemId) {
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.field("name").value();
        Integer price = Integer.parseInt(form.field("price").value());
        String description = form.field("description").value();
        String size = form.field("size").value();
        Integer itemCategory =Integer.parseInt(form.field("itemCategory").value());


        Integer storeId = Item.updateItem(name, price, description, size,itemCategory, itemId);
        return redirect(routes.Items.listOfStoreItems(storeId));
    }

        /* --------------- delete item  ---------------*/
    @Security.Authenticated(Authenticator.PokloniFilter.class)

    public Result deleteItem(Integer itemId) {
        Integer storeId = Item.deleteItem(itemId);
        return redirect(routes.Items.listOfStoreItems(storeId));
    }

            /* --------------- items panel  ---------------*/

    public Result itemsPanel() {
        return ok(views.html.item.itemsPanel.render());
    }

    /* --------------- list of items images  ---------------*/

    public Result listOfItemImages(Integer itemId) {
        List<Image> images = Image.findItemImages(itemId);
        return ok(views.html.item.listOfItemImages.render(images, itemId));
    }

        /* --------------- add item images ---------------*/
    public Result addItemImages(Integer itemId) {
        return ok(views.html.item.addItemImage.render(itemId));
    }


       /* --------------- find all male items  ---------------*/

    public Result findMaleItems() {
        List<Item> items = Item.findMaleItems();
        return ok(views.html.item.categoryItem.render(items));
    }

      /* --------------- find all female items  ---------------*/

    public Result findFemaleItems() {
        List<Item> items = Item.findFemaleItems();
        return ok(views.html.item.categoryItem.render(items));
    }

      /* --------------- find all other items  ---------------*/

    public Result findOtherItems() {
        List<Item> items = Item.findOtherItems();
        return ok(views.html.item.categoryItem.render(items));
    }



}
