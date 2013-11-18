package model;

import java.util.Comparator;
import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model extends Observable {
	private static final Comparator<CalendarEntry> entryComparator = new Comparator<CalendarEntry>() {
		@Override
		public int compare(CalendarEntry e1, CalendarEntry e2) {
			return e1.getDate().compareTo(e2.getDate());
		}
	};
	
	private ObservableList<CalendarEntry> entries = FXCollections.observableArrayList();
	
	public void addEntry(CalendarEntry e) {
		entries.add(e);
		FXCollections.sort(entries, entryComparator);
	}
	
	public void removeEntry(CalendarEntry e) {
		entries.remove(e);
	}
	
	public void modifyEntry(CalendarEntry from, CalendarEntry to) {
		if (entries.remove(from)) {
			entries.add(to);
			FXCollections.sort(entries, entryComparator);
		}
	}

	public ObservableList<CalendarEntry> getCalendarEntries() {
		return FXCollections.unmodifiableObservableList(entries);
	}
}
