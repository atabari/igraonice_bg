package models;

import com.avaje.ebean.Model;
import helpers.ItemCategories;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 5/5/2016.
 */

@Entity
public class Item extends Model {

    @Id
    public Integer id;
    public String name;
    public Integer price;
    @Column(columnDefinition = "TEXT")
    public String description;
    public String size;
    public Integer category;
    public Boolean isVisible;

    @ManyToOne
    public Store store;

    @OneToMany
    public List<Image> images;

    public Item() {}


    private static Model.Finder<String, Item> finder = new Model.Finder<>(Item.class);

            /* --------------- find item by id ---------------*/

    public static Item findItemById (Integer itemId) {
        return finder.where().eq("id", itemId).findUnique();
    }


    /* --------------- find all store items ---------------*/

    public static List<Item> findStoreItems(Integer storeId) {
        return finder.where().eq("store_id", storeId).findList();
    }

            /* --------------- create item  ---------------*/

    public static Item createItem(String name, Integer price, String description, String size, Store store, Integer category) {
        Item item = new Item();

        item.name = name;
        item.price = price;
        item.description = description;
        item.size = size;
        item.store = store;
        item.category = category;
        item.isVisible = true;
        item.save();
        return  item;
    }


            /* --------------- update item ---------------*/

    public static Integer updateItem(String name, Integer price, String description, String size,Integer category, Integer itemId) {

        Item item = findItemById(itemId);
        item.name = name;
        item.price = price;
        item.description = description;
        item.size = size;
        item.category = category;
        item.update();
        return item.store.id;
    }

                /* --------------- delete item ---------------*/

    public static Integer deleteItem(Integer itemId) {
        Item item = findItemById(itemId);
        for(Image image: item.images) {
            Image.deleteImage(image);
        }
        List<ItemReservation> itemReservations = ItemReservation.findItemReservationsByItemId(itemId);
        for (int k = 0; k < itemReservations.size(); k++) {
            itemReservations.get(k).delete();
        }

        item.delete();
        return  item.store.id;
    }


       /* --------------- find all male items  ---------------*/
    public static List<Item> findMaleItems() {
        return finder.where().eq("category", ItemCategories.DJECACI).eq("is_visible", 1).findList();
    }


    /* --------------- find all female items  ---------------*/
    public static List<Item> findFemaleItems() {
        return finder.where().eq("category", ItemCategories.DJEVOJCICE).eq("is_visible",1).findList();
    }

    /* --------------- find all other items  ---------------*/
    public static List<Item> findOtherItems() {
        return finder.where().eq("category", ItemCategories.OSTALO).eq("is_visible",1).findList();
    }


    /* ------------------- get first image if item has images, else return default image ------------------ */
    public static Image getFirstItemImage(Integer itemId) {
        Item item = findItemById(itemId);
        if (item.images.size() > 0) {
            return item.images.get(0);
        } else {
            return null;
        }
    }


           /* --------------- Items to recommend ---------------*/

    public static List<Item> itemsToRecommend(Integer itemId){

        List<Item> recommendedApartments = new ArrayList<>();

        Item item = finder.where().eq("id", itemId).findUnique();

        Integer price = item.price;

//        List<Item> items = finder.where().eq("location", item.store.location).findList();
        List<Item> items = finder.all();
        List<Integer> prices = new ArrayList<>();

        for(int k = 0; k < items.size(); k++) {
            prices.add(items.get(k).price);
        }

        for(int i=0; i < prices.size(); i++) {
            for (int j = price - 10; j <= price + 10; j++) {

                if (items.size() != 0 && items.get(i).price == j) {
                    recommendedApartments.add(items.get(i));
                }

            }
        }
        return recommendedApartments;
    }

}
