package main;

import ballcourt.*;
import gui.*;

public class Main {

    /**
     * The main method is called without parameters
     * and sets up the graphical user interface.
     * After that, a non-terminating update loop
     * is entered which triggers updates to te GUI state
     * every 10ms.
     */
    public static void main(String[] args) {

        BallCourtControl gui = new BallCourtControl();
        gui.setup();

        while (true) {

            gui.update();

            try {
                Thread.sleep(10);
            }
            catch(Exception e){}
        }

    }

}
