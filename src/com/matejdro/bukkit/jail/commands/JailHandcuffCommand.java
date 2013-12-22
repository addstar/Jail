package com.matejdro.bukkit.jail.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.Util;

public class JailHandcuffCommand extends BaseCommand {

	public JailHandcuffCommand(){
			needPlayer = false;
			adminCommand = true;
			permission = "jail.command.handcuff";
	}
	
	@Override
	public Boolean run(CommandSender sender, String[] args) {
		if(args.length < 1){
			Util.Message("You must specify a player!", sender);
		}else{
			Player player = Bukkit.getPlayer(args[0]);
			
			if(player == null){
				Util.Message(ChatColor.RED + "That player is not online!", sender);
				return true;
			}
			
			if(player.hasPermission("jail.cantbehandcuffed")) {
				Util.Message(ChatColor.RED + "They player can't be handcuffed.", sender);
				return true;
			}
			
			if(Jail.prisoners.containsKey(player.getName().toLowerCase())) {
				Util.Message(ChatColor.RED + "That player is currently jailed, you can't handcuff a prisoner.", sender);
			}else if(Jail.instance.getHandCuffManager().isHandCuffed(player.getName())){
				Util.Message(ChatColor.GREEN + "That player is already handcuffed, releasing them now!", sender);
				Jail.instance.getHandCuffManager().removeHandCuffs(player.getName());
				player.sendMessage(ChatColor.GREEN + "Your handcuffs have been removed.");
			}else{
				Jail.instance.getHandCuffManager().addHandCuffs(player.getName(), player.getLocation());
				Util.Message(ChatColor.BLUE + player.getName() + ChatColor.GREEN + " has been handcuffed!", sender);
				player.sendMessage(ChatColor.RED + "You've been handcuffed.");
			}
		}
		return true;
	}

}
