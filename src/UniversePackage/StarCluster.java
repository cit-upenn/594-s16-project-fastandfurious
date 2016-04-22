package UniversePackage;

public class StarCluster {
	
	/**
	 * for every node in the galaxy
	 * create a disjoint set
	 * containing itself
	 * @param stars 
	 */
	public static void makeSet(Node[][] stars) {
		for(int i = 0; i < stars.length; i++) {
			for(int j = 0; j < stars[0].length; j++) {				
				Node star = stars[i][j];
				if(star != null) {
					star.setParentNode(star);
				}
			}
		}
	}
	
	/**
	 * find representative node of the current set
	 * @param node to be located
	 * @return parent node of the target node
	 */
	public static Node find(Node node) {
		
		// if node is its own parent
		// return itself
		Node parent = node.getParentNode();
		if(parent == node) 
			return node;
		node.setParentNode(find(parent));
		return node.getParentNode();
	}
	
	/**
	 * union two nodes into the same set
	 * @param lhs left-hand-side node
	 * @param rhs right-hand-side node
	 */
	public static void union(Node lhs, Node rhs) {
		Node lparent = find(lhs);
		Node rparent = find(rhs);
		if(lparent.getRank() < rparent.getRank()) 
			rhs.setParentNode(lparent);
		else if(lparent.getRank() > rparent.getRank()) 
			lhs.setParentNode(rparent);
		else {
			lhs.setParentNode(rparent);
			rparent.incrementRank();
		}
	}
	
	/**
	 * separate node from its current disjoint-set
	 * @param node to be separated
	 */
	public static void seperateNode(Node node) {
		node.setParentNode(node);
	}
}
