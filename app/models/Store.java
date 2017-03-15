package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by User on 5/5/2016.
 */
@Entity
public class Store extends Model {

    @Id
    public Integer id;
    public String name;
    public String location;
    public String address;
    public String workingHours;

    public String lat;
    public String lng;
    public Boolean isVisible;
    public Integer userId;

    @OneToMany
    public List<Item> items;

    public Store(){}

    public Store(String name, String location, String address, String workingHours, String lat, String lng, Boolean isVisible) {
        this.name = name;
        this.location = location;
        this.address = address;
        this.workingHours = workingHours;
        this.lat = lat;
        this.lng = lng;
        this.isVisible = isVisible;
    }

    private static Model.Finder<String, Store> finder = new Model.Finder<>(Store.class);


        /* --------------- find store by id ---------------*/

    public static Store findStoreById(Integer storeId) {
        return finder.where().eq("id", storeId).findUnique();
    }


        /* --------------- create store ---------------*/

    public static void createStore(String name, String location, String address, String workingHours, String lat, String lng, Integer userId) {
        Store store = new Store();
        store.name = name;
        store.location = location;
        store.address = address;
        store.workingHours = workingHours;
        store.lat = lat;
        store.lng = lng;
        store.isVisible = true;
        store.userId = userId;

        store.save();

    }
        /* --------------- update store ---------------*/

    public static Integer updateStore(String name, String location, String address, String workingHours, String lat, String lng, Integer storeId) {
        Store store = findStoreById(storeId);
        store.name = name;
        store.location = location;
        store.address = address;
        store.workingHours = workingHours;
        store.lat = lat;
        store.lng = lng;
        store.update();
        return store.userId;
    }

            /* --------------- delete store ---------------*/

    public static Integer deleteStore(Integer storeId) {
        Store store = findStoreById(storeId);
        for (int j = 0; j < store.items.size(); j++) {
           for(int i = 0; i < store.items.get(j).images.size(); i++) {
               Image.deleteImage(store.items.get(j).images.get(i));
           }
           List<ItemReservation> itemReservations = ItemReservation.findItemReservationsByItemId(store.items.get(j).id);
           for (int k = 0; k < itemReservations.size(); k++) {
               itemReservations.get(k).delete();
           }
            store.items.get(j).delete();
        }
        store.delete();
        return store.userId;
    }


        /* --------------- Finds all users stores ---------------*/

    public static List<Store> userStores(Integer userId){

        List<Store> stores = finder.where().eq("user_id", userId).findList();

        return stores;
    }


            /* --------------- Finds all stores ---------------*/

    public static List<Store> getAllStores() {
        return finder.all();
    }



       /* --------------- Store visibility on homepage ---------------*/

    public static void isVisible(Integer storeId){
        Store store = findStoreById(storeId);
        if (store.isVisible == false) {
            store.isVisible = true;
            for (int i = 0; i < store.items.size(); i ++) {
                store.items.get(i).isVisible = true;
                store.items.get(i).update();
            }
        }else if (store.isVisible == true){
            store.isVisible = false;
            for (int i = 0; i < store.items.size(); i ++) {
                store.items.get(i).isVisible = false;
                store.items.get(i).update();
            }
        }
        store.update();
    }

}
