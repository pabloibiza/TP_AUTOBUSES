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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NorthPanel extends JPanel {
    private static final String TEXT_SPACER = " ";
    private static final String COLON = ": ";
    private static final String IMAGES_PATH = "storage/img/";
    private static final String PNG_IMAGES_EXTENSION = ".png";
    private static final String CONNECTED_ICON = "connected";
    private static final String DISCONNECTED_ICON = "disconnected";

    //origin-destiny hour:minute [ ID: id ]
    private static final String hourRegex = "\\w*-\\w*\\s(\\d+):\\d+\\s\\[\\sID:\\s\\d+\\s\\]";
    private static final String minuteRegex = "\\w*-\\w*\\s\\d+:(\\d+)\\s\\[\\sID:\\s\\d+\\s\\]";
    private static final String idRegex = "\\w*-\\w*\\s\\d+:\\d+\\s\\[\\sID:\\s(\\d+)\\s\\]";

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
        String elementName;
        travelsComboBox.removeAllItems();

        if(travels == null|| travels.isEmpty() ){
            mainFrame.receivedListEmpty();

        } else {
            travels = sortTravels(travels);
            Iterator it = travels.iterator();
            while (it.hasNext()) {
                Travel element = (Travel) it.next();
                elementName = element.getOrigin() + "-" + element.getDestiny() +
                        TEXT_SPACER +
                        String.format("%02d", element.getDate().get(GregorianCalendar.HOUR)) +
                        ":" +
                        String.format("%02d", element.getDate().get(GregorianCalendar.MINUTE)) +
                        TEXT_SPACER +
                        "[" + TEXT_SPACER + "ID:" + TEXT_SPACER + element.getId() + TEXT_SPACER + "]";

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
        Pattern pattern = Pattern.compile(idRegex);
        Matcher matcher = pattern.matcher(selectedTravel);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }


    /**
     * Returns the hour of the selected travel.
     * @return Integer[hour, minute]
     */
    public int[] getTravelHourMinute(){
        int hour = -1;
        int minute = -1;
        String selectedTravel = (String) travelsComboBox.getSelectedItem();

        Pattern pattern = Pattern.compile(hourRegex);
        Matcher matcher = pattern.matcher(selectedTravel);
        if (matcher.find()) {
            hour = Integer.parseInt(matcher.group(1));
        }

        pattern = Pattern.compile(minuteRegex);
        matcher = pattern.matcher(selectedTravel);
        if (matcher.find()) {
            minute = Integer.parseInt(matcher.group(1));
        }

        if(hour >= 0 && minute >= 0){
            int[] hourMinute ={hour,minute};
            return hourMinute;
        }
        return null;


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
        String text = clientID + TEXT_SPACER + "[" + connectionID + "]";
        clientConncetionID.setText(text);
    }

}
