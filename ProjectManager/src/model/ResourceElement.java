package model;

public class ResourceElement {
	private String name;
	private String competence;
	
	public ResourceElement(String n, String c) {
		name = n;
		competence = c;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCompetence() {
		return competence;
	}
}