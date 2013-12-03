package model;

import java.util.HashMap;
import java.util.HashSet;

import javafx.geometry.Point2D;

public class GraphNodeData implements Cloneable{
	public Point2D pos;
	public String name = new String();
	public String duration = new String();
	public String description = new String();
	public String difficulty = new String();
	public String cost = new String();
	
	public GraphNodeData(Point2D pos) {
		this.pos = pos;
	}
	
	@Override
	public Object clone() {
		GraphNodeData copy = new GraphNodeData(pos);
		copy.name = name;
		copy.duration = duration;
		copy.description = description;
		copy.difficulty = difficulty;
		copy.cost = cost;
		return copy;
	}
}
