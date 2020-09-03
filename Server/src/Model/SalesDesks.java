package Model;

import Internationalization.Location;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesDesks {
    private Map<String, SalesDesk> salesDesks;
    private Location location;


    /**
     * Constructor method.
     */
    public SalesDesks(Location location) {
        this.location = location;
        salesDesks = new HashMap<>();
    }


    /**
     *  Inserts new sales desk.
     * @param companyID String
     */
    public void newDesk(String companyID) {
        salesDesks.put(companyID, new SalesDesk(location));
    }


    /**
     * Checks if a sales desk exists.
     * @param companyID String
     * @return Boolean
     */
    public boolean existsDesk(String companyID){
        return salesDesks.get(companyID) != null;
    }


    /**
     * Assigns a seat to a passenger on a travel.
     * @param companyID String
     * @param travel Travel
     * @param passenger Passenger
     * @param seat Integer
     * @return Boolean
     */
    public boolean assign(String companyID, Travel travel, Passenger passenger, int seat) {
        SalesDesk salesDesk = salesDesks.get(companyID);

        if (salesDesk == null) {
            return false;
        }
        salesDesk.addPassenger(passenger);
        salesDesk.assignSeat(travel, passenger, seat);
        return true;
    }


    /**
     * Deallocates a seat of a travel.
     * @param companyID String
     * @param travel Travel
     * @param seat Integer
     * @return Boolean
     */
    public boolean deallocate(String companyID, Travel travel, int seat) {
        SalesDesk salesDesk = salesDesks.get(companyID);

        if (salesDesk == null) {
            return false;
        }
        salesDesk.deletePassenger(salesDesk.whoIsSited(travel.getId(),seat));
        salesDesk.deallocateSeat(travel, seat);
        return true;
    }


    /**
     * Searches a passenger
     * @param companyID String
     * @param dni String
     * @return Boolean
     */
    public Passenger searchPassenger(String companyID, String dni) {
        SalesDesk salesDesk = salesDesks.get(companyID);

        if (salesDesk == null) {
            return null;
        }
        return salesDesk.searchPassenger(dni);
    }


    /**
     * Searches a travel.
     * @param companyID String
     * @param travelID String
     * @return Travel
     */
    public Travel searchTravel(String companyID, String travelID) {
        SalesDesk salesDesk = salesDesks.get(companyID);

        if (salesDesk == null) {
            return null;
        }
        return salesDesk.searchTravel(travelID);
    }


    /**
     * Returns the passenger of a seat.
     * @param companyID
     * @param travel
     * @param seat
     * @return
     */
    public Passenger whoIsSitting(String companyID, Travel travel, int seat) {
        SalesDesk salesDesk = salesDesks.get(companyID);

        if (salesDesk == null) {
            return null;
        }
        return salesDesk.whoIsSited(travel.getId(), seat);
    }


    /**
     * Searches all travels on a given date.
     * @param companyID
     * @param date
     * @return
     */
    public List searchTravelsPerDate(String companyID, GregorianCalendar date) {
        SalesDesk salesDesk = salesDesks.get(companyID);

        if (salesDesk == null) {
            return null;
        }
        return salesDesk.searchTravelsPerDate(date);
    }
}
