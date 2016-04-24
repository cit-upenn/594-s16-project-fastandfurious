package player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

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
	private Color pColor;	
	private boolean isThinking;
	private final float dash1[] = {10.0f};
	private BasicStroke lineStroke = new BasicStroke(2.0f);
	private BasicStroke dashStroke = new BasicStroke(2.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f);
	private	Node selected;
	
	private HashSet<Node> nodesRuled;
	
	private ConcurrentLinkedQueue<Node> destinations;
	
	private Queue<Node> candidates;
	
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

		this.x = x;
		this.y = y;
		this.radius = 15;
		this.speed = 2;
		p1 = new Point(x, y - radius);
		p2 = new Point(x - radius * Math.cos(Math.PI/6), y + radius/2);
		p3 = new Point(x + radius * Math.cos(Math.PI/6), y + radius/2);
		this.galaxy = galaxy;
		this.isThinking = false;
		
		destinations = new ConcurrentLinkedQueue<>();
		
		sequence = new LinkedList<>();
		candidates = new PriorityQueue<Node>(new NodeComparator());
		
		nodesRuled = new HashSet<Node>();
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
		//TODO
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
		
		// System.out.println(destinations);
		
		dx = 0;
		dy = 0;
		if(!isThinking) {
			Node nextTarget = destinations.peek();
			if(Math.abs(x - nextTarget.getX()) < 1
			   && Math.abs(y - nextTarget.getY()) < 1){
				currentNode = destinations.poll();
				nodesRuled.add(currentNode);
			}
			if(!destinations.isEmpty()) {		
				nextTarget = destinations.peek();
				
				if(!galaxy.hasEdge(currentNode, nextTarget)) {	
					
					if(!galaxy.buildEdge(currentNode, nextTarget, this)) {	
						
						clearDest();
					}
					return;
				}
				
				setVelocity(nextTarget);
				
			}else {
				candidates.clear();
				sequence.clear();
			}
		}
		x += dx;
		y += dy;
	}
	
	@Override
	public void think() {
		
		if(!isThinking&&!inMotion()) {
			
			isThinking = true;
			analyze();
			isThinking = false;
		}
	}
	
	private void analyze() {
		
		LinkedList<Node> newnodes = new LinkedList<>();
		int count = 0;
		int breadth = 5;
		Node choice = null;
		
		while(newnodes.isEmpty() && count < 4) {
			newnodes.addAll(bfs(breadth));
			breadth *= 2;
			count++;
		}
		
		
		for(Node candidate: newnodes) {
			addCandidate(candidate);
			try { Thread.sleep(20); } catch (InterruptedException e) {}
		}
		
		// wait a bit for things to catch up
		try {Thread.sleep(100); } 
		catch (InterruptedException e1) {}
		
		// if still empty
		// pick a random node;
		if(candidates.isEmpty()) {
			LinkedList<Node> allNodes = new LinkedList<>();
			allNodes.addAll(nodesRuled);
			choice = allNodes.get(Galaxy.generator.nextInt(allNodes.size()));	
		}
		else { 
			
			List<Node> sources = new LinkedList<>();
			PriorityQueue<List<Node>> pq = new PriorityQueue<>(new pathComparator());
			
			int threshold = 5;
			int search_depth = 4;
			
			HashMap<Node, Node> preds = new HashMap<>();
			
			while(!candidates.isEmpty() && threshold-- >= 0) {
				
				Node bestCandidate = getBestCandidate();
				sources.add(bestCandidate);
				preds.put(bestCandidate, bestCandidate.getPredecessor());
			}
			
			while(search_depth >= 0) {
				dfs(sources, search_depth--, pq);
			}
			
			List<Node> finalpath = pq.poll();
			
			if(!finalpath.isEmpty()) {
				
				Node head = finalpath.get(0);	
				Node pred = preds.get(head);	
				
				destinations.addAll(Navigator.findSimplePath(currentNode, pred));
				destinations.addAll(finalpath);
				
				addSelection(pred);
				for(Node n: finalpath) addSelection(n);
				
				setSelected(head);
			}
		}
	}
	
	/**
	 * TODO 
	 * @param breadthLim
	 * @return
	 */
	private synchronized LinkedList<Node> bfs(int breadthLim) {
		
		Queue<Node> queue = new LinkedList<>();
		HashSet<Node> visited = new HashSet<>();
		LinkedList<Node> reachable = new LinkedList<>();
		
		queue.offer(currentNode);
		
		int count = 0;
		
		while(!queue.isEmpty()) {		
			Node node = queue.poll();	
			if(node == null) {		
				count++;
				if(count <= breadthLim) continue;
				else break;
			}
			List<Node> adjList = node.getNeighbors();	
			Set<Node> neighbors = galaxy.getNeighboringNodes(node);
			for(Node discovery: neighbors) {
				if(StarCluster.find(node) != StarCluster.find(discovery)) {
					if(!reachable.contains(discovery) && discovery.getRuler() == null) {
						discovery.setPredecessor(node);
						reachable.add(discovery);
					}
				}
			}
			
			for(Node connected: adjList) {
				if(connected != null && !visited.contains(connected)) {
					queue.offer(connected);
				}
			}
			queue.offer(null);
			visited.add(node);
		}
		
		return reachable;
	}
	
	/**
	 * 
	 * @param sources
	 * @param depthLim
	 * @param pq
	 */
	private synchronized void dfs(List<Node> sources, int depthLim, PriorityQueue<List<Node>> pq) {
		
		Iterator<Node> it = sources.iterator();
		
		while(it.hasNext()) {
			
			Node source = it.next();
			Stack<Node> stack = new Stack<Node>();
			HashSet<Node> visited = new HashSet<>();
			source.setDepth(0);
			source.setPredecessor(null);
			stack.push(source);
			while(!stack.isEmpty()) {
				
				Node node = stack.pop();
				
				if(node.getDepth() >= depthLim) {
					
					LinkedList<Node> ppath = new LinkedList<>();
					
					while(node != null) {
						ppath.addFirst(node);
						node = node.getPredecessor();
					}
					
					pq.add(ppath);
					
					continue;
				}
				
				
				List<Node> adjList = new LinkedList<>(galaxy.getNeighboringNodes(node));
				
				for(Node next: adjList) {
					if( StarCluster.find(currentNode) != StarCluster.find(next) 
						&& !visited.contains(next)
						&& !candidates.contains(next)
						&& next.getRuler() == null) {
						
						next.setDepth(node.getDepth() + 1);
						next.setPredecessor(node);
						
						stack.push(next);
					}
				}
				visited.add(node);
			}
			
		}
	}
	
	private synchronized void addCandidate(Node candidate) {
		candidates.offer(candidate);
	}
	
	private synchronized Node getBestCandidate() {
		return candidates.poll();
	}
	
	private synchronized void addSelection(Node selection) {
		sequence.add(selection);
	}
	
	private synchronized void removeSelection(Node selection) {
		sequence.remove(selection);
	}
	private synchronized void clearSelection() {
		sequence.clear();
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
			Shape line = new Line2D.Double(sequence.get(i - 1).getInstX(), sequence.get(i - 1).getInstY(), sequence.get(i).getInstX(),sequence.get(i).getInstY());		
			g2.setStroke(dashStroke);	
			g2.setColor(pColor);
			g2.draw(line);
		}
	}
	
	private synchronized void clearDest() {
		destinations.clear();
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
		rotate(10);
	}
	
	@Override
	public boolean inMotion() {
		return !destinations.isEmpty();
	}
	
	public class NodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node n1, Node n2) {
			return n2.getResourceLevel() - n1.getResourceLevel();
		}
	}

	@Override
	public synchronized void addWealth(int change) {
		wealth += change;
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
	
	private int evaluatePath(List<Node> path) {
		
		int value = 0;
		for(int i = 0; i < path.size(); i++) {		
			value += path.get(i).getResourceLevel();
		}
		return value;
	}
	
	/**
	 * 
	 *
	 */
	public class pathComparator implements Comparator<List<Node>> {
		@Override
		public int compare(List<Node> p1, List<Node> p2) {
			if(p1 != p2) {
				return evaluatePath(p2) - evaluatePath(p1);
			}else {
				return p1.size() - p2.size();
			}
		}
	}
}
