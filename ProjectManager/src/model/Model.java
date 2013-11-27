package model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Model extends Observable {
	private static final Comparator<CalendarEntry> entryComparator = new Comparator<CalendarEntry>() {
		@Override
		public int compare(CalendarEntry e1, CalendarEntry e2) {
			return e1.getDate().compareTo(e2.getDate());
		}
	};
	
	private ObservableList<CalendarEntry> entries = FXCollections.observableArrayList();
	private ObservableList<Project> projects = FXCollections.observableArrayList();
	
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
	
	public void sortProjectPlan()
	{
		
	}
	
	public HashMap<String, Number> calculateMetrics(Project p)
	{
		HashMap<String, Number> metrics = new HashMap<>();
		metrics.put("Average duration", calculateAvgDuration(p));
		metrics.put("Sum of duration", calculateSumDuration(p));
		return metrics;
	}
	
	private int calculateSumDuration(Project p) {
		int sum = 0;
		for(ProjectStep item : p.getAllSteps())
		{
			sum += item.getDuration();
		}
		return sum;
	}

	private double calculateAvgDuration(Project p) {
		return calculateSumDuration(p) / p.getAllSteps().size();
	}

	public void addProject(Project proj)
	{
		projects.add(proj);
	}
	
	public ObservableList<Project> getProjects() {
		return projects;
	}
	
}
