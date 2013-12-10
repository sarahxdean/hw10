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
	
	/** Size of object, in pixels */
	public int width;
	public int height;
	public int depth;

	// mapped coords/dimensions for drawing
	private Map map;
	public int mapped_width;
	public int mapped_height;
	int mapped_x;
	int mapped_y;
	
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
	
	// to track the position of bounces. if ball 
	// is stuck in loop, can take action
	Point backwall_bounce = new Point(0,0);
	int hit_count = 0;

	/**
	 * Constructor
	 */
	public GameObj(int v_x, int v_y, int v_z, int pos_x, int pos_y, int pos_z, 
		int width, int height, int depth, int court_width, int court_height, 
		int court_depth, Map map){
		this.v_x = v_x;
		this.v_y = v_y;
		this.v_z = v_z;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.pos_z = pos_z;
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.map = map;

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
		
		updateMap();

		clip();
	}
	
	// updates the mapped coords
	public void updateMap(){
		mapped_width = map.dimension(width, pos_z); 
		mapped_height = map.dimension(height, pos_z); 

		mapped_x = map.point(pos_x, pos_z);
	    mapped_y = map.point(pos_y, pos_z);
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
		return (pos_x + width >= obj.pos_x
				&& pos_y + height >= obj.pos_y
				&& pos_z + depth >= obj.pos_z
				&& obj.pos_x + obj.width >= pos_x 
				&& obj.pos_y + obj.height >= pos_y
				&& obj.pos_z + obj.depth >= pos_z);
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
		int next_x = pos_x + v_x;
		int next_y = pos_y + v_y;
		int next_obj_x = obj.pos_x + obj.v_x;
		int next_obj_y = obj.pos_y + obj.v_y;
		int next_z = pos_z + v_z;
		int next_obj_z = obj.pos_z + obj.v_z;
		return (next_x + width >= next_obj_x
				&& next_y + height >= next_obj_y
				&& next_z + depth >= next_obj_z
				&& next_obj_x + obj.width >= next_x 
				&& next_obj_y + obj.height >= next_y
				&& next_obj_z + obj.depth >= next_z);
	}

	
	/** Update the velocity of the object in response to hitting
	 *  an obstacle in the given direction. If the direction is
	 *  null, this method has no effect on the object. 
	 *  if the object is stuck in a bouncing loop, change it up */
	public boolean bounce(Direction d) {
		if (d == null) return false;
		switch (d) {
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
		if (map.point(pos_x + v_x, pos_z) < map.point(0, pos_z))
			return Direction.HORIZONTAL;
		else if (map.point(pos_x + v_x, pos_z) > map.point(max_x, pos_z))
			return Direction.HORIZONTAL;
		if (map.point(pos_y + v_y, pos_z) < map.point(0, pos_z))
			return Direction.VERTICAL;
		else if (map.point(pos_y + v_y, pos_z) > map.point(max_y, pos_z))
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
			backwall_bounce = currPos;
			return Direction.DEPTH;
		}
		else return null;
	}

	/** Determine whether the game object will hit another 
	 *  object in the next time step using intersecting rectangles. 
	 *  If so, return the orientation
	 *  of the other object in relation to this game object.
	 *  
	 * @return direction of impending object, null if all clear.
	 */
	public Direction hitObj(GameObj other) {

		if (this.willIntersect(other)) {
			
			Rectangle rxy_other = new Rectangle(other.pos_x, other.pos_y, other.width, other.height);
			Rectangle rxy = new Rectangle(pos_x, pos_y, width, height);
			
			Rectangle rxz_other = new Rectangle(other.pos_x, other.pos_z, other.width, other.depth);
			Rectangle rxz = new Rectangle(pos_x, pos_z, width, depth);
			
			Rectangle rzy_other = new Rectangle(other.pos_z, other.pos_y, other.depth, other.height);
			Rectangle rzy = new Rectangle(pos_z, pos_y, depth, height);

			Direction[] directions = new Direction[3];
			int i=0;
			
			// xy plane
			Rectangle intersection = rxy.getBounds().intersection(rxy_other.getBounds());
			if (intersection.width >= intersection.height) {
			    directions[i] = Direction.VERTICAL;
			    i++;
			} else if (intersection.height >= intersection.width) {
				directions[i] = Direction.HORIZONTAL;
			    i++;
			}
			
			// xz plane
			intersection = rxz.getBounds().intersection(rxz_other.getBounds());
			if (intersection.width >= intersection.height) {
			    directions[i] = Direction.DEPTH;
			    i++;
			} else if (intersection.height >= intersection.width) {
				directions[i] = Direction.HORIZONTAL;
			    i++;
			}
			
			// zy plane
			intersection = rzy.getBounds().intersection(rzy_other.getBounds());
			if (intersection.width >= intersection.height) {
			    directions[i] = Direction.VERTICAL;
			    i++;
			} else if (intersection.height >= intersection.width) {
				directions[i] = Direction.DEPTH;
			    i++;
			}
			
			//find mode -- under assumption that there is a mode
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