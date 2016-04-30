package UniversePackage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import player.ComputerPlayer;
import player.HumanPlayer;
import player.Player;


/**
 * A gigantic galaxy
 * A container of the entire galaxy
 * A galaxy has planets and satellites
 */
public class Galaxy extends Observable{

    // a galaxy has some size
    private int height;
    private int width;
    private int gridLength;
    private Timer timer;
    private Player[] player;
    private int numPlanets;
    private Node[][] starboard;
    private Timer moneyMachine;
    public static Random generator;
    private Map<Node, List<Edge>> adjList;
    private Lock lock;
    
    public Galaxy(int width, int height, int gridLength, int numPlanets) {

        this.width = width;
        this.height = height;
        this.gridLength = gridLength;

        generator = new Random();
        adjList = new HashMap<>();
        this.numPlanets = numPlanets;
        
        lock = new ReentrantLock();
    }

    /**
     * creates all planets on the star board
     * @param numPlanets number of planets existing in the galaxy
     * @throws IllegalArgumentException
     */
    public void init(int numPlanets) throws IllegalArgumentException {
    	 
    	int numRows = height / gridLength + 1;
    	int numCols = width / gridLength + 2;
    	starboard = new Node[numRows][numCols];    	
    	
    	/* place planets on board */
    	
    	starboard[1][1] = new Planet(gridLength, gridLength);
    	starboard[numRows-2][numCols-2]= new Planet((numCols - 2) * gridLength, (numRows - 2) * gridLength);
    	
    	while(numPlanets > 0) {
    		int row = 1 + generator.nextInt(numRows - 1);
    		int col = 1 + generator.nextInt(numCols - 1);
    		if(starboard[row][col] == null && row != numRows - 1 && col != numCols - 1) {
    			starboard[row][col] = new Planet(col *  gridLength, row * gridLength);
    			numPlanets--;
    		}
    	}
    	
    	/* fill-in the vacancy */
    	for(int row = 1; row < numRows - 1; row++) {
    		for(int col = 1; col < numCols - 1; col++) {
    			if(starboard[row][col] == null) {
    				starboard[row][col] = new SupplyStation(col * gridLength, row * gridLength);
    			}
    		}
    	}
    	
    	/* initialize adjacency list */
    	for(int row = 1; row < numRows - 1; row++) {
    		for(int col = 1; col < numCols - 1; col++) {			
    			adjList.put(starboard[row][col], new LinkedList<>());
    		}
    	}
    	
    	// for every node in the galaxy, make set
    	StarCluster.makeSet(starboard);
		
		/* initialize players */
		player = new Player[2];
		// player[0] = new HumanPlayer(starboard[1][1].getX(), starboard[1][1].getY(), new Color(0, 153, 255), this, "Tony"); 
		
		player[0] = new ComputerPlayer(starboard[1][1].getX(), starboard[1][1].getY(), new Color(0, 153, 255), this, "Tony");
		
		player[0].setCurrentNode(starboard[1][1]);
		starboard[1][1].setRuler(player[0]);
		
		player[1] = new ComputerPlayer(starboard[numRows-2][numCols-2].getX(),starboard[numRows-2][numCols-2].getY(), Color.yellow , this, "Steve");
		player[1].setCurrentNode(starboard[starboard.length-2][starboard[0].length-2]);
		starboard[starboard.length-2][starboard[0].length-2].setRuler(player[1]);
	
    }
    
    public void start() {

    	init(numPlanets);
    	timer = new Timer(40, new Strobe());
    	timer.start();
    	
    	moneyMachine = new Timer(2000, new MoneyMaker());
    	moneyMachine.start();
    }
    
    /**
     * Every planet in the galaxy takes a small step
     */
    public void makeStep() {
    	
    	for(int i = 1; i < starboard.length - 1; i++) 
    		for(int j = 1; j < starboard[0].length - 1; j++) 
    			starboard[i][j].move();	
    	  	
    	player[0].think();
    	player[1].think();
    	
    	player[0].move();
    	player[1].move();
    	
    	this.setChanged();
    	this.notifyObservers();
    }
    
    public Node[][] getStarBoard() {
    	return starboard;
    }
    
    /**
     * @param num number of player
     * @return a player in the galaxy
     */
    public Player getPlayer(int num) {
    	if (num > 1 || num < 0) 
    		return null;
    	return player[num];
    }
    
    /**
	 * build an edge between two nodes
	 * @param lhs left-hand-side node
	 * @param rhs right-hand-side node
	 * @return true if operation is successful
	 */
    public boolean buildEdge(Node current, Node togo, Player p) {
    	try{
    		lock.lock();
    		
        	// detects invalid input
        	if(current == null||togo == null) { System.err.println("Null pointer"); return false; }
        	
        	// check for adjacency
        	if(!areAdjacentNodes(current, togo)) {
        		System.out.println("weird");
        		return false;
        	}
        	// check existence
        	if(hasEdge(current, togo)) {
            	System.err.println("Edge already in existence");
            	return false;
        	}
        	
        	Edge out = new Edge(current, togo, p);
        	Edge in = new Edge(togo, current, p);
        	
        	if(!p.addWealth(-1 * out.getCost())) {
        		return false;
        	}
    		
    		if(togo.getRuler() != p) {	
    			if(!captureNode(p, togo)) {
    				p.addWealth(out.getCost());
    				return false;
    			}
    		}
    		
    		adjList.get(current).add(out);
    		adjList.get(togo).add(in);
        	// union nodes if edge build was successful
        	if(StarCluster.find(current) != StarCluster.find(togo)) StarCluster.union(current, togo);
        	
        	return true;
    		
    	}finally{
    		lock.unlock();
    	}
    	

	}
    
    /**
     * destroy an existing edge in the galaxy
     * @param lhs end point of edge
     * @param rhs end point of edge
     * @return true if destruction successful
     */
    public boolean neutralizeNode(Player p, Node target) {
    		
    	try {  		  		
        	if(target.getRuler() == null || target.getRuler() == p) {
        		System.out.println("No target to neutralize");
        		return false;
        	}
        	Player otherPlayer = target.getRuler();
        	
        	int cost = target.getResourceLevel() * 2;
        	
        	if(!otherPlayer.addWealth(-1 * cost)) {
        		System.out.println("can't afford to launch missle");
        		return false;
        	}
        	
        	target.setRuler(null);
        	for(Edge e1: adjList.get(target)) {
        		Node otherEnd = e1.getEnd();
        		for(Edge e2: adjList.get(otherEnd)) {
        			if(e2.getEnd() == target) {
        				adjList.get(otherEnd).remove(e2);
        				break;
        			}
        		}
        	}
        	adjList.get(target).clear();
        	otherPlayer.loseNode(target);
        	
        	SwingWorker<Void, Void> worker1 = new SwingWorker<Void, Void>(){
				@Override
				protected Void doInBackground() throws Exception {
		    		refactor(otherPlayer);
					return null;
				}
        	};
        	worker1.execute();
   		
    		return true;
    		
    	}finally {		
    	}
    	
    }
    
    public void refactor(Player p) {
    	
    	try{
    		lock.lock();
    		List<Node> all = new LinkedList<>(p.getNodesControlled());
        	Iterator<Node> it = all.iterator();
        	// Separate all nodes
        	while(it.hasNext()) {
        		StarCluster.seperateNode(it.next());
        	}
        	it = all.iterator();
        	HashSet<Node> visited = new HashSet<>();	
        	while(it.hasNext()) {
        		Node u = it.next();
        		if(!visited.contains(u)) {
        			depthFirstCluster(u, visited);
        		}
        	}
    	}finally{
    		lock.unlock();
    	}
    }
    
    public void depthFirstCluster(Node source, Set<Node> visited) {
    	
    	Stack<Node> stack = new Stack<>();
    	stack.push(source);
    	while(!stack.isEmpty()) {
    		Node u = stack.pop();
    		for(Edge e: adjList.get(u)) {
    			Node v = e.getEnd();
    			if(!visited.contains(v)) {
    				stack.push(v);
    				StarCluster.union(u, v);
    			}
    		}
    		visited.add(u);
    	}
    }
    
    /**
     * 
     * @param p
     * @param node
     * @return
     */
    public boolean captureNode(Player p, Node node) {
    	
    	try{
    		lock.lock();
        	if(!(p.addWealth(-node.getCost()))) {
        		return false;
        	}
        	
        	if(node.getRuler() != null) {
        		return false;
        	}
        	
        	node.setRuler(p);
        	p.controlNode(node);
        	return true;
    		
    	}finally{
    		lock.unlock();
    	}
    }
    		
    
    /**
     * 
     * @param node
     * @return
     */
    public Set<Node> getNeighboringNodes(Node node) {
    	
    	int row = (int)node.getY()/gridLength;
    	int col = (int)node.getX()/gridLength;
    	Set<Node> neighbors = new HashSet<>();
    	
    	for(int i = row - 1; i <= row + 1; i++) 
    		for(int j = col - 1; j <= col + 1; j++) 
    			if(starboard[i][j] != null && starboard[i][j] != node)
    				neighbors.add(starboard[i][j]);

    	return neighbors;
    }
    
    
    /**
     * This method determines if two nodes
     *  are adjacent to each other
     * @param lhs
     * @param rhs
     * @return
     */
    public boolean areAdjacentNodes(Node thisNode, Node otherNode) {
    	Set<Node> neighbors = getNeighboringNodes(thisNode);
    	return neighbors.contains(otherNode);
    }
    
    /**
     * @return adjacency list of edges
     */
    public Map<Node, List<Edge>> getEdges() {
    	return adjList;
    }
    
    /**
     * check if an edge exists
     * @param s start node
     * @param d source node
     * @return true if edge exists
     */
    public boolean hasEdge(Node start, Node end) {
    	
    	for(Edge edge: adjList.get(start)) {
    		if(edge.getEnd() == end) return true;
    	}
    	return false;
    }
    
    /**
     * @return grid length being used for current galaxy
     */
    public double getGridLength() {
    	return gridLength;
    }
    
    public Map<Node, List<Edge>> getAdjList(){
    	return adjList;
    }
    
    /**
     * @return the human player
     */
    public Player getHumanPlayer(){
    	for(int i = 0; i < player.length; i++) 
    		if(player[i] instanceof HumanPlayer) 
    			return player[i];
    	return null;
    }

    /**
     * Tells the model to advance one "step"
     */
    private class Strobe implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						makeStep();
						return null;
					}
			};
			worker.execute();
		}
    }
    
    private class MoneyMaker implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingWorker<Void, Void> maker = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					HashMap<Player, Integer> incomeMapping = new HashMap<>();
					incomeMapping.put(player[0], 0);
					incomeMapping.put(player[1], 0);
					for(int i = 0; i < starboard.length; i++) {
						for(int j = 0; j < starboard[0].length; j++) {
							Node  node = starboard[i][j];
							if(node != null) {
								Player ruler = node.getRuler();
								if(ruler != null && StarCluster.find(node) == StarCluster.find(ruler.getCurrentNode())) {
									int val = incomeMapping.get(ruler);
									incomeMapping.put(ruler, val + node.getResourceLevel());
								}
							}
						}
					}
					for(List<Edge> list: adjList.values()) {
						for(Edge e: list) {
							Player ruler = e.getRuler();
							int val = incomeMapping.get(ruler);
							if(val - 1 > 0) {
								incomeMapping.put(ruler, val - 1);
							}
						}
					}
					player[0].addWealth(incomeMapping.get(player[0]));
					player[1].addWealth(incomeMapping.get(player[1]));
					return null;
				}
			};
			maker.execute();
		}
    }
    
    /**
     * Locate node based on coordinates
     */
	public Node locateNode(double x, double y) {	
		int col = (int)(x/getGridLength()); double remainder1 = x % getGridLength();	
		if(remainder1 >= 48) col++;else if(remainder1 > 2) return null;
		int row = (int)(y/getGridLength());double remainder2 = y %getGridLength();
		if(remainder2 >= 48) row++;else if(remainder2 > 2) return null;
		return getStarBoard()[row][col];
	}

}
