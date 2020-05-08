/*
 * Central panel of the view. Contains the vehicle seats view.
 *
 * View.CentralPanel.java
 *
 * @version 4.4
 * @author Pablo Sanz Alguacil
 */

package View;
import Model.SalesDesk;
import Model.Travel;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CentralPanel extends JPanel {
    private static final String DISTRIBUTION_SEPARATOR = "x";
    private static final String BACKGROUND_IMAGE_PATH = "src/View/resources/bus.png";
    private static final int CORRIDOR_GAPS = 1;
    private static final int MINUM_SIZE_BACK_DOOR = 7;
    private static final int INFO_PANEL_WIDTH = 175;
    private static final int INFO_PANEL_HEIGHT = 400;
    private static final String BULLET_POINT = "·";
    private static final String HTML_SALT = "<p>";
    private static final String HTML_INDICATOR = "<html>";
    private static final String INFO_ELEMENTS_SEPARATOR = "-";

    private MainFrame mainFrame;
    private SalesDesk salesDesk;
    private Box boxes[][];
    private JPanel matrix;
    private JPanel infoPanel;
    private JLabel info;
    private BufferedImage image;
    private Location local;


    /**
     * Constructor method.
     * @param mainFrame MainFrame
     */
    public CentralPanel (MainFrame mainFrame, SalesDesk salesDesk, Location local) {
        this.mainFrame = mainFrame;
        this.salesDesk = salesDesk;
        this.local = local;
        this.setLayout(new BorderLayout(5,5));
        try {
            image = ImageIO.read(new File(BACKGROUND_IMAGE_PATH));
        } catch (IOException ex) {
            mainFrame.errorMessage(local.getLabel(local.ERROR_BACKGROUND_NOT_FOUND), ex);
        }

        matrix = new JPanel();
        matrix.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));

        infoPanel = new JPanel();
        info = new JLabel();
        infoPanel.add(info);
        infoPanel.setPreferredSize(new Dimension(INFO_PANEL_WIDTH,INFO_PANEL_HEIGHT));
        infoPanel.setBorder(BorderFactory.createTitledBorder(local.getLabel(local.INFO_PANEL_TITTLE)));

        this.add(matrix, BorderLayout.CENTER);
        this.add(infoPanel, BorderLayout.EAST);
    }


    /**
     * Updates the matrix for the received travel.
     * @param travel Travel
     */
    public void updateMatrix(Travel travel) {
        matrix.removeAll();
        int cols = Integer.parseInt(travel.getSeatsDistribution().split(DISTRIBUTION_SEPARATOR)[0]) + CORRIDOR_GAPS;
        int rows = Integer.parseInt(travel.getSeatsDistribution().split(DISTRIBUTION_SEPARATOR)[1]);
        matrix.setLayout(new GridLayout(rows, cols, 5, 5));
        buildMatrix(rows, cols, travel);
        setInfo(travel.getInfo());
        this.revalidate();
        this.repaint();
    }

    /**
     * Builds the matrix to represent the buses seats distribution.
     * @param travel Travel
     */
    private void buildMatrix(int rows, int cols, Travel travel) {
        int seatsIndex = 1;
        int corridorColumn = (((cols - CORRIDOR_GAPS) / 2) + ((cols - CORRIDOR_GAPS) % 2));
        boxes = new Box[rows][cols];

        for(int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (col == corridorColumn && row != (rows - 1)) { //Corridor
                    boxes[row][col] = new Box();
                } else if((row == (rows/2)) && (col > corridorColumn) && rows > MINUM_SIZE_BACK_DOOR) { //Back door
                    boxes[row][col] = new Box();
                } else { //Seats
                    boxes[row][col] = new Box(mainFrame, seatsIndex, salesDesk.searchPassenger(travel.whoIsSited(seatsIndex)));
                    seatsIndex++;
                }
                matrix.add(boxes[row][col]);
            }
        }
    }

    /**
     * Cleans the matrix.
     */
    public void cleanMatrix(){
        matrix.removeAll();
        this.revalidate();
        this.repaint();
    }


    /**
     * Sets the info JLabel text.
     * @param text
     */
    public void setInfo(String text){
        text = BULLET_POINT + text.replace(INFO_ELEMENTS_SEPARATOR, HTML_SALT + BULLET_POINT);
        info.setText(HTML_INDICATOR + text + HTML_INDICATOR);
    }

    /**
     * Paints the packground with and image.
     * @param g Graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 50, 0, 400,400, this);
        matrix.setOpaque(false);
    }

}
