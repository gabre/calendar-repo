package model;

import java.util.ArrayList;
import java.util.Observable;

public class Model extends Observable {
	private ArrayList<CalendarEntry> entries = new ArrayList<CalendarEntry>();
	
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
}
