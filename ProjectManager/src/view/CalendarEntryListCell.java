package view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.CalendarEntry;

public class CalendarEntryListCell extends ListCell<CalendarEntry> {

	@Override
	public void updateItem(CalendarEntry entry, boolean empty) {
		super.updateItem(entry, empty);
		if (empty) {
			return;
		}
		
		DateFormat fmt = new SimpleDateFormat("yyyy. MM. dd.");
		
		VBox box = new VBox(5);
		box.getChildren().addAll(
				new Text(fmt.format(entry.getDate())),
				new Text(entry.getName()));
		if (!entry.getDescription().isEmpty()) {
			box.getChildren().add(new Text(entry.getDescription()));
		}
		
		setGraphic(box);
	}

	
}
