/*
 * Type object box. Is a JButton extender object which contains an identifier, and passenger identifier,
 * a position (row and column), and a status variable.
 *
 * View.Box.java
 *
 * @version 4.4
 * @author Pablo Sanz Alguacil
 */

package View;

import Model.Passenger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SeatBox extends JButton {
    private static final String TEXT_SPACER = " ";
    private static final String ELEMENTS_SEPARATOR = ",";

    private MainFrame mainFrame;
    private int seatNumber;
    private Passenger passenger;
    private boolean inUse;
    private boolean selected;
    private String dni;
    private String nameAndSurnames;
    private Color defaultColor = getBackground();
    private Color darkerGreen = new Color(31, 155, 8);
    private Color darkerRed = new Color(192, 25, 8);


    /**
     * Constructor method.
     * @param mainFrame MainFrame
     * @param seatNumber Integer
     * @param passenger Passenger
     */
    public SeatBox(MainFrame mainFrame, int seatNumber, Passenger passenger) {
        this.mainFrame = mainFrame;
        this.seatNumber = seatNumber;
        this.passenger = passenger;
        selected = false;
        this.setText(String.valueOf(seatNumber));
        if(passenger != null) {
            setAssigned(passenger);
            dni = passenger.getDni();
            nameAndSurnames = passenger.toString().split(ELEMENTS_SEPARATOR)[1] + TEXT_SPACER +
                    passenger.toString().split(ELEMENTS_SEPARATOR)[2];
            setToolTipText(nameAndSurnames + ELEMENTS_SEPARATOR + dni);
        } else {
            setDeallocated();
            setToolTipText("");
        }

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!selected) {
                    mainFrame.setSelectedSeat(SeatBox.this);
                    select();
                } else {
                    mainFrame.setSelectedSeat(null);
                    unselect();
                }
            }
        });
    }


    /**
     * Constructor method for an empty and invisible Box.
     */
    public SeatBox() {
        this.setEnabled(false);
        this.setVisible(false);
    }

    /**
     * Sets the id for the Box, sets the Box status variable to true and changes the color to blue.
     * @param newPassenger Passenger
     */
    public void setAssigned(Passenger newPassenger) {
        passenger = newPassenger;
        inUse = true;
        this.setForeground(darkerRed);
    }


    /**
     * Removes de associated id, sets the Box status variable to false, and changes the color to green.
     */
    public void setDeallocated(){
        passenger = null;
        inUse = false;
        this.setForeground(darkerGreen);
    }


    /**
     * Returns the seat's status.
     * @return  Boolean
     */
    public boolean isOccupied(){
        return inUse;
    }


    /**
     * Returns the seat number.
     * @return Integer
     */
    public int getSeatNumber(){
        return seatNumber;
    }


    /**
     * Returns the dni.
     * @return String
     */
    public String getDni(){
        return passenger.getDni();
    }


    /**
     * Selects the button.
     */
    public void select(){
        selected = true;
        if(inUse) {
            setBackground(Color.ORANGE);
        } else {
            setBackground(Color.GREEN);
        }
        setOpaque(true);
        setBorderPainted(true);
    }


    /**
     * Deselects the button.
     */
    public void unselect() {
        selected = false;
        setBackground(defaultColor);
        setOpaque(false);
    }
}
