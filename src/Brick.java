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
public class Brick extends GameObj {
	public static final int SIZE = 77;
	public static final int DEPTH = 10;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static final int INIT_VEL_Z = 0;
	
	public static final int INIT_Z = 100;
	public boolean isAlive;
	
	
    public Brick(int x, int y, int z, int courtWidth, int courtHeight, int courtDepth, Map map){
        super(INIT_VEL_X, INIT_VEL_Y, INIT_VEL_Z, x, y, INIT_Z + z,
        		SIZE, SIZE, DEPTH, courtWidth, courtHeight, courtDepth, map);
        isAlive = true;
    }

    @Override
    public void draw(Graphics g) {
    	if(isAlive){
    		g.setColor(new Color(72,61,139, 100)); //transparent
    		g.fillRect(mapped_x, mapped_y, mapped_width, mapped_height); 
    	}
    }

}
