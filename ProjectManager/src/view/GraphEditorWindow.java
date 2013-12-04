package view;

import java.sql.SQLException;

import app.ProjectManagerApplication;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import model.CycleDetectedException;
import model.GraphDataModel;
import model.GraphNodeData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GraphEditorWindow extends Window {
    private enum editorState {SELECT_NODE, ADD_NODE, DELETE_NODE, ADD_EDGE, DELETE_EDGE};
    private editorState currentState;
    private GraphDataModel graph_model;
    private final int graph_viewW = 500;
    private final int graph_viewH = 500;
    private Canvas graph_view;
    private Point2D dragStart;
    private Point2D dragAnchor;
    private boolean startedDragOnNode = false;
    
    private HBox hBox;
	private TextField nameField = new TextField("");
	private TextField durationField = new TextField("0");
	private TextField descriptionField = new TextField("");
	private TextField difficultyField = new TextField("0");
	private TextField costField = new TextField("0");
	private ChoiceBox<String> competenceBox = new ChoiceBox<String>();
	private Button saveButton =  new Button("Mentés");

	public GraphEditorWindow(ProjectManagerApplication app) {
		super();
		this.app = app;
	}
    
    private void InitControlButton(Button b, final editorState st)
    {
    	setButtonSize(b);
    	b.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	currentState = st;
            }
        });
    }
    
    private void setButtonSize(Button b) {
    	b.setMinWidth(120);
    	b.setMinHeight(40);
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
        hBox.setPadding(new Insets(5));
        VBox controlButtonBox = new VBox();

        Button selectNodeButton	= new Button("Pont kijelölése");
        Button addNodeButton 	= new Button("Pont hozzáadása");
        Button deleteNodeButton = new Button("Pont törlése");
        Button addEdgeButton 	= new Button("Él hozzáadása");
        Button deleteEdgeButton = new Button("Él törlése");
        Button topSortButton    = new Button("Rendezés");

        InitControlButton(selectNodeButton, editorState.SELECT_NODE);
        InitControlButton(addNodeButton, 	editorState.ADD_NODE);
        InitControlButton(deleteNodeButton, editorState.DELETE_NODE);
        InitControlButton(addEdgeButton, 	editorState.ADD_EDGE);
        InitControlButton(deleteEdgeButton, editorState.DELETE_EDGE);
        setButtonSize(topSortButton);

		VBox nodePropBox = new VBox();
		
		Label l1 = new Label("Név:");
		Label l2 = new Label("Idõtartam:");
		Label l3 = new Label("Leírás:");
		Label l4 = new Label("Nehézség:");
		Label l5 = new Label("Költség:");
		Label l6 = new Label("Kompetencia:");
		
		difficultyField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> event,
					String from, String to) {
				ViewUtils.validateDifficulty(saveButton, to);
			}
		});
		costField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> event,
					String from, String to) {
				ViewUtils.validateNat(saveButton, to);
			}
		});
		durationField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> event,
					String from, String to) {
				ViewUtils.validateNat(saveButton, to);
			}
		});
		
		try {
			competenceBox.setItems(app.getDataManager().getCompetences());
		} catch (SQLException e) {
			
		}
		
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        graph_model.setNodeData(nameField.getText(), Integer.parseInt(durationField.getText()),
		        		descriptionField.getText(), Integer.parseInt(difficultyField.getText()),
		        		Integer.parseInt(costField.getText()));
		        graph_model.renderGraph(graph_view.getGraphicsContext2D());
		    }
		});
		
		topSortButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        try {
					app.projSchedWinOpened(app.getModel().sortProjectPlan(graph_model));
				} catch (CycleDetectedException e1) {
					app.showMessage("A gráfban kör van, így nem rendezhetõ!");
				}
		    }
		});
		
		nodePropBox.setMinWidth(250);
		nodePropBox.setMinHeight(400);
		nodePropBox.getChildren().addAll(
				l1, nameField,
				l2, durationField,
				l3, descriptionField,
				l4, difficultyField,
				l5, costField, ViewUtils.getVPlaceHolder(5),
				l6, competenceBox, ViewUtils.getVPlaceHolder(10),
				saveButton);

		controlButtonBox.setSpacing(10);
        controlButtonBox.getChildren().addAll(
        		selectNodeButton,
        		addNodeButton,
        		deleteNodeButton,
        		addEdgeButton,
        		deleteEdgeButton, ViewUtils.getVPlaceHolder(40),
        		topSortButton);

        hBox.setSpacing(10);
        hBox.getChildren().addAll(controlButtonBox, graph_view, nodePropBox);
        pane.getChildren().addAll(hBox);
        
    	graph_model.renderGraph(graph_view.getGraphicsContext2D());
        
        graph_view.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
               dragStart = new Point2D((float) me.getX(), (float) me.getY());
               dragAnchor = dragStart;
           	   startedDragOnNode = graph_model.validNode(dragStart);
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
            	// draw edge preview line
        		if(currentState == editorState.ADD_EDGE && startedDragOnNode) {
        			graph_view.getGraphicsContext2D().setStroke(Color.RED);
        			graph_view.getGraphicsContext2D().strokeLine(
        					dragStart.getX(),
        					dragStart.getY(),
        					me.getX(),
        					me.getY());
            	}
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
	            		setFields(node_data);
            		}
            	}
            	else if(currentState == editorState.ADD_NODE) {
            		GraphNodeData node_data = graph_model.addNode(click);
            		graph_model.selectNode(click);
            		setFields(node_data);
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

	protected void setFields(GraphNodeData node_data) {
		nameField.setText(node_data.desc.getName());
		durationField.setText(Integer.toString(node_data.desc.getDuration()));
		descriptionField.setText(node_data.desc.getDescription());
		difficultyField.setText(Integer.toString(node_data.desc.getDifficulty()));
		costField.setText(Integer.toString(node_data.desc.getCost()));
	}
}
