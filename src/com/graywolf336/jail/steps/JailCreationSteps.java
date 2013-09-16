package com.graywolf336.jail.steps;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.CreationPlayer;
import com.graywolf336.jail.beans.Jail;

public class JailCreationSteps {
	
	/** Sends the Jail Creation message for starting out. */
	public void startStepping(CreationPlayer cp, Player player) {
		player.sendMessage(ChatColor.AQUA + "----------Jail Zone Creation----------");
		player.sendMessage(ChatColor.GREEN + "First, you must select jail cuboid. Select the first point of the cuboid by right clicking on the block with your wooden sword. DO NOT FORGET TO MARK THE FLOOR AND CEILING TOO!");
		player.sendMessage(ChatColor.AQUA + "--------------------------------------");
	}
	
	/**
	 * Applies the next step in the Jail Creation process that involves a location, null if no location is needed.
	 * 
	 * @param player The player who is doing the creating.
	 * @param cp The {@link CreationPlayer} instance
	 * @param location The location, null if none, being set.
	 */
	public void step(JailManager jm, Player player, CreationPlayer cp, Location location) {
		switch(cp.getState()) {
			case 1:
				firstStep(cp, player, location);
				break;
			case 2:
				secondStep(cp, player, location);
				break;
			case 3:
				thirdStep(cp, player);
				break;
			case 4:
				fourthStep(jm, cp, player);
				break;
			default:
				player.sendMessage(ChatColor.RED + "Something went wrong with the creation of the Jail, please start over");
				jm.removeJailCreationPlayer(player.getName());
				break;
		}
	}
	
	/** Applies the first step, which is setting the first corner. */
	private void firstStep(CreationPlayer cp, Player p, Location location) {
		p.sendMessage(ChatColor.AQUA + "---------- Jail Zone Creation----------");
		p.sendMessage(ChatColor.GREEN + "First point selected. Now select the second point.");
		p.sendMessage(ChatColor.AQUA + "---------------------------------------");
		
		cp.setCornerOne(location);
		cp.nextState();
	}
	
	/** Applies the second step, which is setting the second corner. */
	private void secondStep(CreationPlayer cp, Player p, Location location) {
		p.sendMessage(ChatColor.AQUA + "---------- Jail Zone Creation ----------");
		p.sendMessage(ChatColor.GREEN + "Second point selected. Now go inside the jail and right click anywhere to select your current position as the teleport location for the jail.");
		p.sendMessage(ChatColor.AQUA + "----------------------------------------");
		
		cp.setCornerTwo(location);
		cp.nextState();
	}
	
	/** Applies the third step, which is setting the teleport in location. */
	private void thirdStep(CreationPlayer cp, Player p) {
		p.sendMessage(ChatColor.AQUA + "---------- Jail Zone Creation ----------");
		p.sendMessage(ChatColor.GREEN + "Teleport point selected. Now go outside of the jail and right click anywhere to select your current position as the location where people will be teleported after they are released from this jail.");
		p.sendMessage(ChatColor.AQUA + "----------------------------------------");
		
		cp.setTeleportIn(p.getLocation());
		cp.nextState();
	}
	
	private void fourthStep(JailManager jm, CreationPlayer cp, Player p) {
		cp.setTeleportFree(p.getLocation());
		
		finalStep(jm, cp, p);
	}
	
	private void finalStep(JailManager jm, CreationPlayer cp, Player p) {
		Jail jail = new Jail(cp.getName());
		
		jail.setMinPoint(cp.getCornerOne());
		jail.setMaxPoint(cp.getCornerTwo());
		jail.setTeleportIn(cp.getTeleportInSL());
		jail.setTeleportFree(cp.getTeleportFreeSL());
		
		jm.addJail(jail);
		
		p.sendMessage(ChatColor.GREEN + "Jail (" + jail.getName() +  ") created successfully!");
		
		jm.removeJailCreationPlayer(p.getName());
	}
}