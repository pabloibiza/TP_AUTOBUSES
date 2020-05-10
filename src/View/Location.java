package View;

import javax.swing.*;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Location {
    private static Location location = null;
    private ResourceBundle resources;
    private static final String LANGUAGE_FILES_PATH = "Control/lang/";
    private static final String LANGUAGE_FILES_HEADER = "Labels_";

    public static final String ERROR_BACKGROUND_NOT_FOUND = "ERROR_BACKGROUND_NOT_FOUND";
    public static final String INFO_PANEL_TITTLE = "INFO_PANEL_TITTLE";
    public static final String WINDOW_TITLE = "WINDOW_TITLE";
    public static final String INFO_WINDOW_TITLE = "INFO_WINDOW_TITLE";
    public static final String ERROR_WINDOW_TITLE = "ERROR_WINDOW_TITLE";
    public static final String NO_TRAVELS = "NO_TRAVELS";
    public static final String TRAVEL_OUT_OF_DATE = "TRAVEL_OUT_OF_DATE";
    public static final String FILL_ALL_GAPS = "FILL_ALL_GAPS";
    public static final String INSTANCE_ALREADY_CREATED = "INSTANCE_ALREADY_CREATED";
    public static final String ROUTES = "ROUTES";
    public static final String ASSIGN = "ASSIGN";
    public static final String DEALLOCATE = "DEALLOCATE";
    public static final String GENERATE_ROUTE_SHEET = "GENERATE_ROUTE_SHEET";
    public static final String SEARCH_BUTTON = "SEARCH_BUTTON";
    public static final String DAY_LABEL = "DAY_LABEL";
    public static final String MONTH_LABEL = "MONTH_LABEL";
    public static final String YEAR_LABEL = "YEAR_LABEL";
    public static final String DATE_LABEL = "DATE_LABEL";
    public static final String[] MONTHS={"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    public static final String[] NEW_PASSENGER_QUESTIONS = {"DNI", "NAME", "SURNAME"};
    public static final String NEW_PASSENGER_WINDOW_TITTLE = "NEW_PASSENGER_WINDOW_TITTLE";
    public static final String SUCCESSFUL_ROUTE_GENERATED = "SUCCESSFUL_ROUTE_GENERATED";
    public static final String ERROR_SAVING_PASSENGERS = "ERROR_SAVING_PASSENGERS";
    public static final String ERROR_SAVING_TRAVELS_STATUS = "ERROR_SAVING_TRAVELS_STATUS";
    public static final String PASSENGER_NOT_EXISTANT = "PASSENGER_NOT_EXISTANT";
    public static final String ORIGIN = "ORIGIN";
    public static final String DESTINY = "DESTINY";
    public static final String DATE = "DATE";
    public static final String SEATS_PLAN = "SEATS_PLAN";
    public static final String SEAT = "SEAT";
    public static final String SHEET_NAME_TEXT = "SHEET_NAME_TEXT";
    public static final String SEAT_ASSIGN_ERROR = "SEAT_ASSIGN_ERROR";
    public static final String SEAT_ASSIGN_SUCCESS = "SEAT_ASSIGN_SUCCESS";
    public static final String SURE_DEALLOCATE = "SURE_DEALLOCATE";
    public static final String SEAT_DEALLOCATION_SUCCESS = "SEAT_DEALLOCATION_SUCCESS";
    public static final String SEAT_DEALLOCATION_ERROR = "SEAT_DEALLOCATION_ERROR";
    public static final String ERROR_READING_PASSENGERS = "ERROR_READING_PASSENGERS";
    public static final String ERROR_READING_TRAVELS = "ERROR_READING_TRAVELS";
    public static final String ERROR_READING_STATUS = "ERROR_READING_STATUS";

    String[] systemLabels = {
            "OptionPane.cancelButtonText",
            "OptionPane.okButtonText",
            "OptionPane.noButtonText",
            "OptionPane.yesButtonText",
            "OptionPane.yesButtonText",
            "FileChooser.openDialogTitleText",
            "FileChooser.saveDialogTitleText",
            "FileChooser.lookInLabelText",
            "FileChooser.saveInLabelText",
            "FileChooser.openButtonText",
            "FileChooser.saveButtonText",
            "FileChooser.cancelButtonText",
            "FileChooser.fileNameLabelText",
            "FileChooser.filesOfTypeLabelText",
            "FileChooser.openButtonToolTipText",
            "FileChooser.cancelButtonToolTipText",
            "FileChooser.fileNameHeaderText",
            "FileChooser.upFolderToolTipText",
            "FileChooser.homeFolderToolTipText",
            "FileChooser.newFolderToolTipText",
            "FileChooser.listViewButtonToolTipText",
            "FileChooser.newFolderButtonText",
            "FileChooser.renameFileButtonText",
            "FileChooser.deleteFileButtonText",
            "FileChooser.filterLabelText",
            "FileChooser.detailsViewButtonToolTipText",
            "FileChooser.fileSizeHeaderText",
            "FileChooser.fileDateHeaderText",
            "FileChooser.acceptAllFileFilterText" };


    /**
     * Constructor method. If it can' load a locale with the received parameters, it will load the default one.
     * @param language String
     * @param country String
     */
    private Location(String language, String country) throws MissingResourceException{
        try {
            Locale locale = new Locale(language, country);
            resources = ResourceBundle.getBundle(LANGUAGE_FILES_PATH + LANGUAGE_FILES_HEADER + locale);

            if (!locale.equals(Locale.getDefault())) {
                for (int i = 0; i < systemLabels.length; i++) {
                    UIManager.put(systemLabels[i], resources.getString(systemLabels[i]));
                }
            }
       } catch (MissingResourceException e) {
            Locale locale = new Locale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
            resources = ResourceBundle.getBundle(LANGUAGE_FILES_PATH + LANGUAGE_FILES_HEADER + locale);
        }
    }

    /**
     * Creates a singleton instance.
     * @param language String
     * @param country String
     * @return Location
     */
    public static synchronized Location getSingletonInstance(String language, String country) {
        if (location == null) {
            location = new Location(language, country);
        }
        return location;
    }


    /**
     * Avoids cloning this object.
     * @return Object
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


    /**
     * Returns the text for the received label. If there is no key for the received label, the default language label
     * will be returned.
     * @param label String
     * @return String
     */
    public String getLabel(String label) {
        String text = "";
        try {
            text = resources.getString(label);
        } catch (MissingResourceException e){
            text = ResourceBundle.getBundle( LANGUAGE_FILES_PATH + LANGUAGE_FILES_HEADER).getString(label);
        }
        return text;
    }

}
