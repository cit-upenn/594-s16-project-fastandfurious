package UniversePackage;

import java.util.List;
import java.util.LinkedList;

public class Planet implements Node{
	
	private double x;
	private double y;
	private double radius;
	private List<Node> neighbors;
	private int base = 10;
	private Player ruler;
	
	public Planet(double x, double y) {

		int sizeLevel = 1 + Galaxy.generator.nextInt(4);
		this.x = x;
		this.y = y;
		radius = sizeLevel * base;
		neighbors = new LinkedList<>();
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
}
