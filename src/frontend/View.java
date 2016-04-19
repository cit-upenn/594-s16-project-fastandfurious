package frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import UniversePackage.Galaxy;
import UniversePackage.Node;
import UniversePackage.Planet;
import player.HumanPlayer;

@SuppressWarnings("serial")
public class View extends JPanel implements Observer {

	private Galaxy galaxy;

	private Image bgImg;
	private Image[] planets;
	private Image station;
	
	private HumanPlayer hPlayer;

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
		this.station = new ImageIcon("resources/station.png").getImage();
		
		String[] refs = new String[5];
		refs[0] = "resources/duck.gif";
		refs[1] = "resouces/duck2.gif";
		refs[2] = "resouces/duck3.gif";
		
		hPlayer = new HumanPlayer(refs, 10, 10, 30); 

	}

	/*
	 * Update next step
	 */
	public void nextStep() {
		hPlayer.translate();
		repaint();
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

		Node[][] starboard = galaxy.getStarBoard();
		
		for (int i = 0; i < starboard.length; i++) {
			for (int j = 0; j < starboard[0].length; j++) {
				Node node = starboard[i][j];
				if (node != null) {
					int k = (i + j) % 3;
					if (node instanceof Planet) {
						
						g.setColor(Color.white);
	
						g.drawOval((int)(node.getX() - node.getRadius() / 2), (int)(node.getY() - node.getRadius() / 2), (int)node.getRadius(), (int)node.getRadius());
					
					} else {
						g.setColor(Color.cyan);
						g.drawRect((int)(node.getX() - node.getRadius() / 2), (int)(node.getY() - node.getRadius() / 2), (int)node.getRadius(), (int)node.getRadius());
					}

				}			
			}
		}

	}

	@Override
	public void update(Observable o, Object arg) {

		repaint();
	}

	public void click(Point mousePoint) {
		Node[][] starboard = galaxy.getStarBoard();
		for (int i = 0; i < starboard.length; i++) {
			for (int j = 0; j < starboard[0].length; j++) {
				Node node = starboard[i][j];
				if (node.contains(mousePoint)) {
					node.click();
				}
			}
		}
	}
}