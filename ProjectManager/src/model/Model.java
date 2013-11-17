package model;

import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model extends Observable {
	private ObservableList<CalendarEntry> entries = FXCollections.observableArrayList();
	
	public void addEntry(CalendarEntry e) {
		entries.add(e);
	}
	
	public void removeEntry(CalendarEntry e) {
		entries.remove(e);
	}
	
	public void modifyEntry(CalendarEntry from, CalendarEntry to) {
		if (entries.remove(from)) {
			entries.add(to);
		}
	}

	public ObservableList<CalendarEntry> getCalendarEntries() {
		return FXCollections.unmodifiableObservableList(entries);
	}
}
