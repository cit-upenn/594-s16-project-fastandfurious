package player;

import java.awt.Color;
import java.awt.Graphics2D;

import UniversePackage.Node;

public class ComputerPlayer implements Player {

	private double x;
	private double y;
	private Color pColor;
	private int wealth;
	
	double dx, dy;
	
	public ComputerPlayer(Color pColor) {		
		this.pColor = pColor;
		wealth = 100;
		dx = 0;
		dy = 0;
	}
	
	@Override
	public Node pickTarget() {		
		return null;
	}

	@Override
	public boolean move() {
		
		x += dx;
		y += dy;
		return true;
	}

	@Override
	public Color getPlayerColor() {
		return pColor;
	}

	@Override
	public int getWealth() {		
		return wealth;
	}

	@Override
	public void setCurrentNode(Node node) {
		
	}

	@Override
	public Node getCurrentNode() {
		return null;
	}
	

}
