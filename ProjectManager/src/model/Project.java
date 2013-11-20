package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Project {
	
	private String name;
	private ObservableList<ProjectStep> steps;
	
	public Project(String name)
	{
		this.name = name;
		steps = FXCollections.observableArrayList();
	}
	
	public void addStep(ProjectStep newStep)
	{
		steps.add(newStep);
	}
	
	public void deleteStep(ProjectStep step)
	{
		steps.remove(step);
	}
	
	public void editStep(ProjectStep oldStep, ProjectStep newStep)
	{
		int index = steps.indexOf(oldStep);
		steps.set(index, newStep);
	}

	public ObservableList<ProjectStep> getAllSteps() {
		return steps;
	}

	public String getName() {
		return name;
	}
}
