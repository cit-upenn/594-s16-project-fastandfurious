package player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import UniversePackage.Edge;
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
	private Set<Node> nodesControlled;
	private ConcurrentLinkedQueue<Node> destinations;
	private Queue<Node> candidates;
	private String name;
	
	private Lock lock;
	
	private String status;
	
	/**
	 * Constructor 
	 * Creates new ComputerPlayer instances
	 * @param x initial horizontal position of the player
	 * @param y initial vertical position of the player
	 * @param pColor representative color of the player
	 * @param galaxy a reference to the galaxy (game environment)
	 */
	public ComputerPlayer(double x, double y, Color pColor, Galaxy galaxy, String name) {		
		
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
		candidates = new PriorityQueue<Node>(new NodeComparator());	
		
		nodesControlled = new HashSet<Node>();
		
		sequence = new LinkedList<>();
		
		this.name = name;
		this.status = "Standby";
		
		lock = new ReentrantLock();
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
	public void move() {
		
		dx = 0;
		dy = 0;
		
		if(!isThinking && inMotion()) {
			
			status = "moving";
			
			Node position = locateNode(x, y);
			if(position != null) {
				currentNode = position;
			}
			Node dest = destinations.peek();
			
			if(currentNode == dest) {	
				destinations.poll();
				if(destinations.isEmpty()) {
					clearStuffs();
				}
				return;
			} 
			else {
				if(!galaxy.hasEdge(currentNode, dest)&&galaxy.areAdjacentNodes(currentNode, dest)) {			
					if(dest.getRuler() == null) {						
						boolean success = galaxy.buildEdge(currentNode, dest, this);
						if(!success) {				
							clearStuffs();	
							generateRandomPath();
							return;
						}
					}
					else if(dest.getRuler() == this){
						galaxy.buildEdge(currentNode, dest, this);
						clearStuffs();
						return;
					}
					else {
						clearStuffs();
						return;
					}
				}
				setVelocity(dest);
			}
		}
		x += dx;
		y += dy;
	}
	
	private synchronized void clearStuffs() {
		
		clearDest();
		clearSelection();
		candidates.clear();
	}
	
	@Override
	public void think() {
		
		if(!isThinking&&!inMotion()) {
			
			try {
				lock.lock();
				isThinking = true;	
			}finally{
				lock.unlock();
			}
			
			
			status = "Thinking";
			
			// long tStart = System.currentTimeMillis();
			
			// TODO attack, defend, reinforce if time permits
			
			int dice = Galaxy.generator.nextInt(100)%10;
			
			switch(dice) {
			
				case 0: 
				case 1:
				case 2:
				case 3:maintain();break;
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:explore(); break;
				default:
			}
			
			
			// long tEnd = System.currentTimeMillis();
			
			// System.out.println(this + " took " + (tEnd - tStart) + "ms to think.");
			
			try {
				lock.lock();
				isThinking = false;	
			}finally{
				lock.unlock();
			}
		}
	}
	
	/**
	 * Basic strategy of a computer player
	 */
	private void explore() {
		
		try {
			
			LinkedList<Node> borderNodes = new LinkedList<>();
			int count = 0;
			int bridthLim = 5;
			while(borderNodes.isEmpty() && count < 4) {
				borderNodes.addAll(bfs(bridthLim));
				bridthLim *= 2;
				count++;
			}
			
			for(Node candidate: borderNodes) {
				addCandidate(candidate);
				try { Thread.sleep(20); } catch (InterruptedException e) {}
			}
			
			// wait a bit for human eyes to catch up
			try {Thread.sleep(100); } 
			catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			List<Node> sources = new LinkedList<>();
			
			PriorityQueue<List<Node>> pq = new PriorityQueue<>(new pathComparator());
			HashMap<Node, Node> preds = new HashMap<>();
			
			int threshold = 3;
			int depthLim = 4;
			
			while(!candidates.isEmpty() && threshold-- > 0) {
				Node bestCandidate = getBestCandidate();
				sources.add(bestCandidate);
				preds.put(bestCandidate, bestCandidate.getPredecessor());
			}
			while(depthLim >= 0) {
				// search all possible paths within ranges
				dfs(sources, depthLim--, pq);
			}
			
			// pick the best path
			List<Node> finalpath = pq.poll();
			Node head = finalpath.get(0);	
			Node pred = preds.get(head);		
			destinations.addAll(Navigator.dijkstra(currentNode, pred, galaxy));
			destinations.addAll(finalpath);
			addSelection(pred);
			for(Node n: finalpath) addSelection(n);	
			setSelected(head);
			
		} catch (Exception e) {	
			clearStuffs();
			generateRandomPath();
		}	
	}
	
	/**
	 * Maintain existing grid
	 */
	private void maintain() {
		
		try{	
			lock.lock();
			List<Node> pool = new LinkedList<>();
			outer: for(Node candidate: nodesControlled) {
				Set<Node> neighbors = galaxy.getNeighboringNodes(candidate);
				for(Node neighbor: neighbors) {
					if(nodesControlled.contains(neighbor) && !galaxy.hasEdge(candidate, neighbor)) {
						pool.add(candidate);
						continue outer;
					}
				}
			}
			if(!pool.isEmpty()) {
				
				Collections.shuffle(pool); 	
				Node candidate = pool.get(Galaxy.generator.nextInt(pool.size()));
				pool.clear();
				Set<Node> neighbors = galaxy.getNeighboringNodes(candidate);
				for(Node neighbor: neighbors) {
					if(nodesControlled.contains(neighbor) && !galaxy.hasEdge(candidate, neighbor)) {
						pool.add(neighbor);
					}
				}
				
				Node endpoint1 = candidate;
				Node endpoint2 = pool.get(Galaxy.generator.nextInt(pool.size()));

				List<Node> path1 = Navigator.findSimplePath(currentNode, endpoint1, galaxy);
				List<Node> path2 = Navigator.findSimplePath(currentNode, endpoint2, galaxy);
				List<Node> thePath = path1;
				
				if(path2.size() < path1.size()) {
					Node temp = endpoint2;
					endpoint2 = endpoint1;
					endpoint1 = temp;
					thePath = path2;
				}
				
				setSelected(endpoint1);
				
				destinations.addAll(thePath);
				destinations.add(endpoint2);
				
				sequence.add(endpoint1);
				sequence.add(endpoint2);
			}
		
		}finally{
			
			lock.unlock();
		}
	}
	
	/**
	 * Randomly pick a node other than the
	 * current node and generate path to
	 * that node
	 */
	private void generateRandomPath() {
		Node choice = currentNode;	
		while(choice == currentNode) {
			List<Node> all = new ArrayList<>();
			all.addAll(nodesControlled);
			choice = all.get(Galaxy.generator.nextInt(all.size()));
		}
		setSelected(choice);
		destinations.addAll(Navigator.dijkstra(currentNode, choice, galaxy));
	}
	
	/**
	 * Search reachable nodes adjacent to current reign of player
	 * @param breadthLim set a limit on search area / prevents machine from getting too hot
	 * @return a collection of candidate nodes
	 */
	private synchronized LinkedList<Node> bfs(int breadthLim) {
		
		// prepare tools
		Queue<Node> queue = new LinkedList<>();
		HashSet<Node> visited = new HashSet<>();
		LinkedList<Node> reachable = new LinkedList<>();
		
		// set starting point
		queue.offer(currentNode);
		int hops = 0;
		
		// start search
		while(!queue.isEmpty()) {		
			Node node = queue.poll();	
			if(node == null) {		
				hops++;
				if(hops <= breadthLim) continue;
				else break;
			}
			List<Edge> adjList = galaxy.getAdjList().get(node);
			Set<Node> neighbors = galaxy.getNeighboringNodes(node);
			for(Node discovery: neighbors) {
				if(StarCluster.find(node) != StarCluster.find(discovery)) {
					if(!reachable.contains(discovery) && discovery.getRuler() == null) {
						discovery.setPredecessor(node);
						reachable.add(discovery);
					}
				}
			}
			for(Edge edge: adjList) {
				if(!visited.contains(edge.getEnd())) {
					queue.offer(edge.getEnd());
				}
			}
			queue.offer(null);
			visited.add(node);
		}
		
		// return result
		return reachable;
	}
	
	/**
	 * Perform depth first search from multiple sources
	 * in order to find the best path to explore
	 * @param sources starting points for depth-first searching
	 * @param depthLim set a limit on search depth / prevents machine from getting too hot
	 * @param pq a priority used for storing results
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
	
	private synchronized void clearSelection() {
		sequence.clear();
	}
	
	private synchronized void clearDest() {
		destinations.clear();
	}
		
	@Override
	public LinkedList<Node> getSelections() {
		return sequence;
	}
	
	@Override
	public boolean inMotion() {
		return !destinations.isEmpty();
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

	@Override
	public Node getSelected() {
		return selected;
	}

	@Override
	public void setSelected(Node selection) {
		this.selected = selection;
	}

	@Override
	public void setFocus(Node focus) {}
	
	@Override
	public int getWealth() {		
		return wealth;
	}
	
	public class NodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node n1, Node n2) {
			int diff =  n2.getResourceLevel() - n1.getResourceLevel();
			return diff;
		}
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
	public synchronized boolean addWealth(int change) {
		
		if(wealth + change > 100000) {
			return true;
		}
		else if(wealth + change < 0) {		
			return false;
		}
		
		wealth += change;
		return true;
	}
	
	/**
	 * evaluate a given path
	 * @param path a sequence of nodes
	 * @return value of the path
	 */
	private int evaluatePath(List<Node> path) {
		int value = 0;
		for(int i = 0; i < path.size(); i++) {		
			value += path.get(i).getResourceLevel();
		}
		return value;
	}
	
	/**
	 * Compare values of paths
	 */
	public class pathComparator implements Comparator<List<Node>> {
		@Override
		public int compare(List<Node> p1, List<Node> p2) {
			
			int value1 = evaluatePath(p1);
			int value2 = evaluatePath(p2);
			if(value1 != value2) {
				return value2 - value1;
			}else {
				if(p1.size() != p2.size()) {
					return p1.size() - p2.size();
				}
				else {
					Node h1 = p1.get(0);
					Node h2 = p2.get(0);
					double dist1 = Math.pow(h1.getX() - currentNode.getX(), h1.getY() - currentNode.getY());
					double dist2 = Math.pow(h2.getX() - currentNode.getX(), h2.getY() - currentNode.getY());		
					return (int)dist1 - (int)dist2;
				}
			}
		}
	}
	
	public Set<Node> getNodesControlled() {
		
		return nodesControlled;
	}
	
	@Override
	public String toString() {
		return this.name + "(computer)";
	}

	@Override
	public String getStatus() {
		return status;
	}
	
	private Node locateNode(double x, double y) {
		
		int col = (int)(x/galaxy.getGridLength());
		double remainder1 = x % galaxy.getGridLength();	
		if(remainder1 >= 49) col++;
		else if(remainder1 > 1) return null;
		int row = (int)(y/galaxy.getGridLength());
		double remainder2 = y % galaxy.getGridLength();
		if(remainder2 >= 49) row++;
		else if(remainder2 > 1) return null;
		return galaxy.getStarBoard()[row][col];
	}

	@Override
	public synchronized void controlNode(Node node) {
		nodesControlled.add(node);
	}

	@Override
	public synchronized void loseNode(Node node) {
		nodesControlled.remove(node);
	}
}
