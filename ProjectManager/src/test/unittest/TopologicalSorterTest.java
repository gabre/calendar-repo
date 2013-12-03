package test.unittest;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import model.AdjacencyMatrix;
import model.CycleDetectedException;
import model.TopologicalSorter;

public class TopologicalSorterTest {
	@Test
	public void testSorterNoCycle1() {
		AdjacencyMatrix mx = new AdjacencyMatrix(8);
		mx.adjMx = new boolean[][] { 
				{false,false,true,false,false,false,false,false}, 
				{false,false,true,false,false,false,false,false}, 
				{false,false,false,true,true,false,false,false}, 
				{false,false,false,false,false,false,true,false}, 
				{false,false,false,false,false,false,true,false}, 
				{false,false,false,false,true,false,false,false}, 
				{false,false,false,false,false,false,false,true}, 
				{false,false,false,false,false,false,false,false} };
		Integer[] expectedL = new Integer[] {6,2,1,3,5,4,7,8};
		LinkedList<Integer> l;
		try {
			l = TopologicalSorter.topologicalSort(mx);
			assertArrayEquals(l.toArray(), expectedL);
		} catch (CycleDetectedException e) {
			fail();;
		}	
	}
	
	@Test
	public void testSorterNoCycle2() {
		AdjacencyMatrix mx = new AdjacencyMatrix(8);
		mx.adjMx = new boolean[][] { 
				{false,true,true,false}, 
				{false,false,false,true}, 
				{false,false,false,true}, 
				{false,false,false,false} };
		Integer[] expectedL = new Integer[] {1,3,2,4};
		LinkedList<Integer> l;
		try {
			l = TopologicalSorter.topologicalSort(mx);
			assertArrayEquals(l.toArray(), expectedL);
		} catch (CycleDetectedException e) {
			fail();;
		}	
	}
	
	@Test
	public void testSorterCycle1() {
		AdjacencyMatrix mx = new AdjacencyMatrix(8);
		mx.adjMx = new boolean[][] { 
				{false,false,true,false,false,false,false,false}, 
				{false,false,true,false,false,false,false,false}, 
				{false,false,false,true,true,false,false,false}, 
				{false,false,false,false,false,false,true,false}, 
				{false,false,false,false,false,false,true,false}, 
				{false,false,false,false,true,false,false,false}, 
				{false,false,false,false,false,false,false,true}, 
				{false,false,false,false,true,false,false,false} };
		try {
			LinkedList<Integer> l = TopologicalSorter.topologicalSort(mx);
			fail();
		} catch (CycleDetectedException e){
			
		}
	}

	
	@Test
	public void testSorterCycle2() {
		AdjacencyMatrix mx = new AdjacencyMatrix(8);
		mx.adjMx = new boolean[][] { 
				{false,true,false,false}, 
				{false,false,true,false}, 
				{false,false,false,true}, 
				{true,false,false,false}, 
				};
		LinkedList<Integer> l;
		try {
			l = TopologicalSorter.topologicalSort(mx);
			fail();
		} catch (CycleDetectedException e) {
		}
	}
}
