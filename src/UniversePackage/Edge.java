package UniversePackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import player.Player;

/**
 * An edge connects two nodes in a graph
 */
public class Edge {
	
	// declare instance variables
	private Node p1; // start source
	private Node p2;
	private Player ruler;
	private int cost;
	private double len;
	
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
		this.cost = 2;
		this.len = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
	}
	
	/**
	 * Draw the edge on 2D graphics
	 */
	public void draw(Graphics2D g) {		
		Line2D.Double line = new Line2D.Double(p1.getInstX(), p1.getInstY(), p2.getInstX(), p2.getInstY());	
		if(StarCluster.find(p1) == StarCluster.find(p2) && StarCluster.find(p1) == StarCluster.find(ruler.getCurrentNode())  ) {
			g.setColor(ruler.getPlayerColor());
		}else g.setColor(Color.gray);	
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
	 * Get length of an edge
	 * @return
	 */
	public double getLength() {		
		return len;
	}
	
	/**
	 * @return start point of the edge
	 */
	public Node getStart() {
		return p1;
	}
	
	/**
	 * @return end point of the edge
	 */
	public Node getEnd() {
		return p2;
	}
	
	/**
	 * get ruler of edge
	 * @return
	 */
	public Player getRuler() {
		return ruler;
	}
}
