package test.unittest;

import static org.junit.Assert.*;

import java.sql.SQLException;

import model.DataManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ResourceManagementTest {
	
	@Before
	public void Init() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		
		manager.addCompetence("ut_fõzés");
		manager.addCompetence("ut_takarítás");
		
		manager.addResource("ut_Balázs", "ut_fõzés");
		manager.addResource("ut_János", "ut_takarítás");
	}
	
	@Test
	public void CompetenceTest() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		int count = manager.getCompetences().size();
		
		manager.addCompetence("ut_varrás");
		
		assertEquals(count + 1, manager.getCompetences().size());
		
		manager.editCompetence("ut_varrás", "ut_mosás");
		
		assertFalse(manager.getCompetences().contains("ut_varrás"));
		assertTrue(manager.getCompetences().contains("ut_mosás"));
		
		manager.deleteCompetence("ut_mosás");
		
		assertEquals(count, manager.getCompetences().size());
	}
	
	@Test(expected = SQLException.class)
	public void CompetenceExceptionTest() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		
		manager.deleteCompetence("ut_fõzés");
	}
	
	@Test
	public void ResourceTest() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		int count = manager.getResources().size();
		
		manager.addResource("ut_András", "ut_fõzés");
		
		assertEquals(count + 1, manager.getResources().size());
		
		manager.editResource(manager.getResources().get(count), "ut_András", "ut_takarítás");
		
		assertEquals("ut_takarítás", manager.getResources().get(count).getCompetence());
		
		manager.editResource(manager.getResources().get(count), "ut_Gábor", "ut_takarítás");
		
		assertEquals("ut_Gábor", manager.getResources().get(count).getName());
		
		manager.deleteResource("ut_Gábor");
		
		assertEquals(count, manager.getResources().size());
	}
	
	@Test(expected = SQLException.class)
	public void ResourceExceptionTest() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		
		manager.addResource("ut_Balázs", "ut_fõzés");
	}
	
	@After
	public void Release() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		
		manager.deleteResource("ut_Balázs");
		manager.deleteResource("ut_János");
		
		manager.deleteCompetence("ut_takarítás");
		manager.deleteCompetence("ut_fõzés");
	}
}
