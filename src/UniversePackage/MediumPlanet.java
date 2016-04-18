package UniversePackage;

/**
 * Medium planets are a little bit bigger.
 */
public class MediumPlanet extends Planet{
	
	/**
	 * constructor, creates new MediumPlanet instance
	 * @param initialX horizontal starting position of the planet
	 * @param initialY vertical starting position of the planet
	 */
    public MediumPlanet (double initialX, double initialY) {
    	super(initialX, initialY);
        lowerRadiusLimit = 10;
        upperRadiusLimit = 20;
        double range = upperRadiusLimit - lowerRadiusLimit;
    	radius = lowerRadiusLimit + Galaxy.generator.nextDouble() * range;
    }
    
}
