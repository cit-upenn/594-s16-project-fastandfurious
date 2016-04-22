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
	private Color color;

	public Edge(Node p1, Node p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.color =  Color.orange;
	}
	
	public void draw(Graphics2D g) {
		
		Line2D.Double line = new Line2D.Double(p1.getInstX(), p1.getInstY(), p2.getInstX(), p2.getInstY());
		g.setColor(color);
		final float dash1[] = {10.0f};
		g.setStroke(new BasicStroke(1.5f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, dash1, 0.0f));
		g.draw(line);
	}
	
	public boolean containsPoint(Node node) {
		return p1 == node || p2 == node;
	}

}
