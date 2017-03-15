package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by ajla on 05-Jan-16.
 */
@Entity
public class Price extends Model {
    @Id
    public Integer id;
    public Apartment apartment;
    public Integer personsNo;
    public Integer nightsNo;


}
