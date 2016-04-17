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
    Random generator;

    public Galaxy(int height, int width, int margin, int numPlanets) {

        this.width = width;
        this.height = height;
        this.margin = margin;
        generator = new Random();
        init(numPlanets);
    }

    public void init(int numPlanets) throws IllegalArgumentException{

        // put star at the center of the universe
        // star = new Star(height/2, width/2);

        // initialize container for planets
        planets = new ArrayList<>();

        int leftLim = margin;
        int upperLim = margin;
        int horizontalRange = width - margin * 2;
        int verticalRange = height - margin * 2;
        int trialLimit = 1000;

        for(int i = 0; i < numPlanets; i++) {

            Planet planet = null;

            int counter = 0;

            while(counter < 1000){

                planet = new SmallPlanet(leftLim+generator.nextInt(horizontalRange),upperLim+generator.nextInt(verticalRange));
                for(Planet existingPlanet: planets){
                    if(overlap(planet, existingPlanet)) {
                        counter++;
                        continue;
                    }
                }
                break;
            }

            if(planet == null) throw new IllegalArgumentException("You can put so many planets on board");

            planets.add(planet);
        }
    }

    /**
     * determines f two planets overlap
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
