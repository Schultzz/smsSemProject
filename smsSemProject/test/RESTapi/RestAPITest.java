/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RESTapi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Airport;
import entity.Customer;
import entity.Flight;
import entity.FlightInstance;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author Stefan Duro <stefduro@gmail.com>
 */
public class RestAPITest {

    private SimpleDateFormat df;
    private Gson gson;
    private EntityManager em;

    public RestAPITest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-mm-dd");

        setUpData();

    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testAPIFlightsDatePath() {
        Date sDate1 = null;
        Date sDate2 = null;

//        try {
//            sDate1 = df.parse("2011-01-05");
//            sDate2 = df.parse("2011-01-05");
//        } catch (ParseException ex) {
//            Logger.getLogger(RestAPITest.class.getName()).log(Level.SEVERE, null, ex);
//        }
        try {
            //Change the API path in future version due to API change
            String temp = makeHttpConnection("http://localhost:8084/smsSemProject/api/flights/2011-01-05/2011-01-05", "GET");
            JsonArray ja = new JsonParser().parse(temp).getAsJsonArray();
            
            System.out.println("Method 1:" + ja.size());
            assertTrue(ja.size() == 1);

            JsonObject temp1 = ja.get(0).getAsJsonObject();

            assertTrue(temp1.get("takeOffDate").getAsString().equals("1294182000000"));
            assertTrue(temp1.get("landingDate").getAsString().equals("1294182000000"));
            assertTrue(temp1.get("price").getAsString().equals("1337.37"));
            assertTrue(temp1.get("airline").getAsString().equals("Lyn airlines"));

        } catch (Exception ex) {
            Logger.getLogger(RestAPITest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void testAPIFlightsReservation(){

        try {
            String temp = makeHttpConnection("http://localhost:8084/smsSemProject/api/flights/2017-01-05/2017-01-05", "GET");
            
            JsonArray ja = new JsonParser().parse(temp).getAsJsonArray();

            assertTrue(ja.size() == 2);
            
        } catch (Exception ex) {
            Logger.getLogger(RestAPITest.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    private String makeHttpConnection(String url, String method) throws Exception {

        //Making new URL from string
        URL obj = new URL(url);

        //Setting up the connection
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //Setting method to use for http request
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", "Tester-Agent");

        int responseCode = con.getResponseCode();

        System.out.println("SENDING GET REQUEST TO URL: " + url);
        System.out.println("RESPONSE CODE: " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private void setUpData() {
        em = Persistence.createEntityManagerFactory("smsSemProjectPU").createEntityManager();
        
        Flight flight1 = new Flight("Helicopter", 5);

        Airport airport1 = new Airport("CPH", "Copenhagen");
        Airport airport2 = new Airport("LON", "London");
        Airport airport3 = new Airport("BIL", "Billund");
        Airport airport4 = new Airport("SND", "Soederborg");
        Airport airport5 = new Airport("FIS", "erttww");

        Customer c1 = new Customer("Per", "Larsen", "Laksevej 1", "SKinkeby", "1111", "Norge");
        Customer c2 = new Customer("Klavs", "Larsen", "Laksevej 1", "SKinkeby", "1111", "Norge");

        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-mm-dd");
        Date sDate1 = null, eDate1 = null;
        Date sDate2 = null, eDate2 = null;
        Date sDate3 = null, eDate3 = null;
        try {
            sDate1 = df.parse("2017-01-05");
            sDate2 = df.parse("2011-01-05");
            sDate3 = df.parse("2012-01-05");

//            long epoch = date.getTime();
//            System.out.println(date);
        } catch (ParseException ex) {
        }

        FlightInstance fi1 = new FlightInstance(flight1, sDate1, airport1, airport2, 1337.37, "Lyn airlines");
        FlightInstance fi2 = new FlightInstance(flight1, sDate2, airport3, airport5, 1337.37, "Lyn airlines");
        FlightInstance fi3 = new FlightInstance(flight1, sDate3, airport1, airport4, 1337.37, "Lyn airlines");
        FlightInstance fi4 = new FlightInstance(flight1, sDate1, airport1, airport4, 1000.37, "Lyn airlines");
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
        em.persist(fi4);
        em.getTransaction().commit();

    }

}
