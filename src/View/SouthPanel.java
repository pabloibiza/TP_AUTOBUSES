/*
 * Bottom Panel of the View. Contains the buttons to assign and generate the route sheet.
 *
 * View.SouthPanel.java
 *
 * @version 2.0
 * @author Pablo Sanz Alguacil
 */

package View;

import Control.ViewListener;
import Internationalization.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SouthPanel extends JPanel {
    private MainFrame mainFrame;
    private ViewListener viewListener;
    private Location location;
    private JButton assignButton;
    private JButton deallocateButton;
    private JButton routeSheetButton;


    /**
     * Construtor method.
     * @param mainFrame MainFrame
     * @param viewListener ViewListaner
     * @param location Location
     */
    public SouthPanel(MainFrame mainFrame, ViewListener viewListener, Location location) {
        this.mainFrame = mainFrame;
        this.viewListener = viewListener;
        this.location = location;
        this.setLayout(new FlowLayout());
        buildPanel();
    }

    /**
     * Builds the panel.
     */
    private void buildPanel(){
        assignButton = new JButton(location.getLabel(location.ASSIGN));
        deallocateButton = new JButton(location.getLabel(location.DEALLOCATE));
        routeSheetButton = new JButton(location.getLabel(location.GENERATE_ROUTE_SHEET));
        this.add(assignButton);
        this.add(routeSheetButton);

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mainFrame.assignSeat();
                } catch (Exception exception) {
                    mainFrame.errorMessage(location.getLabel(location.SEAT_ASSIGN_ERROR), exception);
                }
            }
        });

        deallocateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(mainFrame,
                        location.getLabel(location.SURE_DEALLOCATE),
                        "",
                        JOptionPane.YES_NO_OPTION);
                if(option == 0) { // Selected: YES
                    try {
                        mainFrame.deallocateSeat();
                    } catch (Exception exception) {
                        mainFrame.errorMessage(location.getLabel(location.SEAT_DEALLOCATION_ERROR), exception);
                    }
                }
            }
        });

        routeSheetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    viewListener.producedEvent(ViewListener.Event.GENERATE_ROUTE_SHEET, mainFrame.getSelectedTravel());
                } catch (Exception exception) {
                    mainFrame.errorMessage(location.getLabel(location.ERROR_ROUTE_SHEET), exception);
                }
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
     * Replaces the assign button by the deallocate one.
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
