/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer;

import java.util.Date;

/**
 *
 * @author Søren
 */
public interface DBFacadeInterface {
    
    public String getFlightsByDates(String startAirport, String startDate);
    public String getFligtsByDatesAndAirpots(String startAiport, String endAiport, String startDate);
    public String flightReservation(String JSONReservationPayload, String flightId);
    public String getReservation(String reservationID);
    public String deleteReservationById(String reservationID);
}
