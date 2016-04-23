package player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import UniversePackage.Node;

/**
 * Templates for all kinds of players
 */
public interface Player {


    public void move() ;
    
    public int getWealth();
    
	public void setCurrentNode(Node node);
	
    public LinkedList<Node> getSelections();
	
	public Node getCurrentNode();
	
	public double getX();

	public void setX(double x);

	public double getY();
	
	public void setY(double y) ;
	
	public void addTarget(Node target);
	
	public void draw(Graphics2D g2);
	
	public void drawHalo(Graphics2D g2, String type);
	
	public void setFocus(Node focus);

	public void setSelected(Node selection);
	
	public Node getSelected();
	
    public Color getPlayerColor();
	
	public void think();
	
    public void buildPath();
	
	public boolean inMotion();

}
