/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Søren
 */
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String fName;
    private String lName;
    private String street;
    private String city;
    private String zip;
    private String country;
    
    public Customer(){
        
    }

    public Customer(String fName, String lName, String street, String city, String zip, String country) {
        this.fName = fName;
        this.lName = lName;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.country = country;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }
    
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + id + " ]";
    }
    
}
