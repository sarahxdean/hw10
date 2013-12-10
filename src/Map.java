// a map from 3 dimensions to 2 dimensions

public class Map {
	
	private double z_zoom;
	private int center; //assumes square
	
	public Map (double z_zoom, int center){
		this.z_zoom = z_zoom;
		this.center = center;
	}
	
	public int point (int p, int z){
		return (center + (int)((p-center)/(1 + z/z_zoom)));
	}
	
	public int dimension (int d, int z){
		return (int)(d/(1+z/z_zoom));
	}
	
}