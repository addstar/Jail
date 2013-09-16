package com.graywolf336.jail.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		Player player = (Player) sender;
		String name = player.getName();
		String jail = args[0];
		
		//Check if the player is currently creating something else
		if(jm.isCreatingSomething(name)) {
			String message = jm.getStepMessage(name);
			if(!message.isEmpty()) {
				player.sendMessage(ChatColor.RED + message);
			}else {
				player.sendMessage(ChatColor.RED + "You're already creating something else, please finish it or cancel.");
			}
		}else {
			if(jm.isValidJail(jail)) {
				player.sendMessage(ChatColor.RED + "Jail by the name of '" + jail + "' already exist!");
			}else {
				if(jm.addCreatingJail(name, jail)) {
					jm.getJailCreationSteps().startStepping(jm.getJailCreationPlayer(name), player);
				}else {
					player.sendMessage(ChatColor.RED + "Seems like you're already creating a Jail or something went wrong on our side.");
				}
			}
		}
		
		return true;
	}
}
