package model;

public class ProjectStep {
	
	private String name;
	private int duration;
	private String description;

	public ProjectStep(String name, int duration, String description)
	{
		this.name = name;
		this.duration = duration;
		this.description = description;
	}
	
	public void setParams(int duration, String description)
	{
		this.duration = duration;
		this.description = description;
	}

	public String getName()
	{
		return name;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	public String getDescription()
	{
		return description;
	}
}
