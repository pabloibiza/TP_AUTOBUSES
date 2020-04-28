/*
 * Type class Test.TestReader. It reads data from the saved files.
 *
 * Test.TestReader.java
 *
 * @version 1.0
 * @author Pablo Sanz Alguacil
 */

package Test;

import Control.Office;
import Model.SalesDesk;

public class TestReader {

    private static final String PASSENGERS_FILE_PATH = "src/storage/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/travels/status.csv";

    public static void main(String[] args) {
        SalesDesk salesDesk = new SalesDesk(PASSENGERS_FILE_PATH, TRAVELS_FILE_PATH, TRAVELS_STATUS_FILE_PATH);
        Office office = new Office(salesDesk);
    }
}
