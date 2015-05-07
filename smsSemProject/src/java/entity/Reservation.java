/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Søren
 */
@Entity
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @OneToOne
    private Customer customer;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Seat> seat;
    @ManyToOne
    private FlightInstance flightInstance;
    
    
    
    public Reservation(){
        
    }

    public Reservation(Customer customer, List<Seat> tempSeats, FlightInstance flightInstance) {
        this.customer = customer;
        this.flightInstance = flightInstance;
        this.seat = tempSeats;
    }
    
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "entity.Reservation[ id=" + id + " ]";
    }
    
}
