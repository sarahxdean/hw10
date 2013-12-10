/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;

public class BouncingBall extends Ball {

	public static final int SIZE = 20;
	public static final int INIT_POS_Z = 0;
	public static final int INIT_VEL_X = 1;
	public static final int INIT_VEL_Y = -1;
	public static final int INIT_VEL_Z = 2;

	public BouncingBall(int courtWidth, int courtHeight, int courtDepth, 
			Map map, Color color) {
		super(SIZE, courtWidth, courtHeight, 
				courtDepth, map, color);
	}

	// override
		public Direction hitWall() {
			if (map.point(pos_x + v_x, pos_z) < map.point(0, pos_z))
				return Direction.HORIZONTAL;
			else if (map.point(pos_x + v_x, pos_z) > map.point(max_x, pos_z))
				return Direction.HORIZONTAL;
			if (map.point(pos_y + v_y, pos_z) < map.point(0, pos_z))
				return Direction.VERTICAL;
			else if (map.point(pos_y + v_y, pos_z) > map.point(max_y, pos_z))
				return Direction.VERTICAL;
			else if (pos_z + v_z < -4) {
				// random position after out of bounds
				pos_x = (int)(Math.random() * (max_x - 10 - 10) + 10);
				pos_y = (int)(Math.random() * (max_y - 10 - 10) + 10);
				return Direction.DEPTH;
				}
			else if (pos_z + v_z > max_z) {
				Point currPos = new Point (pos_x, pos_y);
				if (currPos.equals(backwall_bounce)){
					hit_count ++;	
				} else {
					hit_count = 0;
				}
				backwall_bounce = currPos;
				return Direction.DEPTH;
			}
			else return null;
		}



}