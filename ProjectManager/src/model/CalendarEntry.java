package model;

import java.util.Date;

public class CalendarEntry {
	// TODO add an optional reference to a Project if the entry belongs to one
	
	private String name;
	private String description;
	private Date date;	
	
	public CalendarEntry(String name, String description, Date date) {
		this.name = name;
		this.description = description;
		this.date = date;
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

}
