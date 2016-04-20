package frontend;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import UniversePackage.Edge;
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

	}

	/*
	 * Update next step
	 */
	public void nextStep() {
		((HumanPlayer) galaxy.getPlayer(0)).translate();
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

		((HumanPlayer) galaxy.getPlayer(0)).draw(g2, (int)((HumanPlayer) galaxy.getPlayer(0)).getX(), (int)((HumanPlayer) galaxy.getPlayer(0)).getY());
		
		Node[][] starboard = galaxy.getStarBoard();
		
		for (int i = 0; i < starboard.length; i++) {
			for (int j = 0; j < starboard[0].length; j++) {
				Node node = starboard[i][j];
				if (node != null) {
					
					double x = node.getInstX();
					double y = node.getInstY();
					double r = node.getRadius();
					
					if (node instanceof Planet) {		
						Shape circle = new Ellipse2D.Double(x - r/2, y - r/2, r, r);
						g2.setColor(node.getColor());
						g2.setStroke(new BasicStroke(2));
						g2.draw(circle);
						
					} else {
						
						Shape rect = new Rectangle2D.Double(x - r/2, y - r/2, r, r);
						g2.setColor(node.getColor());
						g2.setStroke(new BasicStroke(2));
						g2.draw(rect);	
					}
					
				}			
			}
		}
		
		for(Edge edge: galaxy.getEdges()) {
			edge.draw(g2);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

	public void click(Point mousePoint) {
		Node[][] starboard = galaxy.getStarBoard();
		for (int i = 1; i < starboard.length - 1; i++) {
			for (int j = 1; j < starboard[0].length - 1; j++) {
				Node node = starboard[i][j];
				if (node.contains(mousePoint)) {
					node.click();
					Node current = galaxy.getPlayer(0).getCurrentNode();
					galaxy.buildEdge(current, node);
					galaxy.getPlayer(0).setCurrentNode(node);
				}
			}
		}
	}
}