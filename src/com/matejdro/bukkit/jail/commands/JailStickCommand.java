package com.matejdro.bukkit.jail.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.Setting;
import com.matejdro.bukkit.jail.Settings;
import com.matejdro.bukkit.jail.Util;

public class JailStickCommand extends BaseCommand {	
	public JailStickCommand()
	{
		needPlayer = true;
		adminCommand = true;
		permission = "jail.usercmd.jailstick";
	}
	
	public Boolean run(CommandSender sender, String[] args) {		
		
		Player player = (Player) sender;
		Boolean enabled = Jail.jailStickToggle.get(player.getName().toLowerCase());
		
		if (enabled == null || !enabled) {
			Util.Message(Settings.getGlobalString(Setting.MessageJailStickEnabled), sender);
			Jail.jailStickToggle.put(player.getName().toLowerCase(), true);
		} else {
			Util.Message(Settings.getGlobalString(Setting.MessageJailStickDisabled), sender);
			Jail.jailStickToggle.put(player.getName().toLowerCase(), false);
		}
		
		return true;
	}

}
