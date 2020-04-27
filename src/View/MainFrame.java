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

        centralPanel = new CentralPanel(viewListener);
        this.add(centralPanel, BorderLayout.CENTER);

        //this.add(new JButton("Solo estoy ocupando espacio"), BorderLayout.CENTER);
        setVisible(true);

    }

    /**
     * Sets the window parameters.
     */
    private void configureWindow() {
        this.setTitle(WINDOW_TITLE);
        this.setSize(900, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
    public void updateBusMatrix (Travel travel) {
        centralPanel.updateMatrix(travel);
    }
}
