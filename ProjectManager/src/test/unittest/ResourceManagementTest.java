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
		
		model.addCompetence("ut_f�z�s");
		model.addCompetence("ut_takar�t�s");
		
		model.addResource(new ResourceElement("ut_Bal�zs", "ut_f�z�s"));
		model.addResource(new ResourceElement("ut_J�nos", "ut_takar�t�s"));
	}
	
	@Test
	public void CompetenceTest() throws ClassNotFoundException, SQLException {
		Model model = new Model();
		int count = model.getCompetences().size();
		
		model.addCompetence(new String("ut_varr�s"));
		
		assertEquals(count + 1, model.getCompetences().size());
		
		model.editCompetence("ut_varr�s", "ut_mos�s");
		
		assertFalse(model.getCompetences().contains("ut_varr�s"));
		assertTrue(model.getCompetences().contains("ut_mos�s"));
		
		model.deleteCompetence("ut_mos�s");
		
		assertEquals(count, model.getCompetences().size());
	}
	
	@Test(expected = SQLException.class)
	public void CompetenceExceptionTest() throws ClassNotFoundException, SQLException {
		Model model = new Model();
		model.deleteCompetence(new String("ut_f�z�s"));
	}
	
	@Test
	public void ResourceTest() throws ClassNotFoundException, SQLException {
		Model model = new Model();
		int count = model.getResources().size();
		
		model.addResource(new ResourceElement("ut_Andr�s", "ut_f�z�s"));
		
		assertEquals(count + 1, model.getResources().size());
		
		model.editResource(model.getResources().get(count), new ResourceElement("ut_Andr�s", "ut_takar�t�s"));
		
		assertEquals("ut_takar�t�s", model.getResources().get(count).getCompetence());
		
		ResourceElement elem = new ResourceElement("ut_G�bor", "ut_takar�t�s");
		
		model.editResource(model.getResources().get(count), elem);
		
		assertEquals("ut_G�bor", model.getResources().get(count).getName());
		
		model.deleteResource(elem);
		
		assertEquals(count, model.getResources().size());
	}
	
	@Test(expected = SQLException.class)
	public void ResourceExceptionTest() throws ClassNotFoundException, SQLException {
		Model model = new Model();	
		model.addResource(new ResourceElement("ut_Bal�zs", "ut_f�z�s"));
	}
}
