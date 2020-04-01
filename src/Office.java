/*
 * Type class Office. Contains the methods to manage the passengers and travels.
 *
 * Office.java
 *
 * @version 1.0
 * @author Pablo Sanz Alguacil */

import java.io.*;

public class Office {
    private static final int MAX_PASSENGERS = 1000;
    private static final int MAX_TRAVELS = 1000;

    private final Passenger[] passengers;
    private int indexPassengers;
    private final Travel[] travels;
    private int indexTravels;

    /**
     * Constructor method. Creates an empty office.
     */
    public Office() {
        passengers = new Passenger[MAX_PASSENGERS];
        indexPassengers = -1;
        travels = new Travel[MAX_TRAVELS];
        indexTravels = -1;
    }

    /**
     * Constructor method. Creates an office and loads the saved statuses of passengers and travels.
     * @param passengersFile String
     * @param travelsFile String
     * @param travelsStatusFile String
     */
    public Office(String passengersFile, String travelsFile, String travelsStatusFile) {
        passengers = new Passenger[MAX_PASSENGERS];
        indexPassengers = -1;
        travels = new Travel[MAX_TRAVELS];
        indexTravels = -1;
        readTravels(travelsFile);
        readPassengers(passengersFile);
        readTravelsStatus(travelsStatusFile);
    }

    /**
     * Adds a new passenger. Returns true in case of success.
     * @param passenger Passenger.
     * @return bollean.
     */
    public boolean addPassenger (Passenger passenger) {
        if(indexPassengers < MAX_PASSENGERS) {
            if(searchPassenger(passenger.getDni()) == null){
                passengers[indexPassengers + 1] = passenger;
                indexPassengers += 1;
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new travel. Returns true in case of success.
     * @param travel Travel
     * @return boolean
     */
    public boolean addTravel (Travel travel) {
        if(indexTravels < MAX_TRAVELS) {
            if(searchTravel(travel.getId()) == null){
                travels[indexTravels + 1] = travel;
                indexTravels += 1;
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a passenger. Returns true in case of success.
     * @param passenger Passenger
     * @return boolean
     */
    public boolean deletePassenger (Passenger passenger) {
        for (int i = 0; i <= indexPassengers; i++) {
            if (passengers[i].equals(passenger)){
                passengers[i] = passengers[indexPassengers];
                passengers[indexPassengers] = null;
                indexPassengers--;
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a travel. Returns true in case of success.
     * @param travel Travel
     * @return boolean
     */
    public boolean deleteTravel (Travel travel) {
        for (int i = 0; i <= indexTravels; i++) {
            if (travels[i].equals(travel)){
                travels[i] = travels[indexTravels];
                travels[indexTravels] = null;
                indexTravels--;
                return true;
            }
        }
        return false;
    }

    /**
     * Modifies a passenger. Returns true in case of success.
     * @param dni String
     * @param updatedPassenger Passenger
     * @return boolean
     */
    public boolean modifyPassenger (String dni, Passenger updatedPassenger) {
        for (int i = 0; i <= indexPassengers; i++) {
            if (passengers[i].getDni().equals(dni)){
                passengers[i] = updatedPassenger;
                return true;
            }
        }
        return false;
    }

    /**
     * Modifies a travel. Returns true in case of success.
     * @param id String
     * @param updatedTravel Travel.
     * @return boolean.
     */
    public boolean modifyTravel (String id, Travel updatedTravel) {
        for(int i = 0; i <= indexTravels; i++) {
            if (travels[i].getId().equals(id)) {
                travels[i] = updatedTravel;
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the passenger with the received DNI. In case of not success returns null.
     * @param dni String
     * @return Passenger
     */
    public Passenger searchPassenger (String dni){
        for (int i = 0; i <= indexPassengers; i++) {
            if (passengers[i].getDni().equals(dni)){
                return passengers[i];
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
        for (int i = 0; i <= indexTravels; i++) {
            if (travels[i].getId().equals(id)){
                return travels[i];
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
        for(int i = 0;i<=indexPassengers;i++){
            passengersList.append(passengers[i].toString()).append("\n");
        }
        return passengersList;
    }

    /**
     * Lists the travels.
     * @return StringBuilder
     */
    public StringBuilder listTravels (){
        StringBuilder travelsList = new StringBuilder();
        for(int i = 0;i <= indexTravels;i++){
            travelsList.append(travels[i].toString()).append("\n");
        }
        return travelsList;
    }

    /**
     * Assigns the received seat to the received passenger on a travel. Returns true in case of success.
     * @param travel Travel
     * @param passenger Passenger
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
     * @param travel Travel
     * @param seat Ineger
     */
    public void unassignSeat (Travel travel, int seat){
        travel.unassignSeat(seat);
    }

    /**
     * Returns the passenger sited on the received seat.
     * @param travel Travel
     * @param seat Integer
     * @return Passenger
     */
    public Passenger whoIsSited (Travel travel, int seat){
        String dni = travel.whoIsSited(seat);
        if(dni == null) return null;
        return searchPassenger(dni);
    }

    /**
     * Returns the travel route sheet of a travel.
     * @param travel Travel
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
     * @param travel Travel
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
     * Saves a Storable type array in a file.
     * @param fileName String
     * @param elements Storable
     * @throws IOException
     */
    private void save (String fileName, Storable[] elements) throws IOException {
        PrintWriter file = new PrintWriter( new BufferedWriter( new FileWriter(fileName)));
        for(int i = 0; elements[i] != null; i++) {
            elements[i].save(file);
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
        for(int i = 0; i <= indexTravels; i++){
            travels[i].saveTravelStatus(file);
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
                passengers[i] = new Passenger(line);
                indexPassengers++;
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
                travels[i] = new Travel(line);
                indexTravels++;
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
                for(int j = 0; j < indexTravels; j++) { //Each travel on the array
                    if(tokens[0].equals(travels[j].getId())) { //If tavel matches to the readed id
                        for(int k = 1; k < tokens.length; k++) { //Each element on the line
                            String[] elements = tokens[k].split("-");
                            travels[j].assignSeat(Integer.parseInt(elements[0]), elements[1]);
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
