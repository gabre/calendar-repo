package view;

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
		
		VBox box = new VBox(5);
		box.getChildren().addAll(
				new Text(entry.getName()),
				new Text(entry.getDescription()));
		setGraphic(box);
	}

	
}
