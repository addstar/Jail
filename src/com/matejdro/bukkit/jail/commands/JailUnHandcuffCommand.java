package com.matejdro.bukkit.jail.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.Util;

public class JailUnHandcuffCommand extends BaseCommand {
	
	public JailUnHandcuffCommand() {
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
			
			if(Jail.instance.getHandCuffManager().isHandCuffed(player.getName())){
				Util.Message(ChatColor.GREEN + "That player's handcuffs have been removed, releasing them now!", sender);
				Jail.instance.getHandCuffManager().removeHandCuffs(player.getName());
				player.sendMessage(ChatColor.GREEN + "Your handcuffs have been removed.");
			}else{
				Util.Message(ChatColor.BLUE + player.getName() + ChatColor.RED + " doesn't have any handcuffs to remove!", sender);
			}
		}
		
		return true;
	}

}
