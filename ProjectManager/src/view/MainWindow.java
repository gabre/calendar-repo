package view;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.CalendarEntry;
import model.Project;
import app.ProjectManagerApplication;

public class MainWindow extends Window implements Observer {
	
	private ListView<CalendarEntry> entryList;
	private ListView<Project> projectList;
	private TextField newProjectNameField;

	public MainWindow(ProjectManagerApplication app) {
		super();
		this.app = app;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() {
		return new String("Project Manager / Calendar");
	}

	@Override
	public Parent getView() {
		BorderPane mainPane = new BorderPane();

		entryList = new ListView<>(app.getModel().getCalendarEntries());
		entryList.setCellFactory(new Callback<ListView<CalendarEntry>, ListCell<CalendarEntry>>() {
			@Override
			public ListCell<CalendarEntry> call(ListView<CalendarEntry> view) {
				return new CalendarEntryListCell();
			}
		});
		entryList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				entryListMouseHandler(event);
			}
		});
		
		Pane menuPane = createMenuPane();
		Pane projectPane = createProjectPane();
		
		mainPane.setTop(menuPane);
		mainPane.setLeft(projectPane);
		mainPane.setCenter(entryList);
		
		BorderPane.setMargin(menuPane, new Insets(0, 0, 5, 0));
		BorderPane.setMargin(projectPane, new Insets(0, 5, 0, 0));
		
		mainPane.setPadding(new Insets(5));
		return mainPane;
	}
	
	private void openGraphEditor() {
		app.graphEditorWinOpened();	
	}
	
	private void openProjectScheduler() {
		if (projectList.getSelectionModel().isEmpty()) {
			return;
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

	private void entryListMouseHandler(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) &&
				event.getClickCount() == 2 &&
				!entryList.getSelectionModel().isEmpty()) {
			CalendarEntry sel = entryList.getSelectionModel().getSelectedItem();
			app.calendarEntryOpened(sel);
		} else if (event.getButton().equals(MouseButton.SECONDARY) &&
				event.getClickCount() == 2 &&
				!entryList.getSelectionModel().isEmpty()) {
			CalendarEntry sel = entryList.getSelectionModel().getSelectedItem();
			app.getModel().removeEntry(sel);
		} else if (event.getButton().equals(MouseButton.MIDDLE)) {
			CalendarEntry newEntry = new CalendarEntry("Új esemény", "", new Date());
			app.getModel().addEntry(newEntry);
			app.calendarEntryOpened(newEntry);
		}
	}
	
	private Pane createMenuPane() {
		HBox topButtons = new HBox(5);

		Button btnOpenProjectGraphEditor = new Button("Projektgráf-tervezõ");
		Button btnOpenProjectStepEditor = new Button("Projektlépés-tervezõ");
		Button btnOpenProjectScheduler = new Button("Projektütemezõ");
		Button btnOpenResourceManager = new Button("Erõforráskezelõ");

		btnOpenProjectGraphEditor.setOnAction(new EventHandler<ActionEvent>() {
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
		
		topButtons.getChildren().addAll(
				btnOpenProjectGraphEditor,
				btnOpenProjectStepEditor,
				btnOpenProjectScheduler,
				btnOpenResourceManager);
		
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
		
		HBox innerBox = new HBox(5);
		innerBox.getChildren().addAll(btnCreate, btnDelete);
		
		box.getChildren().addAll(projectList, newProjectNameField, innerBox);
		return box;
	}

}
