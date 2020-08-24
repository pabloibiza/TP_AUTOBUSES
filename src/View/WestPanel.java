/*
 * Left panel of the view. Contains the combo boxes and buttons to select a date and search.
 *
 * View.WestPanel.java
 *
 * @version 2.0
 * @author Pablo Sanz Alguacil
 */

package View;

import Control.ViewListener;
import Internationalization.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class WestPanel extends JPanel {
    private static final String COLON = ": ";
    private JComboBox<String> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> dayComboBox;
    private JButton searchButton;
    private JLabel dayLabel;
    private JLabel monthLabel;
    private JLabel yearLabel;
    private JPanel yearPanel;
    private JPanel monthPanel;
    private JPanel dayPanel;
    private JLabel dateLabel;

    private MainFrame mainFrame;
    private ViewListener viewListener;
    private GregorianCalendar actualDate;
    private int actualYear;
    private int actualMonth;
    private Location location;


    /**
     * Constructor method.
     * @param mainframe MainFrame
     * @param viewListener ViewListener
     * @param location Location
     */
    public WestPanel(MainFrame mainframe, ViewListener viewListener, Location location){
        this.mainFrame = mainframe;
        this.viewListener = viewListener;
        this.location = location;
        this.setLayout(new GridLayout(5,1));
        buildPanel();
    }


    /**
     * Builds the panel.
     */
    private void buildPanel(){
        yearPanel = new JPanel();
        monthPanel = new JPanel();
        dayPanel = new JPanel();
        yearComboBox = new JComboBox<>();
        monthComboBox = new JComboBox<String>();
        dayComboBox = new JComboBox<>();
        searchButton = new JButton(location.getLabel(location.SEARCH_BUTTON));
        dayLabel = new JLabel(location.getLabel(location.DAY_LABEL) + COLON);
        monthLabel = new JLabel(location.getLabel(location.MONTH_LABEL) + COLON);
        yearLabel = new JLabel(location.getLabel(location.YEAR_LABEL) + COLON);
        actualDate = new GregorianCalendar();
        actualYear = actualDate.get(GregorianCalendar.YEAR);
        actualMonth = actualDate.get(GregorianCalendar.MONTH);
        dateLabel = new JLabel(location.getLabel(location.DATE_LABEL), SwingConstants.CENTER);

        //Adds elements to the panel.
        this.add(dateLabel);
        yearPanel.add(yearLabel);
        yearPanel.add(yearComboBox);
        this.add(yearPanel);
        monthPanel.add(monthLabel);
        monthPanel.add(monthComboBox);
        this.add(monthPanel);
        dayPanel.add(dayLabel);
        dayPanel.add(dayComboBox);
        this.add(dayPanel);
        this.add(searchButton);


        //Adds years to the years combo box.
        for(int i = (actualYear + 10); i > (actualYear - 10); i--){
            yearComboBox.addItem(String.valueOf(i));
        }
        yearComboBox.setSelectedItem(String.valueOf(actualYear));

        //Adds months to the month combo box.
        for(int i = 0; i < 12; i++){
            monthComboBox.addItem(String.valueOf(location.getLabel(location.MONTHS[i])));
        }
        monthComboBox.setSelectedIndex(actualMonth);

        //Adds days to the combo box.
        updateDays(actualMonth, actualYear);

        //Listener for the years combo box.
        yearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDays(monthComboBox.getSelectedIndex(),
                        Integer.parseInt((String) yearComboBox.getSelectedItem()));
            }
        });

        //Listener for the months combo box.
        monthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDays(monthComboBox.getSelectedIndex(),
                        Integer.parseInt((String) yearComboBox.getSelectedItem()));
            }
        });

        //Listener for the search button.
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GregorianCalendar selectedDate =
                        new GregorianCalendar(Integer.parseInt((String) yearComboBox.getSelectedItem()),
                        (monthComboBox.getSelectedIndex()),
                        Integer.parseInt((String) dayComboBox.getSelectedItem()));
                mainFrame.setSelectedDate(selectedDate);
                mainFrame.updateTravelsPerDate(selectedDate);

            }
        });
    }


    /**
     * Updates the daysComboBox with the number of days for the month and year received as parameters.
     * @param month Integer
     * @param year Integer
     */
    public void updateDays(int month, int year){
        dayComboBox.removeAllItems();
        for(int i = 1; i <= daysInMonth(month, year); i++){
            dayComboBox.addItem(String.valueOf(i));
        }
        dayComboBox.setSelectedItem("1");
    }


    /**
     * Returns the number of days of the month and year received as parameters.
     * @param month Integer
     * @param year Integer
     * @return Integer
     */
    private static int daysInMonth(int month, int year) {
        GregorianCalendar calendar = new GregorianCalendar(year, month, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
