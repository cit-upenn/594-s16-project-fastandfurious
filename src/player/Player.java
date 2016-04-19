package player;

import UniversePackage.Node;

/**
 * Templates for all kinds of players
 */
public interface Player {

    public Node pickTarget();

    public boolean moveTowardTarget();

}
