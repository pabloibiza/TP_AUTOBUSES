/*
 * Model.SalesDesk.java
 *
 * @version 2.0
 * @author Pablo Sanz Alguacil
 */

package Control;

public interface ViewListener {
    public enum Event {NEW_PASSENGER, DELETE_PASSENGER, EXIT, ASSIGN, GENERATE_ROUTE_SHEET, DEALLOCATE}

    public void producedEvent(Event event, Object obj) throws Exception;


}
