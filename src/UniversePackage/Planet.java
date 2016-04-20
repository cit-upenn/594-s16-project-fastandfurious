package UniversePackage;

import java.util.List;

import player.Player;
import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

public class Planet implements Node{

	private double x;
	private double y;
	private double radius;
	private List<Node> neighbors;
	private int base = 20;
	private Player ruler;
	private Color color;
	
	private boolean clicked;
	private boolean inhabitable;
	private double dx;
	private double dy;
	private int resourceLevel;
	private int population;
	private int size;
	private int defenseLevel;
	private double instantX;
	private double instantY;
	
	private int bound;
	private Node parentNode;

	public Planet(double x, double y) {

		this.x = x;
		this.y = y;
		
		instantX = this.x;
		instantY = this.y;
		
		radius = base;
		neighbors = new LinkedList<>();
		ruler = null;
		clicked = false;
		
		dx = 0;
		dy = ( Galaxy.generator.nextInt(2) == 0 )? 0.1: -0.1;
		
		
		resourceLevel = Galaxy.generator.nextInt(6);
		this.color = generateColor(resourceLevel);
		
		bound = 2;
		
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
		return "(P " + this.getX() + " " + this.getY() + ")";

	}

	@Override
	public void click() {
		clicked = true;

	}

	@Override
	public boolean contains(Point p) {
		double xLeft = getX();
		double xRight = getX() + getRadius();
		double yTop = getY();
		double yBottom = getY() + getRadius();
		if(p.x > xLeft && p.x < xRight && p.y < yTop && p.y > yBottom)
			return true;
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
	public boolean buildEdge(Node lhs, Node rhs) {
		if(lhs == null || rhs == null
		   ||Math.abs(lhs.getX() - rhs.getX()) > 50 
		   || Math.abs(lhs.getY() - rhs.getY()) > 50) {
			return false;
		}
		lhs.getNeighbors().add(rhs);
		rhs.getNeighbors().add(lhs);
		return true;
	}

	@Override
	public void move() {
		
		if(Math.abs(instantY - y) >= bound) {		
			dy = -dy;
		}
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
	
	private Color generateColor(int colorNum) {
		
		Color res = Color.LIGHT_GRAY;	
		switch(colorNum) {
			case 0: res = Color.LIGHT_GRAY; break;
			case 1: res = Color.red; break;
			case 2: res = Color.blue; break;
			case 3: res = Color.cyan; break;
			case 4: res = Color.green; break;
			case 5: res = Color.yellow;
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
}
