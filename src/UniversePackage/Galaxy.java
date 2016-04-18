package UniversePackage;

import java.util.ArrayList;
import java.util.List;
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
    

    public Galaxy(int width, int height, int gridLength) {

        this.width = width;
        this.height = height;
        this.gridLength = gridLength;


        this.width = width;
        this.height = height;
        generator = new Random();
        init();
    }

    /**
     * creates all planets on the star board
     * @param numPlanets number of planets existing in the galaxy
     * @throws IllegalArgumentException
     */
    public void init() throws IllegalArgumentException {
    	 
    	int numRows = height/gridLength;
    	int numCols = width/gridLength;
    	
    	starboard = new Node[numRows][numCols];
    	
    	
    }
    
    /**
     * Every planet in the galaxy takes a small step
     */
    public void makeStep() {
    	
    	
    	this.notifyObservers();
    }

}
