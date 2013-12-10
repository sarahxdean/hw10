/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;

/** A basic game object displayed as a black square, starting in the 
 * upper left corner of the game court.
 *
 */
public class Paddle extends GameObj {
	public static final int SIZE = 30;
	public static final int DEPTH = 3;
	public static final int INIT_X = 0;
	public static final int INIT_Y = 0;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	
    public Paddle(int courtWidth, int courtHeight, int courtDepth, Map map){
        super(INIT_VEL_X, INIT_VEL_Y, 0, INIT_X, INIT_Y, 0,
        		SIZE, SIZE, DEPTH, courtWidth, courtHeight, courtDepth, map);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0,0, 200 )); //transparent
        g.fillRect(mapped_x, mapped_y, mapped_width, mapped_height);
    }

}
