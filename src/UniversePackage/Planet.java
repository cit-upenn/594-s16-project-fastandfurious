package UniversePackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Template for all kinds of planet
 */
public abstract class Planet {

    // position of the planet
    public double x, y;
    public double lowerRadiusLimit;
    public double upperRadiusLimit;
    public double radius;
    public List<Planet> satellites;

    /**
     * creates a new planet instance
     * will never be invoked since
     * Planet class is abstract
     * @param x initial horizontal position
     * @param y initial vertical position
     */
    public Planet(double x, double y) {
        this.x = x;
        this.y = y;
        satellites = new ArrayList<>();
    }

    /**
     * take a small step
     * @param dx tiny change in x
     * @param dy tiny change in y
     * @return true if move was made successfully
     */
    public boolean move(double dx, double dy) {

        x += dx;
        y += dy;
        return true;
    }

    /**
     * get current position of the planet
     * @return
     */
    public double[] getPosition() {
        return new double[]{x, y};
    }

    /**
     * retrieve radius of the current planet
     * @return radius as a double
     */
    public double getRadius() {
        return radius;
    }
    
    /**
     * factory method, creates a new planet instance
     * @return a newly created planet instance
     */
    public static Planet createPlanet(int x, int y){
    	
    	int sizeNum = Galaxy.generator.nextInt(4);
    	
    	Planet newPlanet = null;
    	
    	switch(sizeNum) {
    	
    		case 0: newPlanet = new SmallPlanet(x, y);break;
    		case 1: newPlanet = new MediumPlanet(x, y);break;
    		case 2: newPlanet = new HugePlanet(x, y);break;
    		case 3: newPlanet = new GiantPlanet(x, y);break;
    		default: System.out.println("How on earth did you get here?");
    	}
    	
    	return newPlanet;
    }

}
