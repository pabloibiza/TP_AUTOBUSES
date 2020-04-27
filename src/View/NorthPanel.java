/*
 * Top panel of the view. Contains the combo box and the button to show and select a travel.
 *
 * View.NorthPanel.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package View;

import Control.ViewListener;
import Model.Travel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class NorthPanel extends JPanel {
    private static final String ROUTES = "ROUTES:";
    private static final String VIEW_SEATS_BUTTON = "View seats";
    private static final String TEXT_SPACER = " ";

    private MainFrame mainFrame;
    private ViewListener viewListener;
    private JLabel travelsLabel;
    private JComboBox travelsComboBox;
    private JButton viewSeatsButton;

    private String selectedDate;


    /**
     * Constructor method.
     *
     * @param mainFrame MainFrame
     */
    public NorthPanel(MainFrame mainFrame, ViewListener viewListener) {
        this.mainFrame = mainFrame;
        this.viewListener = viewListener;
        this.setLayout(new GridLayout(1, 3));
        buildPanel();
    }


    /**
     * Builds the panel.
     */
    private void buildPanel() {
        travelsLabel = new JLabel(ROUTES, SwingConstants.RIGHT);
        travelsComboBox = new JComboBox();
        viewSeatsButton = new JButton(VIEW_SEATS_BUTTON);

        this.add(travelsLabel);
        this.add(travelsComboBox);
        this.add(viewSeatsButton);
        viewSeatsButton.setEnabled(false);

        viewSeatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewListener.producedEvent(ViewListener.Event.VIEW_SEATS, getSelectedTravel());
                mainFrame.setSelectedTravel(getSelectedTravel());
            }
        });
    }

    /**
     * Updates the travels combo box with a received collection of travels.
     * Enables or disables the buttons related to the travels depending on the received list (if it's empty the buttons
     * are disabled and vice versa).
     * @param travels Collection
     */
    public void updateTravels(ArrayList travels){
        String[] tokens;
        String elementName;
        travelsComboBox.removeAllItems();

        if(travels.isEmpty()){
            viewSeatsButton.setEnabled(false);
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
            viewSeatsButton.setEnabled(true);
        }
    }


    /**
     * Sorts a list of travels by origin --> destiny --> hour --> minute
     * @param travels ArrayList
     * @return ArrayList
     */
    public ArrayList sortTravels(ArrayList travels){
        Collections.sort(travels, new Comparator<Travel>(){

            @Override
            public int compare(Travel t1, Travel t2) {
                if(t1.getOrigin().compareToIgnoreCase(t2.getOrigin()) == 0) {
                    if(t1.getDestiny().compareToIgnoreCase(t2.getDestiny()) == 0) {
                        if(String.valueOf(t1.getHour()).compareToIgnoreCase(String.valueOf(t2.getHour())) == 1){
                            return String.valueOf(t1.getMinute()).compareTo(String.valueOf(t2.getMinute()));
                        } else {
                            return String.valueOf(t1.getHour()).compareToIgnoreCase(String.valueOf(t2.getHour()));
                        }
                    } else {
                        return
                                t1.getDestiny().compareToIgnoreCase(t2.getDestiny());
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


}
