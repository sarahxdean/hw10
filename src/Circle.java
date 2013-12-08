/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;

/** A basic game object displayed as a yellow circle, starting in the 
 * upper left corner of the game court.
 *
 */
public class Circle extends GameObj {

	public static final int SIZE = 20;       
	public static final int INIT_POS_X = 170;  
	public static final int INIT_POS_Y = 170; 
	public static final int INIT_VEL_X = 1;
	public static final int INIT_VEL_Y = -1;

	public Circle(int x, int y, int z, int courtWidth, int courtHeight, int courtDepth) {
		super(INIT_VEL_X, INIT_VEL_Y, 2, x, y, z,
				SIZE, SIZE, SIZE, courtWidth, courtHeight, courtDepth);
	}

	
	@Override
	public void draw(Graphics g) {
		g.setColor(new Color(51, 0, 102));
		g.fillOval(mapped_x, mapped_y, mapped_width, mapped_height); 
	}



}