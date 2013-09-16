package com.graywolf336.jail.beans;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SimpleLocation {
	private String world;
	private double x, y, z;
	private float yaw, pitch;
	
	public SimpleLocation(String world, double x, double y, double z, float yaw, float pitch) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(world);
	}
	
	public String getWorldName() {
		return this.world;
	}
	
	public Location getLocation() {
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}
}
