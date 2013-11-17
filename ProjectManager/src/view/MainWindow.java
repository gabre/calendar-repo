package view;

import java.util.Observable;
import java.util.Observer;

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
	private ProjectManagerApplication app;

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
		
		topButtons.getChildren().addAll(
				btnOpenProjectStepEditor,
				btnOpenProjectScheduler,
				btnOpenResourceManager);
		
		final ListView<CalendarEntry> entryList = new ListView<>(app.getModel().getCalendarEntries());
		entryList.setCellFactory(new Callback<ListView<CalendarEntry>, ListCell<CalendarEntry>>() {
			@Override
			public ListCell<CalendarEntry> call(ListView<CalendarEntry> view) {
				return new CalendarEntryListCell();
			}
		});
		entryList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.PRIMARY) &&
						event.getClickCount() == 2 &&
						!entryList.getSelectionModel().isEmpty()) {
					CalendarEntry sel = entryList.getSelectionModel().getSelectedItem();
					app.calendarEntryOpened(sel);
				}
			}
		});
		
		mainPane.setTop(topButtons);
		mainPane.setCenter(entryList);
		
		BorderPane.setMargin(topButtons, new Insets(5, 5, 0, 5));
		BorderPane.setMargin(entryList, new Insets(5));
		
		return mainPane;
	}

}
