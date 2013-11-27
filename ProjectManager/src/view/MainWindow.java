package view;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.CalendarEntry;
import app.ProjectManagerApplication;

public class MainWindow extends Window implements Observer {
	
	private ListView<CalendarEntry> entryList;

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

	// Should be factored down to smaller methods
	// If we doesn't want to use something (e.g. titleLabel) in the future, we can create a local variable and use it that way
	@Override
	public Parent getView() {
		BorderPane mainPane = new BorderPane();
		
		HBox topButtons = new HBox(5);
		
		Button btnOpenProjectStepEditor = new Button("Projektlépés-tervezõ");
		Button btnOpenProjectScheduler = new Button("Projektütemezõ");
		Button btnOpenResourceManager = new Button("Erõforráskezelõ");
		
		btnOpenProjectScheduler.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openProjectScheduler();
			}
		});
		
		topButtons.getChildren().addAll(
				btnOpenProjectStepEditor,
				btnOpenProjectScheduler,
				btnOpenResourceManager);
		
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
		
		mainPane.setTop(topButtons);
		mainPane.setCenter(entryList);
		
		BorderPane.setMargin(topButtons, new Insets(5, 5, 0, 5));
		BorderPane.setMargin(entryList, new Insets(5));
		
		return mainPane;
	}
	
	private void openProjectScheduler() {
		app.projSchedWinOpened(app.getModel().getProjects().get(0));	
	}
	
	/*
	private void closeProjectScheduler() {
		app.projSchedWinClosed();		
	} */

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

}
