package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import UniversePackage.Node;

/**
 * Templates for all kinds of players
 */
public interface Player {

    public LinkedList<Node> getSelections();
    
    public void buildPath();

    public boolean move() ;
    
    public Color getPlayerColor();
    
    public int getWealth();
    
	public void setCurrentNode(Node node);
	
	public Node getCurrentNode();
	
	public double getX();

	public void setX(double x);

	public double getY();
	
	public void setY(double y) ;
	
	public void addTarget(Node target);
	
	public void draw(Graphics2D g2);
	
	public void drawHalo(Graphics2D g2, String type);
	
	public void setFocus(Node focus);
	
	public Node getFocus();
	
	public void setSelected(Node selection);
	
	public Node getSelected();
	/**
	 * players need to think before they can make move
	 */
	public void think();

}
