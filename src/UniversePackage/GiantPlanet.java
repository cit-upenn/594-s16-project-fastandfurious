package UniversePackage;

/**
 * These guys are Titans.
 */
public class GiantPlanet extends Planet{

	/**
	 * constructor, creates new GiantPlanet instance
	 * @param initialX horizontal starting position of the planet
	 * @param initialY vertical starting position of the planet
	 */
    public GiantPlanet (double initialX, double initialY) {
    	super(initialX, initialY);
        lowerRadiusLimit = 50;
        upperRadiusLimit = 100;
        double range = upperRadiusLimit - lowerRadiusLimit;
    	radius = lowerRadiusLimit + Galaxy.generator.nextDouble() * range;
    }
}
