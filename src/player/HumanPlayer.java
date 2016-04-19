package player;

import java.awt.Image;

import javax.swing.ImageIcon;

public class HumanPlayer {

	private Animate animation;
	private int sequenceNum;
	private double x;
	private double y;
	private int size;
	
	private double dx;
	private double dy;
	
	public HumanPlayer(String[] refs, double x, double y, int size) {
		Image[] images = new Image[refs.length];
		for(int i = 0; i < refs.length; i++) {
			Image image = new ImageIcon(refs[i]).getImage();
			images[i] = image;
		}
		this.animation = new Animate(images);
		sequenceNum = 0;
		this.setX(x);
		this.setY(y);
		this.setSize(size);
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void translate() {
		
	}
}
