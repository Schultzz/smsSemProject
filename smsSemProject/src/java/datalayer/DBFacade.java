/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Airport;
import entity.FlightInstance;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                "yyyy mm dd");
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
                "yyyy mm dd");
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
    public String flightReservation(String JSONReservationPayload) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getReservation(String reservationID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteReservationById(String reservationID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
