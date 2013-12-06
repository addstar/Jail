package com.matejdro.bukkit.jail;

import java.util.HashMap;

import org.bukkit.Location;

public class HandCuffManager {
	private HashMap<String, Long> handcuffed;
	private HashMap<String, Location> locs;
	
	public HandCuffManager() {
		this.handcuffed = new HashMap<String, Long>();
		this.locs = new HashMap<String, Location>();
	}
	
	public void addHandCuffs(String name, Location loc) {
		this.handcuffed.put(name.toLowerCase(), System.currentTimeMillis());
		this.locs.put(name.toLowerCase(), loc);
	}
	
	public void removeHandCuffs(String name) {
		this.handcuffed.remove(name.toLowerCase());
	}
	
	public boolean isHandCuffed(String name) {
		return this.handcuffed.containsKey(name.toLowerCase());
	}
	
	public Long getNextMessageTime(String name) {
		return this.handcuffed.get(name.toLowerCase()); 
	}

	public void updateNextTime(String name) {
		this.handcuffed.put(name.toLowerCase(), System.currentTimeMillis() + 10000);
	}
	
	public Location getLocation(String name) {
		return this.locs.get(name.toLowerCase());
	}
}
