/*
 * This class acts as a link for the view and the model.
 *
 * Control.Office.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package Control;

import Model.Passenger;
import Model.SalesDesk;
import Model.Travel;
import View.MainFrame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;


public class Office implements ViewListener {
    private static final String PASSENGERS_FILE_PATH = "src/storage/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/travels/status.csv";
    private static final String SUCCESSFUL_ROUTE_GENERATED = "Route sheet generated successfuly";
    private static final String ERROR_SAVING_PASSENGERS = "Error while saving changes to passengers file";
    private static final String ERROR_SAVING_TRAVELS = "Error while saving changes to travels file";
    private static final String TRAVEL_NOT_EXISTANT = "Travel doesn't exists";
    private static final String PASSENGER_NOT_EXISTANT = "Passenger doesn't exists";
    private static final String PASSENGER_ALREADY_EXISTS = "Passenger already exists";
    private static final String TRAVEL_ALREADY_ADDED = "Travel already added";

    private SalesDesk salesDesk;
    private MainFrame mainFrame;


    /**
     * Constructor method.
     * @param salesDesk
     */
    public Office(SalesDesk salesDesk){
        this.salesDesk = salesDesk;
        mainFrame = new MainFrame(this);
    }


    /**
     * Saves applications's data and exit.
     */
    private void exit() {
        try {
            salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        } catch (IOException e) {
            errorMessage("Error while saving changes to passengers file", e);
        }

        try {
            salesDesk.saveTravels(TRAVELS_FILE_PATH);
        } catch (IOException e) {
            errorMessage("Error while saving changes to travels file", e);
        }

        try {
            salesDesk.saveTravelsStatus(TRAVELS_STATUS_FILE_PATH);
        } catch (IOException e) {
            errorMessage("Error while saving changes to travels status file", e);
        }

        System.exit(0);
    }


    /**
     * Puts an error message.
     * @param message String
     * @param e Exception
     */
    private void errorMessage(String message, Exception e) {
        mainFrame.errorMessage(message);
    }


    /**
     * Creates a new travel and saves all travels.
     * @param travel Travel
     */
    private void newTravel(Travel travel){
        if(!salesDesk.addTravel(travel)){
            errorMessage(TRAVEL_ALREADY_ADDED, null);
        }
        try {
            salesDesk.saveTravels(TRAVELS_FILE_PATH);
        } catch (IOException e) {
            errorMessage(ERROR_SAVING_TRAVELS, e);
        }
    }


    /**
     * Deletes a travel and saves all travels.
     * @param travel Travel
     */
    private void deleteTravel(Travel travel){
        if(!salesDesk.deleteTravel(travel)){
            errorMessage(TRAVEL_NOT_EXISTANT, null);
        }
        ;
        try {
            salesDesk.saveTravels(TRAVELS_FILE_PATH);
        } catch (IOException e) {
            errorMessage(ERROR_SAVING_TRAVELS, e);
        }
    }


    /**
     * Adds a new passenger and saves all passengers.
     * @param passenger Passenger
     */
    private void newPassenger(Passenger passenger){
        if(!salesDesk.addPassenger(passenger)) {
            errorMessage(PASSENGER_ALREADY_EXISTS ,null);
        };
        try {
            salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        } catch (IOException e) {
            errorMessage(ERROR_SAVING_PASSENGERS, e);
        }
    }


    /**
     * Deletes a passenger and saves all passengers.
     * @param passenger Passenger
     */
    private void deletePassenger(Passenger passenger){
        if(!salesDesk.deletePassenger(passenger)){
            errorMessage(PASSENGER_NOT_EXISTANT, null);
        };
        try {
            salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        } catch (IOException e) {
            errorMessage(ERROR_SAVING_PASSENGERS, e);
        }
    }


    /**
     * Updates the travels list for a date in the view.
     * @param date String
     */
    private void updateTravelsPerDate(GregorianCalendar date){
        ArrayList result = salesDesk.searchTravelsPerDate(date);
        mainFrame.updateTravels(result);
    }


    /**
     * Shows the bus seats plan for the given id.
     * @param id String
     */
    private void viewSeats(String id){
        mainFrame.setSelectedTravel(id);
    }


    /**
     * Generates the route sheet for the received travel id.
     * @param id String
     * @throws IOException
     */
    private void generateRouteSheet(String id) {
        salesDesk.generateTravelSheet(salesDesk.searchTravel(id));
        mainFrame.infoMessage(SUCCESSFUL_ROUTE_GENERATED);
    }


    /**
     * Produced events manager.
     * @param event Event
     * @param object Object
     */
    @Override
    public void producedEvent(Event event, Object object) {
        switch(event) {
            case NEW_TRAVEL:
                newTravel((Travel) object);
                break;

            case DELETE_TRAVEL:
                deleteTravel((Travel) object);
                break;

            case NEW_PASSENGER:
                newPassenger((Passenger) object);
                break;

            case DELETE_PASSENGER:
                deletePassenger((Passenger) object);
                break;

            case EXIT:
                exit();
                break;

            case SEARCH:
                updateTravelsPerDate((GregorianCalendar) object);
                break;

            case VIEW_SEATS:
                viewSeats((String) object);
                break;

            case ASSIGN:
                mainFrame.infoMessage("Seat assigned");
                break;

            case GENERATE_ROUTE_SHEET:
                generateRouteSheet((String) object);
                break;
        }
    }
}
