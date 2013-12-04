package model;

public class AdjacencyMatrix {
	public boolean adjMx[][];
	public AdjacencyMatrix (int size)
	{
		adjMx = new boolean[size][size];
	}
	
	public AdjacencyMatrix (boolean adjMx[][])
	{
		this.adjMx = adjMx.clone();
	}
}
