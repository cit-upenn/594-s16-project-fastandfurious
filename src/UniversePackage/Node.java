package UniversePackage;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics2D;

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
	 * @return ruler of the node
	 */
	public Player getRuler();
	
	/**
	 * @return color of the node
	 */
	public Color getColor();
	
	/**
	 * change ruler of a node
	 */
	public void setRuler(Player ruler);
	
	/**
	 * slightly change the location of a node
	 * might be a planet or a supply station
	 * planets and supply stations move differently
	 */
	public void move();
	
	/**
	 * @return instantaneous x coordinate of node center
	 */
	public double getInstX();
	
	/**
	 * @return instantaneous y coordinates of node center
	 */
	public double getInstY();
	
	/**
	 * general method for draw a shape
	 * @param g graphics instance used for drawing
	 */
	public void draw(Graphics2D g);
	
	/**
	 * @return rank of the set containing the node
	 */
	public int getRank();
	
	/**
	 * Reset rank of node
	 */
	public void setRank(int rank);
	
	/**
	 * increment rank of current node
	 */
	public void incrementRank();
	
	/**
	 * @return resource level of planet
	 */
	public int getResourceLevel();
	
	/**
	 * set depth of a node
	 * will be used for depth-first-search
	 */
	public void setDepth(int d);
	
	/**
	 * @return depth of a node
	 */
	public int getDepth();
	
	/**
	 * @return neighbors of the node
	 */
	public List<Node> getNeighbors();
	
	/**
	 * @return type of the node
	 */
	public String getType();
	
	/**
	 * @return parent node of the current node
	 */
	public Node getParentNode();
	
	/**
	 * @param parent Node that is to be set as parent
	 */
	public void setParentNode(Node parent);
	
	/**
	 * @return current predecessor of node
	 */
	public Node getPredecessor();
	
	/**
	 * set current predecessor of a node
	 */
	public void setPredecessor(Node pred);
	
	/**
	 * @return cost to capture the planet
	 */
	public int getCost();
	
	/**
	 * @return defense level of a node
	 */
	public int getDefenseLevel();
}

