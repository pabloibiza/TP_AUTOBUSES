/*
 * Type class Test. It reads data from the saved files.
 *
 * Test.java
 *
 * @version 1.0
 * @author Pablo Sanz Alguacil */

public class TestReader {

    private static final String PASSENGERS_FILE_PATH = "src/storage/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/travels/status.csv";

    public static void main(String[] args) {
        Office office = new Office(PASSENGERS_FILE_PATH, TRAVELS_FILE_PATH, TRAVELS_STATUS_FILE_PATH);

        System.out.println(office.listPassengers());
        System.out.println(office.listTravels());

        //SE IMPRIME QUIEN ESTA SENTADO EN CIERTOS ASIENTOS
        System.out.println("~~ÃSIGNACION DE ASIENTOS VIAJE 1: ~~~");
        System.out.println("ASIENTO 5: " + office.whoIsSited(office.searchTravel("1"), 5));
        System.out.println("ASIENTO 6: " + office.whoIsSited(office.searchTravel("1"), 6));
        System.out.println("ASIENTO 30: " + office.whoIsSited(office.searchTravel("1"), 30));
        System.out.println("ASIENTO 42: " + office.whoIsSited(office.searchTravel("1"), 42));
    }
}
