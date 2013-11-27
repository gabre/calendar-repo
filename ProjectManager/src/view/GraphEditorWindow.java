package view;

import app.ProjectManagerApplication;
import javafx.geometry.Point2D;
import model.GraphDataModel;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GraphEditorWindow extends Window {
    private enum editorState {SELECT_NODE, ADD_NODE, DELETE_NODE, ADD_EDGE, DELETE_EDGE};
    private editorState currentState;
    private GraphDataModel graph_model;
    private final int graph_viewW = 500;
    private final int graph_viewH = 500;
    private Canvas graph_view;
    private Point2D dragStart;
    private Point2D dragAnchor;
    private HBox hBox;
	
	public GraphEditorWindow(ProjectManagerApplication app) {
		super();
		this.app = app;
	}
    
    private void InitControlButton(Button b, final editorState st)
    {
    	b.setMinWidth(120);
    	b.setMinHeight(40);
    	b.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	currentState = st;
            }
        });
    }

	@Override
	public String getTitle() {
		return "Gráfszerkesztõ";
	}

	@Override
	public Parent getView() {
		BorderPane pane = new BorderPane();

    	currentState = editorState.SELECT_NODE;
    	
    	graph_model = new GraphDataModel();
    	graph_view = new Canvas(graph_viewW, graph_viewH);
        hBox = new HBox();
        VBox controlButtonBox = new VBox();

        Button selectNodeButton	= new Button("Pont kijelölése");
        Button addNodeButton 	= new Button("Pont hozzáadása");
        Button deleteNodeButton = new Button("Pont törlése");
        Button addEdgeButton 	= new Button("Él hozzáadása");
        Button deleteEdgeButton = new Button("Él törlése");

        InitControlButton(selectNodeButton, editorState.SELECT_NODE);
        InitControlButton(addNodeButton, 	editorState.ADD_NODE);
        InitControlButton(deleteNodeButton, editorState.DELETE_NODE);
        InitControlButton(addEdgeButton, 	editorState.ADD_EDGE);
        InitControlButton(deleteEdgeButton, editorState.DELETE_EDGE);
        
        controlButtonBox.getChildren().addAll(selectNodeButton, addNodeButton, deleteNodeButton, addEdgeButton, deleteEdgeButton);

        //hBox.getChildren().add(controlButtonBox);
        hBox.getChildren().addAll(controlButtonBox, graph_view);

        //root.getChildren().addAll(graph_view, hBox);
        pane.getChildren().addAll(hBox);
        
    	graph_model.renderGraph(graph_view.getGraphicsContext2D());
        
        graph_view.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
               dragStart = new Point2D((float) me.getX(), (float) me.getY());
               dragAnchor = dragStart;
           }
        });

        graph_view.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	if(currentState == editorState.SELECT_NODE) {
            		Point2D mousePos = new Point2D((float) me.getX(), (float) me.getY());
            		if(graph_view.contains(mousePos)) {
	            		graph_model.dragNode(dragAnchor, mousePos);
	            		dragAnchor = mousePos;
            		}
            	}
            	graph_model.renderGraph(graph_view.getGraphicsContext2D());
            }
        });
        
        graph_view.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	if(currentState == editorState.ADD_EDGE) {
            		graph_model.addEdge(dragStart,
            				new Point2D((float) me.getX(), (float) me.getY()));
            	}
            	graph_model.renderGraph(graph_view.getGraphicsContext2D());
            }
        });

        graph_view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	final Point2D click = new Point2D((float) me.getX(), (float) me.getY());
            	if(currentState == editorState.SELECT_NODE) {
            		graph_model.selectNode(click);
            	}
            	else if(currentState == editorState.ADD_NODE) {
            		graph_model.addNode(click);
            	}
            	else if(currentState == editorState.DELETE_NODE) {
            		graph_model.deleteNode(click);
            	}
            	else if(currentState == editorState.DELETE_EDGE) {
            		graph_model.deleteEdge(click);
            	}
            	graph_model.renderGraph(graph_view.getGraphicsContext2D());
            }
        });
		return pane;
	}
}
