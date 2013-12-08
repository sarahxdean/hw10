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
	public static final int INIT_VEL_Y = 1;

	public Circle(int x, int y, int z, int courtWidth, int courtHeight, int courtDepth) {
		super(INIT_VEL_X, INIT_VEL_Y, 2, x, y, z,
				SIZE, SIZE, SIZE, courtWidth, courtHeight, courtDepth);
	}

	public Direction hitObj(GameObj other) {
		if (other instanceof Brick){
			if (((Brick)other).isAlive){
				if (this.willIntersect(other)) {
					((Brick)other).isAlive = false;
					return super.hitObj(other);
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return super.hitObj(other);
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		//transform width and height to match depth
		int mapped_width;
		int mapped_height;
		mapped_width =(int)((float)width * (1.0 - ((pos_z / 2.0) / 100.0)));
		mapped_height = (int)((float)height * (1.0 - pos_z / 2.0 / 100.0));
		int mapped_x = pos_x + (width*pos_z)/400;
		int mapped_y = pos_y + (height*pos_z)/400;
		g.fillOval(mapped_x, mapped_y, mapped_width, mapped_height); 
	}



}