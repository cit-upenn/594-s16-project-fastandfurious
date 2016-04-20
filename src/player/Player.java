package player;

import java.awt.Color;
import java.awt.Graphics2D;

import UniversePackage.Node;

/**
 * Templates for all kinds of players
 */
public interface Player {

    public Node pickTarget();

    public boolean move();
    
    public Color getPlayerColor();
    
    public int getWealth();
    
	public void setCurrentNode(Node node);
	
	public Node getCurrentNode();
	
	public double getX();

	public void setX(double x);

	public double getY();
	
	public void setY(double y) ;
	
	public void addTarget(Node target);
	
	public void draw(Graphics2D g2,int x,int y);
	
	public void drawFocus(Graphics2D g2);
	
	public void drawSelection(Graphics2D g2);
	
	public void setFocus(Node focus);
	
	public Node getFocus();
	
	public void setSelected(Node selection);
	
	public Node getSelected();

}
