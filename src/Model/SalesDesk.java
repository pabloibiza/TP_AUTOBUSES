/*
 * Type class Model.SalesDesk. Contains the methods to manage the passengers and travels.
 *
 * Model.SalesDesk.java
 *
 * @version 4.4
 * @author Pablo Sanz Alguacil
 */

package Model;

import java.io.*;
import java.util.*;

public class SalesDesk {
    private static Collection<Passenger> passengers;
    private static Collection<Travel> travels;
    private static final String ELEMENTS_SEPARATOR = ",";
    private static final String DISTRIBUTION_SEPARATOR = "x";
    private static final String COLON = ": ";
    private static final String ORIGIN = "ORIGIN";
    private static final String DESTINY = "DESTINY";
    private static final String DATE = "DATE";
    private static final String SEATS_PLAN = "SEATS_PLAN";
    private static final String SEAT = "SEAT ";
    private static final String ROUTE_SHEET_FILE_ESXTENSION = ".txt";
    private static final String  SHEET_NAME_TEXT = "_ROUTE_SHEET";
    private static final String[] COLUMNS_DESIGNATION = {"A", "B", "C", "D", "E", "F"};
    private static final int MINUM_SIZE_BACK_DOOR = 7;

    /**
     * Constructor method. Creates an empty office.
     */
    public SalesDesk() {
        passengers = new ArrayList<>();
        travels = new ArrayList<>();
    }

    /**
     * Constructor method. Creates an office and loads the saved statuses of passengers and travels.
     * @param passengersFile String
     * @param travelsFile String
     * @param travelsStatusFile String
     */
    public SalesDesk(String passengersFile, String travelsFile, String travelsStatusFile) {
        passengers = new ArrayList<>();
        travels = new ArrayList<>();
        readTravels(travelsFile);
        readPassengers(passengersFile);
        readTravelsStatus(travelsStatusFile);
    }

    /**
     * Adds a new passenger. Returns true in case of success.
     * @param passenger Model.Passenger.
     * @return boolean.
     */
    public boolean addPassenger (Passenger passenger) {
        if(searchPassenger(passenger.getDni()) == null) {
            passengers.add(passenger);
            return true;
        }
        return false;
    }


    /**
     * Adds a new travel. Returns true in case of success.
     * @param travel Model.Travel
     * @return boolean
     */
    public boolean addTravel (Travel travel) {
        if(searchTravel(travel.getId()) == null) {
            travels.add(travel);
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
            return true;
        }
        return false;
    }


    /**
     * Deletes a travel.
     * @param travel Model.Travel
     * @return boolean
     */
    public boolean deleteTravel (Travel travel) {
        if(searchTravel(travel.getId()) != null) {
            travels.remove(travel);
            return true;
        }
        return false;
    }


    /**
     * Modifies a passenger. Returns true in case of success.
     * @param dni String
     * @param updatedPassenger Model.Passenger
     * @return boolean
     */
    public boolean modifyPassenger (String dni, Passenger updatedPassenger) {
        if(searchPassenger(dni) != null) {
            passengers.remove(searchPassenger(dni));
            passengers.add(updatedPassenger);
            return true;
        }
        return false;
    }


    /**
     * Modifies a travel. Returns true in case of success.
     * @param id String
     * @param updatedTravel Model.Travel.
     * @return boolean.
     */
    public boolean modifyTravel (String id, Travel updatedTravel) {
        if(searchTravel(id) != null){
            travels.remove(searchTravel(id));
            travels.add(updatedTravel);
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
     * Lists the passengers.
     * @return StringBuilder
     */
    public StringBuilder listPassengers (){
        StringBuilder passengersList = new StringBuilder();

        Iterator it = passengers.iterator();
        while(it.hasNext()) {
            Passenger element = (Passenger) it.next();
            passengersList.append(element.toString()).append("\n");
        }
        return passengersList;
    }


    /**
     * Lists the travels.
     * @return StringBuilder
     */
    public StringBuilder listTravels (){
        StringBuilder travelsList = new StringBuilder();

        Iterator it = travels.iterator();
        while(it.hasNext()) {
            Travel element = (Travel) it.next();
            travelsList.append(element.toString()).append("\n");
        }
        return travelsList;
    }


    /**
     * Assigns the received seat to the received passenger on a travel. Returns true in case of success.
     * @param travel Model.Travel
     * @param passenger Model.Passenger
     * @param seat Integer
     * @return boolean
     */
    public boolean assignSeat (Travel travel, Passenger passenger, int seat){
        if (travel.isSeatFree(seat)){
            return travel.assignSeat(seat, passenger.getDni());
        }
        return false;
    }


    /**
     * Deallocates the received seat to its passenger on the received travel.
     * @param travel Model.Travel
     * @param seat Ineger
     */
    public void deallocateSeat(Travel travel, int seat){
        travel.deallocateSeat(seat);
    }


    /**
     * Returns the passenger sited on the received seat.
     * @param travel Model.Travel
     * @param seat Integer
     * @return Model.Passenger
     */
    public Passenger whoIsSited (Travel travel, int seat){
        String dni = travel.whoIsSited(seat);
        if(dni == null) return null;
        return searchPassenger(dni);
    }


    /**
     * Returns the travel route sheet of a travel.
     * @param travel Model.Travel
     * @return String Builder
     */
    public void generateTravelSheet(Travel travel) {
        StringBuilder plan = new StringBuilder();
        plan.append(ORIGIN).append(COLON).append(travel.getOrigin()).append("\n");
        plan.append(DESTINY).append(COLON).append(travel.getDestiny()).append("\n");
        plan.append(DATE).append(COLON).append(travel.getDateToPrint()).append("\n");
        plan.append(SEATS_PLAN).append(COLON).append(travel.getSeatsDistribution()).append("\n\n");
        plan.append(seatsStatus(travel));
        plan.append("\n\n");

        for(int i = 0; i < travel.getSeatsNumber(); i++) {
            if(!travel.isSeatFree(i)) {
                plan.append(SEAT).append(i).append(COLON).append(whoIsSited(travel, i)).append("\n");
            }
        }

        String name = travel.getId()+ SHEET_NAME_TEXT + ROUTE_SHEET_FILE_ESXTENSION;
        PrintWriter file = null;
        try {
            file = new PrintWriter( new BufferedWriter( new FileWriter(name)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.println(plan);
        file.close();
    }


    /**
     * Retuns the seats status of a travel.
     * @param travel Model.Travel
     * @return StringBuilder
     */
    public StringBuilder seatsStatus(Travel travel) {
        int cols = Integer.parseInt(travel.getSeatsDistribution().split(DISTRIBUTION_SEPARATOR)[0]) + 1;
        int rows = Integer.parseInt(travel.getSeatsDistribution().split(DISTRIBUTION_SEPARATOR)[1]);
        int seatsIndex = 1;
        int corridorColumn = (((cols - 1) / 2) + ((cols - 1) % 2));
        String corridorGaps = "     ";
        StringBuilder plan = new StringBuilder();

        plan.append("_____");
        for(int i = 0; i < cols; i++){
            if (i < corridorColumn) {
                plan.append("____");
                plan.append(COLUMNS_DESIGNATION[i]);
            } else if (i > corridorColumn) {
                plan.append(COLUMNS_DESIGNATION[i - 1]);
                plan.append("____");
            } else {
                plan.append("_________");
            }
        }
        plan.append("\n");
        for(int row = 0; row < rows; row++) {
            plan.append("_").append(String.format("%02d", row + 1)).append("_").append("| ");
            for (int col = 0; col < cols; col++) {
                if (col == corridorColumn && row != (rows - 1)) { //Corridor
                    plan.append(corridorGaps);
                } else if((row == (rows/2)) && (col > corridorColumn) && rows > MINUM_SIZE_BACK_DOOR) { //Back door
                    plan.append("  ");
                } else {
                    if (travel.isSeatFree(seatsIndex)) {
                        plan.append(" ").append(" ").append(String.format("%02d", seatsIndex)).append(" ");
                    } else {
                        plan.append(" ").append("(").append(String.format("%02d", seatsIndex)).append(")");
                    }
                    seatsIndex++;
                }
            }
            plan.append("\n");
        }
        return plan;
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

        Iterator it = travels.iterator();
        while(it.hasNext()) {
            Travel element = (Travel) it.next();
            element.saveTravelStatus(file);
        }
        file.close();
    }


    /**
     * Reads the passengers from a file.
     * @param file String
     */
    public void readPassengers (String file){

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();

            for (int i = 0; line != null; i++) {
                passengers.add(new Passenger(line));
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Reads the travels from a file.
     * @param file String
     */
    public void readTravels (String file){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();

            for (int i = 0; line != null; i++) {
                travels.add(new Travel(line));
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Reads seats status of a travel from a file.
     * @param file String
     */
    public void readTravelsStatus (String file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();

            for (int i = 0; line != null; i++) { //Each line of the file.
                String[] tokens =  line.split(ELEMENTS_SEPARATOR);
                Iterator it = travels.iterator();
                while(it.hasNext()) { //Each travel on the list.
                    Travel element = (Travel) it.next();
                    if(tokens[0].equals(element.getId())) { //If tavel matches to the read id.
                        for(int k = 1; k < tokens.length; k++) { //Each element on the line.
                            String[] elements = tokens[k].split("-");
                            try {
                                if (!element.assignSeat(Integer.parseInt(elements[0]), elements[1])) {
                                    throw new SeatsReadException(2, null);
                                }
                            } catch (NumberFormatException e){
                                throw new SeatsReadException(1, tokens[k]);
                            }
                        }
                    }
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Searches the travels for a concrete date.
     * @param date String
     * @return Collection LinkedList
     */
    public ArrayList searchTravelsPerDate(GregorianCalendar date){
        ArrayList foundTravels = new ArrayList();
        int day = date.get(GregorianCalendar.DAY_OF_MONTH);
        int month = date.get(GregorianCalendar.MONTH);
        int year = date.get(GregorianCalendar.YEAR);

        Iterator it = travels.iterator();
        while(it.hasNext()) {
            Travel element = (Travel) it.next();
            if(element != null &&
                    element.getDay() == day &&
                    element.getMonth() == month &&
                    element.getYear() == year) {
                foundTravels.add(element);
            }
        }
        return foundTravels;
    }
}