/*
 * Model.SalesDesk.java
 *
 * @version 4.4
 * @author Pablo Sanz Alguacil
 */

package Control;

public interface ViewListener {
    public enum Event { NEW_TRAVEL, NEW_PASSENGER, DELETE_PASSENGER, EXIT, SEARCH, VIEW_SEATS, ASSIGN,
        GENERATE_ROUTE_SHEET, DEALLOCATE
    }

    public void producedEvent(Event event, Object obj);


}
