package player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Stack;

import javax.swing.ImageIcon;

import UniversePackage.Node;
import UniversePackage.Planet;
import UniversePackage.SupplyStation;

public class HumanPlayer implements Player {

	private Animate animation;
	private int sequenceNum;
	private double x;
	private double y;
	private Stack<Node> destinations;
	
	private int wealth;
	
	
	private double dx;
	private double dy;
	private double speed;
	
	private Node currentNode;
	
	private Color pColor;
	
	public HumanPlayer(String[] refs, double x, double y, Color pColor) {
		
		Image[] images = new Image[refs.length];
		for(int i = 0; i < refs.length; i++) {
			Image image = new ImageIcon(refs[i]).getImage();
			images[i] = image;
		}
		this.animation = new Animate(images);
		sequenceNum = 0;
		
		this.setX(x);
		this.setY(y);
		this.pColor = pColor;
		this.wealth = 100;
		
		speed = 1.5; 
		
		destinations = new Stack<>();
		
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void translate() {
		sequenceNum = (sequenceNum < 2) ? sequenceNum + 1 : 0;
		animation.nextImage(sequenceNum);
	}

	@Override
	public void draw(Graphics2D g2,int x,int y) {
		animation.draw(g2, x, y);
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
				destinations.pop();	
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
		this.currentNode = node;
	}
	
	@Override
	public Node getCurrentNode() {
		return currentNode;
	}
	
	private void setVelocity(Node dest){
		
		double deltaX = dest.getX() - x;
		double deltaY = dest.getY() - y;
		double mod = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		dx = deltaX/mod * speed;
		dy = deltaY/mod * speed;

	}

	@Override
	public void addTarget(Node target) {	
		destinations.push(target);
	}
}
