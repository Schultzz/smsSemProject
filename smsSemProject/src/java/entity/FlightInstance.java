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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author MS
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "FlightInstance.findByDates", query = "SELECT f FROM FlightInstance f WHERE f.dateTakeOf = :startDate AND f.departure = :startAirport"),
    @NamedQuery(name = "FlightInstance.findByDatesAndCities", query = "SELECT f FROM FlightInstance f WHERE f.dateTakeOf = :startDate AND f.departure = :departure AND"
            + " f.arrival = :arrival")
})
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
    @OneToMany(mappedBy = "flightInstance", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
    private String airline;

    public FlightInstance() {
    }

    public FlightInstance(Flight flight, Date dateTakeOf, Airport depature, Airport arrival, double price, String airline) {
        this.flight = flight;
        this.dateTakeOf = dateTakeOf;
        this.arrival = arrival;
        this.departure = depature;
        this.price = price;
        this.airline = airline;
        setUpSeats(flight.getSeats());
        this.reservations = new ArrayList<>();
    }

    private void setUpSeats(int seatCount) {
        this.flightInstanceSeats = new ArrayList<>();
        for (int i = 0; i < seatCount; i++) {
            this.flightInstanceSeats.add(new Seat(i));
        }

    }

    public Reservation addReservation(Customer customer, List<Customer> cList) {

        List<Seat> tempSeats = new ArrayList<>();
        for (Customer c : cList) {

            for (Seat freeSeat : flightInstanceSeats) {
                if (freeSeat.getCustomer() == null) {
                    freeSeat.setCustomer(c);
                    tempSeats.add(freeSeat);
                    break;
                }
            }

        }
        Reservation newReservation = new Reservation(customer, tempSeats, this);
        reservations.add(newReservation);
        return newReservation;
    }

    public int getFreeSeats() {
        int counter = 0;
        for (Seat flightInstanceSeat : flightInstanceSeats) {
            if (flightInstanceSeat.getCustomer() == null) {
                counter++;
            }
        }
        return counter;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
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
