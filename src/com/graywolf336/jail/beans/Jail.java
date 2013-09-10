package com.graywolf336.jail.beans;

import java.util.HashSet;

import org.bukkit.Location;

import com.graywolf336.jail.JailMain;

/** Represents a Jail (formerly JailZone). */
public class Jail {
	private JailMain plugin;
	private HashSet<Cell> cells;
	private HashSet<Prisoner> nocellPrisoners;//prisoners who aren't in a cell
	private String name = "", world = "";
	private int minX, minY, minZ, maxX, maxY, maxZ;
	
	public Jail(JailMain plugin, String name) {
		this.plugin = plugin;
		this.name = name;
		cells = new HashSet<Cell>();
		nocellPrisoners = new HashSet<Prisoner>();
	}
	
	/** Sets the name of the jail. */
	public void setName(String name) {
		this.name = name;
	}
	
	/** Gets the name of the jail. */
	public String getName() {
		return this.name;
	}
	
	/** Sets the location of the <b>minimum</b> point to the given location's coordinates. */
	public void setMinPoint(Location location) {
		if(this.world.isEmpty()) this.world = location.getWorld().getName();
		
		this.minX = location.getBlockX();
		this.minY = location.getBlockY();
		this.minZ = location.getBlockZ();
	}
	
	/** Gets the minimum point as a Bukkit Location class. */
	public Location getMinPoint() {
		return new Location(plugin.getServer().getWorld(world), minX, minY, minZ);
	}
	
	/** Sets the location of the <b>maximum</b> point to the given location's coordinates. */
	public void setMaxPoint(Location location) {
		if(this.world.isEmpty()) this.world = location.getWorld().getName();
		
		this.maxX = location.getBlockX();
		this.maxY = location.getBlockY();
		this.maxZ = location.getBlockZ();
	}
	
	/** Gets the minimum point as a Bukkit Location class. */
	public Location getMaxPoint() {
		return new Location(plugin.getServer().getWorld(world), maxX, maxY, maxZ);
	}
	
	/** Gets the amount of cells the jail. */
	public int getCellCount() {
		return this.cells.size();
	}
	
	/** Gets all the cells in the jail. */
	public HashSet<Cell> getCells() {
		return this.cells;
	}
	
	/** Gets a HashSet of <b>all</b> the prisoners, the ones in cells and ones who aren't. */
	public HashSet<Prisoner> getAllPrisoners() {
		HashSet<Prisoner> all = new HashSet<Prisoner>(nocellPrisoners); //initalize the temp one to return with the prisoners not in any cells
		
		for(Cell c : getCells())
			if(c.getPrisoner() != null)
				all.add(c.getPrisoner());
		
		return all;
	}
	
	/** Gets a HashSet of the prisoners <b>in cells</b>. */
	public HashSet<Prisoner> getPrisonersInCells() {
		HashSet<Prisoner> all = new HashSet<Prisoner>();
		
		for(Cell c : getCells())
			if(c.getPrisoner() != null)
				all.add(c.getPrisoner());
		
		return all;
	}
	
	/** Gets a HashSet of the prisoners <b>not</b> in cells.*/
	public HashSet<Prisoner> getPrisonersNotInCells() {
		return this.nocellPrisoners;
	}
}
