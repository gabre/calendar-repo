package model;

public class Descriptor {
	private String name = "";
	private int duration = 0;
	private String description = "";
	private int difficulty = 0;
	String neededCompetence = "none";
	int cost = 0;
	
	public Descriptor(String name, int duration, String desc, int difficulty) {
		this.name = name;
		this.duration = duration;
		this.description = desc;
		this.difficulty = difficulty;
	}
	
	public Descriptor(String name, int duration, String description,
			int difficulty, String neededCompetence, int cost) {
		this.name = name;
		this.duration = duration;
		this.description = description;
		this.difficulty = difficulty;
		this.neededCompetence = neededCompetence;
		this.cost = cost;
	}

	public Descriptor() {
	}

	public String getNeededCompetence() {
		return neededCompetence;
	}

	public void setNeededCompetence(String neededCompetence) {
		this.neededCompetence = neededCompetence;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
}
