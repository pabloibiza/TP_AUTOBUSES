/*
 * Central panel of the view. Contains the vehicle seats view.
 *
 * View.CentralPanel.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package View;
import Control.ViewListener;
import Model.Travel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CentralPanel extends JPanel {
    private static final String DISTRIBUTION_SEPARATOR = "x";
    private static final String ERROR_BACKGROUND_NOT_FOUND = "Background image not found";
    private static final int CORRIDOR_GAPS = 1;
    private static final int MINUM_SIZE_BACK_DOOR = 7;

    private ViewListener viewListener;
    private MainFrame mainFrame;
    private Box boxes[][];
    private JPanel matrix;
    private BufferedImage image;


    /**
     * Constructor method.
     * @param mainFrame MainFrame
     */
    public CentralPanel (MainFrame mainFrame) {
        this.viewListener = viewListener;
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout(5,5));
        matrix = new JPanel();
        this.add(matrix, BorderLayout.CENTER);
        matrix.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        try {
            image = ImageIO.read(new File("src/View/resources/bus.png"));
        } catch (IOException ex) {
            mainFrame.errorMessage(ERROR_BACKGROUND_NOT_FOUND);
        }
        this.add(new JButton("Solo estoy ocupando espacio"), BorderLayout.EAST);
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
                    boxes[row][col] = new Box(mainFrame, seatsIndex, row, col, travel.whoIsSited(seatsIndex));
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
    //@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 50, 0, 400,400,this); // see javadoc for more info on the parameters
        matrix.setOpaque(false);
    }
*/
}
