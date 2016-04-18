package frontend;

import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import UniversePackage.Galaxy;
import UniversePackage.Planet;

public class View extends JPanel implements Observer {

	private Galaxy galaxy;
	
	
	public View(Galaxy galaxy) {
		this.galaxy = galaxy;
	}
	
	@Override
	public void paint(Graphics g) {
		for (Planet p : galaxy.getPlanet()) {
			g.fillOval((int)p.getPosition()[0], (int)p.getPosition()[1], (int)p.getRadius(), (int)p.getRadius());
			System.out.println(p.getPosition()[0] + " " + p.getPosition()[1] + " " +  p.getRadius());
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		repaint();
	}

}
