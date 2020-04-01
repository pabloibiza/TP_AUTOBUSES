/*
 * Type class Travel. Contains an id, origin, destiny, date and sets distribution.
 *
 * Travel.java
 *
 * @version 1.0
 * @author Pablo Sanz Alguacil */

import com.sun.istack.internal.NotNull;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.Objects;

public class Travel implements Storable{
    private String id;
    private String origin;
    private String destiny;
    private GregorianCalendar date;
    private int seatsNumber;
    private Pair[] seats;
    private String seatsDistribution;

    /**
     * Constructor method.
     * @param id
     * @param origin
     * @param destiny
     * @param date
     * @param seatsDistribution
     */
    public Travel(String id, String origin, String destiny, GregorianCalendar date, String seatsDistribution){
        this.id = id;
        this.origin = origin;
        this.destiny = destiny;
        this.date = date;
        this.seatsDistribution = seatsDistribution;

        String[] distribution = seatsDistribution.split("x");
        seatsNumber = Integer.parseInt(distribution[0]) * Integer.parseInt(distribution[1]);
        seats = new Pair[seatsNumber + 1];
    }

    /**
     * Constructor method to create a new Travel from the received String. The String contains all
     * data separated by ";" for each element.
     * @param line String
     */
    public Travel(@NotNull String line) {
        String[] tokens = line.split(";");
        id = tokens[0];
        origin = tokens[1];
        destiny = tokens[2];
        date = readGregorianCalendar(line);
        seatsDistribution = tokens[8];

        String[] distribution = seatsDistribution.split("x");
        seatsNumber = Integer.parseInt(distribution[0]) * Integer.parseInt(distribution[1]);
        seats = new Pair[seatsNumber + 1];
    }

    /**
     * Returns a GregorianCalendar (year/month/day hh:mm) created from the received String.
     * @param line String
     * @return GregorianCalendar
     */
    private GregorianCalendar readGregorianCalendar(String line) {
        String[] tokens = line.split(";");
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
     * Overwrited toString(). It returns a string composed by the id, origin, destiny, date, and seats distribution.
     * @return String
     */
    @Override
    public String toString() {
        return id + ";" + origin + ";" + destiny + ";" +
                date.get(GregorianCalendar.DAY_OF_MONTH) + ";" +
                date.get(GregorianCalendar.MONTH) + ";" +
                date.get(GregorianCalendar.YEAR) + ";" +
                date.get(GregorianCalendar.HOUR_OF_DAY)+ ";" +
                date.get(GregorianCalendar.MINUTE);
    }

    /**w
     * Saves the travel throw the recieved PrintWriter.
     * @param printWriter PrintWriter
     */
    @Override
    public void save(PrintWriter printWriter){
        StringBuilder line = new StringBuilder();
        line.append(id).append(";")
                .append(origin).append(";")
                .append(destiny).append(";")
                .append(date.get(GregorianCalendar.YEAR)).append(";")
                .append(date.get(GregorianCalendar.MONTH)).append(";")
                .append(date.get(GregorianCalendar.DAY_OF_MONTH)).append(";")
                .append(date.get(GregorianCalendar.HOUR_OF_DAY)).append(";")
                .append(date.get(GregorianCalendar.MINUTE)).append(";")
                .append(seatsDistribution);
        printWriter.println(line);
    }

    /**
     * Saves the travel status throw the recieved PrintWriter.
     * @param printWriter PrintWriter
     */
    public void saveTravelStatus(PrintWriter printWriter){
        StringBuilder line = new StringBuilder();
        line.append(id).append(";");
        for(int i = 1; i < seats.length; i++){
            if(seats[i] != null) {
                line.append(seats[i].getSeat()).append("-").append(seats[i].getPassengerID()).append(";");
            }
        }
        printWriter.println(line);
    }

    /**
     * Assigns a seat to a received passenger (only DNI). Returns true in case of success.
     * @param seat Integer
     * @param passengerID String
     * @return booean
     */
    public boolean assignSeat(int seat, String passengerID){
            if(seats[seat] == null){
                seats[seat] = new Pair<>(seat, passengerID);
                return true;
            }
            return false;
    }

    /**
     * Removes the asignated pasenger of the received seat.
     * @param seat Integer
     */
    public void unassignSeat(int seat){
        seats[seat] = null;
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
}

