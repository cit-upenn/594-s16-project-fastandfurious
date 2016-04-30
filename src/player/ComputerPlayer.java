package player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
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
	private Set<Node> targetNodes;
	private String name;
	private Lock lock;
	private String status;
	private int prevWealth;
	
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
		
		wealth = 300;
		prevWealth = 0;

		this.x = x;
		this.y = y;
		this.radius = 15;
		this.speed = 2;
		p1 = new Point(x, y - radius);
		p2 = new Point(x - radius * Math.cos(Math.PI/6), y + radius/2);
		p3 = new Point(x + radius * Math.cos(Math.PI/6), y + radius/2);
		this.galaxy = galaxy;
		this.isThinking = false;
		
		nodesControlled = new HashSet<Node>();
		destinations = new ConcurrentLinkedQueue<>();
		candidates = new PriorityQueue<Node>(new NodeComparator());	
		targetNodes = new HashSet<>();
		
		sequence = new LinkedList<>();
		
		this.name = name;
		this.status = "Standby";
		
		lock = new ReentrantLock();
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
	public synchronized void move() {
		
		if(!isThinking && inMotion()) {
			Node position = galaxy.locateNode(x, y);
			status = "moving";
			if(position != null) currentNode = position;
			else {
				if(dx == 0 && dy == 0) {	
					clearStuffs();
					List<Node> all = new ArrayList<>();
					all.addAll(nodesControlled);
					Node choice = all.get(Galaxy.generator.nextInt(all.size()));
					currentNode = choice;
					x = choice.getX();
					y = choice.getY();
					return;
				}
			}
			Node dest = destinations.peek();
			if(currentNode == dest) {	
				destinations.poll();
				if(destinations.isEmpty()) {
					clearStuffs();
					dx = 0;
					dy = 0;
				}
			} 
			else if(galaxy.areAdjacentNodes(currentNode, dest)) {
				if(!galaxy.hasEdge(currentNode, dest)) {			
					if(galaxy.locateNode(x, y) != null) {
						if(dest.getRuler() == null) {	
							boolean success = galaxy.buildEdge(currentNode, dest, this);
							if(!success) {				
								clearStuffs();	
								generateRandomPath();
							}
						}
						else if(dest.getRuler() == this){
							boolean success = galaxy.buildEdge(currentNode, dest, this);
							if(!success) {				
								clearStuffs();	
							}
						} else {	
							try{
								lock.lock();
								if(targetNodes.contains(dest)) galaxy.neutralizeNode(this, dest);

							}finally{
								lock.unlock();
							}
							clearStuffs();
						}	
					}
					return;
				}
				setVelocity(dest);
				x += dx;
				y += dy;
			}
			else{
				clearStuffs();
				x = currentNode.getX();
				y = currentNode.getY();
				return;
			}
		}
		rotate(10);
	}
	
	private synchronized void clearStuffs() {
		
		destinations.clear();
		sequence.clear();
		candidates.clear();
		targetNodes.clear();
		setSelected(null);
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
		rotate(10);
	}
	
	@Override
	public void think() {
		
		if(!isThinking&&!inMotion()) {	
			try { lock.lock();status = "Thinking";isThinking = true;}finally{lock.unlock();}
			
			if(wealth <= prevWealth) {
				offensive();
				if(targetNodes.isEmpty()) explore();
				try {lock.lock();Thread.sleep(100);isThinking = false;	prevWealth = wealth;} catch (InterruptedException e) { e.printStackTrace();}finally{lock.unlock();}
				return;
			}
			
			switch(Galaxy.generator.nextInt(200)%20) {	
				case 0: 
				case 1:
				case 2:
				case 3:reinforce();break;
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9: offensive();break;
				default: explore();
			}
			
			try {lock.lock();Thread.sleep(100);isThinking = false;	prevWealth = wealth;} catch (InterruptedException e) { e.printStackTrace();}finally{lock.unlock();}
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
				lock.lock();
				candidates.add(candidate);
				lock.unlock();
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
				Node bestCandidate = candidates.poll();
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
			sequence.add(pred);
			for(Node n: finalpath){
				sequence.add(n);
			}
			setSelected(head);
			
		} catch (Exception e) {	
			clearStuffs();
			generateRandomPath();
		}	
	}
	
	/**
	 * Maintain existing grid
	 */
	private void reinforce() {
		
		try{	
			
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
				
				if(path1 == null || path2 == null) return;
				
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
		}
	}
	
	/**
	 * destroy some of your enemy's out-posts
	 * might destroy his central nodes as well
	 */
	private void offensive() {
		
		try{
			
			// to keep track of nodes that have been visited
			HashSet<Node> visited = new HashSet<>();
			HashMap<Node, Node> mapping = new HashMap<>();
			// FIFO queue for BFS
			Queue<Node> Q = new LinkedList<>();	
			// start point of search
			Node start = currentNode;
			
			PriorityQueue<Node> targets = new PriorityQueue<>(new NodeComparator());
			
			Q.offer(start);
			
			while(!Q.isEmpty()) {
				
				Node u = Q.poll();
				List<Edge> adjNodes = galaxy.getAdjList().get(u);
				Iterator<Edge> edgeIt = adjNodes.iterator();
				while(edgeIt.hasNext()) {
					Node v = edgeIt.next().getEnd();
					if(!visited.contains(v)) {		
						Q.offer(v);
					}
				}
				Set<Node> neighboringNodes = galaxy.getNeighboringNodes(u);
				Iterator<Node> nodeIt = neighboringNodes.iterator();	
				while(nodeIt.hasNext()) {	
					Node neighbor = nodeIt.next();
					if(neighbor.getRuler() != null && neighbor.getRuler() != this) {				
						targets.offer(neighbor);
						mapping.put(neighbor, u);
					}
				}
				visited.add(u);			
			}
			
			if(targets.isEmpty()) return;
			
			Node target = targets.poll();
			targetNodes.add(target);
			destinations.addAll(Navigator.findSimplePath(currentNode, mapping.get(target), galaxy));
			destinations.add(target);
			sequence.add(mapping.get(target));
			sequence.add(target);
			setSelected(target);
			
		} finally {}

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
						&& (next.getRuler() == null||next.getRuler() == this)) {
						
						next.setDepth(node.getDepth() + 1);
						next.setPredecessor(node);
						stack.push(next);
					}
				}
				visited.add(node);
			}	
		}
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
	
	@Override
	public synchronized void controlNode(Node node) {
		nodesControlled.add(node);
	}

	@Override
	public synchronized void loseNode(Node node) {
		nodesControlled.remove(node);
	}

	@Override
	public boolean isThinking() {
		return isThinking;
	}
}
