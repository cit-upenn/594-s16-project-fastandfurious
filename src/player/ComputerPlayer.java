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

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setX(double x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setY(double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTarget(Node target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g2, int x, int y) {
		// TODO Auto-generated method stub
		
	}
	

}
