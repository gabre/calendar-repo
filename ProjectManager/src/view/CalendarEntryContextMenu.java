package view;

import model.CalendarEntry;
import app.ProjectManagerApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class CalendarEntryContextMenu extends ContextMenu {
	
	public CalendarEntryContextMenu(final ProjectManagerApplication app, final CalendarEntry entry) {
		MenuItem editMenuItem = new MenuItem("Szerkesztés");
		MenuItem deleteMenuItem = new MenuItem("Törlés");
		getItems().addAll(editMenuItem, deleteMenuItem);
		
		editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.calendarEntryOpened(entry);
			}
		});
		deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.getModel().removeEntry(entry);
			}
		});
	}

}
