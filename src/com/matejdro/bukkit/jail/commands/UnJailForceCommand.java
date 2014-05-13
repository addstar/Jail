package com.matejdro.bukkit.jail.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.Util;

public class UnJailForceCommand extends BaseCommand {
	
	public UnJailForceCommand()
	{
		needPlayer = false;
		adminCommand = true;
		permission = "jail.command.unjailforce";
	}


	public Boolean run(CommandSender sender, String[] args) {		
		if (args.length < 1)
		{
			Util.Message("Usage: /unjailforce [Name]", sender);
			return true;
		}
		
		OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
		
		if (player == null || !Jail.prisoners.containsKey(player.getUniqueId()))
		{
			Util.Message("That player is not jailed!", sender);
			return true;
		}

		Jail.prisoners.get(player.getUniqueId()).delete();
		Util.Message("Player deleted from the jail database!", sender);
		return true;
	}
}