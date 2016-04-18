package UniversePackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A gigantic galaxy
 * A container of the entire galaxy
 * A galaxy has planets and satellites
 */
public class Galaxy {

    // every galaxy has a star
    public Planet star;
    // a galaxy must have some planets
    public List<Planet> planets;
    // a galaxy has some size
    public int height;
    public int width;
    public int margin;
    public static Random generator;

    public Galaxy(int height, int width, int margin, int numPlanets) {

        this.width = width;
        this.height = height;
        this.margin = margin;
        generator = new Random();
        init(numPlanets);
    }

    /**
     * creates all planets on the star board
     * @param numPlanets number of planets existing in the galaxy
     * @throws IllegalArgumentException
     */
    public void init(int numPlanets) throws IllegalArgumentException {
    	
        // initialize container for planets
        planets = new ArrayList<>();

        star = new GiantPlanet(height/2, width/2);
        planets.add(star);

        int leftLim = margin;
        int upperLim = margin;
        int horizontalRange = width - margin * 2;
        int verticalRange = height - margin * 2;
        int trialLimit = 1000;

        for(int i = 0; i < numPlanets; i++) {

            Planet planet = null;
            int counter = 0;
            while(counter < trialLimit){
                planet = Planet.createPlanet(leftLim+generator.nextInt(horizontalRange),upperLim+generator.nextInt(verticalRange));
                for(Planet existingPlanet: planets){
                    if(overlap(planet, existingPlanet)) {
                        counter++;
                        continue;
                    }
                }
                break;
            }
            if(planet == null) 
            	throw new IllegalArgumentException("You can't put so many planets on board");      
            
            planets.add(planet);
        }
    }
    
    /**
     * Every planet in the galaxy takes a small step
     */
    public void makeStep() {
    	
    	for(Planet planet: planets) {
    		planet.move(0, 0);
    	}
    }

    /**
     * Determines if two planets overlap
     * @param lhs left-hand-side planet
     * @param rhs right-hand-side planet
     * @return true if planets do overlap
     */
    public boolean overlap(Planet lhs, Planet rhs) {

        double lx = (lhs.getPosition())[0];
        double ly = (lhs.getPosition())[1];
        double rx = (rhs.getPosition())[0];
        double ry = (rhs.getPosition())[1];
        double centerDist = Math.sqrt(Math.pow(lx - rx, 2) + Math.pow(ly - ry, 2));

        return centerDist <= lhs.getRadius() + rhs.getRadius();
    }
}
