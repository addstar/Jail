package com.matejdro.bukkit.jail.commands;

import org.bukkit.command.CommandSender;

import com.matejdro.bukkit.jail.Jail;

public class JailVersionCommand extends BaseCommand{
	
	public JailVersionCommand(){
		adminCommand = false;
		needPlayer = false;
		permission = "jail.command.jailversion";
	}
	
	@Override
	public Boolean run(CommandSender sender, String[] args) {
		sender.sendMessage("Jail Version: " + Jail.instance.getDescription().getVersion());
		return true;
	}

}
