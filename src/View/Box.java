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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Box extends JButton {
    private MainFrame mainFrame;
    private int seatNumber;
    private String dni;
    private boolean inUse;
    private Box box = this;
    private Color defaultColor = getBackground();
    private Color darkerGreen = new Color(31, 155, 8);
    private Color darkerRed = new Color(192, 25, 8);


    /**
     * Constructor method.
     * @param mainFrame MainFrame
     * @param seatNumber Integer
     * @param dni String
     */
    public Box(MainFrame mainFrame, int seatNumber, String dni) {
        this.mainFrame = mainFrame;
        this.seatNumber = seatNumber;
        this.dni = dni;
        this.setText(String.valueOf(seatNumber));
        if(dni != null) {
            setAssigned(dni);
        } else {
            setDeallocated();
        }

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setSelectedSeat(box);
                select();
            }
        });
    }


    /**
     * Constructor method for an empty and invisible Box.
     */
    public Box () {
        this.setEnabled(false);
        this.setVisible(false);
    }

    /**
     * Sets the id for the Box, sets the Box status variable to true and changes the color to blue.
     * @param newDni String
     */
    public void setAssigned(String newDni) {
        dni = newDni;
        inUse = true;
        this.setForeground(darkerRed);
    }


    /**
     * Removes de associated id, sets the Box status variable to false, and changes the color to green.
     */
    public void setDeallocated(){
        dni = "";
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
        return dni;
    }


    /**
     * Selects the button.
     */
    public void select(){
        setBackground(Color.YELLOW);
        setOpaque(true);
        setBorderPainted(true);
    }


    /**
     * Deselects the button.
     */
    public void deselect() {
        setBackground(defaultColor);
        setOpaque(false);
    }
}
