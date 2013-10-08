package com.graywolf336.jail;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.graywolf336.jail.command.CommandHandler;
import com.graywolf336.jail.listeners.BlockListener;
import com.graywolf336.jail.listeners.EntityListener;
import com.graywolf336.jail.listeners.PlayerListener;

public class JailMain extends JavaPlugin {
	private JailIO io;
	private JailManager jm;
	private CommandHandler cmdHand;
	
	public void onEnable() {
		io = new JailIO(this);
		jm = new JailManager(this);
		cmdHand = new CommandHandler(this);
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new EntityListener(), this);
		pm.registerEvents(new PlayerListener(this), this);
		
		//For the time, we will use:
		//http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/TimeUnit.html#convert(long, java.util.concurrent.TimeUnit)
	}
	
	public void onDisable() {
		cmdHand = null;
	}
	
	/* Majority of the new command system was heavily influenced by the MobArena.
	 * Thank you garbagemule for the great system you have in place there.
	 *
	 * Send the command off to the CommandHandler class, that way this main class doesn't get clogged up.
	 */
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		cmdHand.handleCommand(jm, sender, command.getName().toLowerCase(), args);
		return true;//Always return true here, that way we can handle the help and command usage ourself.
	}
	
	/** Gets the instance of the {@link JailIO} which contains all the settings and handles the saving/loading. */
	public JailIO getJailIO() {
		return this.io;
	}
	
	/** Gets the {@link JailManager} instance. */
	public JailManager getJailManager() {
		return this.jm;
	}
}
