package UniversePackage;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import player.Player;

public class SupplyStation implements Node{
	
	private double x;
	private double y;
	private double radius;
	private List<Node> neighbors;
	private Player ruler;
	
	private boolean clicked;
	
	public SupplyStation (double x, double y) {
		this.x = x;
		this.y = y;
		this.radius = 25;
		ruler = null;
		neighbors = new LinkedList<>();
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
		return "(S " + this.getX() + " " + this.getY() + ")";
				
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
