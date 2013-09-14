package com.graywolf336.jail;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.graywolf336.jail.command.CommandHandler;

public class JailMain extends JavaPlugin {
	private JailManager jm;
	private CommandHandler cmdHand;
	
	public void onEnable() {
		jm = new JailManager();
		cmdHand = new CommandHandler(this);
		
		//For the time, we will use:
		//http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/TimeUnit.html#convert(long, java.util.concurrent.TimeUnit)
	}
	
	public void onDisable() {
		cmdHand = null;
	}
	
	/* Majority of the new command system was heavily influced by the MobArena.
	 * Thank you garbagemule for the great system you have in place there.
	 *
	 * Send the command off to the CommandHandler class, that way this main class doesn't get clogged up.
	 */
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		cmdHand.handleCommand(jm, sender, command.getName().toLowerCase(), args);
		return true;
	}
}
