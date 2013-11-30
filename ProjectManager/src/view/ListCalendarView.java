package view;

import java.util.Date;

import app.ProjectManagerApplication;
import model.CalendarEntry;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ListCalendarView extends HBox {
	
	private ProjectManagerApplication app;
	private ListView<CalendarEntry> listView;

	public ListCalendarView(ProjectManagerApplication app) {
		super(5);
		this.app = app;
		
		listView = new ListView<>(app.getModel().getCalendarEntries());
		listView.setCellFactory(new Callback<ListView<CalendarEntry>, ListCell<CalendarEntry>>() {
			@Override
			public ListCell<CalendarEntry> call(ListView<CalendarEntry> view) {
				return new CalendarEntryListCell();
			}
		});
		listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				entryListMouseHandler(event);
			}
		});
		
		getChildren().addAll(listView);
	}
	
	private void entryListMouseHandler(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) &&
				event.getClickCount() == 2 &&
				!listView.getSelectionModel().isEmpty()) {
			CalendarEntry sel = listView.getSelectionModel().getSelectedItem();
			app.calendarEntryOpened(sel);
		} else if (event.getButton().equals(MouseButton.SECONDARY) &&
				event.getClickCount() == 2 &&
				!listView.getSelectionModel().isEmpty()) {
			CalendarEntry sel = listView.getSelectionModel().getSelectedItem();
			app.getModel().removeEntry(sel);
		} else if (event.getButton().equals(MouseButton.MIDDLE)) {
			CalendarEntry newEntry = new CalendarEntry("Új esemény", "", new Date());
			app.getModel().addEntry(newEntry);
			app.calendarEntryOpened(newEntry);
		}
	}

}
