package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class GraphStructure implements Cloneable {
	private static final Integer STARTS_FROM = 1;
	private HashMap<Integer, HashSet<Integer>> edgeMap;
	private Integer max_node;
	
	public GraphStructure()
	{
		edgeMap = new HashMap<Integer, HashSet<Integer>>();
		max_node = STARTS_FROM - 1;
	}
	
	public Integer addNode() {
		Integer node = STARTS_FROM;
		boolean found = false;
		
		for(; node <= max_node && !found; ++node) {
			found = !edgeMap.containsKey(node);
		}
		
		if(found) {
			node = node - 1;
		}
		else {
			node = max_node + 1;
			max_node = node;
		}
		edgeMap.put(node, new HashSet<Integer>());
		return node;
	}
	
	public void deleteNode(Integer node) {
		if (edgeMap.containsKey(node)) {
			for (Entry<Integer, HashSet<Integer>> s: edgeMap.entrySet()) {
				s.getValue().remove(node);
			}
			edgeMap.remove(node);
		}
		for (; max_node >= STARTS_FROM && !edgeMap.containsKey(max_node); --max_node);
	}
	
	public void addEdge(Integer node1, Integer node2) {
		if (edgeMap.containsKey(node1) && edgeMap.containsKey(node2)) {
			edgeMap.get(node1).add(node2);
		}
	}
	
	public void deleteEdge(Integer node1, Integer node2)
	{
		if (edgeMap.containsKey(node1)) {
			edgeMap.get(node1).remove(node2);
		}
	}
	
	public HashSet<Integer> getNodes() {
		HashSet<Integer> nodes = new HashSet<Integer>();
		
		for (Entry<Integer, HashSet<Integer>> s: edgeMap.entrySet()) {
			nodes.add(s.getKey());
		}
		
		return nodes;
	}
	
	public HashSet<Integer> getNeighbors(Integer node) {
		if (!edgeMap.containsKey(node)) {
			return new HashSet<Integer>();
		}
		return (HashSet<Integer>) edgeMap.get(node).clone();
	}
	
	public AdjacencyMatrix toAdjacencyMatrix() {
		final int size = edgeMap.size();
		AdjacencyMatrix mx = new AdjacencyMatrix(size);
		for(Integer i = 0; i < size; ++i) {
			for(Integer j = 0; j < size; ++j) {
				mx.adjMx[i][j]=edgeMap.containsKey(i) && edgeMap.get(i).contains(j);
			}
		}
		return mx;
	}
	
	public void fromAdjacencyMatrix(AdjacencyMatrix mx)	{
		edgeMap.clear();
		for(Integer i = 0; i < mx.adjMx.length; ++i) {
			for(Integer j = 0; j < mx.adjMx.length; ++j) {
				if(mx.adjMx[i][j]) {
					if(!edgeMap.containsKey(i))	{
						edgeMap.put(i, new HashSet<Integer>());
					}
					edgeMap.get(i).add(j);
				}
			}
		}
	}
	
	public String toString() {
		String graph_string = new String();
		graph_string += "G: max_node: " + max_node + " E: " + edgeMap.toString();
		return graph_string;
	}
	
	@Override
	public Object clone() {
		GraphStructure copy = new GraphStructure();
		copy.edgeMap = (HashMap<Integer, HashSet<Integer>>) edgeMap.clone();
		copy.max_node = max_node;
		return copy;
	}
	
}
