package model;

public class ProjectStep {
	
	private String name;
	private int duration;
	private String description;

	public ProjectStep()
	{
		this("", 0, "");
	}
	
	public ProjectStep(String name, int duration, String description)
	{
		this.name = name;
		this.duration = duration;
		this.description = description;
	}
	
	public void setParams(String newname, int duration, String description)
	{
		this.name = newname;
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
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
}
