package player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Queue;

import UniversePackage.Node;

public class ComputerPlayer implements Player {

	private Animate animation;
	private int sequenceNum;
	private double x;
	private double y;
	private Queue<Node> destinations;
	private int wealth;
	private double dx;
	private double dy;
	private double speed;
	
	private Node currentNode;
	private Node focus;
	private Node selected;
	
	private Color pColor;
	
	public ComputerPlayer(Color pColor) {		
		this.pColor = pColor;
		wealth = 100;
		destinations = new LinkedList<>();
	}
	
	@Override
	public Node pickTarget() {		
		return null;
	}

	@Override
	public boolean move() {
		
		dx = 0;
		dy = 0;
		if(!destinations.isEmpty()) {
			Node nextTarget = destinations.peek();
			if(Math.abs(x - nextTarget.getX()) < 1
			   && Math.abs(y - nextTarget.getY()) < 1){
				destinations.poll();	
			}
			if(!destinations.isEmpty()) {
				setVelocity(destinations.peek());
			}
		}		
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
	
	private void setVelocity(Node dest){
		
		double deltaX = dest.getX() - x;
		double deltaY = dest.getY() - y;
		double mod = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		dx = deltaX/mod * speed;
		dy = deltaY/mod * speed;

	}


	@Override
	public Node getSelected() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelected(Node selection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node getFocus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus(Node focus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawHalo(Graphics2D g2, String type) {
		// TODO Auto-generated method stub
		
	}

}
