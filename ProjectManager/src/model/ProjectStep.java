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

	public void setNeededCompetence(String competence)
	{
		desc.setNeededCompetence(competence);
	}
	
	public String getNeededCompetence()
	{
		return desc.getNeededCompetence();
	}
	
	public void setCost(int cost)
	{
		desc.setCost(cost);
	}
	
	public int getCost()
	{
		return desc.getCost();
	}	
	
	public void setDifficulty(int cost)
	{
		desc.setDifficulty(cost);
	}
	
	public int getDifficulty()
	{
		return desc.getDifficulty();
	}	
}
