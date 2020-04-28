/*
 * Bottom Panel of the View. Contains the buttons to assign and generate the route sheet.
 *
 * View.SouthPanel.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package View;

import Control.ViewListener;
import Model.Passenger;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SouthPanel extends JPanel {
    private static final String ASSIGN = "Assign";
    private static final String UNASSIGN = "Unassign";
    private static final String GENERATE_ROUTE_SHEET = "Generate Route Sheet";

    private MainFrame mainFrame;
    private ViewListener viewListener;

    private JButton assignButton;
    private JButton unAssignButton;
    private JButton routeSheetButton;


    /**
     * Construtor method.
     * @param mainFrame MainFrame
     */
    public SouthPanel(MainFrame mainFrame, ViewListener viewListener) {
        this.mainFrame = mainFrame;
        this.viewListener = viewListener;
        buildPanel();
    }

    /**
     * Builds the panel.
     */
    private void buildPanel(){
        assignButton = new JButton(ASSIGN);
        unAssignButton = new JButton(UNASSIGN);
        routeSheetButton = new JButton(GENERATE_ROUTE_SHEET);
        this.add(assignButton);
        this.add(routeSheetButton);

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Implementar ventana para introducir datos del pasajero
                mainFrame.infoMessage("AQUI SE INTRODUCEN LOS DATOS DEL PASAJERO");
                Passenger newPassenger = null; //Aqui se reciven los datos de la ventana emergente
                viewListener.producedEvent(ViewListener.Event.NEW_PASSENGER, newPassenger);
                String assignationData[] = {mainFrame.getSelectedTravel(), newPassenger.getDni(), String.valueOf(mainFrame.getSelectedSeat().getSeatNumber())};
                viewListener.producedEvent(ViewListener.Event.ASSIGN, assignationData);

            }
        });

        unAssignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.infoMessage("ASIENTO DESASIGNADO");
                viewListener.producedEvent(ViewListener.Event.DELETE_PASSENGER, null);
                viewListener.producedEvent(ViewListener.Event.UNASSIGN, null);
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
     * Changes the state of the buttons depending on the received parameter.
     * @param option Boolean
     */
    public void enableDisableButtons(Boolean option){
        assignButton.setEnabled(option);
        routeSheetButton.setEnabled(option);
    }


    /**
     * Replaces the aasign button by the unasign one.
     */
    public void changeToUnassign(){
        this.removeAll();
        this.add(unAssignButton);
        this.add(routeSheetButton);
        this.revalidate();
        this.repaint();
    }


    /**
     * replaces the unassign button by the assign one;
     */
    public void changeToAssign(){
        this.removeAll();
        this.add(assignButton);
        this.add(routeSheetButton);
        this.revalidate();
        this.repaint();
    }


}
