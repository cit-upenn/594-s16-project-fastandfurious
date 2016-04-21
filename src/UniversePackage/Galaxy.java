package UniversePackage;

import java.util.HashSet;
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
    	
    	for(int row = 1; row < numRows - 1; row++) {
    		for(int col = 1; col < numCols - 1; col++) {
    			if(starboard[row][col] == null) {
    				starboard[row][col] = new SupplyStation(col * gridLength, row * gridLength);
    			}
    		}
    	}
    	
    	// for every node in the galaxy, make set
    	StarCluster.makeSet(starboard);
    	
    	String[] refs = new String[5];
		refs[0] = "resources/duck.gif";
		refs[1] = "resources/duck2.gif";
		refs[2] = "resources/duck3.gif";
		
		player = new Player[2];
		player[0] = new HumanPlayer(refs, starboard[1][1].getX(), starboard[1][1].getY(), Color.green); 
		((HumanPlayer)player[0]).setCurrentNode(starboard[1][1]);
		player[1] = new ComputerPlayer(Color.orange);
    }
    
    public void start() {

    	init(numPlanets);
    	
    	timer = new Timer(35, new Strobe());
    	timer.start();
    }
    
    /**
     * Every planet in the galaxy takes a small step
     */
    public void makeStep() {
    	
    	for(int i = 1; i < starboard.length - 1; i++) {	
    		for(int j = 1; j < starboard[0].length - 1; j++) {
    			starboard[i][j].move();		
    		}
    	}
    	
    	player[0].move();
    	player[1].move();
    	
    	this.setChanged();
    	this.notifyObservers();
    }
    
    /**
     * factory method. produce a new node instance.
     * @return new node instance
     */
    public static Node generateNode(double x, double y){  	
    	int type = generator.nextInt(2);
    	Node res = null;
    	switch(type) {  	
    		case 0: res = new SupplyStation(x, y); break;
    		case 1: res = new Planet(x, y); break;
    		default: System.err.println("Wth?");
    	}
    	return res;
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
 
    public Player getPlayer(int num) {
    	if (num > 1 || num < 0) {
    		return null;
    	}
    	return player[num];
    }
    
    /**
	 * build an edge between two nodes
	 * @param lhs left-hand-side node
	 * @param rhs right-hand-side node
	 * @return true if operation is successful
	 */
    public boolean buildEdge(Node lhs, Node rhs) {
		
    	if(lhs == null || rhs == null
		   ||Math.abs(lhs.getX() - rhs.getX()) > 50 
		   || Math.abs(lhs.getY() - rhs.getY()) > 50) {
			return false;
		}
		lhs.getNeighbors().add(rhs);
		rhs.getNeighbors().add(lhs);
		
		Edge edge = new Edge(lhs, rhs);
		edges.add(edge);
		
    	this.setChanged();
    	this.notifyObservers();
		
		return true;
	}
    
    /**
     * 
     * @param node
     * @return
     */
    private Set<Node> getNeighboringNodes(Node node) {
    	
    	int row = (int)node.getY()/gridLength;
    	int col = (int)node.getX()/gridLength;
    	Set<Node> neighbors = new HashSet<>();
    	
    	for(int i = row - 1; i <= row + 1; i++) {
    		for(int j = col - 1; j <= col + 1; j++) {
    			neighbors.add(starboard[i][j]);
    		}
    	}
    	
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
     * @return grid length being used for current galaxy
     */
    public double getGridLength() {
    	return gridLength;
    }

}
