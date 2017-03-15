package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by User on 5/24/2016.
 */
@Entity
public class Cake extends Model {

    @Id
    public Integer id;

    public String name;
    public String ingredients;
    public Integer price;
    @Column(columnDefinition = "TEXT")
    public String description;
    public Integer numberOfPersons;

    @OneToMany
    public List<Image> images;

    @ManyToOne
    public Pastry pastry;

    public Cake() {}

    private static Model.Finder<String, Cake> finder = new Model.Finder<>(Cake.class);


    /* --------------- find cake by id  ---------------*/

    public static Cake findCakeById(Integer cakeId) {
        return finder.where().eq("id", cakeId).findUnique();
    }


    /* --------------- find cakes by Store id  ---------------*/
    public static List<Cake> findAllCakesByPastryId(Integer pastryId) {
        return finder.where().eq("pastry_id", pastryId).findList();
    }




    /* --------------- create cake  ---------------*/

    public static Cake createCake(String name, String ingredients, Integer price, String description, Integer numberOfPersons, Integer pastryId) {
        Cake cake = new Cake();
        Pastry pastry = Pastry.findPastryById(pastryId);

        cake.name = name;
        cake.ingredients = ingredients;
        cake.price = price;
        cake.description = description;
        cake.numberOfPersons = numberOfPersons;
        cake.pastry = pastry;
        cake.save();
        return cake;
    }

    /* --------------- update cake  ---------------*/

    public static Integer updateCake(String name, String ingredients, Integer price, String description, Integer numberOfPersons, Integer cakeId) {
        Cake cake = findCakeById(cakeId);

        cake.name = name;
        cake.ingredients = ingredients;
        cake.price = price;
        cake.description = description;
        cake.numberOfPersons = numberOfPersons;
        cake.update();
        return cake.pastry.id;
    }


    /* --------------- delete cake  ---------------*/
    public static void deleteCake(Integer cakeId) {
        Cake cake = findCakeById(cakeId);
        for(int i = 0; i < cake.images.size(); i ++) {
            Image.deleteImage(cake.images.get(i));
        }
        cake.delete();
    }


    /* ------------------- get first image if cake has images, else return default image ------------------ */
    public static Image getFirstCakeImage(Integer cakeId) {
        Cake cake = findCakeById(cakeId);
        if (cake.images.size() > 0) {
            return cake.images.get(0);
        } else {
            return null;
        }
    }

}
