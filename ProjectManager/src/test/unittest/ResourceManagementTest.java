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
		
		manager.addCompetence("ut_f�z�s");
		manager.addCompetence("ut_takar�t�s");
		
		manager.addResource("ut_Bal�zs", "ut_f�z�s");
		manager.addResource("ut_J�nos", "ut_takar�t�s");
	}
	
	@Test
	public void CompetenceTest() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		int count = manager.getCompetences().size();
		
		manager.addCompetence("ut_varr�s");
		
		assertEquals(count + 1, manager.getCompetences().size());
		
		manager.editCompetence("ut_varr�s", "ut_mos�s");
		
		assertFalse(manager.getCompetences().contains("ut_varr�s"));
		assertTrue(manager.getCompetences().contains("ut_mos�s"));
		
		manager.deleteCompetence("ut_mos�s");
		
		assertEquals(count, manager.getCompetences().size());
	}
	
	@Test(expected = SQLException.class)
	public void CompetenceExceptionTest() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		
		manager.deleteCompetence("ut_f�z�s");
	}
	
	@Test
	public void ResourceTest() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		int count = manager.getResources().size();
		
		manager.addResource("ut_Andr�s", "ut_f�z�s");
		
		assertEquals(count + 1, manager.getResources().size());
		
		manager.editResource(manager.getResources().get(count), "ut_Andr�s", "ut_takar�t�s");
		
		assertEquals("ut_takar�t�s", manager.getResources().get(count).getCompetence());
		
		manager.editResource(manager.getResources().get(count), "ut_G�bor", "ut_takar�t�s");
		
		assertEquals("ut_G�bor", manager.getResources().get(count).getName());
		
		manager.deleteResource("ut_G�bor");
		
		assertEquals(count, manager.getResources().size());
	}
	
	@Test(expected = SQLException.class)
	public void ResourceExceptionTest() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		
		manager.addResource("ut_Bal�zs", "ut_f�z�s");
	}
	
	@After
	public void Release() throws ClassNotFoundException, SQLException {
		DataManager manager = new DataManager();
		
		manager.deleteResource("ut_Bal�zs");
		manager.deleteResource("ut_J�nos");
		
		manager.deleteCompetence("ut_takar�t�s");
		manager.deleteCompetence("ut_f�z�s");
	}
}
