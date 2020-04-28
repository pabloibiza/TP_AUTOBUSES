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

import javax.swing.*;
import java.awt.*;

public class CentralPanel extends JPanel {
    private static final String DISTRIBUTION_SEPARATOR = "x";
    private static final String SELECT_A_TRAVEL = "Select a travel please";
    private static final String TO = "to";
    private static final int CORRIDOR_GAPS = 1;

    private ViewListener viewListener;
    private MainFrame mainFrame;
    private Box boxes[][];
    private JLabel travelLabel;
    private JPanel matrix;


    /**
     * Constructor method.
     * @param viewListener ViewListener
     */
    public CentralPanel (MainFrame mainFrame, ViewListener viewListener) {
        this.viewListener = viewListener;
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout(5,5));
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        matrix = new JPanel();
        this.add(matrix, BorderLayout.CENTER);
        travelLabel = new JLabel(SELECT_A_TRAVEL);
        matrix.add(travelLabel);
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
                } else if((row == (rows/2)) && (col > corridorColumn)) { //Back door
                    boxes[row][col] = new Box();
                } else {
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
        matrix.add(travelLabel);
        this.revalidate();
        this.repaint();
    }

}
