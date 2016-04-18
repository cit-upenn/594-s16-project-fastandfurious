package UniversePackage;

import java.util.LinkedList;
import java.util.List;

public class SupplyStation implements Node{
	
	private double x;
	private double y;
	private double radius;
	private List<Node> neighbors;
	private Player ruler;
	
	public SupplyStation (double x, double y) {
		this.x = x;
		this.y = y;
		this.radius = 5;
		ruler = null;
		neighbors = new LinkedList<>();
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
