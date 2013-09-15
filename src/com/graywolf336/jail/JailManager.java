package com.graywolf336.jail;

import java.util.HashMap;
import java.util.HashSet;

import com.graywolf336.jail.beans.CreationPlayer;
import com.graywolf336.jail.beans.Jail;

public class JailManager {
	private HashMap<String, Jail> jails;
	private HashMap<String, CreationPlayer> jailCreators;
	private HashMap<String, CreationPlayer> cellCreators;
	
	public JailManager() {
		this.jails = new HashMap<String, Jail>();
		this.jailCreators = new HashMap<String, CreationPlayer>();
		this.cellCreators = new HashMap<String, CreationPlayer>();
	}
	
	/** Returns a HashSet of all the jails. */
	public HashSet<Jail> getJails() {
		return new HashSet<Jail>(jails.values());
	}
	
	/** Adds a jail to the collection of them. */
	public void addJail(Jail jail) {
		this.jails.put(jail.getName(), jail);
	}
	
	/**
	 * Gets a jail by the given name.
	 * 
	 * @param name The name of the jail to get.
	 * @return The {@link Jail} with the given name, if no jail found this <strong>will</strong> return null.
	 */
	public Jail getJail(String name) {
		return this.jails.get(name);
	}
	
	/**
	 * Checks to see if the given name for a {@link Jail} is valid, returns true if it is a valid jail.
	 * 
	 * @param name The name of the jail to check.
	 * @return True if a valid jail was found, false if no jail was found.
	 */
	public boolean isValidJail(String name) {
		return this.jails.get(name) != null;
	}
	
	/**
	 * Returns whether or not the player is creating a jail or a cell.
	 * 
	 * <p>
	 * 
	 * If you want to check to see if they're just creating a jail then use {@link #isCreatingAJail(name) isCreatingAJail} or if you want to see if they're creating a cell then use {@link #isCreatingACell(name) isCreatingACell}. 
	 * 
	 * @param name The name of the player, in any case as we convert it to lowercase.
	 * @return True if the player is creating a jail or cell, false if they're not creating anything.
	 */
	public boolean isCreatingSomething(String name) {
		return this.jailCreators.containsKey(name.toLowerCase()) || this.cellCreators.containsKey(name.toLowerCase());
	}
	
	/** Returns whether or not someone is creating a <strong>Jail</strong>. */
	public boolean isCreatingAJail(String name) {
		return this.jailCreators.containsKey(name.toLowerCase());
	}
	
	/**
	 * Method for setting a player to be creating a Jail, returns whether or not they were added successfully.
	 * 
	 * @param name The player who is creating a jail.
	 * @return True if they were added successfully, false if they are already creating a Jail.
	 */
	public boolean addCreatingJail(String player, String jailName) {
		if(isCreatingAJail(player)) {
			return false;
		}else {
			this.jailCreators.put(player.toLowerCase(), new CreationPlayer(jailName));
			return true;
		}
	}
	
	/** Returns the instance of the CreationPlayer for this player, null if there was none found. */
	public CreationPlayer getJailCreationPlayer(String name) {
		return this.jailCreators.get(name.toLowerCase());
	}
	
	/** Returns whether or not someone is creating a <strong>Cell</strong>. */
	public boolean isCreatingACell(String name) {
		return this.cellCreators.containsKey(name.toLowerCase());
	}
	
	/**
	 * Method for setting a player to be creating a Jail, returns whether or not they were added successfully.
	 * 
	 * @param name The player who is creating a jail.
	 * @return True if they were added successfully, false if they are already creating a Jail.
	 */
	public boolean addCreatingCell(String player, String cellName) {
		if(isCreatingACell(player)) {
			return false;
		}else {
			this.cellCreators.put(player.toLowerCase(), new CreationPlayer(cellName));
			return true;
		}
	}
	
	/** Returns the instance of the CreationPlayer for this player, null if there was none found. */
	public CreationPlayer getCellCreationPlayer(String name) {
		return this.cellCreators.get(name.toLowerCase());
	}
}
