package Control;

import Internationalization.Location;
import Model.Passenger;
import Model.SalesDesks;
import Model.Travel;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OfficesServer extends Thread{
    private SalesDesks salesDesks;
    private Map<String, SalesDeskPushConnection> salesDeskPushConnectionsMap;
    private String language;
    private String country;
    private Location location;
    private Properties config;

    private int threadsNumber;
    private int serverPort;
    private int defaultPort = 60236;
    private int defaultThreadsNumber = 12;
    private static final int ID_LENGTH = 6; // Unique ID length

    private static final String CONFIG_FILE_PATH  = "storage/conf/config.properties";
    private static final String LANGUAGE_PARAMETER = "language";
    private static final String COUNTRY_PARAMETER = "country";
    private static final String THREADS_PARAMETER = "max_threads";
    private static final String SERVER_PORT_PARAMETER = "port";
    private static final String ERROR_CONFIGS_SERVER = "Error loading server configuration. " +
            "Default values will be loaded.";
    private static final String ERROR_CONFIGS_LANGUAGE = "Error loading language configuration. " +
            "Default values will be loaded.";
    private static final String LANGUAGE_FILE_NOT_FOUND = "The selected language file was not found";
    private static final String WAITING_REQUESTS = "---Waiting for requests---";
    private static final String EXECUTION_ERROR = "Error: Server running in ";
    private static final String DESK_CONNECTION_ERROR = "Failed to create desk connection";


    public static int WAITTIME_CLIENT = 2000; //miliseconds
    private static int WAITTIME_TEST_CONNECTIONS = 100 * 1000; //miliseconds



    public OfficesServer() {
        salesDeskPushConnectionsMap = new ConcurrentHashMap<>();

        loadConfigs();
        //If it can't loads any location, the application closes.

        try {
            location = new Location(language, country);
        } catch (MissingResourceException e) {
            System.out.println(LANGUAGE_FILE_NOT_FOUND);
            System.exit(1);
        }

        salesDesks = new SalesDesks(location);
        sendPeriodicTests();
        start();
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
            serverPort = Integer.parseInt(config.getProperty(SERVER_PORT_PARAMETER));
            threadsNumber = Integer.parseInt(config.getProperty(THREADS_PARAMETER));

            if (language.equals("") || country.equals("")){
                System.out.println(ERROR_CONFIGS_LANGUAGE);
                language = Locale.getDefault().getLanguage();
                country = Locale.getDefault().getCountry();
            }
        } catch (Exception e) {
            System.out.println(ERROR_CONFIGS_SERVER);
            serverPort = defaultPort;
            threadsNumber = defaultThreadsNumber;
        }
    }


    /**
     * Notifies a change on a company travel to all clients.
     * @param companyID String
     * @param communicationPrimitive CommunicationPrimitive
     * @param parameters String
     * @throws IOException
     */
    private void notifyOfficesPush(String companyID, CommunicationPrimitive communicationPrimitive,
                                   String parameters) throws IOException {

        for(SalesDeskPushConnection salesDeskPushConnection : salesDeskPushConnectionsMap.values()) {
            if (salesDeskPushConnection.getCompanyID().equals(companyID)) {
                salesDeskPushConnection.sendRequest(
                        communicationPrimitive,
                        WAITTIME_CLIENT,
                        parameters);
            }
        }
    }


    /**
     * Runnable. Waits for connections on a loop.
     */
    @Override
    public void run() {
        try {
            ExecutorService poolThreads = Executors.newFixedThreadPool(threadsNumber);
            ServerSocket serverSocket = new ServerSocket(serverPort);

            while(true) {
                System.out.println(WAITING_REQUESTS);

                Socket socket = serverSocket.accept();
                poolThreads.execute(new OfficeServer(this, socket, location));
            }

        } catch (BindException e) {
            System.out.println(EXECUTION_ERROR + serverPort);
        } catch (IOException e) {
            System.out.println(DESK_CONNECTION_ERROR);
        }
    }


    /**
     * Sends periodic tests to maintain push connections alive.
     */
    private void sendPeriodicTests() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for(SalesDeskPushConnection salesDeskPushConnection : salesDeskPushConnectionsMap.values()) {
                    try {
                        salesDeskPushConnection.sendRequest(CommunicationPrimitive.TEST,
                                WAITTIME_TEST_CONNECTIONS);
                    } catch (IOException e1) {
                        System.out.println(OfficeServer.DESK_CONNECTION_ERROR + " " +
                                salesDeskPushConnection.toString());

                        salesDeskPushConnectionsMap.remove(salesDeskPushConnection.getConnectionID());
                        try {
                            salesDeskPushConnection.endConnection();
                        } catch (IOException e2) {
                            // Connection has been already closed
                        }
                    }
                }
            }
        }, WAITTIME_TEST_CONNECTIONS, WAITTIME_TEST_CONNECTIONS);
    }


    /**
     *  Creates a connection unique identifier for Offices.
     */
    synchronized String createConnectionID() {
        char[] possibleChars = "0123456789abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder uniqueID = new StringBuilder(ID_LENGTH);
        Random random = new Random();

        for (int i = 0; i<ID_LENGTH; i++) {
            uniqueID.append(possibleChars[random.nextInt(possibleChars.length)]);
        }
        return uniqueID.toString();
    }


    /**
     * New push connection.
     * @param salesDeskPushConnection SalesDeskPushConnection
     */
    synchronized void newPushConnection(SalesDeskPushConnection salesDeskPushConnection) {
        String companyID = salesDeskPushConnection.getCompanyID();
        if ( ! salesDesks.existsDesk(companyID)) {
            salesDesks.newDesk(companyID);
        }

        salesDeskPushConnectionsMap.put(salesDeskPushConnection.getConnectionID(), salesDeskPushConnection);
    }


    /**
     * Removes a push connection.
     * @param connectionID String
     * @return Boolean
     * @throws IOException
     */
    synchronized boolean removePushConnection(String connectionID) throws IOException {
        SalesDeskPushConnection salesDeskPushConnection = salesDeskPushConnectionsMap.get(connectionID);

        if (salesDeskPushConnection == null) {
            return false;
        }

        salesDeskPushConnection.endConnection();
        salesDeskPushConnectionsMap.remove(connectionID);

        return true;
    }





    /**
     * New assignation. Notifies to all sales desk from the same company.
     * @param companyID String
     * @param travel Travel
     * @param passenger Passenger
     * @param seat Integer
     * @return Boolean
     * @throws IOException
     */
    synchronized boolean assign(String companyID, Travel travel, Passenger passenger, int seat)
            throws IOException {

        if ( ! salesDesks.assign(companyID, travel, passenger, seat)) {
            return false;
        }

        notifyOfficesPush(companyID, CommunicationPrimitive.ASSIGN, travel.getId());
        return true;
    }


    /**
     * New deallocation. Notifies to all sales desk from the same company.
     * @param companyID
     * @param travel
     * @param seat
     * @return
     * @throws IOException
     */
    synchronized boolean deallocate(String companyID, Travel travel, int seat) throws IOException {
        if (! salesDesks.deallocate(companyID, travel, seat)) {
            return false;
        }

        notifyOfficesPush(companyID, CommunicationPrimitive.DEALLOCATE, travel.getId());
        return true;
    }


    /**
     * Searches a passenger.
     * @param companyID String
     * @param dni String
     * @return Passenger
     */
    synchronized Passenger searchPassenger(String companyID, String dni) {
        return salesDesks.searchPassenger(companyID, dni);
    }


    /**
     * Searches a travel.
     * @param companyID String
     * @param travelID String
     * @return Passenger
     */
    synchronized Travel searchTravel(String companyID, String travelID) {
        return salesDesks.searchTravel(companyID, travelID);
    }


    /**
     * Returns the passenger of a seat.
     * @param companyID String
     * @param travel Travel
     * @param seat Integer
     * @return Passenger
     */
    synchronized Passenger whoIsSitting(String companyID, Travel travel, int seat) {
        return salesDesks.whoIsSitting(companyID, travel, seat);
    }


    /**
     * Searches travels for a date.
     * @param companyID
     * @param date
     * @return
     */
    synchronized List searchTravelsPerDate(String companyID, GregorianCalendar date) {
        return salesDesks.searchTravelsPerDate(companyID, date);
    }


    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
        OfficesServer officesServer = new OfficesServer();
    }
    

}
