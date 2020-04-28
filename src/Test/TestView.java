/*
 * Type class Test.TestView. Only adds travels.
 *
 * Test.TestView.java
 *
 * @version 1.0
 * @author Pablo Sanz Alguacil
 */

package Test;

import Control.Office;
import Model.Passenger;
import Model.SalesDesk;
import Model.Travel;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestView {
    private static final String PASSENGERS_FILE_PATH = "src/storage/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/travels/status.csv";

    public static void main(String[] args) throws IOException {
        SalesDesk salesDesk = new SalesDesk();
        Office office = new Office(salesDesk);


        //CREACIONDE VIAJES
        Travel travel1 = new Travel("1", "MADRID", "BARCELONA",
                new GregorianCalendar(2020, 03, 1, 14, 0),
                "4x12");
        Travel travel2 = new Travel("2","MADRID", "ZARAGOZA",
                new GregorianCalendar(2020, 03, 1, 21, 30),
                "4x12");
        Travel travel3 = new Travel("3", "MADRID", "TERUEL",
                new GregorianCalendar(2020, 03, 1, 9, 0),
                "5x12");
        Travel travel4 = new Travel("4", "MADRID", "SANTANDER",
                new GregorianCalendar(2020, 03, 1, 11, 0),
                "3x15");
        Travel travel5 = new Travel("5", "ZARAGOZA", "TERUEL",
                new GregorianCalendar(2020, 03, 1, 8, 0),
                "4x12");
        Travel travel6 = new Travel("6", "ZARAGOZA", "VALENCIA",
                new GregorianCalendar(2020, 03, 1, 23, 4),
                "4x12");
        Travel travel7 = new Travel("7", "ZARAGOZA", "VALENCIA",
                new GregorianCalendar(2020, 03, 1, 22, 01),
                "4x12");
        Travel travel8 = new Travel("8", "ZARAGOZA", "VALENCIA",
                new GregorianCalendar(2020, 03, 1, 23, 15),
                "4x12");
        Travel travel9 = new Travel("9", "ZARAGOZA", "VALENCIA",
                new GregorianCalendar(2020, 03, 1, 22, 15),
                "4x12");


        //SE AÑADEN LOS VIAJES
        salesDesk.addTravel(travel1);
        salesDesk.addTravel(travel2);
        salesDesk.addTravel(travel3);
        salesDesk.addTravel(travel4);
        salesDesk.addTravel(travel5);
        salesDesk.addTravel(travel6);
        salesDesk.addTravel(travel7);
        salesDesk.addTravel(travel8);
        salesDesk.addTravel(travel9);

        //CREACION DE PASJEROS
        Passenger passenger1 = new Passenger("11111111A","Tony", "Stark");
        Passenger passenger2 = new Passenger("22222222B", "Steve", "Rogers");
        Passenger passenger3 = new Passenger("33333333C", "Thor", "Tilla");
        Passenger passenger4 = new Passenger("44444444D", "Bruce", "Wayne");
        Passenger passenger5 = new Passenger("55555555E", "Bruce", "Banner");

        //SE AÑADEN LOS PASAJEROS
        if(!salesDesk.addPassenger(passenger1)) System.out.println("DNI ya en memoria");
        salesDesk.addPassenger(passenger2);
        salesDesk.addPassenger(passenger3);
        salesDesk.addPassenger(passenger4);
        salesDesk.addPassenger(passenger5);

        //SE ASIGNAN ASIENTOS A 5 VIAJEROS Y UNO DE ELLOS SE ELIMINA
        salesDesk.assignSeat(travel1, passenger1, 5);
        salesDesk.assignSeat(travel1, passenger2, 6);
        salesDesk.assignSeat(travel1, passenger4, 30);
        salesDesk.assignSeat(travel1, passenger5, 42);
        if(!salesDesk.assignSeat(travel1, passenger1,59)) System.out.println("Asiento no disponible");

    }
}