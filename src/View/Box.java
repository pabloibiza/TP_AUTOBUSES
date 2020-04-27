package View;

import Model.Passenger;
import javax.swing.*;
import java.awt.*;

public class Box extends JButton {
    private int seatNumber;
    private int row;
    private int column;
    private String dni;
    private boolean isOccupied;


    public Box(int seatNumber, int row, int column, String dni) {
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
    }

    public Box () {

    }


    public void setAssigned(String newDni) {
        dni = newDni;
        isOccupied = true;
        this.setForeground(Color.BLUE);
    }

    public void setUnassigned(){
        dni = "";
        isOccupied = false;
        this.setForeground(Color.GREEN);
    }
}
