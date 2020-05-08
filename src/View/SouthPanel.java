/*
 * Bottom Panel of the View. Contains the buttons to assign and generate the route sheet.
 *
 * View.SouthPanel.java
 *
 * @version 4.4
 * @author Pablo Sanz Alguacil
 */

package View;

import Control.ViewListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SouthPanel extends JPanel {
    private MainFrame mainFrame;
    private ViewListener viewListener;
    private Location local;
    private JButton assignButton;
    private JButton deallocateButton;
    private JButton routeSheetButton;


    /**
     * Construtor method.
     * @param mainFrame MainFrame
     */
    public SouthPanel(MainFrame mainFrame, ViewListener viewListener, Location local) {
        this.mainFrame = mainFrame;
        this.viewListener = viewListener;
        this.local = local;
        this.setLayout(new FlowLayout());
        buildPanel();
    }

    /**
     * Builds the panel.
     */
    private void buildPanel(){
        assignButton = new JButton(local.getLabel(local.ASSIGN));
        deallocateButton = new JButton(local.getLabel(local.DEALLOCATE));
        routeSheetButton = new JButton(local.getLabel(local.GENERATE_ROUTE_SHEET));
        this.add(assignButton);
        this.add(routeSheetButton);

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.assignSeat();
            }
        });

        deallocateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.deallocateSeat();
            }
        });

        routeSheetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewListener.producedEvent(ViewListener.Event.GENERATE_ROUTE_SHEET, mainFrame.getSelectedTravel());
            }
        });
    }


    /**
     * Changes the state of the assign button.
     * @param option Boolean
     */
    public void stateAssignDeallocateButtons(Boolean option){
        assignButton.setEnabled(option);
        deallocateButton.setEnabled(option);
    }


    /**
     * Changes the sate of the generate route sheet button.
     * @param option boolean
     */
    public void stateRouteSheetButton(Boolean option){
        routeSheetButton.setEnabled(option);
    }


    /**
     * Replaces the aasign button by the unasign one.
     */
    public void changeToDeallocate(){
        this.removeAll();
        this.add(deallocateButton);
        this.add(routeSheetButton);
        this.revalidate();
        this.repaint();
    }


    /**
     * replaces the deallocate button by the assign one;
     */
    public void changeToAssign(){
        this.removeAll();
        this.add(assignButton);
        this.add(routeSheetButton);
        this.revalidate();
        this.repaint();
    }


}
