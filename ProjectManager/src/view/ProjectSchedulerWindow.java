package view;

import model.Project;
import model.ProjectStep;
import app.ProjectManagerApplication;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProjectSchedulerWindow extends Window {

	private TableView<ProjectStep> tView;
	
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
		
		/* TVIEW TABLE SETUP  */
		tView = new TableView<ProjectStep>();
		TableColumn<ProjectStep, String> col1 = new TableColumn<ProjectStep, String>("Name");
        TableColumn<ProjectStep, Integer> col2 = new TableColumn<ProjectStep, Integer>("Duration");
        TableColumn<ProjectStep, String> col3 = new TableColumn<ProjectStep, String>("Description");
        col1.setCellValueFactory(
        	    new PropertyValueFactory<ProjectStep,String>("name")
        	);
        col2.setCellValueFactory(
        	    new PropertyValueFactory<ProjectStep,Integer>("duration")
        	);
        col3.setCellValueFactory(
        	    new PropertyValueFactory<ProjectStep,String>("description")
        	);
        tView.getColumns().addAll(col1, col2, col3);	
		tView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tView.setItems(app.getModel().getProjectSteps("almafa"));
		/*     *****          */
		
		Button addButton = new Button("Add");
		Button editButton = new Button("Edit");
		Button deleteButton = new Button("Remove");
		HBox buttonsHBox = new HBox();
		HBox props = new HBox();
		VBox metrics = new VBox();
		
		props.setPadding(new Insets(5));
		metrics.setPadding(new Insets(5));
		metrics.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 6;");
		metrics.setMinHeight(100);
		metrics.setAlignment(Pos.TOP_LEFT);
		props.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 6;");
		props.setMinHeight(100);
		metrics.setAlignment(Pos.BOTTOM_LEFT);
		hBox.setSpacing(10);
		rightSideVBox.setSpacing(5);
		buttonsHBox.setSpacing(4);
		
		hBox.getChildren().add(tView);
		hBox.setHgrow(tView, Priority.ALWAYS);
		buttonsHBox.getChildren().addAll(addButton, editButton, deleteButton);
		rightSideVBox.getChildren().addAll(props, buttonsHBox, metrics);
		rightSideVBox.setVgrow(props, Priority.SOMETIMES);
		rightSideVBox.setVgrow(metrics, Priority.SOMETIMES);
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
		return mainPane;
	}

	protected void handleDeleteStep() {
		Project currentProject = app.getModel().getProject("almafa");
		ProjectStep item = getSelectedProjectStep();
		if (item == null)
		{
			app.showMessage("You have to select a project step to delete.");
		} else
		{
			app.getModel().deleteProjectStep(currentProject.getName(), item);	
		}
	}

	private void handleAddStep() {
		Project currentProject = app.getModel().getProject("almafa");
		app.stepEditorWinAddModeOpened(currentProject, new ProjectStep());	
	}

	private void handleEditStep() {
		ProjectStep item = getSelectedProjectStep();
		if (item == null)
		{
			app.showMessage("You have to select a project step to edit.");
		} else
		{
			app.stepEditorWinEditModeOpened(app.getModel().getProject("almafa"), item);
		}
	}
	
	private ProjectStep getSelectedProjectStep() {
		ObservableList<ProjectStep> selectedOne = tView.getSelectionModel().getSelectedItems();
		if (selectedOne.size() == 0)
		{
			return null;
		} else
		{
			return (ProjectStep) selectedOne.get(0);	
		}
		
	}

}
