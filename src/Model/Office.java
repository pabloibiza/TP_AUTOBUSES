/*
 * Type class Model.Office. Contains the methods to manage the passengers and travels.
 *
 * Model.Office.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Office {
    private static Collection<Passenger> passengers;
    private static Collection<Travel> travels;

    /**
     * Constructor method. Creates an empty office.
     */
    public Office() {
        passengers = new ArrayList<>();
        travels = new ArrayList<>();
    }

    /**
     * Constructor method. Creates an office and loads the saved statuses of passengers and travels.
     * @param passengersFile String
     * @param travelsFile String
     * @param travelsStatusFile String
     */
    public Office(String passengersFile, String travelsFile, String travelsStatusFile) {
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
        if(searchTravel(travel.getId()) == null){
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
    public void deletePassenger (Passenger passenger) {
        passengers.remove(passenger);
    }


    /**
     * Deletes a travel.
     * @param travel Model.Travel
     * @return boolean
     */
    public void deleteTravel (Travel travel) {
        travels.remove(travel);
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
        Iterator it = passengers.iterator();
        while(it.hasNext()) {
            Passenger element = (Passenger) it.next();
            if (element.getDni().equals(dni)){
                return element;
            }
        }
        return null;
    }


    /**
     * Returns the travel with the received ID. In case of not success returns null.
     * @param id
     * @return
     */
    public Travel searchTravel(String id){
        Iterator it = travels.iterator();
        while(it.hasNext()) {
            Travel element = (Travel) it.next();
            if (element.getId().equals(id)){
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
     * Unassigns the received seat to its passenger on the received travel.
     * @param travel Model.Travel
     * @param seat Ineger
     */
    public void unassignSeat (Travel travel, int seat){
        travel.unassignSeat(seat);
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
    public StringBuilder viewTravelSheet(Travel travel){
        StringBuilder plan = new StringBuilder();
        plan.append("ORIGEN: ").append(travel.getOrigin()).append("\n");
        plan.append("DESTINO: ").append(travel.getDestiny()).append("\n");
        plan.append("FECHA: ").append(travel.getDateToPrint()).append("\n");
        plan.append("PLAN DE ASIENTOS: ").append(travel.getSeatsDistribution()).append("\n\n");

        plan.append(seatsStatus(travel));

        plan.append("\n\n");

        for(int i = 0; i < travel.getSeatsNumber(); i++) {
            if(!travel.isSeatFree(i)) {
                plan.append("ASIENTO ").append(i).append(": ").append(whoIsSited(travel, i)).append("\n");
            }
        }

        return plan;
    }


    /**
     * Retuns the seats status of a travel.
     * @param travel Model.Travel
     * @return StringBuilder
     */
    public StringBuilder seatsStatus(Travel travel) {
        int indexSeats = 1;
        String[] distribution = travel.getSeatsDistribution().split("x");
        int seatsPerRow = Integer.parseInt(distribution[0]);
        int rows = Integer.parseInt(distribution[1]);
        StringBuilder plan = new StringBuilder();

        plan.append("_____" + "__A__B____C__D__\n");
        for(int i = 0; i < rows; i++) {
            plan.append(String.format("%02d", i + 1)).append(" ").append("| ");
            for (int j = 1; j <= seatsPerRow; j++) {
                if (travel.isSeatFree(indexSeats)) {
                    plan.append(" ").append(String.format("%02d", indexSeats));
                } else {
                    plan.append(" " + " .");
                }
                if (j == 2) plan.append("  ");
                indexSeats++;
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
        save(fileName, passengers);
    }


    /**
     * Saves the travels on a file.
     * @param fileName String
     * @throws IOException
     */
    public void saveTravels (String fileName) throws IOException {
        save(fileName, travels);
    }


    /**
     * Saves a Model.Storable type array in a file.
     * @param fileName String
     * @param collection Collection
     * @throws IOException
     */
    private void save (String fileName, Collection collection) throws IOException {
        PrintWriter file = new PrintWriter( new BufferedWriter( new FileWriter(fileName)));

        Iterator it = collection.iterator();
        while(it.hasNext()) {
            Storable element = (Storable) it.next();
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
    public void readTravelsStatus (String file){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();

            for (int i = 0; line != null; i++) { //Each line of the file
                String[] tokens =  line.split(";");
                Iterator it = travels.iterator();
                while(it.hasNext()) { //Each travel on the array
                    Travel element = (Travel) it.next();
                    if(tokens[0].equals(element.getId())) { //If tavel matches to the readed id
                        for(int k = 1; k < tokens.length; k++) { //Each element on the line
                            String[] elements = tokens[k].split("-");
                            element.assignSeat(Integer.parseInt(elements[0]), elements[1]);
                        }
                    }
                }

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}