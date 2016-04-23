package UniversePackage;

import java.util.List;
import java.util.Observable;
import player.Player;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;

public class Planet extends Observable implements Node  {

	private double x;
	private double y;
	private double radius;
	private List<Node> neighbors;
	private int base = 20;
	private Player ruler;
	private Color color;
	private double dy;
	private int resourceLevel;
	private double instantX;
	private double instantY;
	private int bound;
	private int rank;
	private Node parentNode;
	private Node predecessor;

	public Planet(double x, double y) {

		this.x = x;
		this.y = y;
		instantX = this.x;
		instantY = this.y;
		radius = base;
		resourceLevel = Galaxy.generator.nextInt(6);
		this.color = generateColor(resourceLevel);
		bound = 10;	
		dy = ( Galaxy.generator.nextInt(2) == 0 )? 0.15: -0.15;
		resourceLevel = Galaxy.generator.nextInt(6);
		this.color = generateColor(resourceLevel);
		bound = 2;
		rank = 0;
		neighbors = new LinkedList<>();
		parentNode = null;
		predecessor = null;
		ruler = null;
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
		if(Math.abs(instantY-y) >= bound)
			dy = -dy;
		instantY += dy;
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
	public void draw(Graphics2D g2) {
		Shape circle = new Ellipse2D.Double(instantX - radius/2, instantY - radius/2, radius, radius);
		g2.setColor(color);
		g2.setStroke(new BasicStroke(2));
		g2.draw(circle);
	}

	private Color generateColor(int colorNum) {

		Color res = Color.LIGHT_GRAY;

		switch(colorNum) {

		case 0: res = Color.white; break;
		case 1: res = new Color(255, 51, 0); break;
		case 2: res = new Color(0, 153, 255); break;
		case 3: res = new Color(0, 204, 0); break;
		case 4: res = new Color(102, 102, 255); break;
		case 5: res = new Color(255, 204, 0);
		default:
		}

		return res;
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
}
