package UniversePackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
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
	private int depth;
	private double shortest;

	/**
	 * Constructor
	 * Creates new Supply Station instances
	 * @param x initial horizontal location of a SS
	 * @param y initial vertical location of a SS
	 */
	public SupplyStation (double x, double y) {
		
		this.x = x;
		this.y = y;
		this.radius = 12;
		ruler = null;
		color = Color.lightGray;
		instX = this.x;
		instY = this.y;
		rank = 0;
		bound = 2;
		dy = (Galaxy.generator.nextInt(2) == 1) ? 0.05 : -0.05;
		parentNode = null;
		depth = 0;
		
		shortest =  Integer.MAX_VALUE;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		
		Shape rect = new Rectangle2D.Double(instX - radius/2, instY - radius/2, radius, radius);
		g2.setColor(color);
		g2.setStroke(new BasicStroke(2));
		g2.draw(rect);	
		
		if(ruler != null) {
			
			g2.setColor(ruler.getPlayerColor());
			Shape core = new Rectangle2D.Double(instX - radius/2 + 3, instY - radius/2 + 3, 6, 6);
			g2.fill(core);
		}
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
	public Player getRuler() {
		return ruler;
	}
	
	public String toString() {
		return "(S " + this.getX() + " " + this.getY() + ")";
				
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public int getResourceLevel() {
		return 0;
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
		return "s";
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
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof SupplyStation)) {
			return false;
		}
		SupplyStation otherS = (SupplyStation)other;
		return x == otherS.getX() && y == otherS.getY();
	}
}
