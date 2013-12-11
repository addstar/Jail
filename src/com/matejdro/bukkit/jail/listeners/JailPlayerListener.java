package com.matejdro.bukkit.jail.listeners;

import com.matejdro.bukkit.jail.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionDefault;

import com.matejdro.bukkit.jail.commands.JailSetCommand;

@SuppressWarnings("deprecation")
public class JailPlayerListener implements Listener {
	
	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemInHand().getTypeId() == Settings.getGlobalInt(Setting.SelectionTool))
		{
			if ( JailZoneCreation.players.containsKey(event.getPlayer().getName()))
			{
				JailZoneCreation.select(event.getPlayer(), event.getClickedBlock());
				event.setCancelled(true);
			}
			else if ( JailCellCreation.players.containsKey(event.getPlayer().getName()))
			{
				JailCellCreation.select(event.getPlayer(), event.getClickedBlock());
				event.setCancelled(true);
			}
			else if ( JailSetCommand.players.containsKey(event.getPlayer().getName()))
			{
				JailSetCommand.RightClick(event.getClickedBlock(), event.getPlayer());
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent event){
		if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
		
		Player damager = (Player) event.getDamager();
		Player player = (Player) event.getEntity();
		
		String[] param = InputOutput.jailStickParameters.get(damager.getItemInHand().getTypeId());
		
		if(!Jail.jailStickToggle.containsKey(damager.getName().toLowerCase())){
			Jail.jailStickToggle.put(damager.getName().toLowerCase(), false);
		}

		if(Util.permission(damager, "jail.usejailstick." + String.valueOf(damager.getItemInHand().getTypeId()), PermissionDefault.OP) && Jail.jailStickToggle.get(damager.getName().toLowerCase())){
            if(player != null){
                JailPrisoner prisoner = new JailPrisoner(player.getName().toLowerCase(), Integer.parseInt(param[2]) * 6, param[3], "", false, "", param[4], false, "", damager.getName(), "");
                PrisonerManager.PrepareJail(prisoner, player);
                JailLog logger = new JailLog();
                damager.sendMessage(ChatColor.RED + "You jailed " + ChatColor.GREEN + player.getName() + ChatColor.RED +  " for " + ChatColor.GREEN + Integer.parseInt(param[2]) + ChatColor.RED + " minutes");
                if(Settings.getGlobalBoolean(Setting.EnableLogging)){
                    logger.logToFile(player.getName(), Integer.parseInt(param[2]), param[4], damager.getName());
                }
            }else{
                Util.Message(ChatColor.RED + "That Player does not exist?!?!?!", damager);
            }
		}
	}

	@EventHandler()
	public void onPlayerChat(PlayerChatEvent event) {
		if ( JailCellCreation.players.containsKey(event.getPlayer().getName()))
		{
			if (JailCellCreation.chatmessage(event.getPlayer(), event.getMessage()));
				event.setCancelled(true);
		}

        for(Object o : Settings.getGlobalList(Setting.BannedWords)){
        	String word = (String) o;
            if(event.getMessage().toLowerCase().contains(word + " ") && Settings.getGlobalBoolean(Setting.EnableJailSwear)){
                event.setCancelled(true);
                JailPrisoner prisoner = new JailPrisoner(event.getPlayer().getName(), Settings.getGlobalInt(Setting.JailSwearTime) * 6, "", "", false, "", "Swearing", true, "", "JailSwear", "");
                PrisonerManager.PrepareJail(prisoner, event.getPlayer());
                PrisonerManager.Jail(prisoner, event.getPlayer());
                JailLog logger = new JailLog();
                if(Settings.getGlobalBoolean(Setting.EnableLogging)){
                        logger.logToFile(event.getPlayer().getName(), Settings.getGlobalInt(Setting.JailSwearTime), "Swearing", "JailSwear");
                }
             }
        }
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(event.getPlayer().isOp() && Jail.updateNeeded){
			event.getPlayer().sendMessage(ChatColor.BLUE + "There is an update for the jail plugin!");
		}
		
		 if (Jail.prisoners.containsKey(event.getPlayer().getName().toLowerCase())) {
			 JailPrisoner prisoner = Jail.prisoners.get(event.getPlayer().getName().toLowerCase());
			 
			 if(prisoner != null) {
				 if (prisoner.offlinePending()) {
					 //If the prisoner has something we're supposed to do when they come back online
					 if (prisoner.getTransferDestination().isEmpty()) {
						 //We're not transferring the player
						 if (prisoner.getRemainingTime() != 0) {
							 //They still have time to server, so they are probably newly jailed.
							 PrisonerManager.Jail(prisoner, event.getPlayer());
							 return;
						 }else if(prisoner.getJail() != null) {
							 //Their jail wasn't null and they no longer have any time, let's unjail them
							 PrisonerManager.UnJail(prisoner, event.getPlayer());
							 return;
						 }else {
							 //Something just went completely wrong, so let's go forward and delete the prisoner
							 prisoner.delete();
							 return;
						 }
					 }else {
						 PrisonerManager.Transfer(prisoner, event.getPlayer());
						 return;
					 }
				 }else {
					 if(prisoner.getJail() == null) {
						 JailZone jail = JailZoneManager.findNearestJail(event.getPlayer().getLocation());
						 prisoner.setJail(jail);
						 event.getPlayer().teleport(jail.getTeleportLocation());
					 }else {
						 if(prisoner.getCell() == null) {
							 event.getPlayer().teleport(prisoner.getJail().getTeleportLocation());
						 }else {
							 event.getPlayer().teleport(prisoner.getCell().getTeleportLocation());
						 } 
					 }
				 }
			 }
			 
			 if (prisoner.getJail().getSettings().getBoolean(Setting.IgnorePrisonersSleepingState))
				 event.getPlayer().setSleepingIgnored(true);
		 }
	 }
	 
	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event) {
		JailPrisoner prisoner = Jail.prisoners.get(event.getPlayer().getName().toLowerCase());
		if (prisoner == null) return;

		prisoner.killGuards();
	 }
}
