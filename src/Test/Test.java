/*
 * Type class Test.Test. It doesn't loads any data.
 *
 * Test.Test.java
 *
 * @version 1.0
 * @author Pablo Sanz Alguacil
 */

package Test;

import Control.Office;
import Control.OyenteVista;
import Model.SalesDesk;
import Model.Passenger;
import Model.Travel;
import View.MainFrame;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test {
    private static final String PASSENGERS_FILE_PATH = "src/storage/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/travels/status.csv";

    public static void main(String[] args) throws IOException {
        SalesDesk salesDesk = new SalesDesk();
        Office office = new Office(salesDesk);

        //CREACION DE PASJEROS
        Passenger passenger1 = new Passenger("11111111A","Tony", "Stark");
        Passenger passenger2 = new Passenger("22222222B", "Steve", "Rogers");
        Passenger passenger3 = new Passenger("33333333C", "Thor", "Tilla");
        Passenger passenger4 = new Passenger("44444444D", "Bruce", "Wayne");
        Passenger passenger5 = new Passenger("55555555E", "Bruce", "Banner");

        //CREACIONDE VIAJES
        Travel travel1 = new Travel("1", "MADRID", "BARCELONA",
                new GregorianCalendar(2020, Calendar.MARCH, 25, 14, 0),
                "4x12");
        Travel travel2 = new Travel("2","BARCELONA", "MADRID",
                new GregorianCalendar(2020, Calendar.MARCH, 27, 21, 30),
                "4x12");
        Travel travel3 = new Travel("3", "ZARAGOZA", "TERUEL",
                new GregorianCalendar(2020, Calendar.MARCH, 26, 9, 0),
                "4x12");
        Travel travel4 = new Travel("4", "SEVILLA", "SANTANDER",
                new GregorianCalendar(2020, Calendar.MARCH, 19, 11, 0),
                "4x12");
        Travel modifiedTravel3 = new Travel("3", "ZARAGOZA", "VALENCIA",
                new GregorianCalendar(2020, Calendar.MARCH, 19, 11, 0),
                "4x12");


        System.out.println("~~~SE AÑADEN LOS PASAJEROS Y VIAJES~~~");
        //SE AÑADEN LOS PASAJEROS
        salesDesk.addPassenger(passenger1);
        if(!salesDesk.addPassenger(passenger1)) System.out.println("DNI ya en memoria");
        salesDesk.addPassenger(passenger2);
        salesDesk.addPassenger(passenger3);
        salesDesk.addPassenger(passenger4);
        salesDesk.addPassenger(passenger5);


        //SE AÑADEN LOS VIAJES
        salesDesk.addTravel(travel1);
        if(!salesDesk.addTravel(travel1)) System.out.println("VIAJE ya en memoria");
        salesDesk.addTravel(travel2);
        salesDesk.addTravel(travel3);
        salesDesk.addTravel(travel4);
        System.out.println("\n\n");

        //SE IMPRIME LA LISTA DE PASAJEROS Y VIAJES
        System.out.println("~~~PASAJEROS Y VIAJES - SIN MODIFICAR~~~");
        System.out.println(salesDesk.listPassengers());
        System.out.println("--------------------------------------------");
        System.out.println(salesDesk.listTravels());
        System.out.println("\n\n\n");


        //SE MODIFICA UN PASAJERO Y UN VIAJE
        salesDesk.modifyPassenger("44444444D", new Passenger("44444444D", "Agapito", "Wayne"));
        salesDesk.modifyTravel("3", modifiedTravel3);

        System.out.println("~~~PASAJEROS Y VIAJES - PASAJERO 44...D MODIFICADO Y VIAJE 3 MODIFICADO~~~");
        System.out.println(salesDesk.listPassengers());
        System.out.println("--------------------------------------------");
        System.out.println(salesDesk.listTravels());
        System.out.println("\n\n\n");


        //SE ELIMINA UN PASAJERO Y UN VIAJE
        salesDesk.deletePassenger(passenger3);
        salesDesk.deleteTravel(travel2);

        System.out.println("~~~PASAJEROS Y VIAJES - PASAJERO 33...C Y VIAJE 2 ELIMINADOS~~~");
        System.out.println(salesDesk.listPassengers());
        System.out.println("--------------------------------------------");
        System.out.println(salesDesk.listTravels());
        System.out.println("\n\n\n");


        //SE ASIGNAN ASIENTOS A 5 VIAJEROS Y UNO DE ELLOS SE ELIMINA
        System.out.println("~~~ASIGNACION DE ASIENTOS~~~");
        salesDesk.assignSeat(travel1, passenger1, 5);
        salesDesk.assignSeat(travel1, passenger2, 6);
        salesDesk.assignSeat(travel1, passenger4, 30);
        salesDesk.assignSeat(travel1, passenger5, 42);
        if(!salesDesk.assignSeat(travel1, passenger1,59)) System.out.println("Asiento no disponible");
        salesDesk.unassignSeat(travel1, 6);
        System.out.println("\n\n\n");


        //SE IMPRIME LA HOJA DE RUTA DEL VIAJE 1
        System.out.println(salesDesk.viewTravelSheet(travel1));



        salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        salesDesk.saveTravels(TRAVELS_FILE_PATH);
        salesDesk.saveTravelsStatus(TRAVELS_STATUS_FILE_PATH);
    }
}