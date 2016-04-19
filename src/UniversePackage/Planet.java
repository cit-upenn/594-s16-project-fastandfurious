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

	public Planet(double x, double y) {

		this.x = x;
		this.y = y;
		radius = base;
		neighbors = new LinkedList<>();
		ruler = null;
		clicked = false;
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
			|| Math.abs(lhs.getX() - rhs.getX()) > 50 
			|| Math.abs(lhs.getY() - rhs.getY()) > 50) {
			return false;
		}
		lhs.getNeighbors().add(rhs);
		rhs.getNeighbors().add(lhs);
		return true;
	}
}
