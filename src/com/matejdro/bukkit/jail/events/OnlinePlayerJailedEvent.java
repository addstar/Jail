package com.matejdro.bukkit.jail.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OnlinePlayerJailedEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private Player player;
	private String jail, cell, reason, jailer;
	private int time;
	private boolean muted;

	/**
	 * New event for when an online player is jailed.
	 * 
	 * @param player The player who is being jailed.
	 * @param time The amount of time, in minutes, we are jailing them.
	 * @param jail  The name of the jail they will be in.
	 * @param cell The name of the cell they will be in.
	 * @param reason The reason they are being jailed.
	 * @param muted If the player is to be muted in jail or not.
	 * @param jailer The person who jailed them.
	 */
	public OnlinePlayerJailedEvent(Player player, int time, String jail, String cell, String reason, boolean muted, String jailer) {
		this.player = player;
		this.time = time;
		this.jail = jail;
		this.cell = cell;
		this.reason = reason;
		this.muted = muted;
		this.jailer = jailer;
	}
	
	/** Returns the player who is getting jailed. */
	public Player getPlayer() {
		return this.player;
	}
	
	/** Returns the name of the jail the player will be put in at. */
	public String getJail() {
		return this.jail;
	}
	
	/** Returns the amount of time the player is jailed for. */
	public int getTime() {
		return this.time;
	}
	
	/** Sets the amount of time the player is jailed for. */
	public void setTime(int time) {
		this.time = time;
	}
	
	/** Gets the cell which we are jailing them to. */
	public String getCell() {
		return this.cell;
	}
	
	/** Sets the cell we should jail them to, it doesn't check if it is valid or not so be sure it is. */
	public void setCell(String cell) {
		this.cell = cell;
	}
	
	/** Gets the reason we are jailing them for. */
	public String getReason() {
		return this.reason;
	}
	
	/** Sets the reason we are jailing them for. */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	/** Returns if the player is muted or not. */
	public boolean isMuted() {
		return this.muted;
	}
	
	/** Sets whether the player is muted or not. */
	public void setMuted(boolean mute) {
		this.muted = mute;
	}
	
	/** Returns who jailed the player.  */
	public String getJailer() {
		return this.jailer;
	}
	
	/** Sets the jailer who jailed this player. */
	public void setJailer(String jailer) {
		this.jailer = jailer;
	}
	
	/** Checks whether this event is cancelled or not. */
	public boolean isCancelled() {
		return this.cancelled;
	}

	/** Sets whether this event should be cancelled. */
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	public HandlerList getHandlers() {
		return handlers;
	}
}
