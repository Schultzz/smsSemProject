/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author MS
 */
@Entity
public class FlightInstance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @ManyToOne
    private Flight flight;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateTakeOf;
    @OneToOne
    private Airport arrival;
    @OneToOne
    private Airport departure;
    private double price;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Seat> flightInstanceSeats;
    @OneToMany(mappedBy = "flightInstance", cascade = CascadeType.PERSIST)
    private List<Reservation> reservations;
    public FlightInstance() {
    }

    public FlightInstance(Flight flight, Date dateTakeOf, Airport arrival, Airport departure, double price) {
        this.flight = flight;
        this.dateTakeOf = dateTakeOf;
        this.arrival = arrival;
        this.departure = departure;
        this.price = price;
        setUpSeats(flight.getSeats());
        this.reservations = new ArrayList<>();
    }

    
    private void setUpSeats(int seatCount){
        this.flightInstanceSeats = new ArrayList<>();
        for (int i = 0; i < seatCount; i++) {
            this.flightInstanceSeats.add(new Seat(i));
        }
        
    }
    
    
    
    public void addReservation(Customer customer, List<Customer> cList){
        
        List<Seat> tempSeats = new ArrayList<>();
        for (Customer c : cList) {
            
            for (Seat freeSeat : flightInstanceSeats) {
                if(freeSeat.getCustomer()== null){
                    freeSeat.setCustomer(c);
                    tempSeats.add(freeSeat);
                    System.out.println("test");
                    break;
                }
            }
            
        }
        reservations.add(new Reservation(customer, tempSeats, this));
        
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateTakkeOf() {
        return dateTakeOf;
    }

    public void setDateTakkeOf(Date dateTakkeOf) {
        this.dateTakeOf = dateTakkeOf;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Airport getArrival() {
        return arrival;
    }

    public void setArrival(Airport arrival) {
        this.arrival = arrival;
    }

    public Airport getDeparture() {
        return departure;
    }

    public void setDeparture(Airport departure) {
        this.departure = departure;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Seat> getFlightInstanceSeats() {
        return flightInstanceSeats;
    }

    public void setFlightInstanceSeats(List<Seat> freeSeats) {
        this.flightInstanceSeats = freeSeats;
    }

    @Override
    public String toString() {
        return "entity.FlightInstance[ id=" + id + " ]";
    }

}
