package frontend;

import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import UniversePackage.Galaxy;

public class View extends JPanel implements Observer {

	private Galaxy galaxy;
	
	
	public View(Galaxy galaxy) {
		this.galaxy = galaxy;
	}
	
	@Override
	public void paint(Graphics g) {
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		repaint();
	}

}
