package model;

import java.util.LinkedList;
import java.util.List;

public class TopologicalSorter {

	private enum Colour { White, Grey, Black };
	private class Edge {
		Edge(int f, int t) {
			from = f;
			to   = t;
		}
		public int from;
		public int to;
	}
	
	public void topologicalSort(AdjacencyMatrix graph) {
		boolean[][] G = graph.adjMx;
		Colour[] state = newBoolMtxSetToFalse(G.length);
		LinkedList<Integer> L = new LinkedList();
		
		for(int v=0; v<G.length; ++v) {
			if(state[v] == Colour.White) {
				sortFromVertex(G, state, v, L);
			}
		}
	}

	private void sortFromVertex(boolean[][] g, Colour[] state, int i, LinkedList L) {
		state[i] = Colour.Grey;
		for(int k=0; k<g.length; ++k) {
			if(g[i][k] && state[i] == Colour.White) {
				sortFromVertex(g, state, k, L);
			} else if(state[i] == Colour.Grey) {
				return;
			}
		}
		state[i] = Colour.Black;
		L.addFirst(i);
	}

	private Colour[] newBoolMtxSetToFalse(int edgeNum) {
		Colour[] arr = new Colour[edgeNum];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = Colour.White;
		}
		return arr;
	}

}
