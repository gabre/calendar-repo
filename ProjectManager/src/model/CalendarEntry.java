package model;

import java.util.Date;

public class CalendarEntry {
	private String name;
	private String description;
	private Date date;
	private Project project;
	
	public CalendarEntry(String name, String description, Date date) {
		this.name = name;
		this.description = description;
		this.date = date;
	}

	public CalendarEntry(String name, String description, Date date,
			Project project) {
		this.name = name;
		this.description = description;
		this.date = date;
		this.project = project;
	}


	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Date getDate() {
		return date;
	}

	public Project getProject() {
		return project;
	}

}
