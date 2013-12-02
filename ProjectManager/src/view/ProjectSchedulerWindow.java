package view;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import model.Project;
import model.ProjectStep;
import app.ProjectManagerApplication;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ProjectSchedulerWindow extends Window {

	private TableView<ProjectStep> tView;
	private Project currentProject;
	private VBox metrics;
	private VBox props;

	public ProjectSchedulerWindow(ProjectManagerApplication app) {
		super();
		this.app = app;
	}

	@Override
	public String getTitle() {
		return new String("Project Scheduler");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Parent getView() {
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(10));
		HBox hBox = new HBox();
		VBox rightSideVBox = new VBox();

		/* TVIEW TABLE SETUP */
		tView = new TableView<ProjectStep>();
		TableColumn<ProjectStep, String> col1 = new TableColumn<ProjectStep, String>(
				"Name");
		TableColumn<ProjectStep, Integer> col2 = new TableColumn<ProjectStep, Integer>(
				"Duration");
		TableColumn<ProjectStep, String> col3 = new TableColumn<ProjectStep, String>(
				"Description");
		col1.setCellValueFactory(new PropertyValueFactory<ProjectStep, String>(
				"name"));
		col2.setCellValueFactory(new PropertyValueFactory<ProjectStep, Integer>(
				"duration"));
		col3.setCellValueFactory(new PropertyValueFactory<ProjectStep, String>(
				"description"));
		tView.getColumns().addAll(col1, col2, col3);
		tView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tView.getColumns().get(0).setPrefWidth(150);
		tView.getColumns().get(1).setPrefWidth(100);
		tView.getColumns().get(2).setPrefWidth(250);
		tView.setPrefWidth(470);
		/* ***** */

		Button addButton = new Button("Add");
		Button editButton = new Button("Edit");
		Button deleteButton = new Button("Remove");
		HBox buttonsHBox = new HBox();
		HBox upDownHBox = new HBox();
		props = new VBox();
		metrics = new VBox();
		
		Label metricsText = new Label("Metrics");

		Button upButton = new Button("Up");
		Button downButton = new Button("Down");

		props.setPadding(new Insets(5));
		metrics.setPadding(new Insets(5));
		metrics.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 6;");
		metrics.setMinHeight(100);
		metrics.setAlignment(Pos.TOP_LEFT);
		props.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 6;");
		props.setMinHeight(100);
		hBox.setSpacing(10);
		rightSideVBox.setSpacing(5);
		buttonsHBox.setSpacing(4);

		metrics.getChildren().add(metricsText);
		
		hBox.getChildren().add(tView);
		HBox.setHgrow(tView, Priority.ALWAYS);
		buttonsHBox.getChildren().addAll(addButton, editButton, deleteButton);
		upDownHBox.getChildren().addAll(downButton, upButton);
		rightSideVBox.getChildren().addAll(props, buttonsHBox, upDownHBox,
				metrics);
		VBox.setVgrow(props, Priority.SOMETIMES);
		VBox.setVgrow(metrics, Priority.SOMETIMES);
		hBox.getChildren().add(rightSideVBox);

		mainPane.setCenter(hBox);

		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleAddStep();
			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleEditStep();
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleDeleteStep();
			}
		});

		upButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				exchangeSteps(-1);
			}
		});

		downButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				exchangeSteps(1);
			}
		});
		tView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<ProjectStep>()
			    {
	        @Override
	        public void onChanged(Change<? extends ProjectStep> change)
	        {
	            if (change.getList().size() == 1)
	            {
	            	setupProps(change.getList().get(0));
	            }
	        }

	    });
		
		return mainPane;
	}
	
	protected void setupProps(ProjectStep projectStep) {
    	props.getChildren().clear();
    	props.getChildren().addAll(new Label("Competence needed: " + projectStep.getNeededCompetence()),	
    							   new Label("Cost: " + Integer.toString(projectStep.getCost())),
    							   new Label("Hardness: " + Integer.toString(projectStep.getDifficulty())));
	}

	private void addMetrics(VBox metrics) {
		if(currentProject == null) {
			return;
		}
		Set<Entry<String, Number>> metricsSet = app.getModel().calculateMetrics(currentProject).entrySet();
		metrics.getChildren().clear();
		for( Entry<String, Number> m : metricsSet ) {
			metrics.getChildren().add(new Label(m.getKey() + ": " + m.getValue().toString()));
		}
	}

	public void setCurrentProject(Project project) {
		this.currentProject = project;
		tView.setItems(currentProject.getAllSteps());
		addMetrics(metrics);
	}

	protected void handleDeleteStep() {
		ProjectStep item = getSelectedProjectStep();
		if (item == null) {
			app.showMessage("You have to select a project step to delete.");
		} else {
			currentProject.deleteStep(item);
		}
	}

	private void handleAddStep() {
		app.stepEditorWinAddModeOpened(currentProject, new ProjectStep());
	}

	private void handleEditStep() {
		ProjectStep item = getSelectedProjectStep();
		if (item == null) {
			app.showMessage("You have to select a project step to edit.");
		} else {
			app.stepEditorWinEditModeOpened(
					currentProject, item);
		}
	}

	private ProjectStep getSelectedProjectStep() {
		ObservableList<ProjectStep> selectedOne = tView.getSelectionModel()
				.getSelectedItems();
		if (selectedOne.size() == 0) {
			return null;
		} else {
			return (ProjectStep) selectedOne.get(0);
		}
	}

	private void exchangeSteps(int indexOffset) {
		ProjectStep item = getSelectedProjectStep();
		if (item == null) {
			return;
		}
		int refreshIndex = currentProject.exchangeSteps(item, indexOffset);
		tView.getSelectionModel().select(refreshIndex);
	}
}
