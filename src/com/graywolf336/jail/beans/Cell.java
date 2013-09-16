package com.graywolf336.jail.beans;

/** Represents a Cell inside of a {@link Jail}.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class Cell {
	private String name;
	private Prisoner p;
	
	public Cell(String name) {
		this.name = name;
	}
	
	/** Gets the name of the cell. */
	public String getName() {
		return this.name;
	}
	
	/** Sets the prisoner in this cell. */
	public void setPrisoner(Prisoner prisoner) {
		this.p = prisoner;
	}
	
	/** Gets the prisoner being held in this cell. */
	public Prisoner getPrisoner() {
		return this.p;
	}
}
