package frontend;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import UniversePackage.Galaxy;

public class View extends JPanel implements Observer {

	private Galaxy galaxy;

	private Image bgImg;
	private Image[] planets;
	
	public View(Galaxy galaxy) {
		this.galaxy = galaxy;
		
		this.bgImg = new ImageIcon("resources/universe960x720.jpg").getImage();
		Dimension size = new Dimension(bgImg.getWidth(null), bgImg.getHeight(null));
		setPreferredSize(size);
		setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	    
	    this.planets = new Image[3];
	    for (int i = 0; i < planets.length; i++) {
	    	if (i % 3 == 0) {
	    		planets[i] = new ImageIcon("resources/Venus.png").getImage();	   
	    		
	    	} else if (i % 3 == 1) {
	    		planets[i] = new ImageIcon("resources/jupiter.png").getImage();
	    	
	    	} else {
	    		planets[i] = new ImageIcon("resources/planet1.png").getImage();
	    		
	    	}
	    	    	
	    }
	}
	
	 
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(960, 740);
	}
	
	@Override
	public void paint(Graphics g) {	
		super.paintComponent(g);		
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(bgImg, 0, 0, null);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		repaint();
	}

}
