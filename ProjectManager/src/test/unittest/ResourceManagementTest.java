package test.unittest;

import static org.junit.Assert.*;

import java.sql.SQLException;

import model.Model;
import model.ResourceElement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ResourceManagementTest {
	
	@Before
	public void Init() throws ClassNotFoundException, SQLException {
		Model model = new Model();
		
		model.addCompetence("ut_fõzés");
		model.addCompetence("ut_takarítás");
		
		model.addResource(new ResourceElement("ut_Balázs", "ut_fõzés"));
		model.addResource(new ResourceElement("ut_János", "ut_takarítás"));
	}
	
	@Test
	public void CompetenceTest() throws ClassNotFoundException, SQLException {
		Model model = new Model();
		int count = model.getCompetences().size();
		
		model.addCompetence(new String("ut_varrás"));
		
		assertEquals(count + 1, model.getCompetences().size());
		
		model.editCompetence("ut_varrás", "ut_mosás");
		
		assertFalse(model.getCompetences().contains("ut_varrás"));
		assertTrue(model.getCompetences().contains("ut_mosás"));
		
		model.deleteCompetence("ut_mosás");
		
		assertEquals(count, model.getCompetences().size());
	}
	
	@Test(expected = SQLException.class)
	public void CompetenceExceptionTest() throws ClassNotFoundException, SQLException {
		Model model = new Model();
		model.deleteCompetence(new String("ut_fõzés"));
	}
	
	@Test
	public void ResourceTest() throws ClassNotFoundException, SQLException {
		Model model = new Model();
		int count = model.getResources().size();
		
		model.addResource(new ResourceElement("ut_András", "ut_fõzés"));
		
		assertEquals(count + 1, model.getResources().size());
		
		model.editResource(model.getResources().get(count), new ResourceElement("ut_András", "ut_takarítás"));
		
		assertEquals("ut_takarítás", model.getResources().get(count).getCompetence());
		
		ResourceElement elem = new ResourceElement("ut_Gábor", "ut_takarítás");
		
		model.editResource(model.getResources().get(count), elem);
		
		assertEquals("ut_Gábor", model.getResources().get(count).getName());
		
		model.deleteResource(elem);
		
		assertEquals(count, model.getResources().size());
	}
	
	@Test(expected = SQLException.class)
	public void ResourceExceptionTest() throws ClassNotFoundException, SQLException {
		Model model = new Model();	
		model.addResource(new ResourceElement("ut_Balázs", "ut_fõzés"));
	}
}
