package player;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;


/**
 * The animated images to display on the screen.
 *
 */
public class Animate {
	
	/** The images to be drawn for this animate */
	private Image[] images;
	/** The current image that should be displayed */
	private Image image;
	
	/**
	 * Create a new animate based on an image
	 * @param images 
	 */
	public Animate(Image[] images) {
		this.images = images;
		image = images[0];
	}
	
	/**
	 * Get the width of the drawn image
	 * @return The width in pixels of this image
	 */
	public int getWidth() {
		return image.getWidth(null);
	}

	/**
	 * Get the height of the drawn image
	 * @return The height in pixels of this image
	 */
	public int getHeight() {
		return image.getHeight(null);
	}
	
	/**
	 * Draw the image onto the graphics context provided
	 * @param g The graphics context on which to draw the image
	 * @param x The x location at which to draw the image
	 * @param y The y location at which to draw the image
	 */
	public void draw(Graphics2D g2,int x,int y) {
		g2.drawImage(image,x,y, null);
	}
	
	/**
	 * Change the current image that we are on
	 * @param imgNumber the sequence image we want
	 */
	public void nextImage(int imgNumber) {
		if(imgNumber < images.length)
			image = images[imgNumber];
	}
}

