package UniversePackage;
/**
 * Small planet is usually very tiny
 * Let's say they don't possess satellites
 */
public class SmallPlanet extends Planet {
	
	/**
	 * constructor, creates new SmallPlanet instance
	 * @param initialX horizontal starting position of the planet
	 * @param initialY vertical starting position of the planet
	 */
    public SmallPlanet (double initialX, double initialY) {
    	super(initialX, initialY);
        lowerRadiusLimit = 5;
        upperRadiusLimit = 10;
        double range = upperRadiusLimit - lowerRadiusLimit;
    	radius = lowerRadiusLimit + Galaxy.generator.nextDouble() * range;
    }
}
