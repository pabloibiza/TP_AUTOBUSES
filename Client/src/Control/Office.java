/*
 * This class acts as a link for the view and the model.
 *
 * Control.Office.java
 *
 * @version 2.0
 * @author Pablo Sanz Alguacil
 */

package Control;

import Model.Passenger;
import Model.SalesDesk;
import Model.Travel;
import Internationalization.Location;
import View.MainFrame;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;


public class Office implements ViewListener {
    private static final String CONFIG_FILE_PATH  = "storage/conf/config.properties";
    private static final String LANGUAGE_PARAMETER = "language";
    private static final String COUNTRY_PARAMETER = "country";
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
            JOptionPane.showMessageDialog( null,
                    LANGUAGE_FILE_NOT_FOUND, "",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        salesDesk = SalesDesk.getSingletonInstance(location);
        mainFrame = MainFrame.getSingletonInstance(this, salesDesk, location);
        salesDesk.newObserver(mainFrame);
        salesDesk.connect();
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
     * Exit.
     */
    private void exit() {
        System.exit(0);
    }


    /**
     * Generates the route sheet for the received travel id.
     * @param id String
     * @throws IOException
     */
    private void generateRouteSheet(String id) throws Exception{
        salesDesk.generateTravelSheet(salesDesk.searchTravel(id));
        mainFrame.infoMessage(location.getLabel(location.SUCCESSFUL_ROUTE_GENERATED));
    }


    /**
     * Assigns a seat for a passenger on a travel.
     * @param assignationData String[] [travel,passenger,seat]
     */
    private void assignSeat(Object[] assignationData) throws Exception {
        Travel travel = (Travel) assignationData[0];
        Passenger passenger = (Passenger) assignationData[1];
        int seat = (Integer) assignationData[2];
        if(salesDesk.assignSeat(travel, passenger, seat)){
            mainFrame.infoMessage(location.getLabel(location.SEAT_ASSIGN_SUCCESS));
        } else
            mainFrame.errorMessage(location.getLabel(location.SEAT_ASSIGN_ERROR), null);
    }


    /**
     * Unassigns a seat for a passenger.
     * @param unassignationData String[] [travel, seat]
     */
    private void unassignSeat(Object[] unassignationData) throws Exception {
        Travel travel = (Travel) unassignationData[0];
        int seat = (Integer) unassignationData[1];
        if(salesDesk.deallocateSeat(travel, seat)){
            mainFrame.infoMessage(location.getLabel(location.SEAT_DEALLOCATION_SUCCESS));
        } else {
            mainFrame.errorMessage(location.getLabel(location.SEAT_DEALLOCATION_ERROR), null);
        }
    }


    /**
     * Produced events manager.
     * @param event Event
     * @param object Object
     */
    @Override
    public void producedEvent(Event event, Object object) throws Exception{
        switch(event) {
            case EXIT:
                salesDesk.disconnect();
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

    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
        Office office = Office.getSingletonInstance();
    }
}


