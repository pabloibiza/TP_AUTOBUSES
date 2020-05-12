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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public class NorthPanel extends JPanel {
    private static final String TEXT_SPACER = " ";
    private static final String COLON = ": ";
    private static final String FLAG_IMAGES_PATH = "storage/img/";
    private static final String FLAGS_IMAGES_EXTENSION = ".png";

    private MainFrame mainFrame;
    private JLabel travelsLabel;
    private JComboBox travelsComboBox;
    private Location location;
    private JLabel languageIcon;


    /**
     * Constructor method.
     *
     * @param mainFrame MainFrame
     */
    public NorthPanel(MainFrame mainFrame, Location location) {
        this.mainFrame = mainFrame;
        this.location = location;
        this.setLayout(new BorderLayout(10, 10));
        buildPanel();
    }


    /**
     * Builds the panel.
     */
    private void buildPanel() {
        languageIcon = new JLabel();
        languageIcon.setIcon(new ImageIcon(FLAG_IMAGES_PATH + location.getLocale()
                + FLAGS_IMAGES_EXTENSION));
        languageIcon.setToolTipText(location.getLabel(location.CHANGE_LANGUAGE));

        travelsLabel = new JLabel(location.getLabel(location.ROUTES) + COLON, SwingConstants.RIGHT);
        travelsComboBox = new JComboBox();
        travelsComboBox.setPreferredSize(new Dimension(400, 45));

        this.add(languageIcon, BorderLayout.WEST);
        this.add(travelsLabel, BorderLayout.CENTER);
        this.add(travelsComboBox, BorderLayout.EAST);

        travelsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    mainFrame.updateBusMatrix(getSelectedTravel());
                    mainFrame.travelSelected(getSelectedTravel());
                } catch (NullPointerException nullPointerException){
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

        if(travels.isEmpty()){
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

}
