package Control;

import Model.Passenger;
import Model.SalesDesk;
import Model.Travel;
import View.MainFrame;

import java.io.IOException;

public class Office implements OyenteVista{
    private SalesDesk salesDesk;
    private MainFrame mainFrame;
    private static final String PASSENGERS_FILE_PATH = "src/storage/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/travels/status.csv";

    public Office(SalesDesk salesDesk){
        this.salesDesk = salesDesk;
        mainFrame = new MainFrame(this);
    }

    private void exit() {
        try {
            salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        } catch (IOException e) {
            errorMessage("Error while saving changes to passengers file", e);
        }

        try {
            salesDesk.saveTravels(TRAVELS_FILE_PATH);
        } catch (IOException e) {
            errorMessage("Error while saving changes to travels file", e);
        }

        try {
            salesDesk.saveTravelsStatus(TRAVELS_STATUS_FILE_PATH);
        } catch (IOException e) {
            errorMessage("Error while saving changes to travels status file", e);
        }

        System.exit(0);
    }

    private void errorMessage(String mensaje, Exception e) {
        //vista.mensajeDialogo(mensaje);
    }

    private void newTravel(Travel travel){
        salesDesk.addTravel(travel);
        try {
            salesDesk.saveTravels(TRAVELS_FILE_PATH);
        } catch (IOException e) {
            errorMessage("Error while saving changes to travels file", e);
        }
    }

    private void deleteTravel(Travel travel){
        salesDesk.deleteTravel(travel);
        try {
            salesDesk.saveTravels(TRAVELS_FILE_PATH);
        } catch (IOException e) {
            errorMessage("Error while saving changes to travels file", e);
        }
    }

    private void newPassenger(Passenger passenger){
        salesDesk.addPassenger(passenger);
        try {
            salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        } catch (IOException e) {
            errorMessage("Error while saving changes to passengers file", e);
        }
    }

    private void deletePassenger(Passenger passenger){
        salesDesk.deletePassenger(passenger);
        try {
            salesDesk.savePassengers(PASSENGERS_FILE_PATH);
        } catch (IOException e) {
            errorMessage("Error while saving changes to passengers file", e);
        }
    }

    @Override
    public void producedEvent(Event evento, Object object) {
        switch(evento) {
            case NEW_TRAVEL:
                newTravel((Travel) object);
                break;

            case DELETE_TRAVEL:
                deleteTravel((Travel) object);
                break;

            case NEW_PASSENGER:
                newPassenger((Passenger) object);
                break;

            case DELETE_PASSENGER:
                deletePassenger((Passenger) object);
                break;

            case EXIT:
                exit();
                break;
        }
    }
}
