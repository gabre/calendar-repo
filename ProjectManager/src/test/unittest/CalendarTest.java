package test.unittest;

import static org.junit.Assert.*;

import java.util.Date;

import model.CalendarEntry;
import model.Model;
import model.Project;
import model.ProjectImporter;
import model.ProjectStep;

import org.junit.Test;

public class CalendarTest {
	
	@Test
	public void importProjectTest() throws Exception {
		Model m = new Model();
		Project p = new Project("p");
		int durations[] = new int[] {2, 0, 1, 3};
		for (int d : durations) {
			ProjectStep s = new ProjectStep();
			s.setDuration(d);
			p.addStep(s);
		}
		m.addProject(p);
		ProjectImporter.importProject(p, new Date(2000, 01, 01), m.getCalendarEntries());
		// All entries were generated.
		assertEquals(6, m.getCalendarEntries().size());
		// The span of the entries is correct.
		assertEquals(new Date(2000, 01, 01), m.getCalendarEntries().get(0).getDate());
		assertEquals(new Date(2000, 01, 06), m.getCalendarEntries().get(5).getDate());
		// The entries' parent project is set correctly.
		assertEquals(p, m.getCalendarEntries().get(0).getProject());
	}
	
	@Test
	public void calendarEntryTest() throws Exception {
		Model m = new Model();
		m.addEntry(new CalendarEntry("a", "", new Date(2000, 01, 02)));
		m.addEntry(new CalendarEntry("c", "", new Date(2000, 01, 01)));
		m.addEntry(new CalendarEntry("b", "", new Date(2000, 01, 01)));
		// All entries were added.
		assertEquals(3, m.getCalendarEntries().size());
		// The entry list is sorted by date and name.
		assertEquals("b", m.getCalendarEntries().get(0).getName());
		assertEquals("c", m.getCalendarEntries().get(1).getName());
		assertEquals("a", m.getCalendarEntries().get(2).getName());
	}

}
