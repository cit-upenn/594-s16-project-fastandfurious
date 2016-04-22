package player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import UniversePackage.Galaxy;
import UniversePackage.Node;

public class ComputerPlayer implements Player {

	private double x;
	private double y;
	private Queue<Node> destinations;
	private int wealth;
	private double dx;
	private double dy;
	private double speed;
	private double radius;
	
	private Point p1;
	private Point p2;
	private Point p3;
	private int deg;

	private Node currentNode;
	private Node focus;
	private Node selected;
	private Galaxy galaxy;
	
	private LinkedList<Node> selections;
	
	private Color pColor;
	
	private boolean isThinking;
	private boolean isMoving;
	
	public ComputerPlayer(double x, double y, Color pColor, Galaxy galaxy) {		
		
		this.pColor = pColor;
		wealth = 100;
		destinations = new LinkedList<>();
		this.x = x;
		this.y = y;
		this.radius = 15;
		p1 = new Point(x, y - radius);
		p2 = new Point(x - radius * Math.cos(Math.PI/6), y + radius/2);
		p3 = new Point(x + radius * Math.cos(Math.PI/6), y + radius/2);
		this.galaxy = galaxy;
		
		this.isMoving = false;
		this.isThinking = false;
		
		selections = new LinkedList<>();
		
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
		currentNode = node;
	}

	@Override
	public Node getCurrentNode() {
		return currentNode;
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void addTarget(Node target) {
		destinations.add(target);
	}

	@Override
	public void draw(Graphics2D g2) {
		int[] xpoints = new int[]{(int)p1.getX(), (int)p2.getX(), (int)p3.getX()};
		int[] ypoints = new int[]{(int)p1.getY(), (int)p2.getY(), (int)p3.getY()};
		Shape triangle = new Polygon(xpoints, ypoints, 3);
		g2.setStroke(new BasicStroke(2));
		g2.setColor(pColor);
		g2.draw(triangle);
		drawHalo(g2, "focus");
		drawHalo(g2, "selection");
		rotate(5);
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
		return selected;
	}

	@Override
	public void setSelected(Node selection) {
		this.selected = selection;
	}

	@Override
	public Node getFocus() {
		return focus;
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
	
	private void rotate(double increment) {
		
		this.deg += increment;
		this.deg %= 360;
		
		double radians1 = (double)this.deg/180;
		double radians2 = radians1 + Math.PI * 2/3;
		double radians3 = radians2 + Math.PI * 2/3;
	
		p1.setX(x + radius * Math.cos(radians1));
		p1.setY(y + radius * Math.sin(radians1));
		
		p2.setX(x + radius * Math.cos(radians2));
		p2.setY(y + radius * Math.sin(radians2));
		
		p3.setX(x + radius * Math.cos(radians3));
		p3.setY(y + radius * Math.sin(radians3));
	}
	
	@Override
	public void think() {
		
		if(!isThinking) {
			
			isThinking = true;
			Analyzer a = new Analyzer();
			Thread thinking = new Thread(a);
			thinking.start();
			
			try {
				thinking.join();
				isThinking = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class Analyzer implements Runnable {

		@Override
		public void run() {
			
			System.out.println("start thinkg!");
			
			// first step try to perform a simple 
			// b-f-s expansion
			
			// find all adjacent unconnected nodes
			
			Node[][] board = galaxy.getStarBoard();
			Node current = currentNode;
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("end thinkg!");
			
			return;
		}
		
		
	}
	
	private class Point {
		
		private double x, y;
		
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
		public double getX() {
			return x;
		}
		public double getY() {
			return y;
		}
		public void setX(double x) {
			this.x = x;
		}
		public void setY(double y) {
			this.y = y;
		}
	}

	@Override
	public LinkedList<Node> getSelections() {
		// TODO Auto-generated method stub
		return selections;
	}
	@Override
	public void buildPath() {
		// TODO Auto-generated method stub
		
	}
}
