package com.matejdro.bukkit.jail.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.matejdro.bukkit.jail.InputOutput;
import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.JailPrisoner;
import com.matejdro.bukkit.jail.Util;

public class JailMuteCommand extends BaseCommand {	
	public JailMuteCommand()
	{
		needPlayer = false;
		adminCommand = true;
		permission = "jail.command.jailmute";
	}
	
	public Boolean run(CommandSender sender, String[] args) {		
		if (args.length < 1)
		{
			Util.Message("Usage: /jailmute [Player name]", sender);
			return true;
		}
		
		Player player = Bukkit.getPlayer(args[0]);
		
		if(player == null)
		{
			Util.Message("Unknown player " + args[0], sender);
			return true;
		}
		
		JailPrisoner prisoner = Jail.prisoners.get(player.getUniqueId());
		
		if (prisoner == null)
		{
			Util.Message(args[0] + " is not jailed!", sender);
			return true;
		}
		
		if (prisoner.isMuted())
		{
			prisoner.setMuted(false);
			InputOutput.UpdatePrisoner(prisoner);
			Util.Message(args[0] + " can speak again!", sender);
		}
		else
		{
			prisoner.setMuted(true);
			InputOutput.UpdatePrisoner(prisoner);
			Util.Message(args[0] + " is now muted!", sender);
		}
		return true;
		
	}

}
