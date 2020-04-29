/*
 * Main frame of the view.
 *
 * View.MainFrame.java
 *
 * @version 4.4
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
    private static final String NO_TRAVELS = "No travels found for this date.";
    private static final String TRAVEL_OUT_OF_DATE = "This travel is out of date.";
    private static final String FILL_ALL_GAPS = "All fields must be filled";
    private static final String[] NEW_PASSENGER_QUESTIONS = {"DNI", "Name", "Surname"};
    private static final String COLON = ": ";
    private static final String ELEMENTS_SEPARATOR = ",";

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
        southPanel.stateRouteSheetButton(false);
        southPanel.stateAssignDeallocateButtons(false);

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
     * @param date GregorianCalendar
     */
    public void setSelectedDate(GregorianCalendar date){
        selectedDate = date;
    }


    /**
     * Returns the selected date.
     * @return GregorianCalendar
     */
    public GregorianCalendar getSelectedDate(){
        return selectedDate;
    }


    /**
     * Sets the id of the selected travel and enables or disables the south panel depending on the received data.
     * @param id String
     */
    public void travelSelected(String id){
        selectedTravel = id;
        if(id == null) {
            southPanel.stateRouteSheetButton(false);
            southPanel.stateAssignDeallocateButtons(false);
        } else if(isOutOfDate()){
            southPanel.stateAssignDeallocateButtons(false);
            southPanel.stateRouteSheetButton(true);
            infoMessage(TRAVEL_OUT_OF_DATE);
        } else {
            southPanel.stateRouteSheetButton(true);
        }
    }


    /**
     * Returns the id of the selected travel.
     * @return String
     */
    public String getSelectedTravel(){
        return selectedTravel;
    }


    /**
     * Actions to perform if the received travels list is empty.
     */
    public void receivedListEmpty(){
        southPanel.stateRouteSheetButton(false);
        southPanel.stateAssignDeallocateButtons(false);
        infoMessage(NO_TRAVELS);
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
    public void errorMessage(String message, Exception e){
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
            southPanel.changeToDeallocate();
        } else {
            southPanel.changeToAssign();
        }
        if(!isOutOfDate()){
            southPanel.stateAssignDeallocateButtons(true);
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
        Passenger newPassenger = askPassengerInfo();
        if(newPassenger != null) {
            viewListener.producedEvent(ViewListener.Event.NEW_PASSENGER, newPassenger);
            viewListener.producedEvent(ViewListener.Event.ASSIGN,
                    new String[]{getSelectedTravel(),
                            newPassenger.getDni(),
                            String.valueOf(getSelectedSeat().getSeatNumber())});
            getSelectedSeat().setAssigned(newPassenger.getDni());
            southPanel.changeToDeallocate();
        }
    }


    /**
     * Deallocates a seat on a travel for a passenger.
     */
    public void deallocateSeat() {
        viewListener.producedEvent(ViewListener.Event.DELETE_PASSENGER, getSelectedSeat().getDni());
        viewListener.producedEvent(ViewListener.Event.DEALLOCATE,
                new String[] {getSelectedTravel(), String.valueOf(getSelectedSeat().getSeatNumber())});
        getSelectedSeat().setDeallocated();
        southPanel.changeToAssign();
    }


    /**
     * Checks if the currently selected travel is out of date;
     * @return boolean
     */
    public boolean isOutOfDate(){
        int[] hourMinute = northPanel.getTravelHour();
        GregorianCalendar date = new GregorianCalendar(
                getSelectedDate().get(GregorianCalendar.YEAR),
                getSelectedDate().get(GregorianCalendar.MONTH),
                getSelectedDate().get(GregorianCalendar.DAY_OF_MONTH),
                hourMinute[0],
                hourMinute[1]);
        return date.before(new GregorianCalendar());

    }


    /**
     * Generates a dialog window to ask for data to create a new passenger, and returns that passenger.
     * @return Passenger
     */
    public Passenger askPassengerInfo(){
        StringBuilder data = new StringBuilder();
        Passenger passenger;
        for (int i = 0; i < 3; i++) {
            data.append(JOptionPane.showInputDialog(NEW_PASSENGER_QUESTIONS[i] + COLON)).append(ELEMENTS_SEPARATOR);
        }
        try{
            passenger = new Passenger(data.toString());
            return passenger;
        } catch (ArrayIndexOutOfBoundsException e) {
            infoMessage(FILL_ALL_GAPS);
        }
        return null;

    }

}
