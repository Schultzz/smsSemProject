/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import datalayer.DBFacade;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author Søren
 */
@Path("flights")
public class Rest {

    @Context
    private UriInfo context;
    private static DBFacade dbf;

    /**
     * Creates a new instance of Rest
     */
    public Rest() {
        dbf = DBFacade.getInstance();
    }

    /**
     * Retrieves any given flight within the date given in the input params
     *
     * @param startDate
     * @param endDate
     * @return an JSON object with all the flighs found
     */
    @GET
    @Produces("application/json")
    @Path("/{startDate}/{endDate}")
    public String getAllFlights(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {
        //Date format:  "yyyy.mm.dd"
        return dbf.getFlightsByDates(startDate, endDate);
    }

    /**
     * Retrieves all Flights when given a start- and end destination, and a data
     *
     * @param startAirport
     * @param endAirport
     * @param sDate
     * @param eDate
     * @return an JSON object with all the flighs found
     */
    @GET
    @Produces("application/json")
    @Path("/{startAirport}/{endAirport}/{sDate}/{eDate}")
    public String getAllFlightsFromRoutes(@PathParam("startAirport") String startAirport,
            @PathParam("endAirport") String endAirport, @PathParam("sDate") String sDate, @PathParam("eDate") String eDate) {
        //Date format:  "yyyy.mm.dd"
        //Airport codes are made uppercase to match the database
        return dbf.getFligtsByDatesAndAirpots(startAirport.toUpperCase(), endAirport.toUpperCase(), sDate, eDate);
    }

    /**
     * Retrieves a Flights when given a resevationId
     *
     * @param reservationId
     * @return an JSON object with the flight found
     */
    @GET
    @Produces("application/json")
    @Path("/{reservationId}")
    public String getReservation(@PathParam("reservationId") String reservationId) {

        return reservationId;
    }

    /**
     * Consumes a ReservationPayload object
     *
     * @param content
     * @return a Reservation JSON object
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{flightId}")
    public String addPerson(String content) {
        return content;
    }

    /**
     * Consumes a ReservationPayload object
     *
     * @param content
     * @return a Reservation object
     *
     */
    @DELETE
    @Consumes("application/json")
    @Produces("application/json")
    public String deletePerson(String content) {
        return content;
    }
}
