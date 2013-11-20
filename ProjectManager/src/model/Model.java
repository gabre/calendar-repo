package model;

import java.util.Comparator;
import java.util.HashMap;
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
	private ObservableMap<String, Project> projects = FXCollections.observableHashMap();
	
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
	
	public void addProjectStep(String pname, String name, int duration, String description) {
		 projects.get(pname).addStep(new ProjectStep(name, duration, description));
	}
	
	public void deleteProjectStep(String pname, ProjectStep step) {
		projects.get(pname).deleteStep(step);
	}
	
	public void editProjectStep(String pname, ProjectStep oldStep, ProjectStep newStep)
	{
		projects.get(pname).editStep(oldStep, newStep);
	}

	public ObservableList<ProjectStep> getProjectSteps(String name)
	{
		return projects.get(name).getAllSteps();
	}

	public Project getProject(String name)
	{
		return projects.get(name);
	}
	
	public HashMap<String, Integer> calculateMetrics(Project p)
	{
		return new HashMap<String, Integer>();
	}
	
	public void addProject(String name)
	{
		projects.put(name, new Project(name));
	}
}
