package player;

/**
 * Point class represents a geometric point
 * on game canvas
 */
public class Point {
	// instance variables
	private double x, y;		
	/**
	 * constructor
	 * @param x starting x position
	 * @param y starting y position
	 */
	public Point(double x, double y) {this.x = x;this.y = y;}
	/**
	 * get x position
	 * @return double x
	 */
	public double getX() {return x;}
	/**
	 * get y position
	 * @return double y
	 */
	public double getY() {return y;}
	/**
	 * set x position
	 * @param x new x position
	 */
	public void setX(double x) {this.x = x;}
	/**
	 * set y position
	 * @param y new y position
	 */
	public void setY(double y) {this.y = y;}
}
