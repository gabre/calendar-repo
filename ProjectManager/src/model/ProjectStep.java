package model;

public class ProjectStep {
	
	private Descriptor desc;	

	public ProjectStep()
	{
		this(new Descriptor("", 0, "", 0));
	}
	
	public ProjectStep(Descriptor desc)
	{
		this.desc = desc;
	}
	
	public void setParams(Descriptor desc)
	{
		this.desc = desc;
	}

	public String getName()
	{
		return desc.getName();
	}
	
	public int getDuration()
	{
		return desc.getDuration();
	}
	
	public String getDescription()
	{
		return desc.getDescription();
	}
	
	public void setName(String name)
	{
		desc.setName(name);
	}
	
	public void setDuration(int duration)
	{
		desc.setDuration(duration);
	}
	
	public void setDescription(String description)
	{
		desc.setDescription(description);
	}
}
