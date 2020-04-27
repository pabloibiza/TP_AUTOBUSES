/*
 * Type class Model.Passenger. Contains a DNI, name, and surname.
 *
 * Model.Passenger.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package Model;

import java.io.PrintWriter;
import java.util.Objects;

public class Passenger implements Storable {
    private String dni;
    private String name;
    private String surname;
    private static final String ELEMENTS_SEPARATOR = ",";

    /**
     * Constructor method.
     * @param dni
     * @param name
     * @param surname
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
     * Overwrited toString(). It returns a string composed by the DNI, name, and surname.
     * @return String
     */
    @Override
    public String toString(){
        return dni + ELEMENTS_SEPARATOR + name + ELEMENTS_SEPARATOR + surname;
    }


    /**
     * Overwrited equals. Compares an object with this passenger.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(!(obj instanceof Passenger)) return false;
        Passenger tmp = (Passenger)obj;
        return Objects.equals(dni, tmp.dni);
    }


    /**
     * Saves a contact throw the receives PrintWriter.
     * @param printWriter
     */
    @Override
    public void save(PrintWriter printWriter){
        printWriter.println(this.toString());
    }
}
