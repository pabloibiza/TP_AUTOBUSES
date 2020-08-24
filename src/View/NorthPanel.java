/*
 * Top panel of the view. Contains the combo box and the button to show and select a travel.
 *
 * View.NorthPanel.java
 *
 * @version 2.0
 * @author Pablo Sanz Alguacil
 */

package View;

import Internationalization.Location;
import Model.Travel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public class NorthPanel extends JPanel {
    private static final String TEXT_SPACER = " ";
    private static final String COLON = ": ";
    private static final String IMAGES_PATH = "storage/img/";
    private static final String PNG_IMAGES_EXTENSION = ".png";
    private static final String CONNECTED_ICON = "connected";
    private static final String DISCONNECTED_ICON = "disconnected";

    private MainFrame mainFrame;
    private JLabel travelsLabel;
    private JComboBox travelsComboBox;
    private Location location;
    private JLabel languageIcon;
    private JLabel connectedIcon;
    private JPanel west;
    private JLabel clientConncetionID;



    /**
     * Constructor method.
     *
     * @param mainFrame MainFrame
     */
    public NorthPanel(MainFrame mainFrame, Location location, String clientID, String connectionID) {
        this.mainFrame = mainFrame;
        this.location = location;
        this.setLayout(new BorderLayout(10, 10));
        buildPanel();
        updateConnectionIDLabel(clientID, connectionID);
    }


    /**
     * Builds the panel.
     */
    private void buildPanel() {
        languageIcon = new JLabel();
        languageIcon.setIcon(new ImageIcon(IMAGES_PATH + location.getLocale()
                + PNG_IMAGES_EXTENSION));
        languageIcon.setToolTipText(location.getLabel(location.CHANGE_LANGUAGE));

        connectedIcon = new JLabel();
        connectedIcon.setIcon(new ImageIcon(IMAGES_PATH + DISCONNECTED_ICON + PNG_IMAGES_EXTENSION));

        travelsLabel = new JLabel(location.getLabel(location.ROUTES) + COLON, SwingConstants.RIGHT);
        travelsComboBox = new JComboBox();
        travelsComboBox.setPreferredSize(new Dimension(400, 45));

        clientConncetionID = new JLabel();

        west = new JPanel(new FlowLayout(0,30,3));
        west.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        west.add(languageIcon);
        west.add(connectedIcon);
        west.add(clientConncetionID);
        this.add(west, BorderLayout.WEST);
        this.add(travelsLabel, BorderLayout.CENTER);
        this.add(travelsComboBox, BorderLayout.EAST);

        travelsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String travelID = getSelectedTravel();
                    mainFrame.updateBusMatrix(travelID);
                    mainFrame.travelSelected(travelID);
                } catch (NullPointerException nullPointerException){
                    mainFrame.cleanMatrix();
                } catch (Exception exception) {
                    mainFrame.cleanMatrix();
                }
            }
        });

    }

    /**
     * Updates the travels combo box with a received collection of travels.
     * Enables or disables the buttons related to the travels depending on the received list (if it's empty the buttons
     * are disabled and vice versa).
     * @param travels Collection
     */
    public void updateTravels(List<Travel> travels){
        String[] tokens;
        String elementName;
        travelsComboBox.removeAllItems();

        if(travels == null|| travels.isEmpty() ){
            mainFrame.receivedListEmpty();

        } else {
            travels = sortTravels(travels);
            Iterator it = travels.iterator();
            while (it.hasNext()) {
                Travel element = (Travel) it.next();
                tokens = element.toString().split(",");
                elementName = tokens[1] +
                        "-" +
                        tokens[2] +
                        " " +
                        String.format("%02d", Integer.parseInt(tokens[6])) +
                        ":" +
                        String.format("%02d", Integer.parseInt(tokens[7])) +
                        " " +
                        "[ ID: " +
                        tokens[0] +
                        " ]";
                travelsComboBox.addItem(elementName);
            }
        }
    }


    /**
     * Sorts a list of travels by origin --> destiny --> hour --> minute
     * @param travels ArrayList
     * @return ArrayList
     */
    private List<Travel> sortTravels(List<Travel> travels){
        Collections.sort(travels, new Comparator<Travel>(){
            @Override
            public int compare(Travel t1, Travel t2) {
                Integer t1Hour = t1.getDate().get(GregorianCalendar.HOUR);
                Integer t1Minute = t1.getDate().get(GregorianCalendar.MINUTE);
                Integer t2Hour = t2.getDate().get(GregorianCalendar.HOUR);
                Integer t2Minute = t2.getDate().get(GregorianCalendar.MINUTE);
                if(t1.getOrigin().compareToIgnoreCase(t2.getOrigin()) == 0) {
                    if(t1.getDestiny().compareToIgnoreCase(t2.getDestiny()) == 0) {
                        if(t1Hour.compareTo(t2Hour) == 0){
                            return t1Minute.compareTo(t2Minute);
                        } else {
                            return t1Hour.compareTo(t2Hour);
                        }
                    } else {
                        return t1.getDestiny().compareToIgnoreCase(t2.getDestiny());
                    }
                } else {
                    return t1.getOrigin().compareToIgnoreCase(t2.getOrigin());
                }
            }
        });
        return travels;
    }


    /**
     * Returns the the id from the selected travel.
     * @return String
     */
    public String getSelectedTravel(){
        String selectedTravel = (String) travelsComboBox.getSelectedItem();
        return selectedTravel.split(TEXT_SPACER)[4];
    }


    /**
     * Returns the hour of the selected travel.
     * @return Integer[hour, minute]
     */
    public int[] getTravelHour(){
        String selectedItem = (String) travelsComboBox.getSelectedItem();
        int hour = Integer.parseInt(selectedItem.split(" ")[1].split(":")[0]);
        int minute = Integer.parseInt(selectedItem.split(" ")[1].split(":")[1]);
        int[] hourMinute = {hour,minute};
        return hourMinute;
    }


    /**
     * Changes the connected icon depending on the received status.
     * @param status Boolean
     */
    public void setConnectedIcon(boolean status) {
        if(status) {
            connectedIcon.setIcon(new ImageIcon(IMAGES_PATH + CONNECTED_ICON + PNG_IMAGES_EXTENSION));
        } else {
            connectedIcon.setIcon(new ImageIcon(IMAGES_PATH + DISCONNECTED_ICON + PNG_IMAGES_EXTENSION));
        }
    }


    /**
     * Updates the connection ID
     */
    public void updateConnectionIDLabel(String clientID, String connectionID) {
        String text = clientID + " [" + connectionID + "]";
        clientConncetionID.setText(text);
    }

}
