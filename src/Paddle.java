/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;

public class Paddle extends GameObj {
	public static final int SIZE = 30;
	public static final int DEPTH = 3;
	public static final int INIT_X = 0;
	public static final int INIT_Y = 0;
	
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static final int INIT_VEL_Z = 0;
	
	private Color color;

	
    public Paddle(int z, int courtWidth, int courtHeight, int courtDepth, Map map, Color color){
        super(INIT_VEL_X, INIT_VEL_Y, INIT_VEL_Z, INIT_X, INIT_Y, z,
        		SIZE, SIZE, DEPTH, courtWidth, courtHeight, courtDepth, map);
        this.color = color;
    }
    

    @Override
    public void draw(Graphics g) {
        g.setColor(color); 
        g.fillRect(mapped_x, mapped_y, mapped_width, mapped_height);
    }

}
