package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GraphDataModel {
    private final float node_radius = 6;
    private final float node_select_radius = 10;
    private final float edge_select_distance = 5;
    private final float arrow_length = 20;
    private final float arrow_half_angle = (float) (Math.PI/20);
    private final float line_offs_amount = 3;
    private final Font label_font;
    private Integer selected_node;
    private GraphStructure graph;
    private HashMap<Integer, GraphNodeData> graph_nodes;
    public GraphDataModel() {
    	label_font = new Font("arial", 16);
    	selected_node = null;
    	graph = new GraphStructure();
    	graph_nodes = new HashMap<Integer, GraphNodeData>();
    }
    
    public GraphStructure getGraphStructure() {
    	return (GraphStructure) graph.clone();
    }
    
    public HashMap<Integer, GraphNodeData> getNodes() {
    	HashMap<Integer, GraphNodeData> copy = new HashMap<Integer, GraphNodeData>();
		for (Entry<Integer, GraphNodeData> s: graph_nodes.entrySet()) {
			copy.put(s.getKey(), (GraphNodeData) s.getValue().clone());
		}
    	return copy;
    }
    
    public GraphNodeData getSelectedNodeData() {
    	if (selected_node == null) return null;
    	else return (GraphNodeData) graph_nodes.get(selected_node).clone();
    }
    
    public boolean validNode(Point2D pos) {
		return getNodeByPos(pos) != null;    	
    }
  
    public void selectNode(Point2D pos) {
		selected_node = getNodeByPos(pos);
    }
    
    public void dragNode(Point2D pos1, Point2D pos2) {
    	if (selected_node != null) {
	    	GraphNodeData node_data = graph_nodes.get(selected_node);
	    	if (Geom2D.distance(pos1, node_data.pos) <= node_select_radius) {
	    		node_data.pos = Geom2D.add(node_data.pos, Geom2D.sub(pos2, pos1));
	    	}
    	}
    }
  
    public GraphNodeData addNode(Point2D pos) {
    	GraphNodeData data = new GraphNodeData(pos);
    	Integer id = graph.addNode();
    	data.desc.setName("unnamed step (id " + id.toString() + ")");
    	graph_nodes.put(id, data);
    	return data;
    }
    
    public void deleteNode(Point2D pos) {
		Integer node_id = getNodeByPos(pos);

    	if(node_id != null) {
    		graph_nodes.remove(node_id);
    		graph.deleteNode(node_id);
    		if(node_id == selected_node) {
    			selected_node = null;
    		}
    	}
    }
  
    public void addEdge(Point2D pos1, Point2D pos2) {
		Integer node1_id = getNodeByPos(pos1);
		Integer node2_id = getNodeByPos(pos2);
		
		graph.addEdge(node1_id, node2_id);
    }
  
    public void deleteEdge(Point2D pos) {
		for ( Integer node1 : graph.getNodes()) {
			for ( Integer node2 : graph.getNeighbors(node1)) {
				float pos_edge_dist = (float) Geom2D.distance( pos,
						graph_nodes.get(node1).pos,
						graph_nodes.get(node2).pos );
				if ( pos_edge_dist <= edge_select_distance ) {
					graph.deleteEdge(node1, node2);
					return;
				}					
			}
		}
    }
    
    public void setNodeData(String name, int duration,
    		String description, int difficulty, int cost) {
    	if(selected_node != null) {
    		GraphNodeData node = graph_nodes.get(selected_node);
    		node.desc = new Descriptor(name, duration, description, difficulty, "", cost);
    	}
    }
    
    public void renderGraph(GraphicsContext gc)
    {
		gc.setStroke(Color.BLACK);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getWidth());
        gc.setFill(Color.BLACK);
    	for( Entry<Integer, GraphNodeData> n : graph_nodes.entrySet() ) {
    		final Point2D node_pos = n.getValue().pos;
    		
    		// draw label
    		gc.setFont(label_font);
    		String node_label = n.getValue().desc.getName();
    		if( node_label.length() == 0) node_label = n.getKey().toString();
    		gc.fillText(node_label,
    					node_pos.getX() + node_radius * 1.5,
    					node_pos.getY());
    		
    		//draw node
            gc.fillOval(node_pos.getX() - node_radius,
            			node_pos.getY() - node_radius,
            			node_radius * 2,
            			node_radius * 2);
            
            //draw edges
            for( Integer m : graph.getNeighbors(n.getKey()) ) {
            	Point2D line_start = node_pos;
            	Point2D line_end = graph_nodes.get(m).pos;
            	Point2D line_vec = Geom2D.normalize(Geom2D.sub(line_start, line_end));
            	Point2D arrow1;
            	Point2D arrow2;
            	arrow1 = Geom2D.scalar(arrow_length, line_vec);
            	arrow2 = arrow1;
            	Point2D line_offs = Geom2D.scalar(line_offs_amount,
            			Geom2D.rotate(Math.PI/2, line_vec));
            	line_start = Geom2D.add(line_start, line_offs);
            	line_end   = Geom2D.add(line_end  , line_offs);
            	//draw arrow head
            	arrow1 = Geom2D.add(line_end, Geom2D.rotate( arrow_half_angle, arrow1));
            	arrow2 = Geom2D.add(line_end, Geom2D.rotate(-arrow_half_angle, arrow2));
            	gc.fillPolygon(new double[]{arrow1.getX(), line_end.getX(), arrow2.getX()}, 
		                       new double[]{arrow1.getY(), line_end.getY(), arrow2.getY()},
		                       3);
            	//draw line
            	gc.strokeLine(line_start.getX(),
            			  	  line_start.getY(),
            			  	  line_end.getX(),
            			  	  line_end.getY());
            }
    	}
    	//draw selection circle
    	if (selected_node != null) {
    		GraphNodeData node_data = graph_nodes.get(selected_node);
    		gc.strokeOval(node_data.pos.getX()-node_radius*1.5,
    					  node_data.pos.getY()-node_radius*1.5,
    					  node_radius*3,
    					  node_radius*3);
    	}
    }
    
	private Integer getNodeByPos(Point2D pos) {
		Integer node_id = null;
    	for ( Entry<Integer, GraphNodeData> n : graph_nodes.entrySet() ) {
    		if(pos.distance(n.getValue().pos) <= node_select_radius) {
    			node_id = n.getKey();
    			break;
    		}
    	}
    	
    	return node_id;
	}
}
