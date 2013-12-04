package model;

public class ResourceElement {
	private String name;
	private String competence;
	private int competenceId;
	
	static private int id = 0;
	static private int getId() {
		return ++id;
	}
	
	public ResourceElement(String n, String c) {
		name = n;
		competence = c;
		competenceId = getId();
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
	
	public void setCompetence(String value) {
		competence = value;
	}
}