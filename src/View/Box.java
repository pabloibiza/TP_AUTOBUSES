/*
 * Type object box. Is a JButton extender object which contains an identifier, and passenger identifier,
 * a position (row and column), and a status variable.
 *
 * View.Box.java
 *
 * @version 2.1
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
    private int row;
    private int column;
    private String dni;
    private boolean inUse;
    private Box box = this;


    /**
     * Constructor method.
     * @param mainFrame MainFrame
     * @param seatNumber Integer
     * @param row Integer
     * @param column Integer
     * @param dni String
     */
    public Box(MainFrame mainFrame, int seatNumber, int row, int column, String dni) {
        this.mainFrame = mainFrame;
        this.seatNumber = seatNumber;
        this.row = row;
        this.column =column;
        this.dni = dni;
        this.setText(String.valueOf(seatNumber));
        if(dni != null) {
            setAssigned(dni);
        } else {
            setUnassigned();
        }

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select();
                mainFrame.setSelectedSeat(box);
            }
        });
    }


    /**
     * Constructor method for an empty Box.
     */
    public Box () {
        this.setEnabled(false);
        this.setVisible(false);
    }

    /**
     * Sets the id for the Box, sets the Box status variable to true and changes the color to blue.
     * @param newDni
     */
    public void setAssigned(String newDni) {
        dni = newDni;
        inUse = true;
        this.setForeground(Color.RED);
    }


    /**
     * Removes de associated id, sets the Box status variable to false, and changes the color to green.
     */
    public void setUnassigned(){
        dni = "";
        inUse = false;
        this.setForeground(Color.BLUE);
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
        setBackground(new JButton().getBackground());
        setOpaque(false);
    }
}
