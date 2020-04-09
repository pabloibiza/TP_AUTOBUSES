package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

public class TopPanel extends JPanel {
    private static final String SEARCH_BUTTON_TEXT = "Search";

    private JComboBox yearComboBox;
    private JComboBox monthComboBox;
    private JComboBox dayComboBox;
    private JButton searchButton;
    private GregorianCalendar actualDate;
    private int actualYear;
    private int actualMonth;
    private int actualDay;


    /**
     * Constructor method.
     */
    public TopPanel(){
        this.setLayout(new GridLayout(1,4));
        buildPanel();
    }


    /**
     * Builds the panel.
     */
    public void buildPanel(){
        yearComboBox = new JComboBox();
        monthComboBox = new JComboBox();
        dayComboBox = new JComboBox();
        searchButton = new JButton(SEARCH_BUTTON_TEXT);
        actualDate = new GregorianCalendar();
        actualYear = actualDate.get(GregorianCalendar.YEAR);
        actualMonth = actualDate.get(GregorianCalendar.MONTH);
        actualDay = actualDate.get(GregorianCalendar.DAY_OF_MONTH);

        //Adds elements to the panel.
        this.add(dayComboBox);
        this.add(monthComboBox);
        this.add(yearComboBox);
        this.add(searchButton);

        //Adds years to the years combo box.
        for(int i = (actualYear + 10); i > (actualYear - 10); i--){
            yearComboBox.addItem(String.valueOf(i));
        }
        yearComboBox.setSelectedItem(String.valueOf(actualYear));

        //Adds months to the month combo box.
        for(int i = 1; i <= 12; i++){
            monthComboBox.addItem(String.valueOf(i));
        }
        monthComboBox.setSelectedItem(String.valueOf(actualMonth));

        //Adds days to the combo box.
        updateDays(actualMonth, actualYear);

        //Listener for the years combo box.
        yearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDays(Integer.parseInt(monthComboBox.getSelectedItem().toString()),
                        Integer.parseInt(yearComboBox.getSelectedItem().toString()));
            }
        });

        //Listener for the months combo box.
        monthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDays(Integer.parseInt(monthComboBox.getSelectedItem().toString()),
                        Integer.parseInt(yearComboBox.getSelectedItem().toString()));
            }
        });

        //Listener for the search button.
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("\n\nFECHA SELECCIONADA: " +
                        yearComboBox.getSelectedItem().toString() + "/" +
                        monthComboBox.getSelectedItem().toString() + "/" +
                        dayComboBox.getSelectedItem().toString());
            }
        });
    }


    /**
     * Updates the daysComboBox with the number of days for the month and year recieved as parameters.
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
     * Returns the number of days of the month and year recieved as parameters.
     * @param month Integer
     * @param year Integer
     * @return Integer
     */
    public static int daysInMonth(int month, int year) {

        int numberOfDays = -1;
        switch (month) {
            case 1:
                numberOfDays = 31;
                break;
            case 3:
                numberOfDays = 31;
                break;
            case 5:
                numberOfDays = 31;
                break;
            case 7:
                numberOfDays = 31;
                break;
            case 8:
                numberOfDays = 31;
                break;
            case 10:
                numberOfDays = 31;
                break;
            case 12:
                numberOfDays = 31;
                break;
            case 4:
                numberOfDays = 30;
                break;
            case 6:
                numberOfDays = 30;
                break;
            case 9:
                numberOfDays = 30;
                break;
            case 11:
                numberOfDays = 30;
                break;
            case 2:
                if (isLeapYear(year)) {
                    numberOfDays = 29;
                } else {
                    numberOfDays = 28;
                }
                break;

        }

        return numberOfDays;
    }


    /**
     * Checks if a year is leap.
     * @param year Integer
     * @return Boolean
     */
    public static boolean isLeapYear(int year) {
        GregorianCalendar calendar = new GregorianCalendar();
        boolean isLeap = false;
        if (calendar.isLeapYear(year)) {
            isLeap = true;
        }
        return isLeap;

    }

}
