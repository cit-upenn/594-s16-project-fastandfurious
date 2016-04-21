package player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.Queue;
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
	private Queue<Node> destinations;
	private int wealth;
	
	private double dx;
	private double dy;
	private double speed;
	
	private Node currentNode;
	private Node focus;
	private Node selected;
	
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
		destinations = new LinkedList<>();
		
		this.focus = null;
		this.selected = null;
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
				destinations.poll();	
			}
			if(!destinations.isEmpty()) {
				setVelocity(destinations.peek());
			}
			else {
				currentNode = nextTarget;
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
		destinations.offer(target);
	}

	@Override
	public Node getSelected() {
		return selected;
	}

	@Override
	public void setSelected(Node selection) {
		this.selected = selection;
	}

	@Override
	public Node getFocus() {
		return this.focus;
	}

	@Override
	public void setFocus(Node focus) {
		this.focus = focus;
	}

	@Override
	public void drawHalo(Graphics2D g2, String type) {
		
		Node node = null;
		if(type.equals("focus")) {
			node = focus;
			
		}else if(type.equals("selection")) {
			node = selected;
		}
		if(node != null) {
			double cx = node.getInstX();
			double cy = node.getInstY();
			double radius = 30;
			Shape halo = new Ellipse2D.Double(cx - radius/2, cy - radius/2, radius, radius);
			if(type.equals("focus")) {			
				final float dash1[] = {10.0f};
				g2.setStroke(new BasicStroke(1.5f,
		                BasicStroke.CAP_BUTT,
		                BasicStroke.JOIN_MITER,
		                10.0f, dash1, 0.0f));	
				
			}else if(type.equals("selection")) {
				g2.setStroke(new BasicStroke(1.5f));
			}
			g2.setColor(pColor);
			g2.draw(halo);
		}
	}


}
