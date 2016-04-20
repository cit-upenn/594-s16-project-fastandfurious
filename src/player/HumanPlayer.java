package player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;

import UniversePackage.Node;
import UniversePackage.Planet;
import UniversePackage.SupplyStation;

public class HumanPlayer implements Player, Observer {

	private Animate animation;
	private int sequenceNum;
	private double x;
	private double y;
	private int size;
	
	private int wealth;
	
	private double dx;
	private double dy;
	
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
		dx = 5;
		dy = 5;
		this.pColor = pColor;
		this.wealth = 100;
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
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void translate() {
		sequenceNum = (sequenceNum < 2) ? sequenceNum + 1 : 0;
		animation.nextImage(sequenceNum);
	}

	
	public void draw(Graphics2D g2,int x,int y) {
		animation.draw(g2, (int)this.getX(), (int)this.getY());
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

	public void setCurrentNode(Node node) {
		this.currentNode = node;
	}
	
	public Node getCurrentNode() {
		return currentNode;
	}
	@Override
	public void update(Observable node, Object arg) {
		if (node instanceof Planet) {
			setX(((Planet) node).getX());
			setY(((Planet) node).getY());
		} else if (node instanceof SupplyStation) {
			setX(((SupplyStation) node).getX());
			setY(((SupplyStation) node).getY());
		}
		
	}
}
