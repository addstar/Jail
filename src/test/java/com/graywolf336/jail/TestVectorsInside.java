package test.java.com.graywolf336.jail;

import static org.junit.Assert.*;

import org.bukkit.util.Vector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.graywolf336.jail.Util;

public class TestVectorsInside {
	private Vector bottomCorner;
	private Vector topCorner;
	
	@Before
	public void setUp() {
		bottomCorner = new Vector(-10.50, 50.25, 100.00);
		topCorner = new Vector(50, 100, 250);
	}
	
	@Test
	public void testIsInsideAB() {
		Vector inside = new Vector(35, 64, 110);
		assertTrue(Util.isInsideAB(inside, bottomCorner, topCorner));
	}
	
	@Test
	public void testIsOutsideAB() {
		Vector outside = new Vector(350, 15, 350);
		assertFalse(Util.isInsideAB(outside, bottomCorner, topCorner));
	}
	
	@After
	public void tearDown() {
		bottomCorner = null;
		topCorner = null;
	}
}
