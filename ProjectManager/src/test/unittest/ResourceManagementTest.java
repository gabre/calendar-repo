package test.unittest;

import static org.junit.Assert.*;

import java.sql.SQLException;

import model.Model;
import model.ResourceElement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ResourceManagementTest {
	
	@Test
	public void CompetenceTest() throws ClassNotFoundException, SQLException {
		Model model = new Model();
		int count = model.getCompetences().size();
		
		model.addCompetence(new String("ut_varr�s"));
		
		assertEquals(count + 1, model.getCompetences().size());
		
		model.editCompetence("ut_varr�s", "ut_mos�s");
		
		assertFalse(model.getCompetences().contains("ut_varr�s"));
		assertTrue(model.getCompetences().contains("ut_mos�s"));
		
		assertFalse(model.addCompetence("ut_mos�s"));
		
		model.deleteCompetence("ut_mos�s");
		
		assertEquals(count, model.getCompetences().size());
	}
	
	@Test
	public void ResourceTest() throws ClassNotFoundException, SQLException {
		Model model = new Model();
		int count = model.getResources().size();
		
		model.addCompetence("ut_f�z�s");
		model.addCompetence("ut_takar�t�s");
		model.addResource(new ResourceElement("ut_Andr�s", "ut_f�z�s"));
		
		assertEquals(count + 1, model.getResources().size());
		
		model.editResource(model.getResources().get(count), new ResourceElement("ut_Andr�s", "ut_takar�t�s"));
		
		assertEquals("ut_takar�t�s", model.getResources().get(count).getCompetence());
		
		ResourceElement elem = new ResourceElement("ut_G�bor", "ut_takar�t�s");
		
		model.editResource(model.getResources().get(count), elem);
		
		assertEquals("ut_G�bor", model.getResources().get(count).getName());
		assertFalse(model.addResource(new ResourceElement("ut_G�bor", "ut_takar�t�s")));
		
		model.deleteResource(elem);
		
		assertEquals(count, model.getResources().size());
		
		model.deleteCompetence("ut_f�z�s");
		model.deleteCompetence("ut_takar�t�s");
	}
}
