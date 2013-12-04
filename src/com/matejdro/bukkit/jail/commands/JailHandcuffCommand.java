package com.matejdro.bukkit.jail.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.Util;

public class JailHandcuffCommand extends BaseCommand{

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
			
			if(Jail.instance.handcuffed.contains(player.getName())){
				Util.Message(ChatColor.GREEN + "That player is already handcuffed, releasing them now!", sender);
				Jail.instance.handcuffed.remove(player.getName());
			}else{
				Jail.instance.handcuffed.add(player.getName());
				Util.Message(ChatColor.BLUE + player.getName() + ChatColor.GREEN + " has been handcuffed!", sender);
			}
		}
		return true;
	}

}
