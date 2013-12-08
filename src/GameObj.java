/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;


/** An object in the game. 
 *
 *  Game objects exist in the game court. They have a position, 
 *  velocity, size and bounds. Their velocity controls how they 
 *  move; their position should always be within their bounds.
 */
public class GameObj {

	/** Current position of the object (in terms of graphics coordinates)
	 *  
	 * Coordinates are given by the upper-left hand corner of the object.
	 * This position should always be within bounds.
	 *  0 <= pos_x <= max_x 
	 *  0 <= pos_y <= max_y 
	 */
	public int pos_x; 
	public int pos_y;
	public int pos_z;
	Point backwall_bounce = new Point(0,0);
	int hit_count = 0;
	
	int mapped_x;
	int mapped_y;

	public int mapped_width;
	public int mapped_height;
	
	/** Size of object, in pixels */
	public int width;
	public int height;
	public int depth;
	
	/** Velocity: number of pixels to move every time move() is called */
	public int v_x;
	public int v_y;
	public int v_z;

	/** Upper bounds of the area in which the object can be positioned.  
	 *    Maximum permissible x, y positions for the upper-left 
	 *    hand corner of the object
	 */
	public int max_x;
	public int max_y;
	public int max_z;

	/**
	 * Constructor
	 */
	public GameObj(int v_x, int v_y, int v_z, int pos_x, int pos_y, int pos_z, 
		int width, int height, int depth, int court_width, int court_height, int court_depth){
		this.v_x = v_x;
		this.v_y = v_y;
		this.v_z = v_z;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.pos_z = pos_z;
		this.width = width;
		this.height = height;
		this.depth = depth;

		updateMap();
		
		// take the width and height into account when setting the 
		// bounds for the upper left corner of the object.
		this.max_x = court_width - width;
		this.max_y = court_height - height;
		this.max_z = court_depth;

	}


	/**
	 * Moves the object by its velocity.  Ensures that the object does
	 * not go outside its bounds by clipping.
	 */
	public void move(){
		pos_x += v_x;
		pos_y += v_y;
		pos_z += v_z;
		//v_z += -.5;
		
		updateMap();

		clip();
	}
	
	public int map(int x){
		//Map3d.dimesion(x, pos_z);
		return (int)(x/(1+pos_z/200.0));
	}
	
	public void updateMap(){
		//int z = pos_z;
		//if (pos_z == 0) z=1;
		mapped_width = map(width); //(int)((float)width * (1.0 - ((pos_z / 2.0) / 100.0)));
		mapped_height = map(height);//(int)((float)height * (1.0 - pos_z / 2.0 / 100.0));

		mapped_x = 175 + map(pos_x - 175);
	    mapped_y = 175 + map(pos_y - 175);
	}

	/**
	 * Prevents the object from going outside of the bounds of the area 
	 * designated for the object. (i.e. Object cannot go outside of the 
	 * active area the user defines for it).
	 */ 
	public void clip(){
		if (pos_x < 0) pos_x = 0;
		else if (pos_x > max_x) pos_x = max_x;

		if (pos_y < 0) pos_y = 0;
		else if (pos_y > max_y) pos_y = max_y;
		
		if (pos_z < -6) pos_z = -6;
		else if (pos_z > max_z) pos_z = max_z;
	}

	/**
	 * Determine whether this game object is currently intersecting
	 * another object.
	 * 
	 * Intersection is determined by comparing bounding boxes. If the 
	 * bounding boxes overlap, then an intersection is considered to occur.
	 * 
	 * @param obj : other object
	 * @return whether this object intersects the other object.
	 */
	public boolean intersects(GameObj obj){
		return (mapped_x + mapped_width >= obj.mapped_x
				&& mapped_y + mapped_height >= obj.mapped_y
				&& obj.mapped_x + obj.mapped_width >= pos_x 
				&& obj.mapped_y + obj.mapped_height >= mapped_y
				&& obj.pos_z == pos_z);
	}

	
	/**
	 * Determine whether this game object will intersect another in the
	 * next time step, assuming that both objects continue with their 
	 * current velocity.
	 * 
	 * Intersection is determined by comparing bounding boxes. If the 
	 * bounding boxes (for the next time step) overlap, then an 
	 * intersection is considered to occur.
	 * 
	 * @param obj : other object
	 * @return whether an intersection will occur.
	 */
	public boolean willIntersect(GameObj obj){
		int next_x = mapped_x + v_x;
		int next_y = mapped_y + v_y;
		int next_obj_x = obj.mapped_x + obj.v_x;
		int next_obj_y = obj.mapped_y + obj.v_y;
		int next_z = pos_z + v_z;
		int next_obj_z = obj.pos_z + obj.v_z;
		return (next_x + mapped_width >= next_obj_x
				&& next_y + mapped_height >= next_obj_y
				&& next_z + depth >= next_obj_z
				&& next_obj_x + obj.mapped_width >= next_x 
				&& next_obj_y + obj.mapped_height >= next_y
				&& next_obj_z + obj.depth >= next_z);
	}

	
	/** Update the velocity of the object in response to hitting
	 *  an obstacle in the given direction. If the direction is
	 *  null, this method has no effect on the object. */
	public boolean bounce(Direction d) {
		if (d == null) return false;
		switch (d) {
		/*case UP:    v_y = Math.abs(v_y); System.out.println("up"); break;  
		case DOWN:  v_y = -Math.abs(v_y); System.out.println("dn"); break;
		case LEFT:  v_x = Math.abs(v_x); System.out.println("lf"); break;
		case RIGHT: v_x = -Math.abs(v_x); System.out.println("rt"); break;
		case OUT: v_z = Math.abs(v_z); System.out.println("out"); break;
		case IN: v_z = -Math.abs(v_z); System.out.println("in"); break;*/
		case VERTICAL:	v_y = -v_y; break;
		case HORIZONTAL:	v_x = -v_x; break;
		case DEPTH:	
			v_z = -v_z; 
			if (hit_count > 1) {
				v_x=-v_x;
				v_y=-v_y;
				hit_count = 0;
			}
			break;
		}
		return true;
	}
	
	/** Determine whether the game object will hit a 
	 *  wall in the next time step. If so, return the direction
	 *  of the wall in relation to this game object.
	 *  
	 * @return direction of impending wall, null if all clear.
	 */
	public Direction hitWall() {
		if (150 + map(pos_x + v_x - 150) < 150 + map(0 - 150))
			return Direction.HORIZONTAL;
		else if (150 + map(pos_x + v_x - 150) > 150 + map(max_x - 150))
			return Direction.HORIZONTAL;
		if (150 + map(pos_y + v_y - 150) < 150 + map(0 - 150))
			return Direction.VERTICAL;
		else if (150 + map(pos_y + v_y - 150) > 150 + map(max_y- 150))
			return Direction.VERTICAL;
		else if (pos_z + v_z < 0)
			return Direction.DEPTH;
		else if (pos_z + v_z > max_z) {
			Point currPos = new Point (pos_x, pos_y);
			if (currPos.equals(backwall_bounce)){
				hit_count ++;	
			} else {
				hit_count = 0;
			}
			System.out.println(hit_count);
			System.out.println(currPos.toString());
			backwall_bounce = currPos;
			return Direction.DEPTH;
		}
		else return null;
	}

	/** Determine whether the game object will hit another 
	 *  object in the next time step. If so, return the direction
	 *  of the other object in relation to this game object.
	 *  
	 * @return direction of impending object, null if all clear.
	 */
	public Direction hitObj(GameObj other) {

		if (this.willIntersect(other)) {
			//System.out.println("going to intersect");
			//double dx = other.pos_x + other.width /2.0 - (pos_x + width /2.0);
			//double dy = other.pos_y + other.height/2.0 - (pos_y + height/2.0);
			//double dz = other.pos_z + other.depth/2.0 - (pos_z + depth/2.0);
			
			Rectangle rxy_other = new Rectangle(other.mapped_x, other.mapped_y, other.mapped_width, other.mapped_height);
			Rectangle rxy = new Rectangle(mapped_x, mapped_y, mapped_width, mapped_height);
			
			Rectangle rxz_other = new Rectangle(other.mapped_x, other.pos_z, other.mapped_width, other.depth);
			Rectangle rxz = new Rectangle(mapped_x, pos_z, mapped_width, depth);
			
			Rectangle rzy_other = new Rectangle(other.pos_z, other.mapped_y, other.depth, other.mapped_height);
			Rectangle rzy = new Rectangle(pos_z, mapped_y, depth, mapped_height);
			
			//if (dz < 0){
			//	return Direction.OUT;
			//} else {
			//	return Direction.IN;
			//}

			Direction[] directions = new Direction[3];
			int i=0;
			
			Rectangle intersection = rxy.getBounds().intersection(rxy_other.getBounds());
			if (intersection.width >= intersection.height) {
			    directions[i] = Direction.VERTICAL;
			    i++;
			} else if (intersection.height >= intersection.width) {
				directions[i] = Direction.HORIZONTAL;
			    i++;
			}
			
			intersection = rxz.getBounds().intersection(rxz_other.getBounds());
			if (intersection.width >= intersection.height) {
			    directions[i] = Direction.DEPTH;
			    i++;
			} else if (intersection.height >= intersection.width) {
				directions[i] = Direction.HORIZONTAL;
			    i++;
			}
			
			intersection = rzy.getBounds().intersection(rzy_other.getBounds());
			if (intersection.width >= intersection.height) {
			    directions[i] = Direction.VERTICAL;
			    i++;
			} else if (intersection.height >= intersection.width) {
				directions[i] = Direction.DEPTH;
			    i++;
			}
			
			/*//xy plane calculation
			double theta = Math.atan2(dy, dx);
			double diagTheta = Math.atan2(height, width);

			if ( -diagTheta <= theta && theta <= diagTheta ) {
				directions[i] = Direction.RIGHT;
				i++;
			} else if ( diagTheta <= theta 
					&& theta <= Math.PI - diagTheta ) {
				directions[i] =  Direction.DOWN;
				i++;
			} else if ( Math.PI - diagTheta <= theta 
					|| theta <= diagTheta - Math.PI ) {
				directions[i] = Direction.LEFT;
				i++;
			} else {
				directions[i] =  Direction.UP;
				i++;
			}
			
			//xz plane calc
			theta = Math.atan2(dz, dx);
			diagTheta = Math.atan2(depth, width);

			if ( -diagTheta <= theta && theta <= diagTheta ) {
				directions[i] = Direction.RIGHT;
				i++;
			} else if ( diagTheta <= theta 
					&& theta <= Math.PI - diagTheta ) {
				directions[i] =  Direction.IN;
				i++;
			} else if ( Math.PI - diagTheta <= theta 
					|| theta <= diagTheta - Math.PI ) {
				directions[i] = Direction.LEFT;
				i++;
			} else {
				directions[i] =  Direction.OUT;
				i++;
			}
			
			//yz plane calc
			theta = Math.atan2(dy, dz);
			diagTheta = Math.atan2(height, depth);

			if ( -diagTheta <= theta && theta <= diagTheta ) {
				directions[i] = Direction.IN;
				i++;
			} else if ( diagTheta <= theta 
					&& theta <= Math.PI - diagTheta ) {
				directions[i] =  Direction.DOWN;
				i++;
			} else if ( Math.PI - diagTheta <= theta 
					|| theta <= diagTheta - Math.PI ) {
				directions[i] = Direction.OUT;
				i++;
			} else {
				directions[i] =  Direction.UP;
				i++;
			}*/
			
			//find mode -- under assumption that there is a mode
			System.out.print(directions[0]);
			System.out.print(directions[1]);
			System.out.println(directions[2]);
			if (directions[0] == directions [1]){
				return directions[0];
			} else {
				return directions[2];
			}

		} else {
			return null;
		}

	}
	
	/**
	 * Default draw method that provides how the object should be drawn 
	 * in the GUI. This method does not draw anything. Subclass should 
	 * override this method based on how their object should appear.
	 * 
	 * @param g 
	 *	The <code>Graphics</code> context used for drawing the object.
	 * 	Remember graphics contexts that we used in OCaml, it gives the 
	 *  context in which the object should be drawn (a canvas, a frame, 
	 *  etc.)
	 */
	public void draw(Graphics g) {
	}
	
}