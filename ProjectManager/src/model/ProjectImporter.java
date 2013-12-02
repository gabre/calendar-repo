package model;

import java.util.Calendar;
import java.util.Date;

import javafx.collections.ObservableList;

public class ProjectImporter {

	public static void importProject(Project project, Date date, ObservableList<CalendarEntry> calendarEntries) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		for (ProjectStep step : project.getAllSteps()) {
			int d = step.getDuration();
			if (d > 0) {
				CalendarEntry begin = new CalendarEntry("Kezdet: " + step.getName(), "", c.getTime(), project);
				c.add(Calendar.DATE, d - 1);
				CalendarEntry end = new CalendarEntry("Befejezés: " + step.getName(), "", c.getTime(), project);
				c.add(Calendar.DATE, 1);
				calendarEntries.addAll(begin, end);
			}
		}
	}

}
