package UniversePackage;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;

public class Navigator {
	
	
	/**
	 * Find a simple path connecting the start point and the end
	 * @param start point of search
	 * @param end target for searching
	 * @return list of node representing path from start to end
	 */
	public static List<Node> findSimplePath(Node start, Node end, Galaxy galaxy) {
		
		Queue<Node> queue = new LinkedList<>();
		start.setPredecessor(null);
		queue.offer(start);
		HashSet<Node> visited = new HashSet<>();
		boolean found = false;
		
		while(!queue.isEmpty()) {
			
			Node u = queue.poll();
			if(u == end) {
				found = true;
				break;
			}
			
			List<Edge> edges = galaxy.getAdjList().get(u);
			
			for(Edge edge: edges) {
				Node v = edge.getEnd();
				if(!visited.contains(v)) {
					v.setPredecessor(u);
					queue.offer(v);
				}
			}
			visited.add(u);
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
	
	public static List<Node> dijkstra(Node start, Node end, Galaxy galaxy) {
		
		LinkedList<Node> path = new LinkedList<>();
		PriorityQueue<Node> minheap = new PriorityQueue<Node>(new Comparator<Node>() {
			@Override
			public int compare(Node v1, Node v2) {
					double diff = v1.getShortest() - v2.getShortest(); 
					return (int)(diff*100);
				}
			});
		
		List<Node> allnodes = new LinkedList<>();
		for(Node node: galaxy.getAdjList().keySet()) {
			if(StarCluster.find(node) == StarCluster.find(start)) {
				allnodes.add(node);
			}
		}
		
		for(Node node: allnodes) {
			node.setPredecessor(null);
			node.setShortest(Double.MAX_VALUE);	
		}
		
		start.setShortest(0);
		start.setPredecessor(null);
		minheap.addAll(allnodes);
		
		while(!minheap.isEmpty()) {
			Node u = minheap.poll();
			List<Edge> adj = galaxy.getAdjList().get(u);
			for(Edge e: adj) {
				Node v = e.getEnd();
				double currDist = v.getShortest();
				double newDist = u.getShortest() + e.getLength();
				if (newDist <= currDist) {
					v.setShortest(newDist);
					v.setPredecessor(u);
					updateHeap(minheap);
				}
			}
		}
		while(end != null) {
			path.addFirst(end);
			end = end.getPredecessor();
		}
		return path;
	}
	
	/**
	 * private method used to update heap;
	 * @param the heap that needs to be updated
	 */
	private static void updateHeap(PriorityQueue<Node> heap) {
		List<Node> nodes = new LinkedList<Node>();
		nodes.addAll(heap);
		heap.clear();
		heap.addAll(nodes);
	}
}
