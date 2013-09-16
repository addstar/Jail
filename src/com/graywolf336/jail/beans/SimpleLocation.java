package com.graywolf336.jail.beans;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Simple location class which doesn't store any instances of {@link World worlds} or {@link Block blocks}, just uses strings, floats, and doubles.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
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
