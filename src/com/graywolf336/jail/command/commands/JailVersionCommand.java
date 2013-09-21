package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

public class JailVersionCommand implements Command{

	@CommandInfo(
			maxArgs = -1,
			minimumArgs = 0,
			needsPlayer = false,
			pattern = "jailversion|jv",
			permission = "jail.command.jailversion",
			usage = "/jailversion"
		)
	
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		// Sends the version number to the sender
		sender.sendMessage("Jail Version: " + jm.getPlugin().getDescription().getVersion());
		
		return true; //If they made it this far, the command is intact and ready to be processed. :)
	}

}
