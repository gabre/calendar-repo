package view;

import app.ProjectManagerApplication;
import javafx.geometry.Point2D;
import model.GraphDataModel;
import model.GraphNodeData;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

	private TextField nameField = new TextField();
	private TextField durationField = new TextField();
	private TextField descriptionField = new TextField();
	private TextField difficultyField = new TextField();
	private TextField costField = new TextField();
	private Button saveButton =  new Button("Mentés");

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
        

		VBox nodePropBox = new VBox();
		
		Label l1 = new Label("Név:");
		Label l2 = new Label("Idõtartam:");
		Label l3 = new Label("Leírás:");
		Label l4 = new Label("Nehézség:");
		Label l5 = new Label("Költség:");
		
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        graph_model.setNodeData(nameField.getText(), durationField.getText(),
		        		descriptionField.getText(), difficultyField.getText(),
		        		costField.getText());
		        graph_model.renderGraph(graph_view.getGraphicsContext2D());
		    }
		});
		
		nodePropBox.setMinWidth(250);
		nodePropBox.setMinHeight(400);
		nodePropBox.getChildren().addAll(
				l1, nameField,
				l2, durationField,
				l3, descriptionField, 
				l4, difficultyField,
				l5, costField,
				saveButton);

		controlButtonBox.setSpacing(10);
        controlButtonBox.getChildren().addAll(
        		selectNodeButton,
        		addNodeButton,
        		deleteNodeButton,
        		addEdgeButton,
        		deleteEdgeButton);

        hBox.setSpacing(10);
        hBox.getChildren().addAll(controlButtonBox, graph_view, nodePropBox);
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
            		GraphNodeData node_data = graph_model.getSelectedNodeData();
            		if(node_data != null) {
	            		nameField.setText(node_data.name);
	    				durationField.setText(node_data.duration);
	    				descriptionField.setText(node_data.description);
	    				difficultyField.setText(node_data.difficulty);
    					costField.setText(node_data.cost);
            		}
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
