/*
 * Top panel of the view. Contains the combo box and the button to show and select a travel.
 *
 * View.NorthPanel.java
 *
 * @version 4.4
 * @author Pablo Sanz Alguacil
 */

package View;

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

    private MainFrame mainFrame;
    private JLabel travelsLabel;
    private JComboBox travelsComboBox;
    private Location local;


    /**
     * Constructor method.
     *
     * @param mainFrame MainFrame
     */
    public NorthPanel(MainFrame mainFrame, Location local) {
        this.mainFrame = mainFrame;
        this.local = local;
        this.setLayout(new GridLayout(1, 3, 5, 5));
        buildPanel();
    }


    /**
     * Builds the panel.
     */
    private void buildPanel() {
        travelsLabel = new JLabel(local.getLabel(local.ROUTES) + COLON, SwingConstants.RIGHT);
        travelsComboBox = new JComboBox();

        this.add(travelsLabel);
        this.add(travelsComboBox);

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
                if(t1.getOrigin().compareToIgnoreCase(t2.getOrigin()) == 0) {
                    if(t1.getDestiny().compareToIgnoreCase(t2.getDestiny()) == 0) {
                        if(t1.getHour().compareTo(t2.getHour()) == 0){
                            return t1.getMinute().compareTo(t2.getMinute());
                        } else {
                            return t1.getHour().compareTo(t2.getHour());
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
