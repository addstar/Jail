package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.CreationPlayer;
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
		String name = args[0];
		
		if(jm.isValidJail(name)) {
			sender.sendMessage("Jail with that name already exist!");
			return true;
		}else {
			//Create jail and all that good stuff here
			if(jm.addCreatingJail(player.getName(), name)) {
				jm.getJailCreationSteps().startStepping(jm.getJailCreationPlayer(player.getName()), player);
			}else {
				CreationPlayer cp = jm.getJailCreationPlayer(player.getName());
				String message = "You're already creating a Jail with the name '" + cp.getName() + "' and you still need to ";
				
				switch(cp.getState()) {
					case 1:
						message += "select the first point.";
						break;
					case 2:
						message += "select the second point.";
						break;
					case 3:
						message += "set the teleport in location.";
						break;
					case 4:
						message += "set the release location.";
						break;
				}
				
				player.sendMessage(message);
			}
			
			return true;
		}
	}
}
