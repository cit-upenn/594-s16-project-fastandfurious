package UniversePackage;

import java.util.Observable;
import player.Player;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Planet is a special node that has abundant resources
 */
public class Planet extends Observable implements Node  {
	
	// declare instance variables
	private Player ruler;
	private Color color;
	private int resourceLevel;
	private double x;
	private double y;
	private double dy;
	private double radius;
	private double instantX;
	private double instantY;
	private double shortest;	
	private int depth;
	private int bound;
	private int rank;
	private Node parentNode;
	private Node predecessor;

	/**
	 * constructor
	 * creates new Planet instance
	 * @param x starting vertical position
	 * @param y starting horizontal position
	 */ 
	public Planet(double x, double y) {

		this.x = x;
		this.y = y;
		this.instantX = this.x;
		this.instantY = this.y;
		this.radius = 25;
		this.resourceLevel = 1 + Galaxy.generator.nextInt(6);
		this.color = generateColor(resourceLevel);
		this.bound = 10;	
		this.dy = ( Galaxy.generator.nextInt(2) == 0 )? 0.15: -0.15;
		this.bound = 2;
		this.rank = 0;
		this.parentNode = null;
		this.predecessor = null;
		this.ruler = null;
		this.depth = 0;
		this.shortest = Integer.MAX_VALUE;
	}
	
	@Override
	public void draw(Graphics2D g2) {	
		Shape circle = new Ellipse2D.Double(instantX - radius/2, instantY - radius/2, radius, radius);
		g2.setColor(color);
		g2.setStroke(new BasicStroke(2));
		g2.draw(circle);		
		if(ruler != null) {
			Shape core = new Ellipse2D.Double(instantX - radius/2 + 7, instantY - radius/2 + 7, radius - 15, radius - 15);
			g2.setColor(ruler.getPlayerColor());
			g2.fill(core);
		}	
	}
	
	/**
	 * Generate color for planet
	 * based on resource level
	 * @param colorNum resource level
	 * @return corresponding color
	 */
	private Color generateColor(int colorNum) {
		Color res = Color.LIGHT_GRAY;
		switch(colorNum) {
			case 1: res = Color.white; break;
			case 2: res = new Color(255, 51, 0); break;
			case 3: res = new Color(0, 102, 204); break;
			case 4: res = new Color(0, 204, 0); break;
			case 5: res = new Color(102, 102, 255); break;
			case 6: res = new Color(255, 204, 0);break;
			default: 
		}
		return res;
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof Planet)) { return false; }
		return this.hashCode() == other.hashCode();
	}
	
	@Override
	public void move() {
		if(Math.abs(instantY-y) >= bound) dy = -dy;
		instantY += dy;
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
	public Player getRuler() {
		return ruler;
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
	public double getInstX() {
		return this.instantX;
	}

	@Override
	public double getInstY() {
		return this.instantY;
	}

	@Override
	public Node getParentNode() {		
		return this.parentNode;
	}

	@Override
	public synchronized void setParentNode(Node parent) {	
		this.parentNode = parent;
	}

	@Override
	public Node getPredecessor() {
		return this.predecessor;
	}

	@Override
	public void setPredecessor(Node pred) {	
		this.predecessor = pred;	
	}

	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public void incrementRank() {
		rank++;
	}	
	
	public String toString() {
		return "(P " + this.getX() + " " + this.getY() + ")";

	}

	@Override
	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public int getResourceLevel() {
		return resourceLevel * 2 + rank;
	}

	@Override
	public void setDepth(int d) {
		depth = d;
	}

	@Override
	public int getDepth() {
		return depth;
	}

	@Override
	public String getType() {
		return "p";
	}

	@Override
	public int getCost() {
		
		return getResourceLevel() * 10;
	}

	@Override
	public int getDefenseLevel() {
		return 0;
	}
	
	@Override
	public double getShortest() {
		
		return shortest;
	}
	
	@Override
	public void setShortest(double dist) {
		this.shortest = dist;
	}
}
