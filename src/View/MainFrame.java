/*
 * Main frame of the view.
 *
 * View.MainFrame.java
 *
 * @version 2.1
 * @author Pablo Sanz Alguacil
 */

package View;

import Control.OyenteVista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame{
    private static final String WINDOW_TITLE = "BUS OFFICE";

    private OyenteVista oyenteVista;
    private TopPanel topPanel;

    public MainFrame(OyenteVista oyenteVista){
        super();
        this.oyenteVista = oyenteVista;
        configureWindow();
        topPanel = new TopPanel();
        this.add(topPanel);

        setVisible(true);

    }

    /**
     * Sets the window paremaneters.
     */
    private void configureWindow() {
        this.setTitle(WINDOW_TITLE);
        this.setSize(900, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(1,2));
        this.setResizable(false);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                oyenteVista.producedEvent(OyenteVista.Event.EXIT, null);
            }
        });

    }


    /**
     * Notifies an event to the Control module
     * @param event Event
     * @param obj Object
     */
    public void notification(OyenteVista.Event event, Object obj) {
        oyenteVista.producedEvent(event, obj);
    }
}
