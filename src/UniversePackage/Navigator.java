package UniversePackage;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import player.Player;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Navigator {
	
	
	/**
	 * Find a simple path connecting the start point and the end
	 * @param start point of search
	 * @param end target for searching
	 * @return list of node representing path from start to end
	 */
	public static List<Node> buildBFSPath(Node start, Node end, Galaxy galaxy) {
	
		LinkedList<Node> path = new LinkedList<>();
		if(BFS(start, 100, galaxy).contains(end)) {
			while(end != null) {
				path.addFirst(end);
				end = end.getPredecessor();
			}
		}	
		return path;
	}
	
	/**
	 * Search reachable nodes adjacent to current reign of player
	 * @param breadthLim set a limit on search area / prevents machine from getting too hot
	 * @return a collection of candidate nodes
	 */
	public synchronized static List<Node> BFS(Node start, int breadthLim, Galaxy galaxy) throws IllegalArgumentException{
		
		if(start == null) throw new IllegalArgumentException();
		
		// prepare tools
		Queue<Node> queue = new LinkedList<>();
		List<Node> result = new LinkedList<>();
		Set<Node> visited = new HashSet<>();
		
		// set starting point
		start.setPredecessor(null);
		queue.offer(start);
		queue.offer(null);
		int hops = 0;
		
		// start search
		while(!queue.isEmpty()) {		
			Node u = queue.poll();	
			if(u == null) {		
				hops++;
				if(hops <= breadthLim) {
					queue.offer(null);
					continue;
				}
				else break;
			}
			List<Edge> adjList = galaxy.getAdjList().get(u);
			for(Edge edge: adjList) {
				Node v = edge.getEnd();
				if(!visited.contains(v)&&v.getRuler()==start.getRuler()) {
					v.setPredecessor(u);
					queue.offer(v);
				}
			}
			visited.add(u);
			result.add(u);
		}
		
		return result;
	}
	
	/**
	 * Perform depth first search from multiple sources
	 * in order to find the best path to explore
	 * @param sources starting points for depth-first searching
	 * @param depthLim set a limit on search depth / prevents machine from getting too hot
	 * @param pq a priority used for storing results
	 */
	public synchronized static List<List<Node>> DFS(List<Node> sources, Player p, int lim, Galaxy galaxy, boolean aggresive) {
		
		List<List<Node>> result = new LinkedList<>();
		Iterator<Node> it = sources.iterator();
		
		while(it.hasNext()) {
			
			Node source = it.next();
			
			if(aggresive) {
				if(source.getRuler() == null||source.getRuler() == p) {
					continue;
				}
			}
			
			Stack<Node> stack = new Stack<Node>();
			HashSet<Node> visited = new HashSet<>();
			source.setDepth(0);
			source.setPredecessor(null);
			stack.push(source);
			while(!stack.isEmpty()) {
			
				Node node = stack.pop();
				if(node.getDepth() >= lim) {
					LinkedList<Node> ppath = new LinkedList<>();
					while(node != null) {
						ppath.addFirst(node);
						node = node.getPredecessor();
					}
					result.add(ppath);	
					continue;
				}
				List<Node> adjList = new LinkedList<>(galaxy.getNeighboringNodes(node));
				for(Node next: adjList) {
					if( StarCluster.find(p.getCurrentNode()) != StarCluster.find(next) 
						&& !visited.contains(next)) {
						
						boolean flag = false;
						if(aggresive) {
							if(node.getRuler()!=null&&node.getRuler()!=p) {
								flag = true;
							}
						}else{
							if((next.getRuler() == null||next.getRuler() == p)){
								flag = true;
							}
						}
						if(flag) {
							next.setDepth(node.getDepth() + 1);
							next.setPredecessor(node);
							stack.push(next);
						}
					}
				}
				visited.add(node);
			}	
		}
		
		return result;
	}
	
	public static List<Node> buildDijkstraPath(Node start, Node end, Galaxy galaxy) {
		
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
