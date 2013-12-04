package model;

import java.util.LinkedList;

public class TopologicalSorter {
	
	private enum Colour { White, Grey, Black };
	
	public static LinkedList<Integer> topologicalSort(AdjacencyMatrix graph) throws CycleDetectedException {
		boolean[][] G = graph.adjMx;
		Colour[] state = newArraySetToWhite(G.length);
		LinkedList<Integer> L = new LinkedList<Integer>();
		for(int v=0; v<G.length; ++v) {
			if(state[v] == Colour.White) {
				sortFromVertex(G, state, v, L);
			}
		}
		return L;
	}

	private static void viz(boolean[][] adjMx) {
		for(int i=0; i<adjMx.length; ++i) {
			for(int j=0; j<adjMx.length; ++j) {
				System.out.print(adjMx[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
		
	}

	private static void sortFromVertex(boolean[][] g, Colour[] state, int i, LinkedList<Integer> L) throws CycleDetectedException {
		state[i] = Colour.Grey;
		for(int k=0; k<g.length; ++k) {
			if(g[i][k] && state[k] == Colour.White) {
				sortFromVertex(g, state, k, L);
			} else if(g[i][k] && state[k] == Colour.Grey) {
				throw new CycleDetectedException();
			}
		}
		state[i] = Colour.Black;
		L.addFirst(transformToAlgorithmicalForm(i));
	}

	private static Integer transformToAlgorithmicalForm(int i) {
		return i + 1;
	}

	private static Colour[] newArraySetToWhite(int edgeNum) {
		Colour[] arr = new Colour[edgeNum];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = Colour.White;
		}
		return arr;
	}

}
