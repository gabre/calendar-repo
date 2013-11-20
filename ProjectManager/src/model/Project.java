package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Project {
	
	private String name;
	private ObservableMap<String, ProjectStep> steps;
	
	public Project(String name)
	{
		name = this.name;
		steps = FXCollections.observableHashMap();
	}
	
	public void addStep(String name, int duration, String description)
	{
		steps.put(name, new ProjectStep(name, duration, description));
	}
	
	public void deleteStep(String name)
	{
		steps.remove(name);
	}
	
	public void editStep(String name, int duration, String description)
	{
		ProjectStep s = steps.get(name);
		s.setParams(duration, description);
	}
	
	public ProjectStep getStep(String name)
	{
		return steps.get(name);
	}

	public ObservableList<ProjectStep> getAllSteps() {
		return FXCollections.observableArrayList(steps.values());
	}
}
