package UniversePackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import player.Player;

public class SupplyStation extends Observable implements Node{
	
	private double x;
	private double y;
	private double radius;
	private List<Node> neighbors;
	private Player ruler;
	private Color color;
	private boolean clicked;
	
	private double instX;
	private double instY;
	
	private double bound;
	double dx, dy;
	
	private Node parentNode;
	private Node pred;
	
	public SupplyStation (double x, double y) {
		
		this.x = x;
		this.y = y;
		this.radius = 10;
		ruler = null;
		neighbors = new LinkedList<>();
		clicked = false;
		color = Color.white;
		instX = this.x;
		instY = this.y;
		bound = 2;
		dy = (Galaxy.generator.nextInt(2) == 1) ? 0.2 : -0.2;
		parentNode = null;
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

	@Override
	public void click() {
		clicked = true;	
		setChanged();
		notifyObservers();
	}

	@Override
	public boolean contains(Point p) {
		double xLeft = getX() - getRadius() / 2;
		double xRight = getX() + getRadius() / 2;
		double yTop = getY() - getRadius() / 2;
		double yBottom = getY() + getRadius() / 2;
		if(p.getX() > xLeft && p.getX() < xRight && p.getY() > yTop && p.getY() < yBottom) {
			return true;
		}			
		return false;
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
	public void draw(Graphics2D g2) {
		
		Shape rect = new Rectangle2D.Double(instX - radius/2, instY - radius/2, radius, radius);
		g2.setColor(color);
		g2.setStroke(new BasicStroke(2));
		g2.draw(rect);	
		
	}
}
