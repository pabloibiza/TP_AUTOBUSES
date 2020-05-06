/*
 * Type class Model.Passenger. Contains a DNI, name, and surname.
 *
 * Model.Passenger.java
 *
 * @version 4.4
 * @author Pablo Sanz Alguacil
 */

package Model;

import java.io.PrintWriter;
import java.util.Objects;

public class Passenger {
    private String dni;
    private String name;
    private String surname;
    private static final String ELEMENTS_SEPARATOR = ",";


    /**
     * Constructor method.
     * @param dni String
     * @param name String
     * @param surname String
     */
    public Passenger(String dni, String name, String surname){
        this.dni = dni;
        this.name = name;
        this.surname = surname;

    }


    /**
     * Constructor method to create a new Model.Passenger from the received String. The String contains all data
     * separated by ";" for each element.
     * @param line String
     */
    public Passenger(String line) {
        String[] elements = line.split(ELEMENTS_SEPARATOR);
        this.dni = elements[0];
        this.name = elements[1];
        this.surname = elements[2];
    }


    /**
     * Returns the DNI of the passenger.
     * @return Integer
     */
    public String getDni(){
        return dni;
    }


    /**
     * Overrided toString(). It returns a string composed by the DNI, name, and surname.
     * @return String
     */
    @Override
    public String toString(){
        return dni + ELEMENTS_SEPARATOR + name + ELEMENTS_SEPARATOR + surname;
    }


    /**
     * Overwrited equals. Compares an object with this passenger.
     * @param obj Object
     * @return Object
     */
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(!(obj instanceof Passenger)) return false;
        Passenger tmp = (Passenger)obj;
        return Objects.equals(dni, tmp.dni);
    }


    /**
     * Overwrited hashCode.
     * @return Integer
     */
    @Override
    public int hashCode() {
        int result = 23;
        result = 19 * result + name.hashCode() + name.hashCode();
        return 19 * result + dni.hashCode();
    }


    /**
     * Saves a contact throw the receives PrintWriter.
     * @param printWriter PrintWriter
     */
    public void save(PrintWriter printWriter){
        printWriter.println(this.toString());
    }
}
