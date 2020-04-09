package Control;

public interface OyenteVista {
    public enum Event { NEW_TRAVEL, DELETE_TRAVEL, NEW_PASSENGER, DELETE_PASSENGER, EXIT }

    public void producedEvent(Event evento, Object obj);


}
