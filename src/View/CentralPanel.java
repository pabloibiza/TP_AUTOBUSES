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
    private static final int ROW_HEIGHT = 120;
    private static final int COL_HEIGHT = 120;

    private ViewListener viewListener;
    private Box boxes[][];
    private JLabel label;


    /**
     * Constructor method.
     * @param viewListener ViewListener
     */
    public CentralPanel (ViewListener viewListener) {
        this.viewListener = viewListener;
        label = new JLabel(SELECT_A_TRAVEL);
        this.add(label);
        this.setBorder(BorderFactory.createCompoundBorder());

    }

    public void updateMatrix(Travel travel) {
        this.removeAll();
        int cols = Integer.parseInt(travel.getSeatsDistribution().split(DISTRIBUTION_SEPARATOR)[0]);
        int rows = Integer.parseInt(travel.getSeatsDistribution().split(DISTRIBUTION_SEPARATOR)[1]);
        this.setLayout(new GridLayout(rows, cols));
        buildMatrix(rows, cols, travel);
        this.setPreferredSize(new Dimension(rows * ROW_HEIGHT, cols * COL_HEIGHT));
        this.revalidate();
        this.repaint();
    }

    /**
     * Builds the matrix to represent the buses seats distribution.
     * @param travel Travel
     */
    private void buildMatrix(int rows, int cols, Travel travel) {
        int seatsIndex = 1;
        int lastCorridorGap = 0;
        int leftSeatsPerRow = ((cols / 2) + (cols % 2));
        int rightSeatsPerRow = (cols / 2);
        int corridorGapJumps = (leftSeatsPerRow + rightSeatsPerRow + 1);
        boxes = new Box[rows][cols];

        for(int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if ((seatsIndex == leftSeatsPerRow + 1) || (seatsIndex == (lastCorridorGap + corridorGapJumps))){
                    boxes[row][col] = new Box();
                    lastCorridorGap = seatsIndex;
                } else {
                    if(!travel.isSeatFree(seatsIndex)){
                        boxes[row][col] = new Box(seatsIndex, row, col, travel.whoIsSited(seatsIndex));
                        this.add(boxes[row][col]);
                    } else {
                        boxes[row][col] = new Box(seatsIndex, row, col, null);
                    }
                    this.add(boxes[row][col]);
                    seatsIndex++;
                }
            }
        }
    }

}
