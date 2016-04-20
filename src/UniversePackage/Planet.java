package UniversePackage;

import java.util.List;
import java.util.Observable;

import javax.swing.SwingWorker;
import javax.swing.Timer;

import player.Player;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Planet extends Observable implements Node  {

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
	
	private Timer timer;

	public Planet(double x, double y) {

		this.x = x;
		this.y = y;

		instantX = this.x;
		instantY = this.y;

		radius = base;
		neighbors = new LinkedList<>();
		ruler = null;
		clicked = false;

		dx = -1 + 2 * Galaxy.generator.nextDouble();
		dy = -1 + 2 * Galaxy.generator.nextDouble();

		resourceLevel = Galaxy.generator.nextInt(6);
		this.color = generateColor(resourceLevel);

		bound = 10;	

		timer = new Timer(20, new Strobe());
		timer.start();
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
		if(p.x > xLeft && p.x < xRight && p.y > yTop && p.y < yBottom) {
			System.out.println("Pressed at the planet!");
			return true;
		}			
		System.out.println("not pressed at the planet!");
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

		if(Math.abs(instantX - x) >= bound) {		
			dx = -dx;
		}
		else if(Math.abs(instantY - y) >= bound) {		
			dy = -dy;
		}
		instantX += dx;
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

		case 0: res = Color.gray; break;
		case 1: res = Color.red; break;
		case 2: res = Color.blue; break;
		case 3: res = Color.cyan; break;
		case 4: res = Color.green; break;
		case 5: res = Color.yellow;
		default:
		}

		return res;
	}
	
	
	/**
	 * Tells the player that the node has been clicked.
	 *
	 */
	private class Strobe implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						if (clicked) {
							setChanged();
							notifyObservers();
						}
						return null;
					}
			};
			worker.execute();
		}
    }
}
