package com.graywolf336.jail;

import java.util.HashMap;
import java.util.HashSet;

import com.graywolf336.jail.beans.Jail;

public class JailManager {
	private HashMap<String, Jail> jails;
	
	public JailManager() {
		this.jails = new HashMap<String, Jail>();
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
}
