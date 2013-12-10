/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;

public class Ball extends GameObj {

	public static final int INIT_POS_Z = 0;
	public static final int INIT_VEL_X = 1;
	public static final int INIT_VEL_Y = -1;
	public static final int INIT_VEL_Z = 2;
	private Color color;

	public Ball(int size, int courtWidth, int courtHeight, int courtDepth, 
			Map map, Color color) {
		//random position
		super(INIT_VEL_X, INIT_VEL_Y, INIT_VEL_Z, (int)(Math.random() * 
				(courtWidth-20)+10), (int)(Math.random() * (courtWidth-20)+10),
				INIT_POS_Z, size, size, size, courtWidth, courtHeight, 
				courtDepth, map);
		this.color = color;
	}

	//sphere-rectangle intersection override
	public boolean intersects(GameObj obj){
		int xc = pos_x + width/2;
		int yc = pos_y + height/2;
		int zc = pos_z + depth/2;
		return (xc >= obj.pos_x - width/2
			    && xc <= obj.pos_x + obj.width + width/2
			    && yc >= obj.pos_y - height/2
				&& yc <= obj.pos_y + obj.height + height/2    
				&& zc >= obj.pos_z - depth/2 
				&& zc <= obj.pos_z + obj.depth + depth/2);
	}
	
	public boolean willIntersect(GameObj obj){
		int next_xc = pos_x + v_x + width/2;
		int next_yc = pos_y + v_y + height/2;
		int next_zc = pos_z + v_z + depth/2;
		int next_obj_x = obj.pos_x + obj.v_x;
		int next_obj_y = obj.pos_y + obj.v_y;
		int next_obj_z = obj.pos_z + obj.v_z;
		return (next_xc >= next_obj_x - width/2
			    && next_xc <= next_obj_x + obj.width + width/2
			    &&next_yc >= next_obj_y - height/2
				&& next_yc <= next_obj_y + obj.height + height/2
				&& next_zc >= next_obj_z - depth/2 
				&& next_zc <= next_obj_z + obj.depth + depth/2);

	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(mapped_x, mapped_y, mapped_width, mapped_height); 
	}



}