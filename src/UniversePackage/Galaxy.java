package UniversePackage;

import java.util.Observable;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingWorker;
import javax.swing.Timer;


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
    public Timer timer;
    

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
    
    public void start() {
    	
    	timer = new Timer(50, new Strobe());
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
 
}
