package ballcourt;


/**
 * Class representing a ball in a BallCourt instance.
 *
 * @author Jan Peleska
 * @version 2020-01-25
 */

import java.awt.*;
import java.util.Random;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;


public class Ball implements Serializable
{

    /** Diameter of the ball */
    public final int BALL_DIAM = 20;

    /** Maximal value of speed */
    static final int MAX = 10;

    /** Position vector (x-ccordinate of upper left "courner" of the ball) */
    private int x;
    /** Position vector (y-ccordinate of upper left "courner" of the ball) */
    private int y;

    /** Velocity vector, x-component */
    private int dx;
    /** Velocity vector, y-component */
    private int dy;

    /** The BallCourt the ball is in */
    private BallCourt bc;

    /** 
     * Random number generator for adding changes
     *   to ball velocity and direction.
     */
    private Random r;

    /** Flag indicating that we hit the ball previously */
    private boolean previousHit;
    
     
/**
 * Constructor for a ball instance
 * @param bc Ball court the ball is residing in
 * @param xStart start position of the ball in the court: x-coordinate
 *               of upper left courner
 * @param yStart start position of the ball in the court: y-coordinate
 *               of upper left courner             
 */
    public Ball(BallCourt bc, int xStart, int yStart) {
        this.bc = bc;
        x = xStart;
        y = yStart;
        r = new Random();
    }

    /** Return x-position of ball's upper left corner */
    public int getPosX() {
        return x;   
    }
    
    /** Return y-position of ball's upper left corner */
    public int getPosY() {
        return y;   
    }

    /** Return x-ccordinate of ball's speed vector */
    public int getVelX() {
        return dx;   
    }

     /** Return y-ccordinate of ball's speed vector */
     public int getVelY() {
        return dy;   
    }

    /** 
     * Calculate the next position by moving the ball
     * in (dx,dy)-direction
     */
    public void move() {

        // If our speed is zero, set it to (1,1)
        if ( dx == 0 && dy == 0 ) {
            dx = 1;
            dy = 1;
        }

        // Move a (dx,dy) step
        x = x + dx; 
        y = y + dy; 

        // Clip new (x,y) coordinates, so that they do not
        // cross the court boundary
        if ( x > bc.RECT_WIDTH - BALL_DIAM ) {
            x = bc.RECT_WIDTH - BALL_DIAM;   
        }
        if (x < 0 ) {
            x = 0;   
        }
        if ( y > bc.RECT_HEIGHT - BALL_DIAM ) {
            y = bc.RECT_HEIGHT - BALL_DIAM;
        }
        if ( y < 0 ) {
            y = 0;   
        }

        // Get the player from the ball court
        Player player = bc.getPlayer();

        // Have we hit a player? Then bounce back 
        // with appropriate angle
        if ( player != null ) {
            if ( x <= player.getPosX() + player.WIDTH && 
            player.getPosX() <= x + BALL_DIAM &&
            y <= player.getPosY() + player.HEIGHT &&
            y + BALL_DIAM >= player.getPosY() ) {
                // Ignore if we have hit the player in the 
                // previous Delta t cycle: We might still overlap
                // with the player, then the direction should not be 
                // inverted again.
                if ( ! previousHit ) {
                    if ( Math.abs(dx) > 0 ) {
                        // Normal case: hit player from left or right
                        dx = -dx;
                    }
                    else {
                        // Special case: Hit player's head or butt
                        dy = -dy;
                    }
                    // player has hit the ball once more
                    player.incrementScore();
                    previousHit = true;
                }
                return;
            }
        }

        // Mark that we did not hit the ball just now
        previousHit = false;

        if ( x <= 0 || x >= bc.RECT_WIDTH - BALL_DIAM ) {
            dx = -dx;
            int sigdx = Integer.signum(dx);
            int deltax = r.nextInt() % 3;
            if ( Integer.signum(dx + deltax) == sigdx ) {
                dx = dx + deltax;   
                if ( Math.abs(dx) > MAX ) {
                    dx = Integer.signum(dx)*MAX;
                }
            }
        }

        if ( y <= 0 || y >= bc.RECT_HEIGHT - BALL_DIAM ) {
            dy = -dy;
            int sigdy = Integer.signum(dy);
            int deltay = r.nextInt() % 3;
            if ( Integer.signum(dy + deltay) == sigdy ) {
                dy = dy + deltay;  
                if ( Math.abs(dy) > MAX ) {
                    dy = Integer.signum(dy)*MAX;
                }
            }
        }

    }

    /**
     * Re-draw the ball at its current position
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(x+bc.RECT_X,y+bc.RECT_Y,BALL_DIAM,BALL_DIAM);   

    }
}
