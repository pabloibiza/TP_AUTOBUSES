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
import Model.SalesDesk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.GregorianCalendar;
import java.util.List;

public class MainFrame extends JFrame implements PropertyChangeListener{
    private static final String COLON = ": ";
    private static final String ELEMENTS_SEPARATOR = ",";

    private static MainFrame mainFrame;
    private ViewListener viewListener;
    private SalesDesk salesDesk;
    private WestPanel westPanel;
    private NorthPanel northPanel;
    private SouthPanel southPanel;
    private CentralPanel centralPanel;
    private GregorianCalendar selectedDate;
    private String selectedTravel = "";
    private SeatBox selectedSeat;
    private Location local;

    private MainFrame(ViewListener viewListener, SalesDesk salesDesk, Location location){
        super();
        this.viewListener = viewListener;
        this.salesDesk = salesDesk;
        this.local = location;
        configureWindow();

        westPanel = new WestPanel(this, viewListener, location);
        this.add(westPanel, BorderLayout.WEST);

        northPanel = new NorthPanel(this, location);
        this.add(northPanel, BorderLayout.NORTH);

        southPanel = new SouthPanel(this, viewListener, location);
        this.add(southPanel, BorderLayout.SOUTH);
        southPanel.stateRouteSheetButton(false);
        southPanel.stateAssignDeallocateButtons(false);

        centralPanel = new CentralPanel(this, salesDesk, location);
        this.add(centralPanel, BorderLayout.CENTER);

        setVisible(true);
    }


    /**
     * Creates a singleton instance.
     * @param viewListener ViewListener
     * @param salesDesk SalesDesk
     * @return Mainframe
     */
    public static synchronized MainFrame getSingletonInstance(ViewListener viewListener, SalesDesk salesDesk, Location local) {
        if (mainFrame == null){
            mainFrame = new MainFrame(viewListener, salesDesk, local);
        }
        else{
            mainFrame.errorMessage(local.getLabel(local.INSTANCE_ALREADY_CREATED) + mainFrame.getClass().getSimpleName(), null);
        }
        return mainFrame;
    }


    /**
     * Avoids cloning this object.
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Sets the window parameters.
     */
    private void configureWindow() {
        this.setTitle(local.getLabel(local.WINDOW_TITLE));
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
     * @param date GregorianCalendar
     */
    public void updateTravelsPerDate(GregorianCalendar date){
        List travels = salesDesk.searchTravelsPerDate(date);
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
        southPanel.stateAssignDeallocateButtons(false);
        if(id == null) {
            southPanel.stateRouteSheetButton(false);
        } else if(isOutOfDate()){
            southPanel.stateRouteSheetButton(true);
            infoMessage(local.getLabel(local.TRAVEL_OUT_OF_DATE));
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
        infoMessage(local.getLabel(local.NO_TRAVELS));
    }


    /**
     * Generates an info dialog window with the received message.
     * @param message String
     */
    public void infoMessage(String message){
        JOptionPane.showMessageDialog(this,
                message,
                local.getLabel(local.INFO_WINDOW_TITLE),
                JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Generates an error dialog window with the received message.
     * @param message String
     */
    public void errorMessage(String message, Exception e){
        JOptionPane.showMessageDialog(this,
                message,
                local.getLabel(local.ERROR_WINDOW_TITLE),
                JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Builds the seats matrix for a given travel
     * @param travelID String
     */
    public void updateBusMatrix(String travelID) {
        centralPanel.updateMatrix(salesDesk.searchTravel(travelID));
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
    public void setSelectedSeat(SeatBox newSeat) {
        if (selectedSeat != null || newSeat == null) {
            selectedSeat.unselect();
            southPanel.stateAssignDeallocateButtons(false);
        }

        selectedSeat = newSeat;
        if (!isOutOfDate() && newSeat != null){
            southPanel.stateAssignDeallocateButtons(true);
            if (newSeat.isOccupied()) {
                southPanel.changeToDeallocate();
            } else {
                southPanel.changeToAssign();
            }
        }
    }


    /**
     * Returns the selected seat.
     * @return Integer
     */
    public SeatBox getSelectedSeat() {
        return selectedSeat;
    }


    /**
     * Assigns a seat on a travel for a passenger.
     */
    public void assignSeat(){
        Passenger newPassenger = askPassengerInfo();
        if(newPassenger != null) {
            viewListener.producedEvent(
                    ViewListener.Event.NEW_PASSENGER,
                    newPassenger);
            viewListener.producedEvent(
                    ViewListener.Event.ASSIGN,
                    new Object[]{
                            salesDesk.searchTravel(getSelectedTravel()),
                            newPassenger,
                            getSelectedSeat().getSeatNumber()});
        }
    }


    /**
     * Deallocates a seat on a travel for a passenger.
     */
    public void deallocateSeat() {
        viewListener.producedEvent(
                ViewListener.Event.DELETE_PASSENGER,
                getSelectedSeat().getDni());
        viewListener.producedEvent(ViewListener.Event.DEALLOCATE,
                new Object[] {
                        salesDesk.searchTravel(getSelectedTravel()),
                        getSelectedSeat().getSeatNumber()});
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
        String dialogInput;
        Passenger passenger;
        for (int i = 0; i < 3; i++) {
            dialogInput = JOptionPane.showInputDialog(this,
                    local.getLabel(local.NEW_PASSENGER_QUESTIONS[i]) + COLON,
                    local.getLabel(local.NEW_PASSENGER_WINDOW_TITTLE),
                    JOptionPane.QUESTION_MESSAGE);
            if(dialogInput == null){
                return null;
            }
            data.append(dialogInput + ELEMENTS_SEPARATOR);
        }
        try{
            passenger = new Passenger(data.toString());
            return passenger;
        } catch (ArrayIndexOutOfBoundsException e) {
            infoMessage(local.getLabel(local.FILL_ALL_GAPS));
        }
        return null;

    }


    /**
     * Receives events and perform different actions depending on the event.
     * @param event PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch(event.getPropertyName()){
            case "SEAT_CHANGE":
                updateBusMatrix(getSelectedTravel());
                setSelectedSeat(null);
                break;
        }
    }
}
