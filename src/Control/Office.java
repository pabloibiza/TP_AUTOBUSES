/*
 * This class acts as a link for the view and the model.
 *
 * Control.Office.java
 *
 * @version 4.4
 * @author Pablo Sanz Alguacil
 */

package Control;

import Model.Passenger;
import Model.SalesDesk;
import Model.Travel;
import View.MainFrame;
import com.sun.tools.javac.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;


public class Office implements ViewListener {
    private static final String PASSENGERS_FILE_PATH = "src/storage/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/travels/status.csv";
    private static final String SUCCESSFUL_ROUTE_GENERATED = "Route sheet generated successfully";
    private static final String ERROR_SAVING_PASSENGERS = "Error while saving changes to passengers file";
    private static final String ERROR_SAVING_TRAVELS_STATUS = "Error while saving changes to travels status file";
    private static final String PASSENGER_NOT_EXISTANT = "Passenger doesn't exists";
    private static final String INSTANCE_ALREADY_CREATED = "Is not possible to create more than one instance of ";

    private SalesDesk salesDesk;
    private MainFrame mainFrame;
    private static Office office;


    /**
     * Constructor method.
     */
    public Office(){
        salesDesk = SalesDesk.getSingletonInstance(PASSENGERS_FILE_PATH, TRAVELS_FILE_PATH, TRAVELS_STATUS_FILE_PATH);
        mainFrame = MainFrame.getSingletonInstance(this, salesDesk);
        salesDesk.setTravelsObserver(mainFrame);
    }


    /**
     * Creates a singleton instance.
     * @return Office
     */
    public static synchronized Office getSingletonInstance() {
        if ( office == null){
            office = new Office();
        }
        else{
            System.out.println(INSTANCE_ALREADY_CREATED + office.getClass().getSimpleName());
        }
        return office;
    }


    /**
     * Avoids cloning this object.
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


    /**
     * Saves applications's data and exit.
     */
    private void exit() {
        try {
            salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        } catch (IOException e) {
            mainFrame.errorMessage("Error while saving changes to passengers file", e);
        }

        try {
            salesDesk.saveTravelsStatus(TRAVELS_STATUS_FILE_PATH);
        } catch (IOException e) {
            mainFrame.errorMessage("Error while saving changes to travels status file", e);
        }

        System.exit(0);
    }


    /**
     * Adds a new passenger and saves all passengers.
     * @param passenger Passenger
     */
    private void newPassenger(Passenger passenger){
        salesDesk.addPassenger(passenger);
        try {
            salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        } catch (IOException e) {
            mainFrame.errorMessage(ERROR_SAVING_PASSENGERS, e);
        }
    }


    /**
     * Deletes a passenger and saves all passengers.
     * @param id String
     */
    private void deletePassenger(String id){
        if(!salesDesk.deletePassenger(salesDesk.searchPassenger(id))){
            mainFrame.errorMessage(PASSENGER_NOT_EXISTANT, null);
        };
        try {
            salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        } catch (IOException e) {
            mainFrame.errorMessage(ERROR_SAVING_PASSENGERS, e);
        }
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
     * Assigns a seat for a passenger on a travel.
     * @param assignationData String[] [travel,passenger,seat]
     */
    private void assignSeat(Object[] assignationData) {
        Travel travel = (Travel) assignationData[0];
        Passenger passenger = (Passenger) assignationData[1];
        int seat = (Integer) assignationData[2];
        salesDesk.assignSeat(travel, passenger, seat);
        try {
            salesDesk.saveTravelsStatus(TRAVELS_STATUS_FILE_PATH);
        } catch (IOException e) {
            mainFrame.errorMessage(ERROR_SAVING_TRAVELS_STATUS, e);
        }
    }


    /**
     * Unassigns a seat for a passenger.
     * @param unassignationData String[] [travel, seat]
     */
    private void unassignSeat(Object[] unassignationData) {
        Travel travel = (Travel) unassignationData[0];
        int seat = (Integer) unassignationData[1];
        salesDesk.deallocateSeat(travel, seat);
        try {
            salesDesk.saveTravelsStatus(TRAVELS_STATUS_FILE_PATH);
        } catch (IOException e) {
            mainFrame.errorMessage(ERROR_SAVING_TRAVELS_STATUS, e);
        }
    }


    /**
     * Produced events manager.
     * @param event Event
     * @param object Object
     */
    @Override
    public void producedEvent(Event event, Object object) {
        switch(event) {
            case NEW_PASSENGER:
                newPassenger((Passenger) object);
                break;

            case DELETE_PASSENGER:
                deletePassenger((String) object);
                break;

            case EXIT:
                exit();
                break;

            case ASSIGN:
                assignSeat((Object[]) object);
                break;

            case DEALLOCATE:
                unassignSeat((Object[]) object);
                break;

            case GENERATE_ROUTE_SHEET:
                generateRouteSheet((String) object);
                break;
        }
    }
}
