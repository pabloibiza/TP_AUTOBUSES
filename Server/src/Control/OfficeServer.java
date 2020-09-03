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

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import static Control.OfficesServer.WAITTIME_CLIENT;


public class OfficeServer implements Runnable {

    public static String DESK_CONNECTION_ERROR = "Desk connection closed";
    private Location location;
    private OfficesServer officesServer;
    private Socket socket;
    private BufferedReader influx;
    private PrintWriter outflux;



    /**
     * Constructor method.
     */
    public OfficeServer(OfficesServer officesServer, Socket socket, Location location) throws IOException {
        this.officesServer = officesServer;
        this.socket = socket;
        this.location = location;

        influx = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outflux = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream())), true);
    }


    /**
     * Terminates connection.
     * @throws IOException
     */
    private void endConnection() throws IOException {
        influx.close();
        outflux.close();
        socket.close();
    }


    /**
     * Connects a sales desk by push.
     * @throws IOException
     * @throws InterruptedException
     */
    private void pushConnectionOffice() throws IOException, InterruptedException {
        String companyID = readCompanyID();
        CountDownLatch connectionClose = new CountDownLatch(1);
        String connectionID = officesServer.createConnectionID();
        SalesDeskPushConnection salesDeskPushConnection = new SalesDeskPushConnection(connectionID,
                companyID, socket, connectionClose);
        CommunicationPrimitive response = salesDeskPushConnection.sendRequest(
                CommunicationPrimitive.NEW_CONNECTION_ID,
                WAITTIME_CLIENT,
                connectionID);

        if (response.equals(CommunicationPrimitive.OK)) {
            officesServer.newPushConnection(salesDeskPushConnection);
            connectionClose.await();

        } else {
            salesDeskPushConnection.endConnection();
        }
    }


    /**
     *  Disconnects push office.
     *
     */
    private void disconnectPushOffice() throws IOException {
        String connectionID = influx.readLine();

        if (officesServer.removePushConnection(connectionID)) {
            outflux.println(CommunicationPrimitive.OK);
        } else {
            outflux.println(CommunicationPrimitive.NOK);
        }

        endConnection();
    }

    /**
     * Reads companyID
     * @return String
     * @throws IOException
     */
    private String readCompanyID() throws IOException {
        String id = influx.readLine();
        return id;
    }


    /**
     * Reads a travel from the input.
     * @return Travel
     * @throws IOException
     */
    private Travel readTravelFromInput() throws IOException {
        Travel travel = null;
        try {
            travel = new Travel(influx.readLine());
        } catch (NoSuchElementException e) {
            System.out.println(location.getLabel(location.ERROR_READING_INPUT));
        }
        return travel;
    }


    /**
     * Reads a passenger from the input.
     * @return Passenger
     * @throws IOException
     */
    private Passenger readPassengerFromInput() throws IOException {
        Passenger passenger = null;
        try {
            passenger = new Passenger(influx.readLine());
        } catch (NoSuchElementException e) {
            System.out.println(location.getLabel(location.ERROR_READING_INPUT));
        }
        return passenger;
    }


    /**
     * Assigns a passenger to a travel on a specific company.
     * @throws IOException
     */
    private void assignationRequest() throws IOException {
        String companyId = readCompanyID();
        Travel travel = readTravelFromInput();
        Passenger passenger = readPassengerFromInput();
        int seat = Integer.parseInt(influx.readLine());

        if (companyId != null && travel != null && passenger != null) {
            outflux.println(CommunicationPrimitive.ASSIGN);
            if (officesServer.assign(companyId, travel, passenger, seat)) {
                String travelID = travel.getId();
                if (travelID != null) {
                    outflux.println(travelID);
                }
            }
        } else {
            outflux.println(CommunicationPrimitive.NOK.toString());
        }

        endConnection();
    }


    /**
     * Deallocates a passenger from a travel on a specific company.
     * @throws IOException
     */
    private void deallocationRequest() throws IOException {
        String companyID = readCompanyID();
        Travel travel = readTravelFromInput();
        int seat = Integer.parseInt(influx.readLine());

        if (companyID != null && travel != null) {
            outflux.println(CommunicationPrimitive.DEALLOCATE);
            if (officesServer.deallocate(companyID, travel, seat)) {
                String travelID = travel.getId();
                if (travelID != null) {
                    outflux.println(travelID);
                }
            }
        } else {
            outflux.println(CommunicationPrimitive.NOK.toString());
        }

        endConnection();
    }


    /**
     * Searches a travel.
     * @throws IOException
     */
    private void searchTravel() throws IOException {
        String companyID = readCompanyID();
        String travelID = influx.readLine();

        if (companyID != null && travelID != null) {
            Travel travel = officesServer.searchTravel(companyID, travelID);
            outflux.println(CommunicationPrimitive.SEARCH_TRAVEL);
            if (travel != null) {
                outflux.println(travel.toString());
            }
        } else {
            outflux.println(CommunicationPrimitive.NOK.toString());
        }

        endConnection();
    }


    /**
     * Gets the passenger of a seat.
     * @throws IOException
     */
    private void whoIsSiting() throws IOException {
        String companyID = readCompanyID();
        Travel travel = readTravelFromInput();
        int seat = Integer.parseInt(influx.readLine());

        if (companyID != null && travel != null) {
            outflux.println(CommunicationPrimitive.WHO_SITTING);
            Passenger passenger = officesServer.whoIsSitting(companyID, travel, seat);
            if (passenger != null) {
                outflux.println(passenger.toString());
            }
        } else {
            outflux.println(CommunicationPrimitive.NOK.toString());
        }
        endConnection();
    }


    /**
     * Searches travels for a date.
     * @throws IOException
     */
    private void searchTravelsPerDate() throws IOException {
        String companyID = readCompanyID();
        int year = Integer.parseInt(influx.readLine());
        int month = Integer.parseInt(influx.readLine());
        int day = Integer.parseInt(influx.readLine());

        if (companyID != null) {
            GregorianCalendar date = new GregorianCalendar(year, month, day);
            outflux.println(CommunicationPrimitive.SEARCH_TRAVELS_PER_DATE);
            List<Travel> travels = officesServer.searchTravelsPerDate(companyID, date);
            if (companyID != null && date != null && travels != null && ! travels.isEmpty()) {
                for (Travel travel : travels) {
                    outflux.println(travel.toString());
                }
            }
        } else {
            outflux.println(CommunicationPrimitive.NOK.toString());
        }
        endConnection();
    }


    /**
     * Manages SalesDesk request.
     */
    @Override
    public void run() {
        try {
            CommunicationPrimitive request = CommunicationPrimitive.newPrimitive(new Scanner(
                    new StringReader(influx.readLine())));

            switch(request) {
                case CONNECT_PUSH:
                    pushConnectionOffice();
                    break;

                case DISCONNECT_PUSH:
                    disconnectPushOffice();
                    break;

                case ASSIGN:
                    assignationRequest();
                    break;

                case DEALLOCATE:
                    deallocationRequest();
                    break;

                case SEARCH_TRAVEL:
                    searchTravel();
                    break;

                case WHO_SITTING:
                    whoIsSiting();
                    break;

                case SEARCH_TRAVELS_PER_DATE:
                    searchTravelsPerDate();
                    break;
            }
        } catch (IOException e) {
            System.out.println(location.getLabel(location.CONNECTION_ERROR_SALESDESK) + ": " + e.toString());
        } catch (InterruptedException e) {
            System.out.println(location.getLabel(location.CONNECTION_ERROR_SALESDESK) + ": " + e.toString());
        }
    }
}


