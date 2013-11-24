package model;

public class ResourceElement {
	private String name;
	private String competence;
	private int competenceId;
	
	public ResourceElement(String n, String c, int id) {
		name = n;
		competence = c;
		competenceId = id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCompetence() {
		return competence;
	}
	
	public int getCompetenceId() {
		return competenceId;
	}
}