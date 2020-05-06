/*
 * Type class Model.Travel. Contains an id, origin, destiny, date and sets distribution.
 *
 * Model.Travel.java
 *
 * @version 4.4
 * @author Pablo Sanz Alguacil
 */

package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class Travel {
    private String id;
    private String origin;
    private String destiny;
    private GregorianCalendar date;
    private int seatsNumber;
    private Pair[] seats;
    private String seatsDistribution;
    private String info;
    private PropertyChangeSupport observers;
    private static final String ELEMENTS_SEPARATOR = ",";
    private static final String DISTRIBUTION_SEPARATOR = "x";
    private static final String DNI_SEAT_SEPARATOR = "-";

    /**
     * Constructor method.
     * @param id String
     * @param origin String
     * @param destiny String
     * @param date String
     * @param seatsDistribution String
     * @param info String
     */
    public Travel(String id, String origin, String destiny, GregorianCalendar date, String seatsDistribution,
                  String info){
        this.id = id;
        this.origin = origin;
        this.destiny = destiny;
        this.date = date;
        this.info = info;
        this.seatsDistribution = seatsDistribution;
        observers = new PropertyChangeSupport(this);

        String[] distribution = seatsDistribution.split(DISTRIBUTION_SEPARATOR);
        seatsNumber = (Integer.parseInt(distribution[0]) * Integer.parseInt(distribution[1])) + 1;
        seats = new Pair[seatsNumber + 1];
    }

    /**
     * Constructor method to create a new Model.Travel from the received String. The String contains all
     * data separated by ";" for each element.
     * @param line String
     */
    public Travel(String line) {
        String[] tokens = line.split(ELEMENTS_SEPARATOR);
        id = tokens[0];
        origin = tokens[1];
        destiny = tokens[2];
        date = readGregorianCalendar(line);
        seatsDistribution = tokens[8];
        info = tokens[9];
        observers = new PropertyChangeSupport(this);

        String[] distribution = seatsDistribution.split(DISTRIBUTION_SEPARATOR);
        seatsNumber = Integer.parseInt(distribution[0]) * Integer.parseInt(distribution[1]) + 1;
        seats = new Pair[seatsNumber + 1];
    }

    /**
     * Returns a GregorianCalendar (year/month/day hh:mm) created from the received String.
     * @param line String
     * @return GregorianCalendar
     */
    private GregorianCalendar readGregorianCalendar(String line) {
        String[] tokens = line.split(ELEMENTS_SEPARATOR);
        return new GregorianCalendar(
                Integer.parseInt(tokens[3]),
                Integer.parseInt(tokens[4]),
                Integer.parseInt(tokens[5]),
                Integer.parseInt(tokens[6]),
                Integer.parseInt(tokens[7]));
    }


    /**
     * Returns id.
     * @return String
     */
    public String getId() {
        return id;
    }


    /**
     * Returns the origin.
     * @return String
     */
    public String getOrigin(){
        return origin;
    }


    /**
     * Returns the destiny.
     * @return String
     */
    public String getDestiny(){
        return destiny;
    }


    /**
     * Returns the day.
     * @return Integer
     */
    public int getDay(){
        return date.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * Returns the month.
     * @return Integer
     */
    public int getMonth(){
        return date.get(Calendar.MONTH);
    }


    /**
     * Returns the year.
     * @return Integer
     */
    public int getYear(){
        return date.get(Calendar.YEAR);
    }

    /**
     * Returns the hour.
     * @return Integer
     */
    public Integer getHour(){
        return date.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Returns the minutes.
     * @return Integer
     */
    public Integer getMinute(){
        return date.get(Calendar.MINUTE);
    }


    /**
     * Returns the date ready to print on the screen.
     * @return String
     */
    public String getDateToPrint(){
        return date.get(GregorianCalendar.DAY_OF_MONTH) + "/" +
                String.valueOf(Integer.parseInt(String.valueOf(date.get(GregorianCalendar.MONTH) + 1))) + "/" +
                date.get(GregorianCalendar.YEAR) + "  " +
                date.get(GregorianCalendar.HOUR_OF_DAY)+ ":" +
                date.get(GregorianCalendar.MINUTE);
    }

    /**
     * Returns the seats distribution (sits per row * rows)
     * @return String
     */
    public String getSeatsDistribution(){
        return seatsDistribution;
    }


    /**
     * Returns the number of seats.
     * @return Integer
     */
    public int getSeatsNumber(){
        return seatsNumber;
    }

    /**
     * Returns info.
     * @return String
     */
    public String getInfo(){
        return info;
    }


    /**
     * Overwrited equals. Compares an object with this travel.
     * @param obj Object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(!(obj instanceof Travel)) return false;
        Travel tmp = (Travel)obj;
        return Objects.equals(id, tmp.id);
    }


    /**
     * Overwrited hashCode.
     * @return Integer
     */
    @Override
    public int hashCode() {
        int result = 23;
        result = 19 * result + origin.hashCode();
        result = 19 * result + destiny.hashCode();
        result = 19 * result + date.hashCode();
        result = 19 * result + seatsDistribution.hashCode();
        return 19 * result + id.hashCode();
    }


    /**
     * Overwrited toString(). It returns a string composed by the id, origin, destiny, date, and seats distribution.
     * @return String
     */
    @Override
    public String toString() {
        return id + ELEMENTS_SEPARATOR +
                origin + ELEMENTS_SEPARATOR +
                destiny + ELEMENTS_SEPARATOR +
                date.get(GregorianCalendar.DAY_OF_MONTH) + ELEMENTS_SEPARATOR +
                date.get(GregorianCalendar.MONTH) + ELEMENTS_SEPARATOR +
                date.get(GregorianCalendar.YEAR) + ELEMENTS_SEPARATOR +
                date.get(GregorianCalendar.HOUR_OF_DAY)+ ELEMENTS_SEPARATOR +
                date.get(GregorianCalendar.MINUTE);
    }


    /**
     * Saves the travel status throw the received PrintWriter.
     * @param printWriter PrintWriter
     */
    public void saveTravelStatus(PrintWriter printWriter){
        StringBuilder line = new StringBuilder();
        line.append(id).append(ELEMENTS_SEPARATOR);
        for(int i = 1; i < seats.length; i++){
            if(seats[i] != null) {
                line.append(seats[i].getSeat()).append(DNI_SEAT_SEPARATOR).append(seats[i].getPassengerID()).append(ELEMENTS_SEPARATOR);
            }
        }
        printWriter.println(line);
    }


    /**
     * Assigns a seat to a received passenger (only DNI). Returns true in case of success.
     * @param seat Integer
     * @param passengerID String
     * @return boolean
     */
    public boolean assignSeat(int seat, String passengerID){
            if(seats[seat] == null){
                seats[seat] = new Pair<>(seat, passengerID);
                observers.firePropertyChange("SEAT_CHANGE", null, null);
                return true;
            }
            return false;
    }


    /**
     * Removes the asignated pasenger of the received seat.
     * @param seat Integer
     */
    public void deallocateSeat(int seat){
        seats[seat] = null;
        observers.firePropertyChange("SEAT_CHANGE", null, null);
    }


    /**
     * Checks if the received seat free. Returns true in case of be free.
     * @param seat Integer
     * @return boolean
     */
    public boolean isSeatFree(int seat){
        if(seat <= seatsNumber) {
            return seats[seat] == null;
        }
        return false;
    }


    /**
     * Returns the passenger DNI who is sited on the received seat.
     * @param seat Integer
     * @return String
     * @throws NullPointerException
     */
    public String whoIsSited(int seat) throws NullPointerException{
        String passengerID;
        if(!isSeatFree(seat)){
            passengerID = seats[seat].getPassengerID();
        }else{
            return null;
        }
        return passengerID;
    }


    /**
     * Adds an observer to this travel.
     * @param observer PropertyChangeListener
     */
    public void addObserver(PropertyChangeListener observer) {
        this.observers.addPropertyChangeListener(observer);
    }



}

