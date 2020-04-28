/*
 * Left panel of the view. Contains the combo boxes and buttons to select a date and search.
 *
 * View.WestPanel.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package View;

import Control.ViewListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class WestPanel extends JPanel {
    private static final String SEARCH_BUTTON = "Search";
    private static final String DAY_LABEL = "Day:";
    private static final String MONTH_LABEL = "Month:";
    private static final String YEAR_LABEL = "Year:";
    private static final String DATE_LABEL = "DATE:";
    private static final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};

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


    /**
     * Constructor method.
     * @param mainframe MainFrame
     */
    public WestPanel(MainFrame mainframe, ViewListener viewListener){
        this.mainFrame = mainframe;
        this.viewListener = viewListener;
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
        searchButton = new JButton(SEARCH_BUTTON);
        dayLabel = new JLabel(DAY_LABEL);
        monthLabel = new JLabel(MONTH_LABEL);
        yearLabel = new JLabel(YEAR_LABEL);
        actualDate = new GregorianCalendar();
        actualYear = actualDate.get(GregorianCalendar.YEAR);
        actualMonth = actualDate.get(GregorianCalendar.MONTH);
        dateLabel = new JLabel(DATE_LABEL, SwingConstants.CENTER);

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
        for(int i = 0; i < months.length; i++){
            monthComboBox.addItem(String.valueOf(months[i]));
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
                viewListener.producedEvent(ViewListener.Event.SEARCH, selectedDate);

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
