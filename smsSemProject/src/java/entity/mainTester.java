/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
/**
 *
 * @author Søren
 */
public class mainTester {
    public static void main(String[] args) {
        
        Customer c = new Customer();
        
        EntityManager em = Persistence.createEntityManagerFactory("smsSemProjectPU").createEntityManager();
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        
        
        System.out.println(c.getId());
        Flight f = new Flight();
        System.out.println(f.getId());
    }
    
}
