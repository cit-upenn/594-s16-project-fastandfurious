package UniversePackage;
import java.util.List;

/**
 * A Node represents an existing location in the galaxy
 */
public interface Node {
	
	/**
	 * @return x coordinate of the graph node
	 */
	public double getX();
	
	/**
	 * @return y coordinate of the graph node
	 */
	public double getY();
	
	/**
	 * @return radius of the current node
	 */
	public double getRadius();
	
	/**
	 * @return neighbors of the node
	 */
	public List<Node> getNeighbors();
	
	/**
	 * @return ruler of the node
	 */
	public Player getRuler();
	
}
