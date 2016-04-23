package UniversePackage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
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
    public static Random generator;
    private Node[][] starboard;
    private Timer timer;
    private Player[] player;
    private List<Edge> edges;
    private int numPlanets;
    
    public Galaxy(int width, int height, int gridLength, int numPlanets) {

        this.width = width;
        this.height = height;
        this.gridLength = gridLength;

        generator = new Random();
        edges = new LinkedList<>();
        this.numPlanets = numPlanets;
        
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
    	
    	// for every node in the galaxy, make set
    	StarCluster.makeSet(starboard);
		
		/* initialize players */
		player = new Player[2];
		player[0] = new HumanPlayer(starboard[1][1].getX(), starboard[1][1].getY(), Color.orange, this); 
		player[0].setCurrentNode(starboard[1][1]);
		player[1] = new ComputerPlayer(starboard[numRows-2][numCols-2].getX(),starboard[numRows-2][numCols-2].getY(), Color.red, this);
		player[1].setCurrentNode(starboard[starboard.length-2][starboard[0].length-2]);
    }
    
    public void start() {

    	init(numPlanets);
    	
    	timer = new Timer(25, new Strobe());
    	timer.start();
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
    public boolean buildEdge(Node lhs, Node rhs, Player p) {
    	
    	if(lhs.getNeighbors().contains(rhs) || rhs.getNeighbors().contains(lhs)) {
    		System.out.println("Edge already in existance");
    		return false;
    	}
		lhs.getNeighbors().add(rhs);
		rhs.getNeighbors().add(lhs);
		Edge edge = new Edge(lhs, rhs, p);
		edges.add(edge);
    	this.setChanged();
    	this.notifyObservers();
    	StarCluster.union(lhs, rhs);
		return true;
	}
    
    /**
     * destroy an existing edge in the galaxy
     * @param lhs end point of edge
     * @param rhs end point of edge
     * @return true if destruction successful
     */
    public boolean destroyEdge(Node lhs, Node rhs) {
    	
    	if( lhs == null 
    		|| rhs == null 
    		|| Math.abs(lhs.getX() - rhs.getX()) > 50 
    		|| Math.abs(lhs.getY() - rhs.getY()) > 50
    		|| StarCluster.find(lhs) != StarCluster.find(rhs)) {

    		System.out.println("Edge does not exist");
    		return false;
    	}
    	lhs.getNeighbors().remove(rhs);
    	rhs.getNeighbors().remove(lhs);
    	Iterator<Edge> edgeIterator = edges.iterator(); 
    	while(edgeIterator.hasNext()) {
    		Edge edge = edgeIterator.next();
    		if(edge.containsPoint(lhs) || edge.containsPoint(rhs)) {
    			edgeIterator.remove();
    			break;
    		}
    	}
    	return true;
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
     * @return list of edges
     */
    public List<Edge> getEdges() {
    	return this.edges;
    }
    
    /**
     * check if an edge exists
     * @param s start node
     * @param d source node
     * @return true if edge exists
     */
    public boolean hasEdge(Node s, Node d) {
    	
    	for(Edge e: edges) {
    		if(e.containsPoint(s)&&e.containsPoint(d)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * @return grid length being used for current galaxy
     */
    public double getGridLength() {
    	return gridLength;
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

}
