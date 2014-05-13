package com.matejdro.bukkit.jail.commands;

import org.bukkit.command.CommandSender;

import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.JailPrisoner;
import com.matejdro.bukkit.jail.Util;

public class JailClearForceCommand extends BaseCommand {
	
	public JailClearForceCommand()
	{
		needPlayer = false;
		adminCommand = true;
		permission = "jail.command.jailclearforce";
	}


	public Boolean run(CommandSender sender, String[] args) {		
		for (JailPrisoner prisoner : Jail.prisoners.values())
			prisoner.delete();

		Util.Message("Everyone have been cleared!", sender);
		return true;
		}

}
