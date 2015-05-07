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
public class dbTester {
    
    public static void main(String[] args) {
        
        DBFacade dbf = DBFacade.getInstance();
        
        System.out.println(dbf.getFlightsByDates("2015-01-02", "2020-01-02"));
        
        System.out.println(dbf.getFligtsByDatesAndAirpots("CPH", "SND", "2015-01-02", "2020-01-02"));
        
        System.out.println(dbf.flightReservation("{'Passengers' : [{'firstName': 'Per', 'lastName' : 'Larsen', 'city' : '123By', 'country' : 'Okland', 'street' : 'asdvej' }, {'firstName': 'ASD', 'lastName' : 'ASDPER', 'city' : '567By', 'country' : 'IkkeOkLand', 'street' : 'test test tets' }]}", "22"));
        
    }
    
}
