package Notes;

import javax.swing.*;
import java.io.IOException;

public class LookAndFeel {
    private static final String PASSENGERS_FILE_PATH = "src/storage/passengers.csv";
    private static final String TRAVELS_FILE_PATH = "src/storage/travels.csv";
    private static final String TRAVELS_STATUS_FILE_PATH = "src/storage/travels/status.csv";

    public static void main(String[] args) throws IOException {
        UIManager.LookAndFeelInfo info[] = UIManager.getInstalledLookAndFeels();
        for(UIManager.LookAndFeelInfo look: info)
            System.out.println(look.getClassName());
    }
}
