package UniversePackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import player.Player;

public class Edge {
	
	private Node p1;
	private Node p2;
	private Player ruler;

	public Edge(Node p1, Node p2, Player builtBy) {
		this.p1 = p1;
		this.p2 = p2;
		this.ruler = builtBy;
	}
	
	public void draw(Graphics2D g) {
		
		Line2D.Double line = new Line2D.Double(p1.getInstX(), p1.getInstY(), p2.getInstX(), p2.getInstY());
		g.setColor(ruler.getPlayerColor());
		g.setStroke(new BasicStroke(2.0f));
		
		g.draw(line);
	}
	
	public boolean containsPoint(Node node) {
		return p1 == node || p2 == node;
	}

}
