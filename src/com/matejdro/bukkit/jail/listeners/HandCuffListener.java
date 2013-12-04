package com.matejdro.bukkit.jail.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.matejdro.bukkit.jail.Jail;

public class HandCuffListener implements Listener {
	private Jail pl;
	
	public HandCuffListener(Jail plugin) {
		this.pl = plugin;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled()) return;
		
		if (pl.getHandCuffManager().isHandCuffed(event.getPlayer().getName())) {
			event.getPlayer().teleport(pl.getHandCuffManager().getLocation(event.getPlayer().getName()));
			
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
			if(event.getTo() != pl.getHandCuffManager().getLocation(event.getPlayer().getName())) {
				event.getPlayer().teleport(pl.getHandCuffManager().getLocation(event.getPlayer().getName()));
				
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
			event.getPlayer().sendMessage(ChatColor.RED + "You are handcuffed and cant move!");
		}
	}
}
