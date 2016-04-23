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

import UniversePackage.Galaxy;
import UniversePackage.Navigator;
import UniversePackage.Node;
import UniversePackage.StarCluster;

/**
 * A computer player is a player controlled by the computer
 * A computer player runs on built-in strategy or AI.
 */
public class ComputerPlayer implements Player {

	// declare instance variables
	private double x;
	private double y;
	private int wealth;
	private double dx;
	private double dy;
	private double speed;
	private double radius;
	private Point p1;
	private Point p2;
	private Point p3;
	private int deg;
	private Node currentNode;
	private Galaxy galaxy;
	private LinkedList<Node> sequence;
	private Queue<Node> destinations;
	private Color pColor;	
	private boolean isThinking;
	private final float dash1[] = {10.0f};
	private BasicStroke lineStroke = new BasicStroke(2.0f);
	private BasicStroke dashStroke = new BasicStroke(2.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f);
	private LinkedList<Node> candidates;
	private	Node selected;
	private Node focus;
	
	/**
	 * Constructor 
	 * Creates new ComputerPlayer instances
	 * @param x initial horizontal position of the player
	 * @param y initial vertical position of the player
	 * @param pColor representative color of the player
	 * @param galaxy a reference to the galaxy (game environment)
	 */
	public ComputerPlayer(double x, double y, Color pColor, Galaxy galaxy) {		
		
		this.pColor = pColor;
		wealth = 100;
		destinations = new LinkedList<>();
		this.x = x;
		this.y = y;
		this.radius = 15;
		this.speed = 1.5;
		p1 = new Point(x, y - radius);
		p2 = new Point(x - radius * Math.cos(Math.PI/6), y + radius/2);
		p3 = new Point(x + radius * Math.cos(Math.PI/6), y + radius/2);
		this.galaxy = galaxy;
		this.isThinking = false;
		sequence = new LinkedList<>();
		candidates = new LinkedList<>();
	}

	@Override
	/**
	 * Get representative color of the player
	 */
	public Color getPlayerColor() {
		return pColor;
	}

	@Override
	public void setCurrentNode(Node node) {
		currentNode = node;
	}

	@Override
	public Node getCurrentNode() {
		return currentNode;
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void addTarget(Node target) {
		destinations.add(target);
	}

	/**
	 * Draw everything related to player
	 * @param g2 graphics to draw upon
	 */
	public void draw(Graphics2D g2) {
		int[] xpoints = new int[]{(int)p1.getX(), (int)p2.getX(), (int)p3.getX()};
		int[] ypoints = new int[]{(int)p1.getY(), (int)p2.getY(), (int)p3.getY()};
		Shape triangle = new Polygon(xpoints, ypoints, 3);
		g2.setStroke(new BasicStroke(2));
		g2.setColor(pColor);
		g2.draw(triangle);
		drawHalo(g2, "focus");
		drawHalo(g2, "selection");
		drawSelections(g2);		
		rotate(5);
	}
	
	/**
	 * Set velocity vector for player
	 * @param dest next target node
	 */
	private void setVelocity(Node dest){
		double deltaX = dest.getX() - x;
		double deltaY = dest.getY() - y;
		double mod = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		dx = deltaX/mod * speed;
		dy = deltaY/mod * speed;

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
	public int getWealth() {		
		return wealth;
	}
	
	@Override
	public synchronized void drawHalo(Graphics2D g2, String type) {
		
		Node node = null;
		if(type.equals("selection")) {
			node = selected;
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
		else {
			for(Node candidate: candidates) {
				double cx = candidate.getInstX();
				double cy = candidate.getInstY();
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
	}
	
	/**
	 * rotate the player's little triangle a bit
	 * @param increment in degree
	 */
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
	
	@Override
	public void move() {
		
		dx = 0;
		dy = 0;
		if(!destinations.isEmpty()) {
			Node nextTarget = destinations.peek();
			if(Math.abs(x - nextTarget.getX()) < 1
			   && Math.abs(y - nextTarget.getY()) < 1){
				currentNode = destinations.poll();
			}
			if(!destinations.isEmpty()) {		
				nextTarget = destinations.peek();
				if(!galaxy.hasEdge(currentNode, nextTarget)) {
					galaxy.buildEdge(currentNode, nextTarget, this);
				}
				setVelocity(nextTarget);
			}else {
				candidates.clear();
			}
		}
		x += dx;
		y += dy;
	}
	
	@Override
	public void think() {
		
		if(!isThinking &&!inMotion()) {
			
			isThinking = true;
			Analyzer a = new Analyzer();
			Thread thinking = new Thread(a);
			thinking.start();
			
			try {
				thinking.join();
				isThinking = false;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class Analyzer implements Runnable {

		@Override
		public void run() {
			
			LinkedList<Node> path = breadthFirstSearch();
			destinations.addAll(path);
			return;
		}
	}
	
	private LinkedList<Node> breadthFirstSearch() {
		
		LinkedList<Node> res = new LinkedList<>();
		Queue<Node> queue = new LinkedList<>();
		HashSet<Node> visited = new HashSet<>();
		queue.offer(currentNode);
		
		while(!queue.isEmpty()) {
			Node node = queue.poll();
			List<Node> adjList = node.getNeighbors();	
			Set<Node> neighbors = galaxy.getNeighboringNodes(node);
			for(Node discovery: neighbors) {
				if(StarCluster.find(node) != StarCluster.find(discovery)) {
					if(!candidates.contains(discovery)) {
						discovery.setPredecessor(node);
						addCandidate(discovery);
						try { Thread.sleep(50);} catch (InterruptedException e) {}
					}
				}
			}
			for(Node connected: adjList) {
				if(!visited.contains(connected) ) {
					queue.offer(connected);
				}
			}
			visited.add(node);
		}
		
		Node luckNode = candidates.get(Galaxy.generator.nextInt(candidates.size()));
		List<Node> path = Navigator.findSimplePath(currentNode, luckNode.getPredecessor());
		res.addAll(path);
		res.add(luckNode);
		
		setSelected(luckNode);
		
		return res;
	}
	
	private synchronized void addCandidate(Node candidate) {
		
		candidates.add(candidate);
	}
	
	@Override
	public LinkedList<Node> getSelections() {
		return sequence;
	}
	
	/**
	 * Draw selected sequence of nodes
	 * @param g2
	 */
	private void drawSelections(Graphics2D g2) {

		for(int i = 1; i < sequence.size(); i++) {	
			
			Shape line = new Line2D.Double( sequence.get(i - 1).getInstX(), 
											sequence.get(i - 1).getInstY(),
											sequence.get(i).getInstX(), 
											sequence.get(i).getInstY());		
			g2.setStroke(dashStroke);	
			g2.setColor(pColor);
			g2.draw(line);
		}
	}

	
	@Override
	public void buildPath() {}
	
	@Override
	public boolean inMotion() {
		return !destinations.isEmpty();
	}
	
	/**
	 * Private class in both human and computer player
	 * Stores locations of the small triangle used to
	 * represent player
	 */
	private class Point {
		
		private double x, y;
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
		public double getX() {
			return x;
		}
		public double getY() {
			return y;
		}
		public void setX(double x) {
			this.x = x;
		}
		public void setY(double y) {
			this.y = y;
		}
	}
}
