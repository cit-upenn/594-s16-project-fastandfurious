package UniversePackage;

import java.util.Observable;
import java.util.Random;

/**
 * A gigantic galaxy
 * A container of the entire galaxy
 * A galaxy has planets and satellites
 */
public class Galaxy extends Observable{

    // a galaxy has some size
    public int height;
    public int width;
    public int gridLength;
    public static Random generator;
    public Node[][] starboard;
    

    public Galaxy(int width, int height, int gridLength, int numPlanets) {

        this.width = width;
        this.height = height;
        this.gridLength = gridLength;

        generator = new Random();
        init(numPlanets);
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
    	starboard[numRows-2][numCols-2]= new Planet(  (numCols - 2) * gridLength, (numRows - 2) * gridLength);
    	
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
    }
    
    /**
     * Every planet in the galaxy takes a small step
     */
    public void makeStep() {
    		
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
 
}
