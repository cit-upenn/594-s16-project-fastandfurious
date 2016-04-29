package frontend;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import UniversePackage.*;
import player.HumanPlayer;
import player.Player;

@SuppressWarnings("serial")
public class View extends JPanel implements Observer {

	private Galaxy galaxy;
	private Image bgImg;
	private Image[] planets;
	private Image station;
	private Controller control;
	
	public View(Galaxy galaxy, Controller control) {
	
		this.galaxy = galaxy;
		this.bgImg = new ImageIcon("resources/universe960x720.jpg").getImage();
		Dimension size = new Dimension(bgImg.getWidth(null), bgImg.getHeight(null));
		setPreferredSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
		this.control = control;
	}

	/*
	 * Update next step
	 */
	public void nextStep() {
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(960, 740);
	}

	@Override
	public synchronized void paint(Graphics g) {	
		
		try {
			
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(bgImg, 0, 0, null);
			
			Node[][] starboard = galaxy.getStarBoard();
			
			for (int i = 0; i < starboard.length; i++) {
				for (int j = 0; j < starboard[0].length; j++) {
					Node node = starboard[i][j];
					if (node != null) 
						node.draw(g2);	
				}
			}
			
			for(List<Edge> edges: galaxy.getAdjList().values())  {
				for(Edge edge: edges) {
					edge.draw(g2);
				}
			}
			
			Player p1 = galaxy.getPlayer(0);	
			Player p2 = galaxy.getPlayer(1);
			
			p1.draw(g2);
			p2.draw(g2);
			
			control.updateMessageBoard(0, processMessage(p1));
			control.updateMessageBoard(1, processMessage(p2));
			
		}catch(Exception e){};
	}

	public String processMessage(Player p){
		
		String status = "Status: " + p.getStatus();
		
		String wealthStr = "Wealth: " + p.getWealth();
		int numPlanets = 0;
		int numStations = 0;
		
		for(Node node: p.getPlanetsControlled()) {
			
			if(node.getType().equals("p")) {
				numPlanets++;
			}
			else if(node.getType().equals("s")) {
				numStations++;
			}
		}
		
		String numPlanetsStr = "# Planets: " + numPlanets;
		String numStationsStr = "# Stations: " + numStations;
		
		String output = String.format("%s\n%s\n%s\n%s\n", status, wealthStr, numPlanetsStr, numStationsStr);
		
		return output;
	}
	
	@Override
	public synchronized void update(Observable o, Object arg) {
		repaint();
	}

}