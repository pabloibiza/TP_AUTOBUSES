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
import Model.Cloud.Client;
import Model.Cloud.CommunicationPrimitive;
import Model.Cloud.ServerListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;


public class SalesDesk implements ServerListener{
    private static SalesDesk salesDesk;
    private Location location;
    private Properties config;
    private static final String CONFIG_FILE_PATH  = "storage/conf/config.properties";
    private static final String SERVER_URL_PARAMETER = "server";
    private static final String SERVER_PORT_PARAMETER = "port";
    private static final String CLIENT_ID_PARAMETER = "clientID";

    private String serverURL;
    private int serverPort;
    private String clientID;
    private String defaultServerURL = "localhost";
    private int defaultServerPort = 60236;
    private String defaultClientID = "Guest";
    private boolean connected;
    private String connectionID;
    private PropertyChangeSupport observers;
    private ServerListener serverListener;
    private Client client;

    private static final String DISTRIBUTION_SEPARATOR = "x";
    private static final String COLON = ": ";
    private static final String TEXT_SPACER = " ";
    private static final String SLASH = "/";
    private static final String ROUTE_SHEET_FILE_EXTENSION = ".txt";
    private static final String[] COLUMNS_DESIGNATION = {"A", "B", "C", "D", "E", "F"};
    private static final int MINUM_SIZE_BACK_DOOR = 7;

    private static final String CONNECTED_PROPERTY = "Connected";
    private static final String SEAT_CHANGED_PROPERTY = "Seat changed";

    /**
     * Constructor method. Creates an office and loads the saved status of passengers and travels.
     */
    private SalesDesk(Location location) {
        loadConfigs();
        this.location = location;
        connected = false;
        observers = new PropertyChangeSupport(this);
        serverListener = this;
        client = new Client(serverURL, serverPort);

    }


    /**
     * Creates a singleton instance.
     * @return SalesDesk
     */
    public static synchronized SalesDesk getSingletonInstance(Location location) {
        if (salesDesk == null){
            salesDesk = new SalesDesk(location);
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
     * Loads the configuration from a properties file.
     */
    private void loadConfigs() {
        try {
            config = new Properties();
            config.load(new FileInputStream(CONFIG_FILE_PATH));

            try {
                serverURL = config.getProperty(SERVER_URL_PARAMETER);
            } catch (Exception e) {
                serverURL = defaultServerURL;
            }

            try {
                serverPort = Integer.parseInt(config.getProperty(SERVER_PORT_PARAMETER));
            } catch (Exception e) {
                serverPort = defaultServerPort;
            }

            try {
                clientID = config.getProperty(CLIENT_ID_PARAMETER);
            } catch (Exception e) {
                clientID = defaultClientID;
            }

        } catch (FileNotFoundException e) {
            setAllDefaults();

        } catch (IOException e) {
            setAllDefaults();

        } finally {
            config.setProperty(SERVER_URL_PARAMETER, serverURL);
            config.setProperty(SERVER_PORT_PARAMETER, String.valueOf(serverPort));
        }
    }


    /**
     * Sets all loadable parameters to its default options
     */
    public void setAllDefaults() {
        serverURL = defaultServerURL;
        serverPort = defaultServerPort;
        clientID = defaultClientID;
    }


    /**
     * Adds a new observer.
     * @param observer PropertyChangeListener
     */
    public void newObserver(PropertyChangeListener observer) {
        this.observers.addPropertyChangeListener(observer);
    }


    /**
     * Returns the connection ID.
     * @return String
     */
    public String getConnectionID() {
        if (connected) {
            return connectionID;
        }
        return "---";
    }


    /**
     * Returns the client ID.
     * @return String
     */
    public String getClientID() {
        return clientID;
    }


    /**
     * Stablishes connection to server by long polling.
     */
    public void connect() {
        new Thread() {
            @Override
            public void run() {
                Client client = new Client(serverURL, serverPort);

                while(true) {
                    try {
                        client.sendLongPollingRequest(CommunicationPrimitive.CONNECT_PUSH, Client.WAITTIME_LONGPOOLING, clientID, serverListener);
                    } catch (Exception e) {
                        connected = false;
                        observers.firePropertyChange(CONNECTED_PROPERTY, null, connected);

                        try {
                            sleep(Client.WAITTIME_SERVER_RECONECT_ATTEMPT);
                        } catch (InterruptedException e2) {
                            new RuntimeException();
                        }
                    }
                }
            }
        }.start();
    }


    /**
     * Disconnects the client from the server.
     * @throws Exception
     */
    public void disconnect() throws Exception {
        if ( ! connected) {
            return;
        }

        client.sendRequest(CommunicationPrimitive.DISCONNECT_PUSH, Client.WAITTIME_SERVER, connectionID);
    }


    /**
     * Deletes a passenger. Returns true in case of success.
     * @param passenger Model.Passenger
     * @return boolean
     */
    public boolean deletePassenger (Passenger passenger) throws Exception{
        if ( ! connected) {
            return false;
        }

        String parameters = clientID + "\n" + passenger.getDni() + "\n" + passenger.getName() + "\n" + passenger.getSurname();
        CommunicationPrimitive response = client.sendRequest(CommunicationPrimitive.DELETE_PASSENGER, Client.WAITTIME_SERVER, parameters);

        return response.equals(CommunicationPrimitive.NOK.toString()) ? false : true;
    }


    /**
     * Returns the passenger with the received DNI. In case of not success returns null.
     * @param dni String
     * @return Model.Passenger
     */
    public Passenger searchPassenger (String dni) throws Exception{
        if ( ! connected) {
            return null;
        }
        if (dni == null) {
            return null;
        }

        String parameters = clientID + "\n" + dni;
        List<String> results =  new ArrayList<>();
        CommunicationPrimitive response = client.sendRequest(CommunicationPrimitive.SEARCH_PASSENGER, Client.WAITTIME_SERVER, parameters, results);

        if (results.isEmpty() || response.equals(CommunicationPrimitive.NOK.toString())){
            return null;
        } else {
            return new Passenger(results.get(0));
        }
    }


    /**
     * Returns the travel with the received ID. In case of not success returns null.
     * @param id String
     * @return Travel
     */
    public Travel searchTravel(String id) throws Exception{
        if ( ! connected) {
            return null;
        }

        String parameters = clientID + "\n" + id;
        List<String> results =  new ArrayList<>();
        CommunicationPrimitive response = client.sendRequest(CommunicationPrimitive.SEARCH_TRAVEL, Client.WAITTIME_SERVER, parameters, results);

        if (results.isEmpty() || response.equals(CommunicationPrimitive.NOK.toString())){
            return null;
        } else {
            return new Travel(results.get(0));
        }
    }


    /**
     * Assigns the received seat to the received passenger on a travel. Returns true in case of success.
     * @param travel Model.Travel
     * @param passenger Model.Passenger
     * @param seat Integer
     * @return boolean
     */
    public boolean assignSeat (Travel travel, Passenger passenger, int seat) throws Exception {
        if ( ! connected) {
            return false;
        }

        String parameters = clientID + "\n" + travel.toString() + "\n" + passenger.toString() + "\n" + seat;
        List<String> results =  new ArrayList<>();
        CommunicationPrimitive response = client.sendRequest(CommunicationPrimitive.ASSIGN, Client.WAITTIME_SERVER, parameters, results);

        if(results.isEmpty() || response.equals(CommunicationPrimitive.NOK.toString())) {
            return false;
        } else {
            return response.equals(CommunicationPrimitive.NOK.toString()) ? false : true;
        }


    }


    /**
     * Deallocates the received seat to its passenger on the received travel.
     * @param travel Model.Travel
     * @param seat Integer
     * @return boolean
     */
    public boolean deallocateSeat(Travel travel, int seat) throws Exception{
        if ( ! connected) {
            return false;
        }

        String parameters = clientID + "\n" + travel.toString() + "\n" +  seat;
        List<String> results =  new ArrayList<>();
        CommunicationPrimitive response = client.sendRequest(CommunicationPrimitive.DEALLOCATE, Client.WAITTIME_SERVER, parameters, results);

        if(results.isEmpty() || response.equals(CommunicationPrimitive.NOK.toString())) {
            return false;
        } else {
            return response.equals(CommunicationPrimitive.NOK.toString()) ? false : true;
        }
    }


    /**
     * Returns the passenger sitting on the received seat.
     * @param travel Model.Travel
     * @param seat Integer
     * @return Model.Passenger
     */
    public Passenger whoIsSitting(Travel travel, int seat) throws Exception{
        if ( ! connected) {
            return null;
        }

        String parameters = clientID + "\n" + travel.toString() + "\n" +  seat;
        List<String> results =  new ArrayList<>();
        CommunicationPrimitive response = client.sendRequest(CommunicationPrimitive.WHO_SITTING, Client.WAITTIME_SERVER, parameters, results);

        if (results.isEmpty() || response.equals(CommunicationPrimitive.NOK.toString())){
            return null;
        } else {
            return new Passenger(results.get(0));
        }
    }


    /**
     * Returns the travel route sheet of a travel.
     * @param travel Model.Travel
     * @return String Builder
     */
    public void generateTravelSheet(Travel travel) throws Exception{
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

        Passenger passenger;
        for(int i = 0; i < travel.getSeatsNumber(); i++) {
            passenger = whoIsSitting(travel, i);
            if(passenger != null) {
                plan.append(location.getLabel(location.SEAT)).append(i).append(COLON)
                        .append(passenger).append("\n");
            }
        }

        String name = travel.getId()+ location.getLabel(location.SHEET_NAME_TEXT) + ROUTE_SHEET_FILE_EXTENSION;
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
     * Returns the seats status of a travel.
     * @param travel Model.Travel
     * @return StringBuilder
     */
    public StringBuilder seatsStatus(Travel travel) throws Exception {
        int cols = Integer.parseInt(travel.getSeatsDistribution().split(DISTRIBUTION_SEPARATOR)[0]) + 1;
        int rows = Integer.parseInt(travel.getSeatsDistribution().split(DISTRIBUTION_SEPARATOR)[1]);
        int seatsIndex = 1;
        int corridorColumn = (((cols - 1) / 2) + ((cols - 1) % 2));
        String corridorGaps = "     ";
        StringBuilder plan = new StringBuilder();

        //Generates the column designation (A,B,C...)
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
                    if (whoIsSitting(travel, seatsIndex) == null) {
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
     * Searches the travels for a concrete date.
     * @param date String
     * @return Collection LinkedList
     */
    public List searchTravelsPerDate(GregorianCalendar date) throws Exception{
        if ( ! connected) {
            return null;
        }

        int day = date.get(GregorianCalendar.DAY_OF_MONTH);
        int month = date.get(GregorianCalendar.MONTH);
        int year = date.get(GregorianCalendar.YEAR);

        String parameters = clientID + "\n" + year + "\n" +  month + "\n" + day;
        List<String> results =  new ArrayList<>();
        CommunicationPrimitive response = client.sendRequest(CommunicationPrimitive.SEARCH_TRAVELS_PER_DATE, Client.WAITTIME_SERVER, parameters, results);

        if (results.isEmpty() || response.equals(CommunicationPrimitive.NOK.toString()) ) {
            return null;
        } else {
            List travels = new ArrayList<>();
            for (String travel : results) {
                travels.add(new Travel(travel));
            }
            return travels;
        }
    }


    /**
     * It receives a new connectionID server request.
     * @param results List<String>
     * @return Boolean
     * @throws IOException
     */
    private boolean newConnectionIDServerRequest(List<String> results) throws IOException {
        connectionID = results.get(0);
        if (connectionID == null) {
            return false;
        }

        connected = true;
        observers.firePropertyChange(CONNECTED_PROPERTY, null, connected);
        return true;
    }


    /**
     * Receives a new assignation server request.
     * @param property String
     * @return Boolean
     * @throws IOException
     */
    private boolean assignationServerRequest(String property, String id) throws IOException {
        observers.firePropertyChange(property, null, id);
        return true;
    }


    public boolean serverRequestFired(CommunicationPrimitive request, List<String> results) throws IOException {
        switch(request) {
            case NEW_CONNECTION_ID:
                return newConnectionIDServerRequest(results);

            case ASSIGN:
                return assignationServerRequest(SEAT_CHANGED_PROPERTY, results.get(0));

            case DEALLOCATE:
                return assignationServerRequest(SEAT_CHANGED_PROPERTY, results.get(0));

            default:
                return false;
        }
    }
}