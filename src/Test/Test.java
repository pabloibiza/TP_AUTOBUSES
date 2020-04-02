/*
 * Type class Test.Test. It doesn't loads any data.
 *
 * Test.Test.java
 *
 * @version 1.0
 * @author Pablo Sanz Alguacil
 */

package Test;

import Model.Office;
import Model.Passenger;
import Model.Travel;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test {
    private static final String PASSENGERS_FILE_PATH = "src/storage/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/travels/status.csv";

    public static void main(String[] args) throws IOException {

        Office office = new Office();

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
        office.addPassenger(passenger1);
        if(!office.addPassenger(passenger1)) System.out.println("DNI ya en memoria");
        office.addPassenger(passenger2);
        office.addPassenger(passenger3);
        office.addPassenger(passenger4);
        office.addPassenger(passenger5);


        //SE AÑADEN LOS VIAJES
        office.addTravel(travel1);
        if(!office.addTravel(travel1)) System.out.println("VIAJE ya en memoria");
        office.addTravel(travel2);
        office.addTravel(travel3);
        office.addTravel(travel4);
        System.out.println("\n\n");

        //SE IMPRIME LA LISTA DE PASAJEROS Y VIAJES
        System.out.println("~~~PASAJEROS Y VIAJES - SIN MODIFICAR~~~");
        System.out.println(office.listPassengers());
        System.out.println("--------------------------------------------");
        System.out.println(office.listTravels());
        System.out.println("\n\n\n");


        //SE MODIFICA UN PASAJERO Y UN VIAJE
        office.modifyPassenger("44444444D", new Passenger("44444444D", "Agapito", "Wayne"));
        office.modifyTravel("3", modifiedTravel3);

        System.out.println("~~~PASAJEROS Y VIAJES - PASAJERO 44...D MODIFICADO Y VIAJE 3 MODIFICADO~~~");
        System.out.println(office.listPassengers());
        System.out.println("--------------------------------------------");
        System.out.println(office.listTravels());
        System.out.println("\n\n\n");


        //SE ELIMINA UN PASAJERO Y UN VIAJE
        office.deletePassenger(passenger3);
        office.deleteTravel(travel2);

        System.out.println("~~~PASAJEROS Y VIAJES - PASAJERO 33...C Y VIAJE 2 ELIMINADOS~~~");
        System.out.println(office.listPassengers());
        System.out.println("--------------------------------------------");
        System.out.println(office.listTravels());
        System.out.println("\n\n\n");


        //SE ASIGNAN ASIENTOS A 5 VIAJEROS Y UNO DE ELLOS SE ELIMINA
        System.out.println("~~~ASIGNACION DE ASIENTOS~~~");
        office.assignSeat(travel1, passenger1, 5);
        office.assignSeat(travel1, passenger2, 6);
        office.assignSeat(travel1, passenger4, 30);
        office.assignSeat(travel1, passenger5, 42);
        if(!office.assignSeat(travel1, passenger1,59)) System.out.println("Asiento no disponible");
        office.unassignSeat(travel1, 6);
        System.out.println("\n\n\n");


        //SE IMPRIME LA HOJA DE RUTA DEL VIAJE 1
        System.out.println(office.viewTravelSheet(travel1));



        office.savePassengers(PASSENGERS_FILE_PATH);
        office.saveTravels(TRAVELS_FILE_PATH);
        office.saveTravelsStatus(TRAVELS_STATUS_FILE_PATH);
    }
}