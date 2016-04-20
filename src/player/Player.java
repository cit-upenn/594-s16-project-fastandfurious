package player;

import java.awt.Color;

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

}
