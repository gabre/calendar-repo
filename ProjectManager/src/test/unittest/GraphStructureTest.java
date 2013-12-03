package test.unittest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;

import model.GraphStructure;

import org.junit.Test;

public class GraphStructureTest {
	private static final Integer STARTS_FROM = 1;
	@Test
	public void testNeighbors() {
		HashSet<Integer> nodes = new HashSet<Integer>();
		GraphStructure g = new GraphStructure();
		for(int i = 0; i < 10; ++i)
			nodes.add(g.addNode());
		
		Iterator<Integer> it = nodes.iterator();
		HashSet<Integer> neighbors = new HashSet<Integer>();
		Integer p = it.next();
		while(it.hasNext()) {
			Integer q = it.next();
			neighbors.add(q);
			g.addEdge(p, q);
		}

		for(int i = STARTS_FROM + 1; i < 10; ++i)
			assertTrue(g.getNeighbors(STARTS_FROM).contains(i));
	}
	
	@Test
	public void testRemoveEdges() {
		HashSet<Integer> nodes = new HashSet<Integer>();
		GraphStructure g = new GraphStructure();
		for(int i = 0; i < 10; ++i)
			nodes.add(g.addNode());
		
		Iterator<Integer> it = nodes.iterator();
		HashSet<Integer> neighbors = new HashSet<Integer>();
		Integer p = it.next();
		while(it.hasNext()) {
			Integer q = it.next();
			neighbors.add(q);
			g.addEdge(p, q);
		}

		//deletes edges
		for(int i = STARTS_FROM + 1; i < 10; i+=2)
			g.deleteEdge(STARTS_FROM, i);

		for(int i = STARTS_FROM + 1; i < 10; ++i)
			if(STARTS_FROM + i % 2 == 0)
				assertTrue(g.getNeighbors(0).contains(i));
			else
				assertFalse(g.getNeighbors(0).contains(i));
	}

	@Test
	public void testRemoveNode() {
		HashSet<Integer> nodes = new HashSet<Integer>();
		GraphStructure g = new GraphStructure();
		for(int i = 0; i < 10; ++i)
			nodes.add(g.addNode());
		
		Iterator<Integer> it = nodes.iterator();
		HashSet<Integer> neighbors = new HashSet<Integer>();
		Integer p = it.next();
		while(it.hasNext()) {
			Integer q = it.next();
			neighbors.add(q);
			g.addEdge(p, q);
		}

		g.deleteNode(STARTS_FROM + 1);
		assertFalse(g.getNeighbors(STARTS_FROM).contains(STARTS_FROM + 1));
		
		g.deleteNode(STARTS_FROM);
		assertTrue(g.getNeighbors(STARTS_FROM).size() == 0);
	}

}
