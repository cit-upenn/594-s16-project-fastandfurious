package UniversePackage;

/**
 * Huge planets are big fellas
 */
public class HugePlanet extends Planet{

	/**
	 * constructor, creates new HugePlanet instance
	 * @param initialX horizontal starting position of the planet
	 * @param initialY vertical starting position of the planet
	 */
    public HugePlanet (double initialX, double initialY) {
    	
    	super(initialX, initialY);
        lowerRadiusLimit = 30;
        upperRadiusLimit = 40;
        double range = upperRadiusLimit - lowerRadiusLimit;
    	radius = lowerRadiusLimit + Galaxy.generator.nextDouble() * range;
    }
}
