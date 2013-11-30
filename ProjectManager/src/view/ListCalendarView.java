package view;

import java.util.Date;

import app.ProjectManagerApplication;
import model.CalendarEntry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ListCalendarView extends VBox {

	public ListCalendarView(final ProjectManagerApplication app) {
		super(5);
		
		final ListView<CalendarEntry> listView = new ListView<>(app.getModel().getCalendarEntries());
		listView.setCellFactory(new Callback<ListView<CalendarEntry>, ListCell<CalendarEntry>>() {
			@Override
			public ListCell<CalendarEntry> call(ListView<CalendarEntry> view) {
				return new CalendarEntryListCell();
			}
		});
		listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.PRIMARY) &&
						event.getClickCount() == 2 &&
						!listView.getSelectionModel().isEmpty()) {
					CalendarEntry sel = listView.getSelectionModel().getSelectedItem();
					app.calendarEntryOpened(sel);
				}
			}
		});
		
		HBox buttons = new HBox(5);
		Button btnCreate = new Button("Új esemény");
		Button btnDelete = new Button("Törlés");
		buttons.getChildren().addAll(btnCreate, btnDelete);
		
		btnCreate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				CalendarEntry newEntry = new CalendarEntry("Új esemény", "", new Date());
				app.getModel().addEntry(newEntry);
				app.calendarEntryOpened(newEntry);
			}
		});
		
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!listView.getSelectionModel().isEmpty()) {
					CalendarEntry sel = listView.getSelectionModel().getSelectedItem();
					app.getModel().removeEntry(sel);
				}
			}
		});
		
		getChildren().addAll(listView, buttons);
	}

}
