package UniversePackage;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import player.Player;

/**
 * An edge connects two nodes in a graph
 */
public class Edge {
	
	// declare instance variables
	private Node p1;
	private Node p2;
	private Player ruler;
	private int cost;
	
	/**
	 * Constructor
	 * Creates new edge instance
	 * @param p1 end-point of edge
	 * @param p2 end-point of edge
	 * @param builtBy owner of the edge
	 */
	public Edge(Node p1, Node p2, Player builtBy) {
		this.p1 = p1;
		this.p2 = p2;
		this.ruler = builtBy;
		this.cost = 1;
	}
	
	/**
	 * Draw the edge on 2D graphics
	 */
	public void draw(Graphics2D g) {
		Line2D.Double line = new Line2D.Double(p1.getInstX(), p1.getInstY(), p2.getInstX(), p2.getInstY());
		g.setColor(ruler.getPlayerColor());
		g.setStroke(new BasicStroke(2.0f));
		g.draw(line);
	}
	
	/**
	 * @return cost of an edge
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 * @param cost new cost of edge
	 */
	public void setCost(int cost){
		this.cost = cost;
	}
	
	/**
	 * determines if an edge contains an end-point
	 */
	public boolean containsPoint(Node node) {
		return p1 == node || p2 == node;
	}
}
