package UniversePackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import player.Player;

/**
 * Supply stations are intermediate locations 
 * connecting stars
 */
public class SupplyStation extends Observable implements Node{
	
	// declare instance variables
	private double x;
	private double y;
	private double radius;
	private Player ruler;
	private Color color;	
	private double instX;
	private double instY;
	private double bound;
	private double dy;
	private int rank;
	private Node parentNode;
	private Node pred;
	private List<Node> neighbors;

	/**
	 * Constructor
	 * Creates new Supply Station instances
	 * @param x initial horizontal location of a SS
	 * @param y initial vertical location of a SS
	 */
	public SupplyStation (double x, double y) {
		this.x = x;
		this.y = y;
		this.radius = 10;
		ruler = null;
		neighbors = new LinkedList<>();
		color = Color.white;
		instX = this.x;
		instY = this.y;
		rank = 0;
		bound = 2;
		dy = (Galaxy.generator.nextInt(2) == 1) ? 0.05 : -0.05;
		parentNode = null;
		
	}
	
	@Override
	public void draw(Graphics2D g2) {
		
		Shape rect = new Rectangle2D.Double(instX - radius/2, instY - radius/2, radius, radius);
		g2.setColor(color);
		g2.setStroke(new BasicStroke(2));
		g2.draw(rect);	
		
	}

	@Override
	public Color getColor() {
		return color;
	}
	
	@Override
	public void setRuler(Player ruler) {
		this.ruler = ruler;
	}

	@Override
	public void move() {
		
		if(Math.abs(instY - y) > bound)
			dy *= -1;	
		instY += dy;
	}

	@Override
	public double getInstX() {
		return instX;
	}

	@Override
	public double getInstY() {
		return instY;
	}

	@Override
	public Node getParentNode() {
		return this.parentNode;
	}

	@Override
	public void setParentNode(Node parent) {
		this.parentNode = parent;
	}

	@Override
	public Node getPredecessor() {
		return pred;
	}

	@Override
	public void setPredecessor(Node pred) {
		this.pred = pred;
	}

	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public void incrementRank() {
		rank++;
	}
	
	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getRadius() {
		return radius;
	}

	@Override
	public List<Node> getNeighbors() {
		return neighbors;
	}

	@Override
	public Player getRuler() {
		return ruler;
	}
	
	public String toString() {
		return "(S " + this.getX() + " " + this.getY() + ")";
				
	}
}
