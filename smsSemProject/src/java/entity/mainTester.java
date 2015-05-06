/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
/**
 *
 * @author Søren
 */
public class mainTester {
    public static void main(String[] args) {
        
        
        
        EntityManager em = Persistence.createEntityManagerFactory("smsSemProjectPU").createEntityManager();
        
        Flight flight1 = new Flight("Helicopter", 5);
        
        Airport airport1 = new Airport("CPH", "Copenhagen");
        Airport airport2 = new Airport("LON", "London");
        Airport airport3 = new Airport("BIL", "Billund");
        Airport airport4 = new Airport("SND", "Soederborg");
        Airport airport5 = new Airport("FIS", "FISSEH");
        
        Customer c1 = new Customer("Per", "Larsen", "Laksevej 1", "SKinkeby", "1111", "Norge");
        Customer c2 = new Customer("Klavs", "Larsen", "Laksevej 1", "SKinkeby", "1111", "Norge");
        
        
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy mm dd");
        Date sDate = null, eDate = null;
        try {
            sDate = df.parse("2017 01 05");
            
//            long epoch = date.getTime();
//            System.out.println(date);
        } catch (ParseException ex) {
        }
        
        
        FlightInstance fi1 = new FlightInstance(flight1, sDate, airport1, airport2, 1337.37, "Lyn airlines");
        FlightInstance fi2 = new FlightInstance(flight1, sDate, airport3, airport5, 1337.37, "Lyn airlines");
        FlightInstance fi3 = new FlightInstance(flight1, sDate, airport1, airport4, 1337.37, "Lyn airlines");
        List<Customer> cList = new ArrayList();
        cList.add(c1);
        cList.add(c2);
        fi1.addReservation(c1, cList);
        em.getTransaction().begin();
        em.persist(c1);
        em.persist(c2);
        em.persist(airport1);
        em.persist(airport2);
        em.persist(airport3);
        em.persist(airport4);
        em.persist(airport5);
        em.persist(flight1);
        em.persist(fi1);
        em.persist(fi2);
        em.persist(fi3);
        em.getTransaction().commit();
        
        
    }
    
}
