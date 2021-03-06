package player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import UniversePackage.Galaxy;
import UniversePackage.Navigator;
import UniversePackage.Node;
import UniversePackage.StarCluster;

public class HumanPlayer implements Player {

	// declare instance variables
	private double x;
	private double y;
	private Queue<Node> destinations;
	private int wealth;
	private Point p1;
	private Point p2;
	private Point p3;
	private int deg;
	private double dx;
	private double dy;
	private double speed;
	private double radius;
	private Node currentNode;
	private Node focus;
	private Node selected;
	private Color pColor;
	private Galaxy galaxy;
	private LinkedList<Node> selections;
	private final float dash1[] = {10.0f};
	private BasicStroke dashStroke = new BasicStroke(2.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
	private BasicStroke lineStroke = new BasicStroke(2.0f);
	private HashSet<Node> reign;
	private String name;
	private Lock lock;
	
	/**
	 * constructor
	 * creates new human player instance
	 * @param x horizontal position of player
	 * @param y vertical position of player
	 * @param pColor player color
	 * @param galaxy reference to galaxy model
	 * @param name of the player
	 */
	public HumanPlayer(double x, double y, Color pColor, Galaxy galaxy, String name) {
		
		this.setX(x);
		this.setY(y);
		this.pColor = pColor;
		this.wealth = 300;
		this.pColor = pColor;
		destinations = new LinkedList<>();
		this.focus = null;
		this.selected = null;
		this.galaxy = galaxy;
		speed = 2; 
		radius = 15;
		p1 = new Point(x, y - radius);
		p2 = new Point(x - radius * Math.cos(Math.PI/6), y + radius/2);
		p3 = new Point(x + radius * Math.cos(Math.PI/6), y + radius/2);
		selections = new LinkedList<>();
		reign = new HashSet<Node>();
		this.name = name;
		lock = new ReentrantLock();
	}
	
	/**
	 * @return name of player as string
	 */
	public String getName() {
		return name;
	}

	@Override
	public void draw(Graphics2D g2) {
		
		int[] xpoints = new int[]{(int)p1.getX(), (int)p2.getX(), (int)p3.getX()};
		int[] ypoints = new int[]{(int)p1.getY(), (int)p2.getY(), (int)p3.getY()};
		Shape triangle = new Polygon(xpoints, ypoints, 3);
		g2.setColor(pColor);
		g2.setStroke(lineStroke);
		g2.draw(triangle);
		
		drawHalo(g2, "focus");
		drawHalo(g2, "selection");
		drawSelections(g2);
		
		rotate(10);
	}
	
	@Override
	public void move() {
		
			if(currentNode.getRuler()!=this) {
				teleport();
				return;
			}
		
			if(!inMotion()) return;
			Node position = galaxy.locateNode(x, y);
			if(position != null) currentNode = position;
			Node dest = destinations.peek();
			
			if(currentNode == dest) {	
				destinations.poll();
	        	galaxy.refactor(this);
				if(destinations.isEmpty()) {
					clearStuffs(); dx = 0; dy = 0;
				}
			} 
			else if(galaxy.areAdjacentNodes(currentNode, dest)&&!galaxy.hasEdge(currentNode, dest)&&position != null) {
									
				if(dest.getRuler() == null) {	
					boolean success = galaxy.buildEdge(currentNode, dest, this);
					if(!success) { clearStuffs();}					
				}
				else if(dest.getRuler() == this){
					boolean success = galaxy.buildEdge(currentNode, dest, this);
					if(!success) { clearStuffs(); }
				} 
				else {	
					try{ 
						lock.lock(); 
						galaxy.neutralizeNode(this, dest);
					}
					finally{ clearStuffs(); lock.unlock();}
				}	
			}
			else{			
				if(galaxy.hasEdge(currentNode, dest)){
					Navigator.setVelocity(this, dest);
					x += dx;
					y += dy;
					rotate(20);
				}else{
					clearStuffs();
					teleport();
				}
			}
	}
	
	/**
	 * teleport if weird things happen
	 */
	private void teleport() {
		List<Node> all = new LinkedList<>(reign);
		Node choice = all.get(Galaxy.generator.nextInt(all.size()));
		x = choice.getX();
		y = choice.getY();
		currentNode = choice;
	}

	private void clearStuffs() {
		try{
			lock.lock();
			destinations.clear();
			selections.clear();
			setSelected(null);
		}finally{
			lock.unlock();
		}
	}

	@Override
	public void drawHalo(Graphics2D g2, String type) {
		
		Node node = null;
		if(type.equals("focus")) {
			node = focus;
			
		}else if(type.equals("selection")) {
			node = selected;
		}
		if(node != null) {
			double cx = node.getInstX();
			double cy = node.getInstY();
			double radius = 30;
			Shape halo = new Ellipse2D.Double(cx - radius/2, cy - radius/2, radius, radius);
			if(type.equals("focus")) {			
				g2.setStroke(dashStroke);		
			}else if(type.equals("selection")) {
				g2.setStroke(lineStroke);
			}
			g2.setColor(pColor);
			g2.draw(halo);
		}
	}
	
	@Override
	public boolean inMotion() {
		return !destinations.isEmpty();
	}
	
	private void drawSelections(Graphics2D g2) {	
		for(int i = 1; i < selections.size(); i++) {	
			Node first = selections.get(i-1);
			Node second = selections.get(i);
			Shape line = new Line2D.Double(first.getInstX(), first.getInstY(), second.getInstX(), second.getInstY());
			g2.setStroke(dashStroke);	
			g2.setColor(pColor);
			g2.draw(line);
		}
	}
	
	private void rotate(double increment) {
		
		this.deg += increment;
		this.deg %= 360;
		double radians1 = (double)this.deg/180;
		double radians2 = radians1 + Math.PI * 2/3;
		double radians3 = radians2 + Math.PI * 2/3;
		p1.setX(x + radius * Math.cos(radians1));
		p1.setY(y + radius * Math.sin(radians1));
		p2.setX(x + radius * Math.cos(radians2));
		p2.setY(y + radius * Math.sin(radians2));
		p3.setX(x + radius * Math.cos(radians3));
		p3.setY(y + radius * Math.sin(radians3));
	}

	public void buildPath() {

		if(selections.size() < 1) {
			System.err.println("Please specify path to build");
			return;
		}

		Node start = selections.getFirst();

		if(StarCluster.find(start) != StarCluster.find(currentNode)) {
			System.err.println("Path must be connected to current node");
			return;
		}

		destinations.addAll(Navigator.buildDijkstraPath(currentNode, start, galaxy));
		destinations.remove(start);
		destinations.addAll(selections);	
	}
	
	@Override
	public Node getSelected() {
		return selected;
	}

	@Override
	public void setSelected(Node selection) {
		this.selected = selection;
	}

	@Override
	public void setFocus(Node focus) {
		this.focus = focus;
	}
	
	@Override
	public Color getPlayerColor() {
		return pColor;
	}

	@Override
	public int getWealth() {	
		return wealth;
	}

	@Override
	public void setCurrentNode(Node node) {
		this.currentNode = node;
	}
	
	@Override
	public Node getCurrentNode() {
		return currentNode;
	}
	
	@Override
	public LinkedList<Node> getSelections() {
		return selections;
	}
	
	@Override
	public double getX() {
		return x;
	}
	
	@Override
	public void setX(double x) {
		this.x = x;
	}
	
	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public void think() {}

	@Override
	public  synchronized boolean addWealth(int change) {
		if(wealth + change > 100000) {
			return true;
		}
		else if(wealth + change < 0) {		
			System.out.println("Can't afford");
			return false;
		}
		
		wealth += change;
		return true;
	}
	
	@Override
	public String toString() {
		return this.name + "(human)";
	}
	
	public Set<Node> getNodesControlled() {
		return reign;	
	}

	@Override
	public String getStatus() {
		return inMotion()? "moving": "thinking";
	}
	
	@Override
	public synchronized void controlNode(Node node) {
		reign.add(node);
	}

	@Override
	public synchronized void loseNode(Node node) {
		reign.remove(node);
	}

	@Override
	public Queue<Node> getDestinations() {
		return destinations;
	}

	@Override
	public double getDX() {
		return dx;
	}

	@Override
	public double getDY() {
		return dy;
	}

	@Override
	public void setDX(double dx) {
		this.dx = dx;
	}

	@Override
	public void setDY(double dy) {
		this.dy = dy;
	}

	@Override
	public double getSpeed() {
		return speed;
	}
}
