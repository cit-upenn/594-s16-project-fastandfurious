package UniversePackage;

import java.util.List;

import player.Player;

import java.awt.Point;
import java.util.LinkedList;

public class Planet implements Node{

	private double x;
	private double y;
	private double radius;
	private List<Node> neighbors;
	private int base = 20;
	private Player ruler;

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
}
