/*
 * Type class Model.SalesDesk. Contains the methods to manage the passengers and travels.
 *
 * Model.SalesDesk.java
 *
 * @version 2.0
 * @author Pablo Sanz Alguacil
 */

package Model;

import Internationalization.Location;
import javax.swing.*;
import java.io.*;
import java.util.*;


public class SalesDesk {
    private Location location;
    private static Set<Passenger> passengers;
    private static Set<Travel> travels;
    private static final String ELEMENTS_SEPARATOR = ",";
    private static final String COLON = ": ";
    private static final String DNI_SEAT_SEPARATOR = "-";

    private static final String PASSENGERS_FILE_PATH = "storage/data/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "storage/data/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "storage/data/status.csv";

    /**
     * Constructor method. Creates an office and loads the saved statuses of passengers and travels.
     * @param location Location
     */
    public SalesDesk(Location location) {
        //Con los synchronized Sets se asegura la exclusión mutua al acceder a las Colecciones.
        passengers = Collections.synchronizedSet(new HashSet<Passenger>());
        travels =  Collections.synchronizedSet(new HashSet<Travel>());
        this.location = location;
        readTravels(TRAVELS_FILE_PATH);
        readPassengers(PASSENGERS_FILE_PATH);
        readTravelsStatus(TRAVELS_STATUS_FILE_PATH);
    }


    /**
     * Adds a new passenger. Returns true in case of success.
     * @param passenger Model.Passenger.
     * @return boolean.
     */
    public boolean addPassenger (Passenger passenger) {
        if(searchPassenger(passenger.getDni()) == null) {
            passengers.add(passenger);
            try {
                savePassengers(PASSENGERS_FILE_PATH);
            } catch (IOException e) {
                System.out.println("Error saving passengers");
                return false;
            }
            return true;
        }
        return false;
    }


    /**
     * Deletes a passenger. Returns true in case of success.
     * @param passenger Model.Passenger
     * @return boolean
     */
    public boolean deletePassenger (Passenger passenger) {
        if(searchPassenger(passenger.getDni()) != null) {
            passengers.remove(passenger);
            try {
                savePassengers(PASSENGERS_FILE_PATH);
            } catch (IOException e) {
                System.out.println("Error saving passengers");
                return false;
            }
            return true;
        }
        return false;
    }


    /**
     * Returns the passenger with the received DNI. In case of not success returns null.
     * @param dni String
     * @return Model.Passenger
     */
    public Passenger searchPassenger (String dni){
        for (Passenger element : passengers) {
            if (element.getDni().equals(dni)) {
                return element;
            }
        }
        return null;
    }


    /**
     * Returns the travel with the received ID. In case of not success returns null.
     * @param id String
     * @return Travel
     */
    public Travel searchTravel(String id){
        for (Travel element : travels) {
            if (element.getId().equals(id)) {
                return element;
            }
        }
        return null;
    }


    /**
     * Assigns the received seat to the received passenger on a travel. Returns true in case of success.
     * @param receivedTravel Model.Travel
     * @param passenger Model.Passenger
     * @param seat Integer
     * @return boolean
     */
    public boolean assignSeat (Travel receivedTravel, Passenger passenger, int seat){
        Travel travel = searchTravel(receivedTravel.getId());
        if (travel.isSeatFree(seat)){
            Boolean success = travel.assignSeat(seat, passenger.getDni());
            try {
                saveTravelsStatus(TRAVELS_STATUS_FILE_PATH);
            } catch (IOException e) {
                System.out.println(location.getLabel(location.ERROR_SAVING_TRAVELS_STATUS));
            }
            return success;
        }
        return false;
    }


    /**
     * Deallocates the received seat to its passenger on the received travel.
     * @param receivedTravel Model.Travel
     * @param seat Ineger
     * @return boolean
     */
    public boolean deallocateSeat(Travel receivedTravel, int seat){
        Travel travel = searchTravel(receivedTravel.getId());
        if (!travel.isSeatFree(seat)){
            boolean success = travel.deallocateSeat(seat);
            try {
                saveTravelsStatus(TRAVELS_STATUS_FILE_PATH);
            } catch (IOException e) {
                System.out.println(location.getLabel(location.ERROR_SAVING_TRAVELS_STATUS));
            }
            return success;
        }
        return false;
    }


    /**
     * Returns the passenger sited on the received seat.
     * @param travelID String
     * @param seat Integer
     * @return Model.Passenger
     */
    public Passenger whoIsSited (String travelID, int seat){
        Travel travel = searchTravel(travelID);
        String dni = travel.whoIsSited(seat);
        if(dni == null) return null;
        return searchPassenger(dni);
    }


    /**
     * Saves the passengers on a file.
     * @param fileName String
     * @throws IOException
     */
    public void savePassengers (String fileName) throws IOException {
        PrintWriter file = new PrintWriter( new BufferedWriter( new FileWriter(fileName)));

        for (Passenger element : passengers) {
            element.save(file);
        }
        file.close();
    }


    /**
     * Saves the seats status from a travel to a file.
     * @param fileName String
     * @throws IOException
     */
    public void saveTravelsStatus (String fileName) throws IOException {
        PrintWriter file = new PrintWriter( new BufferedWriter( new FileWriter(fileName)));

        for(Travel travel : travels){
            travel.saveTravelStatus(file);
        }
        file.close();
    }


    /**
     * Reads the passengers from a file.
     * @param file String
     */
    public void readPassengers (String file){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = bufferedReader.readLine();

            while(line != null){
                try{
                    passengers.add(new Passenger(line));
                } catch (NoSuchElementException ne) {
                    JOptionPane.showMessageDialog(null,
                            location.getLabel(location.ERROR_READING_A_PASSENGER),
                            "", JOptionPane.ERROR_MESSAGE);
                }
                line = bufferedReader.readLine();

            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    location.getLabel(location.ERROR_READING_PASSENGERS),
                    "", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Reads the travels from a file.
     * @param file String
     */
    public void readTravels (String file){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();

            while(line != null) {
                try {
                    travels.add(new Travel(line));
                } catch (NoSuchElementException e) {
                    JOptionPane.showMessageDialog(null,
                            location.getLabel(location.ERROR_READING_A_TRAVEL) +
                                    COLON + line.split(ELEMENTS_SEPARATOR)[0],
                            "", JOptionPane.ERROR_MESSAGE);
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    location.getLabel(location.ERROR_READING_TRAVELS),
                    "", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Reads seats status of a travel from a file.
     * @param file String
     */
    public void readTravelsStatus (String file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();

            while(line != null) {
                String readID = line.split(ELEMENTS_SEPARATOR)[0]; //The first element on each line is the ID.
                for(Travel travel : travels){ //For each travel on the travels Set.
                    if(readID.equals(travel.getId())) { //Compares the read Id against the actual travel.
                        loadPassengersIntoTravel(travel, line);
                        break;
                    }
                }
                line = bufferedReader.readLine(); //Next line
            }
        } catch (IOException e) {
            System.out.println(location.ERROR_READING_STATUS);
        }
    }


    /**
     * Receives a String containing the seats and DNIs and assigns them to a travel.
     * @param travel Travel
     * @param line String
     */
    public void loadPassengersIntoTravel(Travel travel, String line){
        String assignation;
        //Separates each pair of seats and DNIs
        Scanner elements = new Scanner(line).useDelimiter(ELEMENTS_SEPARATOR);
        elements.next(); //Skips the id

        while(elements.hasNext()) { //For each pair of seat and DNI.
            assignation = elements.next();
            //Separates the seat and the DNI
            Scanner seatDni = new Scanner(assignation).useDelimiter(DNI_SEAT_SEPARATOR);

            //Assigns the passenger ID to the seat on the received travel.
            try {
                if (!travel.assignSeat(seatDni.nextInt(), seatDni.next())) {
                    throw new SeatsReadException("SEAT", null);
                }
            } catch (NumberFormatException e){
                throw new SeatsReadException("TRAVEL", assignation);
            }
        }
    }


    /**
     * Searches the travels for a concrete date.
     * @param date String
     * @return Collection LinkedList
     */
    public List searchTravelsPerDate(GregorianCalendar date){
        List foundTravels = new ArrayList();
        int day = date.get(GregorianCalendar.DAY_OF_MONTH);
        int month = date.get(GregorianCalendar.MONTH);
        int year = date.get(GregorianCalendar.YEAR);

        for (Travel element : travels) {
            if (element.getDate().get(GregorianCalendar.DAY_OF_MONTH) == day
                    && element.getDate().get(GregorianCalendar.MONTH) == month
                    && element.getDate().get(GregorianCalendar.YEAR) == year) {
                foundTravels.add(element);
            }
        }
        return foundTravels;
    }
}