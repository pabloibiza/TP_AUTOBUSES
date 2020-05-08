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
import View.Location;
import View.MainFrame;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;

import static java.lang.Thread.sleep;


public class Office implements ViewListener {
    private static final String PASSENGERS_FILE_PATH = "src/storage/data/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/data/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/data/status.csv";
    private static final String CONFIG_FILE_PATH  = "src/storage/conf/config.properties";
    public static final String LANGUAGE_PARAMETER = "language";
    public static final String COUNTRY_PARAMETER = "country";
    private static final String INSTANCE_ALREADY_CREATED="Is not possible to create more than one instance of ";
    private static final String ERROR_CONFIGS = "Error loading configuration";
    private static final String LANGUAGE_FILE_NOT_FOUND = "The selected language file was not found";

    private SalesDesk salesDesk;
    private MainFrame mainFrame;
    private static Office office;
    private Location location;
    private String language;
    private String country;
    private Properties config;



    /**
     * Constructor method.
     */
    public Office(){
        loadConfigs();
        //If it can't loads any location, the application closes.
        try {
            location = Location.getSingletonInstance(language, country);
        } catch (MissingResourceException e) {
            JOptionPane.showMessageDialog( new JFrame(),LANGUAGE_FILE_NOT_FOUND, "",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        salesDesk = SalesDesk.getSingletonInstance(location, PASSENGERS_FILE_PATH, TRAVELS_FILE_PATH, TRAVELS_STATUS_FILE_PATH);
        mainFrame = MainFrame.getSingletonInstance(this, salesDesk, location);
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
     * @return Object
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


    /**
     * Loads the configuration from a properties file.
     */
    private void loadConfigs() {
        try {
            config = new Properties();
            config.load(new FileInputStream(CONFIG_FILE_PATH));
            language = config.getProperty(LANGUAGE_PARAMETER);
            country = config.getProperty(COUNTRY_PARAMETER);

            if (language.equals("") || country.equals("")){
                language = Locale.getDefault().getLanguage();
                country = Locale.getDefault().getCountry();
            }
        } catch (Exception e) {
            mainFrame.errorMessage(ERROR_CONFIGS,e);
        } finally {
            config.setProperty(LANGUAGE_PARAMETER, language);
            config.setProperty(COUNTRY_PARAMETER, country);
        }
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
            mainFrame.errorMessage(location.getLabel(location.ERROR_SAVING_PASSENGERS), e);
        }
    }


    /**
     * Deletes a passenger and saves all passengers.
     * @param id String
     */
    private void deletePassenger(String id){
        if(!salesDesk.deletePassenger(salesDesk.searchPassenger(id))){
            mainFrame.errorMessage(location.getLabel(location.PASSENGER_NOT_EXISTANT), null);
        };
        try {
            salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        } catch (IOException e) {
            mainFrame.errorMessage(location.getLabel(location.ERROR_SAVING_PASSENGERS), e);
        }
    }


    /**
     * Generates the route sheet for the received travel id.
     * @param id String
     * @throws IOException
     */
    private void generateRouteSheet(String id) {
        salesDesk.generateTravelSheet(salesDesk.searchTravel(id));
        mainFrame.infoMessage(location.getLabel(location.SUCCESSFUL_ROUTE_GENERATED));
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
            mainFrame.errorMessage(location.getLabel(location.ERROR_SAVING_TRAVELS_STATUS), e);
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
            mainFrame.errorMessage(location.getLabel(location.ERROR_SAVING_TRAVELS_STATUS), e);
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
