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
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;


public class SalesDesk {
    private static SalesDesk salesDesk;
    private Location location;
    private static Set<Passenger> passengers;
    private static Set<Travel> travels;
    private static final String ELEMENTS_SEPARATOR = ",";
    private static final String DISTRIBUTION_SEPARATOR = "x";
    private static final String COLON = ": ";
    private static final String TEXT_SPACER = " ";
    private static final String SLASH = "/";
    private static final String ROUTE_SHEET_FILE_ESXTENSION = ".txt";
    private static final String DNI_SEAT_SEPARATOR = "-";
    private static final String INSTANCE_ALREADY_CREATED = "Is not possible to create more than one instance of ";
    private static final String[] COLUMNS_DESIGNATION = {"A", "B", "C", "D", "E", "F"};
    private static final int MINUM_SIZE_BACK_DOOR = 7;

    /**
     * Constructor method. Creates an office and loads the saved statuses of passengers and travels.
     * @param passengersFile String
     * @param travelsFile String
     * @param travelsStatusFile String
     */
    private SalesDesk(Location location, String passengersFile, String travelsFile, String travelsStatusFile) {
        //Con los synchronized Sets se asegura la exclusión mutua al acceder a las Colecciones.
        passengers = Collections.synchronizedSet(new HashSet<Passenger>());
        travels =  Collections.synchronizedSet(new HashSet<Travel>());
        this.location = location;
        readTravels(travelsFile);
        readPassengers(passengersFile);
        readTravelsStatus(travelsStatusFile);
    }


    /**
     * Creates a singleton instance.
     * @param passengersFile String
     * @param travelsFile String
     * @param travelsStatusFile String
     * @return SalesDesk
     */
    public static synchronized SalesDesk getSingletonInstance(Location location, String passengersFile,
                                                              String travelsFile, String travelsStatusFile) {
        if (salesDesk == null){
            salesDesk = new SalesDesk(location, passengersFile, travelsFile, travelsStatusFile);
        }
        else{
            System.out.println(INSTANCE_ALREADY_CREATED + salesDesk.getClass().getSimpleName());
        }
        return salesDesk;
    }


    /**
     * Avoids cloning this object.
     * @return Object
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
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
     * @return boolean
     */
    public boolean deallocateSeat(Travel travel, int seat){
        if (!travel.isSeatFree(seat)){
            return travel.deallocateSeat(seat);
        }
        return false;
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
        GregorianCalendar date = travel.getDate();

        plan.append(location.getLabel(location.ORIGIN))
                .append(COLON)
                .append(travel.getOrigin())
                .append("\n");
        plan.append(location.getLabel(location.DESTINY))
                .append(COLON)
                .append(travel.getDestiny())
                .append("\n");
        plan.append(location.getLabel(location.DATE))
                .append(COLON)
                .append(String.format("%02d",date.get(GregorianCalendar.DAY_OF_MONTH))).append(SLASH)
                .append(String.format("%02d",date.get(GregorianCalendar.MONTH) + 1)).append(SLASH)
                .append(String.format("%02d",date.get(GregorianCalendar.YEAR))).append(TEXT_SPACER)
                .append(String.format("%02d",date.get(GregorianCalendar.HOUR_OF_DAY))).append(COLON)
                .append(String.format("%02d",date.get(GregorianCalendar.MINUTE)))
                .append("\n");
        plan.append(location.getLabel(location.SEATS_PLAN))
                .append(COLON)
                .append(travel.getSeatsDistribution())
                .append("\n\n");

        plan.append(seatsStatus(travel));
        plan.append("\n\n");

        for(int i = 0; i < travel.getSeatsNumber(); i++) {
            if(!travel.isSeatFree(i)) {
                plan.append(location.getLabel(location.SEAT)).append(i).append(COLON)
                        .append(whoIsSited(travel, i)).append("\n");
            }
        }

        String name = travel.getId()+ location.getLabel(location.SHEET_NAME_TEXT) + ROUTE_SHEET_FILE_ESXTENSION;
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

        //Generates the column dessignation (A,B,C...)
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

        //Generates the seats map.
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
                    }
                }
                line = bufferedReader.readLine(); //Next line
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    location.getLabel(location.ERROR_READING_STATUS),
                    "", JOptionPane.ERROR_MESSAGE);
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


    /**
     * Sets and observer for all travel.
     * @param observer PropertyChangeListener
     */
    public void setTravelsObserver(PropertyChangeListener observer){
        for (Travel travel : travels) {
            travel.addObserver(observer);
        }
    }
}