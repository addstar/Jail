package com.graywolf336.jail.beans;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.graywolf336.jail.JailMain;

/** Represents a Jail, contains the prisoners and the cells.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.2
 */
public class Jail {
	private JailMain plugin;
	private HashMap<String, Cell> cells;
	private HashSet<Prisoner> nocellPrisoners;//prisoners who aren't in a cell
	private String name = "", world = "";
	private int minX, minY, minZ, maxX, maxY, maxZ;
	private SimpleLocation in, free;
	private Settings settings;
	
	public Jail(JailMain plugin, String name) {
		this.plugin = plugin;
		this.name = name;
		cells = new HashMap<String, Cell>();
		nocellPrisoners = new HashSet<Prisoner>();
		this.settings = new Settings(this);
	}
	
	/** Gets the instance of the plugin's main class. */
	public JailMain getPlugin() {
		return this.plugin;
	}
	
	/** Sets the name of the jail. */
	public void setName(String name) {
		this.name = name;
	}
	
	/** Gets the name of the jail. */
	public String getName() {
		return this.name;
	}
	
	/** Returns the settings getter/setter for this Jail. */
	public Settings getSettings() {
		return this.settings;
	}
	
	/** Sets the location of the <b>minimum</b> point to the given location's coordinates. */
	public void setMinPoint(Location location) {
		if(this.world.isEmpty()) this.world = location.getWorld().getName();
		
		this.minX = location.getBlockX();
		this.minY = location.getBlockY();
		this.minZ = location.getBlockZ();
	}
	
	/** Accepts an array of ints as the coord, where <strong>0 = x</strong>, <strong>1 = y</strong>, <strong>2 = z</strong>. */
	public void setMinPoint(int[] coords) {
		if(coords.length != 3) return;
		
		this.minX = coords[0];
		this.minY = coords[1];
		this.minZ = coords[2];
	}
	
	/** Gets the minimum point as a Bukkit Location class. */
	public Location getMinPoint() {
		return new Location(Bukkit.getServer().getWorld(world), minX, minY, minZ);
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
		return new Location(Bukkit.getServer().getWorld(world), maxX, maxY, maxZ);
	}
	
	/** Accepts an array of ints as the coord, where <strong>0 = x</strong>, <strong>1 = y</strong>, <strong>2 = z</strong>. */
	public void setMaxPoint(int[] coords) {
		if(coords.length != 3) return;
		
		this.maxX = coords[0];
		this.maxY = coords[1];
		this.maxZ = coords[2];
	}
	
	/** Sets the {@link SimpleLocation location} of the teleport <strong>in</strong>. */
	public void setTeleportIn(SimpleLocation location) {
		if(this.world.isEmpty()) this.world = location.getWorldName();
		
		this.in = location;
	}
	
	/** Gets the {@link Location location} of the teleport in. */
	public Location getTeleportIn() {
		return this.in.getLocation();
	}
	
	/** Sets the {@link SimpleLocation location} of the teleport for the <strong>free</strong> spot. */
	public void setTeleportFree(SimpleLocation location) {
		this.free = location;
	}
	
	/** Gets the {@link Location location} of the teleport free spot.*/
	public Location getTeleportFree() {
		return this.free.getLocation();
	}
	
	/** Adds a cell to the Jail. */
	public void addCell(Cell cell) {
		this.cells.put(cell.getName(), cell);
	}
	
	/** Gets the cell with the given name. */
	public Cell getCell(String name) {
		return this.cells.get(name);
	}
	
	/** Returns the first empty cell, returns null if there aren't any cells or any free cells. */
	public Cell getFirstEmptyCell() {
		for(Cell c : getCells())
			if(c.hasPrisoner())
				continue;
			else
				return c;
		
		return null;
	}
	
	/** Gets the amount of cells the jail. */
	public int getCellCount() {
		return this.cells.size();
	}
	
	/** Gets all the cells in the jail. */
	public HashSet<Cell> getCells() {
		return new HashSet<Cell>(this.cells.values());
	}
	
	/** Gets a HashSet of <b>all</b> the prisoners, the ones in cells and ones who aren't. */
	public HashSet<Prisoner> getAllPrisoners() {
		HashSet<Prisoner> all = new HashSet<Prisoner>(nocellPrisoners); //initalize the temp one to return with the prisoners not in any cells
		
		for(Cell c : cells.values())
			if(c.hasPrisoner())
				all.add(c.getPrisoner());
		
		return all;
	}
	
	/** Gets a HashSet of the prisoners <b>in cells</b>. */
	public HashSet<Prisoner> getPrisonersInCells() {
		HashSet<Prisoner> all = new HashSet<Prisoner>();
		
		for(Cell c : getCells())
			if(c.hasPrisoner())
				all.add(c.getPrisoner());
		
		return all;
	}
	
	/** Gets a HashSet of the prisoners <b>not</b> in cells.*/
	public HashSet<Prisoner> getPrisonersNotInCells() {
		return this.nocellPrisoners;
	}
}
