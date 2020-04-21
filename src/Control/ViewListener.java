/*
 * Model.SalesDesk.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package Control;

public interface ViewListener {
    public enum Event { NEW_TRAVEL, DELETE_TRAVEL, NEW_PASSENGER, DELETE_PASSENGER, EXIT, SEARCH, VIEW_SEATS, ASSIGN,
        GENERATE_ROUTE_SHEET}

    public void producedEvent(Event event, Object obj);


}
