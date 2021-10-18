package ballcourt;


/**
 * Class BallCourt implements the BallCourt consisting
 * of 1 player and one ball.
 *
 * @author Jan Peleska
 * @version 2020-01-25
 */

import java.awt.*;
import javax.swing.JPanel;
import java.util.Random;
import java.lang.String;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;


public class BallCourt extends JPanel implements Serializable {

    /** Upper left corner x-coordinate of painted rectangle denoting the ball court walls*/
    public final int RECT_X;
    /** Upper left corner y-coordinate of painted rectangle denoting the ball court walls*/
    public final int RECT_Y;
    /** Width of the rectangle */
    public int RECT_WIDTH;
    /** Height of the rectangle */
    public int RECT_HEIGHT;

    /** The ball in the court */
    private Ball ball;

    /** The player in the court */
    private Player player;

    /**
     * Constructor for a ball court
     * @param xOrigin  Upper left corner x-coordinate of 
     *                 painted rectangle denoting the ball court walls
     * @param yOrigin  Upper left corner y-coordinate of painted rectangle 
     *                 denoting the ball court walls
     * @param width    Width of the ball court rectangle  
     * @param height   Hight of the ball court rectangle
     */
    public BallCourt(final int xOrigin, final int yOrigin, final int width, final int height) {
        RECT_X = xOrigin;
        RECT_Y = yOrigin;
        RECT_WIDTH = width;
        RECT_HEIGHT = height;
    }

    /** Add a ball to the ball court 
     *  @param ball The ball to be added (only one ball is contained in the court)
     */
    public void addBall(Ball ball) {
        this.ball = ball;    
    }

    /**
     * Add a player to the ball court
     * @param player The player to be added (only one player in a court)
     */
    public void addPlayer(Player player) {
        this.player = player;
    }

    /** 
     * Get reference to the court's ball
     * @return Reference to the ball in the court or null if no
     *         ball has been registered via method addBall
     */
    public Ball getBall() {
        return ball;   
    }

    /** 
     * Get reference to the court's player
     * @return Reference to the player in the court or null if no
     *         player has been registered via method addPlayer
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * paint method called by the Swing repaint method.
     * The method performes the follwing actions:
     * <ul>
     * <li> re-draws the court's  rectangle
     * <li> update the player's score text line (if player exists)
     * <li> call the player's draw method (if player exists)
     * <li> call the ball's draw methd (if ball exists)
     * 
     * </ul>
     * 
     * Note that this method is never called explicitly by the applciation code.
     * @param g graphics object provided by the repaint method calling paint.
     */
    public void paint(Graphics g){
        g.drawRect(RECT_X,RECT_Y,RECT_WIDTH,RECT_HEIGHT);
        if ( player != null ) {
            String score = new String("Player score: ");
            score = score + player.getScore();
            g.drawString(score,RECT_X+10,RECT_Y+15);
            player.draw(g);
        }        

        if ( ball != null ) ball.draw(g);

    }

    /**
     * move method triggering the re-calculation of 
     * ball and player positions by calling their respective
     * move methods.
     */
    public void move(){
        if ( ball != null ) { ball.move(); }
        if ( player != null ) { player.move(); }
    }
}

