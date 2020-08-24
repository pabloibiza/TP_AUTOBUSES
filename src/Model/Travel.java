/*
 * Type class Model.Travel. Contains an id, origin, destiny, date, seats distribution, and ifo about the bus.
 *
 * Model.Travel.java
 *
 * @version 2.0
 * @author Pablo Sanz Alguacil
 */

package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintWriter;
import java.util.*;

public class Travel {
    private String id;
    private String origin;
    private String destiny;
    private GregorianCalendar date;
    private int seatsNumber;
    private String[] seats;
    private String seatsDistribution;
    private String info;
    private static final String ELEMENTS_SEPARATOR = ",";
    private static final String DISTRIBUTION_SEPARATOR = "x";

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

        String[] distribution = seatsDistribution.split(DISTRIBUTION_SEPARATOR);
        seatsNumber = (Integer.parseInt(distribution[0]) * Integer.parseInt(distribution[1])) + 1;
        seats = new String[seatsNumber + 1];
    }

    /**
     * Constructor method to create a new Model.Travel from the received String. The String contains all
     * data separated by ";" for each element.
     * @param line String
     */
    public Travel(String line) throws NoSuchElementException {
        Scanner scanner = new Scanner(line).useDelimiter(ELEMENTS_SEPARATOR);
        id = scanner.next();
        origin = scanner.next();
        destiny = scanner.next();
        date = new GregorianCalendar(scanner.nextInt(),
                scanner.nextInt(),
                scanner.nextInt(),
                scanner.nextInt(),
                scanner.nextInt());
        seatsDistribution = scanner.next();
        info = scanner.next();

        Scanner distribution = new Scanner(seatsDistribution).useDelimiter(DISTRIBUTION_SEPARATOR);
        seatsNumber = (distribution.nextInt() * distribution.nextInt()) + 1;
        seats = new String[seatsNumber + 1];
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
     * Returns the date
     * @return GregorianCalendar
     */
    public GregorianCalendar getDate(){
        return date;
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
                date.get(GregorianCalendar.MINUTE) + ELEMENTS_SEPARATOR +
                seatsDistribution + ELEMENTS_SEPARATOR +
                info;

    }
}

