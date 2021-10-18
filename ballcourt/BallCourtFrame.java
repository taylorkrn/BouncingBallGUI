package ballcourt;


/**
 * Class representing BallCourt window frames.
 * A window is realised as a JFrame instance. Therfore,
 * BallCourtFrame is a sub-class of JFrame.
 *
 * @author Jan Peleska
 * @version 2020-01-25
 */

import java.awt.*;
import javax.swing.*;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class BallCourtFrame extends JFrame implements Serializable
{

    /** The ball court located in this window */
    private BallCourt bc;

    /**
     * Constructor for a BallCourtFrame
     * @param bc reference to the ball court residing in the window
     * @param xOrigin The x-coordinate of the upper left courner of the window on the display
     * @param yOrigin The y-coordinate of the upper left courner of the window on the display
     * @param width The width of the window
     * @param height The height of the window
     */
    public BallCourtFrame(BallCourt bc, int xOrigin, int yOrigin, int width, int height) {
        super();
        this.bc = bc;
        setResizable(false);
        setSize(width,height);
        setLocation(xOrigin,yOrigin);
        setBackground(Color.WHITE);
        add(bc);
        setVisible(true);
    }

    /**
     * The run-method updates the positions of ball and player
     * inside the ball court and triggers graphics updates by calling the
     * repaint method of JFrame.
     */
    public void run(){
        bc.move();
        repaint();
    }
    
}