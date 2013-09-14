package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 1,
		needsPlayer = true,
		pattern = "jailcreate|jc",
		permission = "jail.command.jailcreate",
		usage = "/jailcreate [name]"
	)
public class JailCreateCommand implements Command {

	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		String name = args[0];
		
		if(jm.isValidJail(name)) {
			sender.sendMessage("ail with that name already exist!");
			return true;
		}else {
			//Create jail and all that good stuff here
		}
		
		return true; //If they made it this far, the command is intact and ready to be processed. :)
	}

}
