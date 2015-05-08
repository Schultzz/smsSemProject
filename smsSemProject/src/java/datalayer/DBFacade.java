/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Airport;
import entity.Customer;
import entity.FlightInstance;
import entity.Reservation;
import entity.Seat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Søren
 */
public class DBFacade implements DBFacadeInterface {

    private static DBFacade instance;
    private EntityManager em;
    private Gson gson;

    private DBFacade() {

        em = Persistence.createEntityManagerFactory("smsSemProjectPU").createEntityManager();
        gson = new Gson();
    }

    public static DBFacade getInstance() {

        if (instance == null) {
            instance = new DBFacade();
        }

        return instance;
    }

    @Override
    public String getFlightsByDates(String startDate, String endDate) {
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-mm-dd");
        Date sDate = null, eDate = null;
        try {
            sDate = df.parse(startDate);
            eDate = df.parse(endDate);
        } catch (ParseException ex) {
            Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

        Query q = em.createNamedQuery("FlightInstance.findByDates");
        q.setParameter("startDate", sDate, TemporalType.DATE);
        q.setParameter("endDate", eDate, TemporalType.DATE);

        List<FlightInstance> fInstanceList = (List<FlightInstance>) q.getResultList();

        JsonArray jsonList = new JsonArray();
        for (FlightInstance fInstance : fInstanceList) {
            JsonObject jo = new JsonObject();
            jo.addProperty("airline", fInstance.getAirline());
            jo.addProperty("price", fInstance.getPrice());
            jo.addProperty("flightId", fInstance.getId());
            jo.addProperty("takeOffDate", fInstance.getDateTakkeOf().getTime());
            jo.addProperty("landingDate", fInstance.getDateTakkeOf().getTime());
            jo.addProperty("depature", fInstance.getDeparture().getCode());
            jo.addProperty("arrival", fInstance.getArrival().getCode());
            jo.addProperty("seats", fInstance.getFlight().getSeats());
            jo.addProperty("available seats", fInstance.getFreeSeats());
            jo.addProperty("bookingCode", false);
            jsonList.add(jo);
        }

        return gson.toJson(jsonList);
    }

    @Override
    public String getFligtsByDatesAndAirpots(String startAirport, String endAirport, String startDate, String endDate) {
        //Start airport
        Query q1 = em.createNamedQuery("Airport.findAirportByCode");

        q1.setParameter("code", startAirport);

        Airport startAir = (Airport) q1.getSingleResult();

        //end airport
        Query q2 = em.createNamedQuery("Airport.findAirportByCode");

        q2.setParameter("code", endAirport);

        Airport endAir = (Airport) q2.getSingleResult();

        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-mm-dd");
        Date sDate = null, eDate = null;
        try {
            sDate = df.parse(startDate);
            eDate = df.parse(endDate);
        } catch (ParseException ex) {
            Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

        Query q = em.createNamedQuery("FlightInstance.findByDatesAndCities");

        q.setParameter("startDate", sDate);
        q.setParameter("endDate", eDate);
        q.setParameter("departure", startAir);
        q.setParameter("arrival", endAir);

        List<FlightInstance> fis = q.getResultList();

        JsonArray jsonList = new JsonArray();
        for (FlightInstance fInstance : fis) {
            JsonObject jo = new JsonObject();
            jo.addProperty("airline", fInstance.getAirline());
            jo.addProperty("price", fInstance.getPrice());
            jo.addProperty("flightId", fInstance.getId());
            jo.addProperty("takeOffDate", fInstance.getDateTakkeOf().getTime());
            jo.addProperty("landingDate", fInstance.getDateTakkeOf().getTime());
            jo.addProperty("depature", fInstance.getDeparture().getCode());
            jo.addProperty("arrival", fInstance.getArrival().getCode());
            jo.addProperty("seats", fInstance.getFlight().getSeats());
            jo.addProperty("available seats", fInstance.getFreeSeats());
            jo.addProperty("bookingCode", false);
            jsonList.add(jo);
        }

        return gson.toJson(jsonList);
    }

    @Override
    public String flightReservation(String JSONReservationPayload, String flightId) {

        em.getTransaction().begin();
        JsonObject jo = new JsonParser().parse(JSONReservationPayload).getAsJsonObject();
        JsonArray passengers = jo.getAsJsonArray("Passengers");
        List<Customer> cList = new ArrayList<>();
        for (JsonElement passenger : passengers) {
            JsonObject tempJo = passenger.getAsJsonObject();
            Customer tempCustomer = new Customer();
            tempCustomer.setfName(tempJo.get("firstName").getAsString());
            tempCustomer.setlName(tempJo.get("lastName").getAsString());
            tempCustomer.setCity(tempJo.get("city").getAsString());
            tempCustomer.setCountry(tempJo.get("country").getAsString());
            tempCustomer.setStreet(tempJo.get("street").getAsString());
            cList.add(tempCustomer);
            em.persist(tempCustomer);
        }

        FlightInstance fInstance = em.find(FlightInstance.class, flightId);
        Reservation reservation = fInstance.addReservation(cList.get(0), cList);
        em.persist(reservation);//should work without this?
        em.merge(fInstance);
        em.getTransaction().commit();

        //Setting up the reservationobject to return as JSON
        JsonObject reservationJo = new JsonObject();
        reservationJo.addProperty("reservationID", reservation.getId());
        reservationJo.addProperty("flightID", fInstance.getId());//FlightID or flightinstanceID?
        reservationJo.add("Passengers", passengers);
        reservationJo.addProperty("totalPrice", fInstance.getPrice());
        return reservationJo.toString();
    }

    @Override
    public String getReservation(String reservationID) {

        Reservation reservation = em.find(Reservation.class, reservationID);
        JsonArray passengerArray = new JsonArray();
        JsonObject tempPassenger;
        Customer customer;
        for (Seat seat : reservation.getSeat()) {
            customer = seat.getCustomer();
            tempPassenger = new JsonObject();
            tempPassenger.addProperty("firstName", customer.getfName());
            tempPassenger.addProperty("lastName", customer.getlName());
            tempPassenger.addProperty("city", customer.getCity());
            tempPassenger.addProperty("country", customer.getCountry());
            tempPassenger.addProperty("street", customer.getStreet());
            passengerArray.add(tempPassenger);
        }

        JsonObject reservationJo = new JsonObject();
        reservationJo.addProperty("reservationID", reservation.getId());
        reservationJo.addProperty("flightID", reservation.getFlightInstance().getId());
        reservationJo.addProperty("Passengers", passengerArray.toString());
        reservationJo.addProperty("totalPrice", reservation.getFlightInstance().getPrice());

        return reservationJo.toString();
    }

    @Override
    public String deleteReservationById(String reservationID) {

        em.getTransaction().begin();

        Query q = em.createNamedQuery("Reservation.getReservation");

        q.setParameter("resId", reservationID);

        Reservation reservation = (Reservation) q.getSingleResult();

        int totalPriceCounterMlg = 0;

        JsonObject jo = new JsonObject();
        jo.addProperty("reservationID", reservation.getId());
        jo.addProperty("flightID", reservation.getFlightInstance().getId());
        JsonArray jsonList = new JsonArray();
        for (Seat seat : reservation.getSeat()) {
            JsonObject passJsonObject = new JsonObject();
            passJsonObject.addProperty("firstName", seat.getCustomer().getfName());
            passJsonObject.addProperty("lastName", seat.getCustomer().getlName());
            passJsonObject.addProperty("city", seat.getCustomer().getCity());
            passJsonObject.addProperty("country", seat.getCustomer().getCountry());
            passJsonObject.addProperty("street", seat.getCustomer().getStreet());
            jsonList.add(passJsonObject);
            // to calculate totalprice
            totalPriceCounterMlg++;
        }
        jo.add("Passengers", jsonList);

        //Multiplyes the count of passengers on same resevation with the price
        double totalPrice = (reservation.getFlightInstance().getPrice() * totalPriceCounterMlg);

        jo.addProperty("totalPrice", totalPrice);

        //Removes the passengers from the seat, by setting them null
        for (Seat seat : reservation.getSeat()) {
            seat.setCustomer(null);
        }

        //merginen the new changes
        em.merge(reservation);

        //Removing the reservation from the database
        Query query = em.createQuery("DELETE FROM Reservation r WHERE r.id = :id");

        query.setParameter("id", reservationID);

        int deleted = query.executeUpdate();

        em.getTransaction().commit();

        if (deleted >= 1) {
            System.out.println(deleted + " deleted!");
            return jo.toString();
        }

        return "'error':'intet at slettet'";
    }

}
