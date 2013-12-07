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
public class Square extends GameObj {
	public static final int SIZE = 30;
	public static final int INIT_X = 0;
	public static final int INIT_Y = 0;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	
    /** 
     * Note that because we don't do anything special
     * when constructing a Square, we simply use the
     * superclass constructor called with the correct parameters 
     */
    public Square(int courtWidth, int courtHeight, int courtDepth){
        super(INIT_VEL_X, INIT_VEL_Y, 0, INIT_X, INIT_Y, 5,
        		SIZE, SIZE, courtWidth, courtHeight, courtDepth);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0,0, 100 ));
        int mapped_width;
		int mapped_height;
		mapped_width =(int)((float)width * (1.0 - ((pos_z / 2.0) / 100.0)));
		mapped_height = (int)((float)height * (1.0 - pos_z / 2.0 / 100.0));
        g.fillRect(pos_x, pos_y, mapped_width, mapped_height); 
    }

}
