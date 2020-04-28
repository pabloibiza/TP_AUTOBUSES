/*
 * Main frame of the view.
 *
 * View.MainFrame.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package View;

import Control.ViewListener;
import Model.Passenger;
import Model.Travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class MainFrame extends JFrame{
    private static final String WINDOW_TITLE = "BUS OFFICE";
    private static final String INFO_WINDOW_TITLE = "INFO";
    private static final String ERROR_WINDOW_TITLE = "ERROR";

    private ViewListener viewListener;
    private WestPanel westPanel;
    private NorthPanel northPanel;
    private SouthPanel southPanel;
    private CentralPanel centralPanel;

    private GregorianCalendar selectedDate;
    private String selectedTravel = "";
    private Box selectedSeat;

    public MainFrame(ViewListener viewListener){
        super();
        this.viewListener = viewListener;
        configureWindow();

        westPanel = new WestPanel(this, viewListener);
        this.add(westPanel, BorderLayout.WEST);

        northPanel = new NorthPanel(this, viewListener);
        this.add(northPanel, BorderLayout.NORTH);

        southPanel = new SouthPanel(this, viewListener);
        this.add(southPanel, BorderLayout.SOUTH);
        southPanel.enableDisableButtons(false);

        centralPanel = new CentralPanel(this);
        this.add(centralPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Sets the window parameters.
     */
    private void configureWindow() {
        this.setTitle(WINDOW_TITLE);
        this.setSize(900, 500);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5,5));
        this.setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                viewListener.producedEvent(ViewListener.Event.EXIT, null);
            }
        });
    }


    /**
     * Updates de travels north's panel combo box.
     * @param travels
     */
    public void updateTravels(ArrayList travels){
        northPanel.updateTravels(travels);
    }


    /**
     * Sets the selected date.
     * @param date
     */
    public void setSelectedDate(GregorianCalendar date){
        selectedDate = date;
    }


    /**
     * Returns the selected date.
     * @return String
     */
    public GregorianCalendar getSelectedDate(){
        return selectedDate;
    }


    /**
     * Sets the id of the selected travel and enables or disables the south panel depending on the received data.
     * @param id String
     */
    public void setSelectedTravel(String id){
        selectedTravel = id;
        if(id == null){
            southPanel.enableDisableButtons(false);
        } else {
            southPanel.enableDisableButtons(true);
        }
    }


    /**
     * Returns the id of the selected travel.
     * @return
     */
    public String getSelectedTravel(){
        return selectedTravel;
    }


    /**
     * Actions to perform if the received travels list is empty.
     */
    public void receivedListEmpty(){
        southPanel.enableDisableButtons(false);
    }


    /**
     * Generates an info dialog window with the received message.
     * @param message String
     */
    public void infoMessage(String message){
        JOptionPane.showMessageDialog(this, message, INFO_WINDOW_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Generates an error dialog window with the received message.
     * @param message String
     */
    public void errorMessage(String message){
        JOptionPane.showMessageDialog(this, message, ERROR_WINDOW_TITLE, JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Builds the seats matrix for a given travel
     * @param travel Travel
     */
    public void updateBusMatrix(Travel travel) {
        centralPanel.updateMatrix(travel);
    }


    /**
     * Cleans the central panel matrix.
     */
    public void cleanMatrix(){
        centralPanel.cleanMatrix();
    }


    /**
     * Sets the selected seat.
     * @param newSeat Integer
     */
    public void setSelectedSeat(Box newSeat) {
        if (selectedSeat != null) {
            selectedSeat.deselect();
        }
        selectedSeat = newSeat;
        if(newSeat.isOccupied()){
            southPanel.changeToUnassign();
        } else {
            southPanel.changeToAssign();
        }
    }


    /**
     * Returns the selected seat.
     * @return Integer
     */
    public Box getSelectedSeat() {
        return selectedSeat;
    }


    /**
     * Assigns a seat on a travel for a passenger.
     */
    public void assignSeat(){
        //Implementar ventana para introducir datos del pasajero
        Passenger newPassenger = new Passenger("99999999Z", "Nombre", "Apellido"); //Aqui se reciven los datos de la ventana emergente
        viewListener.producedEvent(ViewListener.Event.NEW_PASSENGER, newPassenger);
        String assignationData[] = {getSelectedTravel(), newPassenger.getDni(), String.valueOf(getSelectedSeat().getSeatNumber())};
        viewListener.producedEvent(ViewListener.Event.ASSIGN, assignationData);
        getSelectedSeat().setAssigned(newPassenger.getDni());
        southPanel.changeToUnassign();
    }


    public void unassignSeat() {
        String unassignationData[] = {getSelectedTravel(), String.valueOf(getSelectedSeat().getSeatNumber())};
        viewListener.producedEvent(ViewListener.Event.DELETE_PASSENGER, getSelectedSeat().getDni());
        viewListener.producedEvent(ViewListener.Event.UNASSIGN, unassignationData);
        getSelectedSeat().setUnassigned();
        southPanel.changeToAssign();
    }

}
