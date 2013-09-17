package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 0,
		minimumArgs = 0,
		needsPlayer = true,
		pattern = "jailstop",
		permission = "jail.command.jailstop",
		usage = "/jailstop"
	)
public class JailStopCommand implements Command {

	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		Player player = (Player) sender;
		
		jm.removeJailCreationPlayer(player.getName());
		jm.removeCellCreationPlayer(player.getName());
		
		player.sendMessage("Any creations, jail or cell, have been stopped.");
		
		return true;
	}

}
