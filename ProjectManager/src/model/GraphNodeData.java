package model;

import java.util.HashMap;
import java.util.HashSet;

import javafx.geometry.Point2D;

public class GraphNodeData implements Cloneable{
	public Point2D pos;
	public Descriptor desc = new Descriptor();
	
	public GraphNodeData(Point2D pos) {
		this.pos = pos;
	}
	
	@Override
	public Object clone() {
		GraphNodeData copy = new GraphNodeData(pos);
		copy.desc = desc;
		return copy;
	}
}
