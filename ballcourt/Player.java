package ballcourt;

/**
 * Class representing a player in a ball court.
 * A player is graphically represented in a ball court 
 * by a rectangle. Players only move in y-direction, never in x-direction
 *
 * @author Jan Peleska 
 * @version 2020-01-25
 */

import java.awt.*;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Player implements Serializable
{

    /** Height of the player rectangle */
    public final int HEIGHT = 40;
    /** Width of the player reactangle */
    public final int WIDTH = 6;

    /** Position vector (x-coordinate of upper left courner of the box) */
    private int x;
    /** Position vector (y-coordinate of upper left courner of the box) */
    private int y;

    /** Velocity vector - x coordinate */
    private int dx;
    /** Velocity vector - y coordinate */
    private int dy;

    /** Maximal speed in y-direction */
    private int maxVelY;

    /** The BallCourt the player is in */
    private BallCourt bc;

    /** Score: How many times did the player hit the ball? */
    private int score;

    /**
     * Constructor for a player
     * @param bc reference to the ball court the player is in
     * @param xStart Position vector (x-coordinate of upper left courner of the box)
     * @param yStart Position vector (y-coordinate of upper left courner of the box)
     * @param maxVelY Maximal speed in y-direction
     */
    public Player(BallCourt bc, int xStart, int yStart, int maxVelY) {
        x = xStart;
        y = yStart;
        this.bc = bc;
        dx = 0; // Never move in x-direction
        dy = 0;
        this.maxVelY = maxVelY;
    }

    /** Return x-position of player (upper left corner, x-coordinate) */
    public int getPosX() {
        return x;   
    }

    /** Return y-position of player (upper left corner, y-coordinate) */
    public int getPosY() {
        return y;      
    }

    /** 
     * Return the score from the player for display 
     * This method is called by the associated ball court 
     * object when updating the score text line.
     * @return Current score
     */
    public int getScore() {
        return score;   
    }

    /** 
     * Increment the player score.
     * This method is called by the ball object every time
     *  the player hits the ball.
     */
    public void incrementScore() {
        score++;   
    }

    /** Calculat the next Delta-t step for the player*/
    public void move() {

        // Where are we now according to the last position and velocity?
        // (x remains unchanged)
        y = y + dy;

        // Respect the boundaries of the ball court
        if ( y < 0 ) {
            y = 0;
            dy = -dy;
        }

        if ( y + HEIGHT > bc.RECT_HEIGHT ) {
            y = y - (y + HEIGHT - bc.RECT_HEIGHT);
            dy = -dy;
        }

        // Get reference to ball from the vall court
        Ball b = bc.getBall();

        // Transform my position to centre coordinates
        int px = x + (WIDTH >> 1);
        int py = y + (HEIGHT >> 1);

        // Get upper left courner of ball position, transform to centre coordinates
        int bx = b.getPosX() + (b.BALL_DIAM >> 1);
        int by = b.getPosY() + (b.BALL_DIAM >> 1);

        // Get ball speed vector
        int bvx = b.getVelX();
        int bvy = b.getVelY();

        // Calculate a speed vector along the y-axis where we
        // can meet the ball - if this vector exists
        dx = 0;
        dy = 0;

        // If bvx = 0, the player cannot influence whether it
        // can hit the ball or not, sinc the player
        // cannot move in x-direction. Therefore,
        // the player just stops in his/her tracks
        if ( bvx == 0 ) return;

        int t = (px - bx)/bvx;

        // If t <= 0 we cannot reach the ball, or the ball is 
        // already where we are. The player doesn't move in this situation
        if ( t <= 0 ) return;

        // Now we can calculate the required dy for the player to reach the ball
        dy = ((by - py)*bvx + (px - bx)*bvy)/(px - bx);

        if ( Math.abs(dy) > maxVelY ) dy = Integer.signum(dy) * maxVelY/2;

    }
    
    /**
     * Re-draw the player in its updated position.
     * @param g Graphics object provided by the caller 
     */
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x+bc.RECT_X,y+bc.RECT_Y,WIDTH,HEIGHT);
    }

}
