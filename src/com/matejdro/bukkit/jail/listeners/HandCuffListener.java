package com.matejdro.bukkit.jail.listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.matejdro.bukkit.jail.Jail;

public class HandCuffListener implements Listener {
	private Jail pl;
	private HashMap<String, Location> tos;
	
	public HandCuffListener(Jail plugin) {
		this.pl = plugin;
		this.tos = new HashMap<String, Location>();
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled()) return;
		
		if (pl.getHandCuffManager().isHandCuffed(event.getPlayer().getName())) {
			Location to = pl.getHandCuffManager().getLocation(event.getPlayer().getName());
			to.setPitch(event.getTo().getPitch());
			to.setYaw(event.getTo().getYaw());
			
			tos.put(event.getPlayer().getName(), to);
			event.getPlayer().teleport(to);
			
			if(System.currentTimeMillis() >= pl.getHandCuffManager().getNextMessageTime(event.getPlayer().getName())) {
				event.getPlayer().sendMessage(ChatColor.RED + "You are handcuffed and cant move!");
				pl.getHandCuffManager().updateNextTime(event.getPlayer().getName());
			}
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(event.isCancelled()) return;
		
		if (pl.getHandCuffManager().isHandCuffed(event.getPlayer().getName())) {
			if(event.getTo() != tos.get(event.getPlayer().getName())) {
				Location to = pl.getHandCuffManager().getLocation(event.getPlayer().getName());
				to.setPitch(event.getTo().getPitch());
				to.setYaw(event.getTo().getYaw());
				
				tos.put(event.getPlayer().getName(), to);
				event.getPlayer().teleport(to);
				
				if(System.currentTimeMillis() >= pl.getHandCuffManager().getNextMessageTime(event.getPlayer().getName())) {
					event.getPlayer().sendMessage(ChatColor.RED + "You are handcuffed and cant move!");
					pl.getHandCuffManager().updateNextTime(event.getPlayer().getName());
				}
			}
		}
	}
	
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event) {
		if(event.isCancelled()) return;
		
		if (pl.getHandCuffManager().isHandCuffed(event.getPlayer().getName())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You are handcuffed and aren't allowed to talk!");
		}
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent event) {
		if(event.isCancelled()) return;
		
		if (pl.getHandCuffManager().isHandCuffed(event.getPlayer().getName())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You are handcuffed and aren't allowed to break blocks!");
		}
	}
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent event) {
		if(event.isCancelled()) return;
		
		if (pl.getHandCuffManager().isHandCuffed(event.getPlayer().getName())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You are handcuffed and aren't allowed to place blocks!");
		}
	}
	
	@EventHandler
	public void preCommands(PlayerCommandPreprocessEvent event) {
		if(event.isCancelled()) return;
		
		if (pl.getHandCuffManager().isHandCuffed(event.getPlayer().getName())) {
			if(!event.getMessage().startsWith("/r") || !event.getMessage().startsWith("/reply")) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "You are handcuffed and aren't allowed to use commands!");
			}
		}
	}
}
