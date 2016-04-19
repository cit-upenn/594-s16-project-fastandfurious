package player;

import java.awt.Color;

import UniversePackage.Node;

/**
 * Templates for all kinds of players
 */
public interface Player {

    public Node pickTarget();

    public boolean moveTowardTarget();
    
    public Color getPlayerColor();

}
