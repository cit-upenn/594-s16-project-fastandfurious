package player;

import java.awt.Color;
import java.awt.Graphics2D;

import UniversePackage.Node;

public class ComputerPlayer implements Player {

	private double x;
	private double y;
	private Color pColor;
	private int wealth;
	
	public ComputerPlayer(Color pColor) {		
		this.pColor = pColor;
		wealth = 100;
	}
	
	
	@Override
	public Node pickTarget() {		
		return null;
	}

	@Override
	public boolean moveTowardTarget() {
		
		return false;
	}

	@Override
	public Color getPlayerColor() {

		return pColor;
	}

	@Override
	public int getWealth() {		
		return wealth;
	}
	

}
