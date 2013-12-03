package test.unittest;

import static org.junit.Assert.*;
import model.Descriptor;
import model.Project;
import model.ProjectStep;

import org.junit.Test;


public class ProjectTest {
	
	String name = "some_name";
	String name2 = "some_other_name";

	@Test
	public void testGetName() {
		Project p = new Project(name);
		assertEquals(name, p.getName());
		
		p.setName(name2);
		assertEquals(name2, p.getName());
	}
	
	@Test
	public void testStepOperation() {
		Project p = new Project(name);
		
		Descriptor aDesc = new Descriptor("a", 10, "", 1);
		Descriptor bDesc = new Descriptor("b", 10, "", 1);
		Descriptor cDesc = new Descriptor("c", 10, "", 1);
		
		ProjectStep aStep = new ProjectStep(aDesc);
		ProjectStep bStep = new ProjectStep(bDesc);
		ProjectStep cStep = new ProjectStep(cDesc);
		
		p.addStep(aStep);
		p.addStep(bStep);
		p.addStep(cStep);
		
		Object[] expected = new Object[] {aStep, bStep, cStep};
		assertArrayEquals(expected, p.getAllSteps().toArray());
		
		p.deleteStep(cStep);
		Object[] expectedAfterDeletion = new Object[] {aStep, bStep};
		assertArrayEquals(expectedAfterDeletion, p.getAllSteps().toArray());
		
		p.editStep(bStep, cStep);
		Object[] expectedAfterEdit = new Object[] {aStep, cStep};
		assertArrayEquals(expectedAfterEdit, p.getAllSteps().toArray());
		
		p.exchangeSteps(aStep, 1);
		Object[] expectedAfterExchange = new Object[] {cStep, aStep};
		assertArrayEquals(expectedAfterExchange, p.getAllSteps().toArray());
	}

}
