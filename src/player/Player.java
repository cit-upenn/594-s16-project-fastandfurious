package player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import UniversePackage.Node;

/**
 * Templates for all kinds of players
 */
public interface Player {

    public void move() ;
    
	public void setCurrentNode(Node node);
	
	public void setX(double x);
	
	public void setY(double y) ;
	
	public void draw(Graphics2D g2);
	
	public void drawHalo(Graphics2D g2, String type);
	
	public void setFocus(Node focus);

	public void setSelected(Node selection);
	
	public void controlNode(Node node);
	
	public void loseNode(Node node);
	
	public void think();
	
    public int getWealth();
	
	public boolean inMotion();
    
    public boolean addWealth(int change);
    
	public double getX();

	public double getY();
	
	public double getDX();
	
	public double getDY();
    
    public Set<Node> getNodesControlled();
    
    public String getStatus();
    
	public Node getSelected();
	
    public Color getPlayerColor();
    
    public LinkedList<Node> getSelections();
    
    public Queue<Node> getDestinations();
	
	public Node getCurrentNode();
	
	public void setDX(double dx);
	
	public void setDY(double dy);
	
	public double getSpeed();

}
