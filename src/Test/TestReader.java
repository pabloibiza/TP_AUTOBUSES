/*
 * Type class Test.TestReader. It reads data from the saved files.
 *
 * Test.TestReader.java
 *
 * @version 1.0
 * @author Pablo Sanz Alguacil
 */

package Test;

import Model.SalesDesk;

public class TestReader {

    private static final String PASSENGERS_FILE_PATH = "src/storage/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/travels/status.csv";

    public static void main(String[] args) {
        SalesDesk salesDesk = new SalesDesk(PASSENGERS_FILE_PATH, TRAVELS_FILE_PATH, TRAVELS_STATUS_FILE_PATH);

        System.out.println(salesDesk.listPassengers());
        System.out.println(salesDesk.listTravels());

        //SE IMPRIME QUIEN ESTA SENTADO EN CIERTOS ASIENTOS
        System.out.println("~~ÃSIGNACION DE ASIENTOS VIAJE 1: ~~~");
        System.out.println("ASIENTO 5: " + salesDesk.whoIsSited(salesDesk.searchTravel("1"), 5));
        System.out.println("ASIENTO 6: " + salesDesk.whoIsSited(salesDesk.searchTravel("1"), 6));
        System.out.println("ASIENTO 30: " + salesDesk.whoIsSited(salesDesk.searchTravel("1"), 30));
        System.out.println("ASIENTO 42: " + salesDesk.whoIsSited(salesDesk.searchTravel("1"), 42));
    }
}
