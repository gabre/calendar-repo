package test.unittest;

import static org.junit.Assert.*;
import model.Descriptor;
import model.ProjectStep;

import org.junit.Test;


public class UtilityTest {

	@Test
	public void testProjectStep() {
		String name = "name";
		int dur = 100;
		String description = "this is a description";
		int difficulty = 10;
		String neededCompetence = "someCompetence";
		int cost = 1000;
		
		Descriptor d = new Descriptor(name, dur, description, difficulty, 
									  neededCompetence, cost);
		ProjectStep pS = new ProjectStep(d);
		
		assertEquals(pS.getName(), name);
		assertEquals(pS.getCost(), cost);
		assertEquals(pS.getDescription(), description);
		assertEquals(pS.getDifficulty(), difficulty);
		assertEquals(pS.getNeededCompetence(), neededCompetence);
		assertEquals(pS.getDuration(), dur);
	}

	@Test
	public void testDescriptor() {
		String name = "a name";
		int dur = 1100;
		String description = "some description";
		int difficulty = 110;
		String neededCompetence = "a competence";
		int cost = 1111;
		
		Descriptor d = new Descriptor(name, dur, description, difficulty, 
									  neededCompetence, cost);;
		
		assertEquals(d.getName(), name);
		assertEquals(d.getCost(), cost);
		assertEquals(d.getDescription(), description);
		assertEquals(d.getDifficulty(), difficulty);
		assertEquals(d.getNeededCompetence(), neededCompetence);
		assertEquals(d.getDuration(), dur);
	}
	
}
