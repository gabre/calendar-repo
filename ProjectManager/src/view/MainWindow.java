package view;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.CalendarEntry;
import model.Project;
import app.ProjectManagerApplication;

public class MainWindow extends Window {
	
	private ListView<Project> projectList;
	private TextField newProjectNameField;
	private StackPane centerPane;

	public MainWindow(ProjectManagerApplication app) {
		super();
		this.app = app;
	}

	@Override
	public String getTitle() {
		return new String("Project Manager / Calendar");
	}

	@Override
	public Parent getView() {
		BorderPane mainPane = new BorderPane();

		Pane menuPane = createMenuPane();
		Pane projectPane = createProjectPane();
		projectPane.setStyle("-fx-border-width: 1; -fx-border-color: lightgray;");
		projectPane.setPadding(new Insets(5));
		projectPane.setPrefWidth(150);
		centerPane = new StackPane();
		centerPane.getChildren().add(new WeekCalendarView(app, 1));
		centerPane.setPrefSize(640, 360);
		
		mainPane.setTop(menuPane);
		mainPane.setLeft(projectPane);
		mainPane.setCenter(centerPane);
		
		BorderPane.setMargin(menuPane, new Insets(0, 0, 5, 0));
		BorderPane.setMargin(centerPane, new Insets(0, 0, 0, 5));
		
		mainPane.setPadding(new Insets(5));
		return mainPane;
	}
	
	private void openGraphEditor() {
		app.graphEditorWinOpened();	
	}
	
	private void openProjectScheduler() {
		if (projectList.getSelectionModel().isEmpty()) {
			projectList.getSelectionModel().select(0);
		}
		
		app.projSchedWinOpened(projectList.getSelectionModel().getSelectedItem());	
	}
	
	/*
	private void closeProjectScheduler() {
		app.projSchedWinClosed();		
	} */

	private void openResourceManagement() {
		app.resourceManagementOpened();
	}

	private Pane createMenuPane() {
		HBox topButtons = new HBox(5);

		Button btnOpenProjectStepEditor = new Button("Projektlépés-tervezõ");
		Button btnOpenProjectScheduler = new Button("Projektütemezõ");
		Button btnOpenResourceManager = new Button("Erõforráskezelõ");
		final ComboBox<String> cbCalendarView = new ComboBox<>(FXCollections.observableArrayList("Heti nézet", "Havi nézet", "Napló nézet"));
		cbCalendarView.getSelectionModel().select(0);

		btnOpenProjectStepEditor.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openGraphEditor();
			}
		});
		
		btnOpenProjectScheduler.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openProjectScheduler();
			}
		});
		
		btnOpenResourceManager.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openResourceManagement();
			}
		});
		
		cbCalendarView.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Node view;
				switch (cbCalendarView.getSelectionModel().getSelectedIndex()) {
				case 0:
					view = new WeekCalendarView(app, 1);
					break;
				case 1:
					view = new WeekCalendarView(app, 4);
					break;
				case 2:
					view = new ListCalendarView(app);
					break;
				default:
					view = null;
				}
				if (view != null) {
					centerPane.getChildren().clear();
					centerPane.getChildren().add(view);
				}
			}
		});
		
		topButtons.getChildren().addAll(
				btnOpenProjectStepEditor,
				btnOpenProjectScheduler,
				btnOpenResourceManager,
				cbCalendarView);
		
		return topButtons;
	}
	
	private Pane createProjectPane() {
		VBox box = new VBox(5);
		
		projectList = new ListView<>(app.getModel().getProjects());
		projectList.setCellFactory(new Callback<ListView<Project>, ListCell<Project>>() {
			@Override
			public ListCell<Project> call(ListView<Project> list) {
				return new TextFieldListCell<Project>(new StringConverter<Project>() {
					@Override
					public Project fromString(String str) {
						return null;
					}

					@Override
					public String toString(Project p) {
						return p.getName();
					}				
				});
			}
		});
		
		newProjectNameField = new TextField();
		newProjectNameField.setPromptText("Új projekt neve");
		
		Button btnCreate = new Button("Új projekt");
		Button btnDelete = new Button("Törlés");
		Button btnRename = new Button("Átnevezés");
		
		btnCreate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = newProjectNameField.getText();
				if (!name.isEmpty()) {
					app.getModel().addProject(new Project(name));
				}
			}
		});
		
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!projectList.getSelectionModel().isEmpty()) {
					Project sel = projectList.getSelectionModel().getSelectedItem();
					app.getModel().removeProject(sel);
				}
			}
		});
		
		btnRename.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = newProjectNameField.getText();
				if (!projectList.getSelectionModel().isEmpty() && !name.isEmpty()) {
					Project sel = projectList.getSelectionModel().getSelectedItem();
					app.getModel().modifyProjectName(sel, name);
				}
			}
		});
		
		HBox innerBox = new HBox(5);
		innerBox.getChildren().addAll(btnCreate, btnDelete, btnRename);
		
		box.getChildren().addAll(projectList, newProjectNameField, innerBox);
		return box;
	}

}
