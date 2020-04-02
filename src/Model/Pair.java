/*
 * Type class Model.Pair. An tuple object that contains a seat and a passenger.
 *
 * Model.Pair.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package Model;

public class Pair<seat, passengerID> {

    private int seat;
    private String passengerID;

    /**
     * Constructor method.
     * @param seat Integer
     * @param passengerID String
     */
    public Pair(int seat, String passengerID) {
        this.seat = seat;
        this.passengerID = passengerID;
    }

    /**
     * Returns the seat.
     * @return Ineger
     */
    public int getSeat(){
        return seat;
    }


    /**
     * Returns the passenger ID.
     * @return String
     */
    public String getPassengerID(){
        return passengerID;
    }


    /**
     * Modifies seat.
     * @param newSeat
     */
    public void setSeat(int newSeat){
        seat = newSeat;
    }


    /**
     * Modifies the ID.
     * @param newPassengerID
     */
    public void setPassengerID(String newPassengerID){
        passengerID = newPassengerID;
    }

}