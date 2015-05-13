/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalayer;

import entity.exceptions.FlightNotFoundException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Søren
 */
public class dbTester {
    
    public static void main(String[] args) throws FlightNotFoundException {
        
        DBFacade dbf = DBFacade.getInstance();
        
        try {
            System.out.println(dbf.getFlightsByDates("CPH", "1483574400000"));
        } catch (FlightNotFoundException ex) {
            Logger.getLogger(dbTester.class.getName()).log(Level.SEVERE, null, ex);
        }
//        
      System.out.println(dbf.getFligtsByDatesAndAirpots("CPH", "SND", "1483574400000"));
//        
//        System.out.println(dbf.flightReservation("{'Passengers' : [{'firstName': 'Per', 'lastName' : 'Larsen', 'city' : '123By', 'country' : 'Okland', 'street' : 'asdvej' }, {'firstName': 'ASD', 'lastName' : 'ASDPER', 'city' : '567By', 'country' : 'IkkeOkLand', 'street' : 'test test tets' }]}", "22"));
//        
//        System.out.println(dbf.getReservation("103"));
        
    }
    
}
