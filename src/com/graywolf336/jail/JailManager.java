package com.graywolf336.jail;

import java.util.HashMap;
import java.util.HashSet;

import com.graywolf336.jail.beans.CreationPlayer;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.steps.CellCreationSteps;
import com.graywolf336.jail.steps.JailCreationSteps;

/**
 * Handles all things related to jails.
 * 
 * <p>
 * 
 * Stores the following:
 * <ul>
 * 	<li>Players creating jails, see {@link CreationPlayer}.</li>
 * 	<li>Players creating jail cells, see {@link CreationPlayer}.</li>
 * 	<li>An instance of {@link JailCreationSteps} for stepping players through the Jail creation process.</li>
 * </ul>
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.1.0
 */
public class JailManager {
	private HashMap<String, Jail> jails;
	private HashMap<String, CreationPlayer> jailCreators;
	private HashMap<String, CreationPlayer> cellCreators;
	private JailCreationSteps jcs;
	private CellCreationSteps ccs;
	
	public JailManager() {
		this.jails = new HashMap<String, Jail>();
		this.jailCreators = new HashMap<String, CreationPlayer>();
		this.cellCreators = new HashMap<String, CreationPlayer>();
		this.jcs = new JailCreationSteps();
		this.ccs = new CellCreationSteps();
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
	 * If you want to check to see if they're just creating a jail then use {@link #isCreatingAJail(String) isCreatingAJail} or if you want to see if they're creating a cell then use {@link #isCreatingACell(String) isCreatingACell}. 
	 * 
	 * @param name The name of the player, in any case as we convert it to lowercase.
	 * @return True if the player is creating a jail or cell, false if they're not creating anything.
	 */
	public boolean isCreatingSomething(String name) {
		return this.jailCreators.containsKey(name.toLowerCase()) || this.cellCreators.containsKey(name.toLowerCase());
	}
	
	/** Returns a message used for telling them what they're creating and what step they're on. */
	public String getStepMessage(String player) {
		String message = "";
		
		if(isCreatingACell(player)) {//Check whether it is a jail cell
			CreationPlayer cp = this.getCellCreationPlayer(player);
			message = "You're already creating a Cell with the name '" + cp.getCellName() + "' and you still need to ";
			
			switch(cp.getStep()) {
				case 1:
					message += "set the teleport in location.";
					break;
				case 2:
					message += "select all the signs.";
					break;
				case 3:
					message += "set the double chest location.";
					break;
			}
			
		}else if(isCreatingAJail(player)) {//If not a cell, then check if a jail.
			CreationPlayer cp = this.getJailCreationPlayer(player);
			message = "You're already creating a Jail with the name '" + cp.getJailName() + "' and you still need to ";
			
			switch(cp.getStep()) {
				case 1:
					message += "select the first point.";
					break;
				case 2:
					message += "select the second point.";
					break;
				case 3:
					message += "set the teleport in location.";
					break;
				case 4:
					message += "set the release location.";
					break;
			}
		}
		
		return message;
	}
	
	/** Returns whether or not someone is creating a <strong>Jail</strong>. */
	public boolean isCreatingAJail(String name) {
		return this.jailCreators.containsKey(name.toLowerCase());
	}
	
	/**
	 * Method for setting a player to be creating a Jail, returns whether or not they were added successfully.
	 * 
	 * @param player The player who is creating a jail.
	 * @param jailName The name of the jail we are creating.
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
	
	/** Removes a CreationPlayer with the given name from the jail creators. */
	public void removeJailCreationPlayer(String name) {
		this.jailCreators.remove(name.toLowerCase());
	}
	
	/** Returns whether or not someone is creating a <strong>Cell</strong>. */
	public boolean isCreatingACell(String name) {
		return this.cellCreators.containsKey(name.toLowerCase());
	}
	
	/**
	 * Method for setting a player to be creating a Cell, returns whether or not they were added successfully.
	 * 
	 * @param player The player who is creating a jail.
	 * @param jailName The name of the jail this cell is going.
	 * @param cellName The name of the cell we are creating.
	 * @return True if they were added successfully, false if they are already creating a Jail.
	 */
	public boolean addCreatingCell(String player, String jailName, String cellName) {
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
	
	/** Removes a CreationPlayer with the given name from the cell creators. */
	public void removeCellCreationPlayer(String name) {
		this.cellCreators.remove(name.toLowerCase());
	}
	
	/** Gets the instance of the {@link JailCreationSteps}. */
	public JailCreationSteps getJailCreationSteps() {
		return this.jcs;
	}
	
	/** Gets the instance of the {@link CellCreationSteps}. */
	public CellCreationSteps getCellCreationSteps() {
		return this.ccs;
	}
}
