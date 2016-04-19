package UniversePackage;
import java.awt.Point;
import java.util.List;
import java.awt.Color;

import player.Player;

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

	/**
	 * Mark that the node has been clicked.
	 */
	public void click();
	
	/**
	 * @return color of the node
	 */
	public Color getColor();
	
	/**
	 * change ruler of a node
	 */
	public void setRuler(Player ruler);
	
	/**
	 * @param p
	 * @return True if the point is contained within the node.
	 */
	public boolean contains(Point p);
	
	/**
	 * build an edge between two nodes
	 * @param lhs left-hand-side node
	 * @param rhs right-hand-side node
	 * @return true if operation is successful
	 */
	public boolean buildEdge(Node lhs, Node rhs);
}

