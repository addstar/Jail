package com.graywolf336.jail.command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.commands.JailCommand;

public class CommandHandler {
	private LinkedHashMap<String, Command> commands;
	
	public CommandHandler() {
		loadCommands();
		
		Bukkit.getLogger().info("Loaded " + commands.size() + " commands.");
		for(Command c : commands.values())
			Bukkit.getLogger().info(c.getClass().getAnnotation(CommandInfo.class).pattern());
	}
	
	public void handleCommand(JailManager jailmanager, CommandSender sender, String command, String[] args) {
		List<Command> matches = getMatches(command);
		
		if(matches.size() == 0) {
			sender.sendMessage("No commands found by the name of " + command + ".");
			return;
		}
		
		if(matches.size() > 1) {
			for(Command c : matches)
				showUsage(sender, c);
			return;
		}
		
		Command c = matches.get(0);
		CommandInfo i = c.getClass().getAnnotation(CommandInfo.class);
		
		// First, let's check if the sender has permission for the command.
		if(!sender.hasPermission(i.permission())) {
			sender.sendMessage("No permission to use that command.");//TODO: Make this configurable
			return;
		}
		
		// Next, let's check if we need a player and if the sender is actually a player
		if(i.needsPlayer() && !(sender instanceof Player)) {
			sender.sendMessage("A player context is required.");
			return;
		}
		
		//Now, let's check the size of the arguments passed. If it is shorter than the minimum required args, let's show the usage.
		if(args.length < i.minimumArgs()) {
			showUsage(sender, c);
			return;
		}
		
		//Then, if the maximumArgs doesn't equal -1, we need to check if the size of the arguments passed is greater than the maximum args.
		if(i.maxArgs() != -1 && i.maxArgs() < args.length) {
			showUsage(sender, c);
			return;
		}
		
		//Since everything has been checked and we're all clear, let's execute it.
		if(!c.execute(jailmanager, sender, args)) {
			showUsage(sender, c);
			return;
		}
	}
	
	private List<Command> getMatches(String command) {
		List<Command> result = new ArrayList<Command>();
		
		for(Entry<String, Command> entry : commands.entrySet()) {
			if(command.matches(entry.getKey())) {
				result.add(entry.getValue());
			}
		}
		
		return result;
	}
	
	/**
	 * Shows the usage information to the sender, if they have permission.
	 * 
	 * @param sender The sender of the command
	 * @param command The command to send usage of.
	 */
	private void showUsage(CommandSender sender, Command command) {
		CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);
		if(!sender.hasPermission(info.permission())) return;
		
		sender.sendMessage(info.usage());
	}
	
	/** Loads all the commands into the hashmap. */
	private void loadCommands() {
		commands = new LinkedHashMap<String, Command>();
		
		load(JailCommand.class);
	}

	private void load(Class<? extends Command> c) {
		CommandInfo info = c.getAnnotation(CommandInfo.class);
		if(info == null) return;
		
		try {
			commands.put(info.pattern(), c.newInstance());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
