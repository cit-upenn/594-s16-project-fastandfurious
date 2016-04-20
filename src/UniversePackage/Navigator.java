package UniversePackage;

import java.util.List;
import java.util.Queue;
import java.util.HashSet;
import java.util.LinkedList;

public class Navigator {
	
	public static List<Node> breathFirstSearch(Node start, Node end) {
		
		Queue<Node> queue = new LinkedList<>();
		start.setPredecessor(null);
		queue.offer(start);
		HashSet<Node> visited = new HashSet<>();
		boolean found = false;
		
		while(!queue.isEmpty()) {
			Node node = queue.poll();
			if(node == end) {
				found = true;
				break;
			}
			List<Node> neighbors = node.getNeighbors();
			for(Node neighbor: neighbors) {
				if(!visited.contains(neighbor)) {
					neighbor.setPredecessor(node);
					queue.offer(neighbor);
				}
			}
			visited.add(node);
		}
		
		if(found) {
			LinkedList<Node> path = new LinkedList<>();
			Node node = end;
			while(node != null) {
				path.addFirst(node);
				node = node.getPredecessor();
			}
			return path;
		}	
		return null;
	}
}
