package com.matejdro.bukkit.jail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import javax.swing.Timer;

import me.muizers.Notifications.Notifications;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.matejdro.bukkit.jail.commands.BaseCommand;
import com.matejdro.bukkit.jail.commands.JailCheckCommand;
import com.matejdro.bukkit.jail.commands.JailClearCommand;
import com.matejdro.bukkit.jail.commands.JailClearForceCommand;
import com.matejdro.bukkit.jail.commands.JailCommand;
import com.matejdro.bukkit.jail.commands.JailCreateCellsCommand;
import com.matejdro.bukkit.jail.commands.JailCreateCommand;
import com.matejdro.bukkit.jail.commands.JailCreateWeCommand;
import com.matejdro.bukkit.jail.commands.JailDeleteCellCommand;
import com.matejdro.bukkit.jail.commands.JailDeleteCellsCommand;
import com.matejdro.bukkit.jail.commands.JailDeleteCommand;
import com.matejdro.bukkit.jail.commands.JailHandcuffCommand;
import com.matejdro.bukkit.jail.commands.JailListCellsCommand;
import com.matejdro.bukkit.jail.commands.JailListCommand;
import com.matejdro.bukkit.jail.commands.JailMuteCommand;
import com.matejdro.bukkit.jail.commands.JailPayCommand;
import com.matejdro.bukkit.jail.commands.JailRecordCommand;
import com.matejdro.bukkit.jail.commands.JailReloadCommand;
import com.matejdro.bukkit.jail.commands.JailSetCommand;
import com.matejdro.bukkit.jail.commands.JailStatusCommand;
import com.matejdro.bukkit.jail.commands.JailStickCommand;
import com.matejdro.bukkit.jail.commands.JailStopCommand;
import com.matejdro.bukkit.jail.commands.JailTeleInCommand;
import com.matejdro.bukkit.jail.commands.JailTeleOutCommand;
import com.matejdro.bukkit.jail.commands.JailTransferAllCommand;
import com.matejdro.bukkit.jail.commands.JailTransferCommand;
import com.matejdro.bukkit.jail.commands.JailUnHandcuffCommand;
import com.matejdro.bukkit.jail.commands.JailVersionCommand;
import com.matejdro.bukkit.jail.commands.JailVoteCommand;
import com.matejdro.bukkit.jail.commands.UnJailCommand;
import com.matejdro.bukkit.jail.commands.UnJailForceCommand;
import com.matejdro.bukkit.jail.listeners.HandCuffListener;
import com.matejdro.bukkit.jail.listeners.JailBlockListener;
import com.matejdro.bukkit.jail.listeners.JailEntityListener;
import com.matejdro.bukkit.jail.listeners.JailPlayerListener;
import com.matejdro.bukkit.jail.listeners.JailPlayerProtectionListener;

public class Jail extends JavaPlugin {
	public static Logger log = Logger.getLogger("Minecraft");
	
	private HandCuffManager handcuffs;
	private JailPlayerListener playerListener;
	private JailBlockListener blockListener;
	private JailPlayerProtectionListener playerPreventListener;
	private JailEntityListener entityListener;
	public JailAPI API;
	public InputOutput IO;
    public Notifications notificationsPlugin;
    public static HashMap<String,JailZone> zones = new HashMap<String,JailZone>();
    public static HashMap<UUID,JailPrisoner> prisoners = new HashMap<UUID,JailPrisoner>();
    public static HashMap<UUID, HashMap<String, ItemStack[]>> prisonerInventories = new HashMap<UUID, HashMap<String, ItemStack[]>>();
    public static HashMap<Creature, JailPrisoner> guards = new HashMap<Creature, JailPrisoner>();
    public static HashMap<String, Boolean> jailStickToggle = new HashMap<String, Boolean>();
    
    private Timer timer;
    private Update update;

    private long lastCheckTime = 0;

    public static Jail instance;

    public static Plugin permissions = null;

    public static boolean updateNeeded = false;

    private HashMap<String, BaseCommand> commands = new HashMap<String, BaseCommand>();
    
    public ArrayList<Material> helmets = new ArrayList<Material>();
	public ArrayList<Material> chestPlates = new ArrayList<Material>();
	public ArrayList<Material> leggings = new ArrayList<Material>();
	public ArrayList<Material> boots = new ArrayList<Material>();

	@Override
	public void onDisable() {
		if(this.handcuffs != null)
			this.handcuffs = null;
		
		if(this.update != null) {
			this.update = null;
			updateNeeded = false;
		}
		
		if (timer != null)
			timer.stop();
		InputOutput.freeConnection();
		for (Creature w : guards.keySet())
			w.remove();
	}

	@Override
	public void onEnable() {
		instance = this;
		playerListener = new JailPlayerListener();
		blockListener = new JailBlockListener();
		playerPreventListener = new JailPlayerProtectionListener(this);
		entityListener = new JailEntityListener(this);
		IO = new InputOutput();
		API = new JailAPI();
		
		IO.LoadSettings();
		IO.PrepareDB();
		IO.LoadJails();
		IO.LoadPrisoners();
		IO.LoadCells();
		
		notificationsPlugin = (Notifications) getServer().getPluginManager().getPlugin("Notifications");
		
		if(notificationsPlugin == null){
			getLogger().info("Notifications plugin not installed, features disabled");
		}else{
			getLogger().info("Notifications plugin installed, features enabled!");
		}
		
		getServer().getPluginManager().registerEvents(blockListener, this);
		getServer().getPluginManager().registerEvents(entityListener, this);
		getServer().getPluginManager().registerEvents(playerListener, this);
		getServer().getPluginManager().registerEvents(playerPreventListener, this);
		getServer().getPluginManager().registerEvents(new HandCuffListener(this), this);
		
		//Init timers
		lastCheckTime = System.currentTimeMillis();
		if (Settings.getGlobalBoolean(Setting.UseBukkitSchedulerTimer)) {
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new TimeEvent(), 20, 20);
		} else {
			timer = new Timer(1000,new ActionListener () {
				public void actionPerformed (ActionEvent event) {
					getServer().getScheduler().scheduleSyncDelayedTask(Jail.instance, new TimeEvent());
				};
			});
			timer.start();
		}

		commands.put("jail", new JailCommand());
		commands.put("unjail", new UnJailCommand());
		commands.put("jaildelete", new JailDeleteCommand());
		commands.put("jailcreatecells", new JailCreateCellsCommand());
		commands.put("jailtelein", new JailTeleInCommand());
		commands.put("jailteleout", new JailTeleOutCommand());
		commands.put("unjailforce", new UnJailForceCommand());
		commands.put("jailclear", new JailClearCommand());
		commands.put("jailclearforce", new JailClearForceCommand());
		commands.put("jailtransfer", new JailTransferCommand());
		commands.put("jailtransferall", new JailTransferAllCommand());
		commands.put("jailstatus", new JailStatusCommand());
		commands.put("jailcheck", new JailCheckCommand());
		commands.put("jaillist", new JailListCommand());
		commands.put("jailmute", new JailMuteCommand());
		commands.put("jailstop", new JailStopCommand());
		commands.put("jailset", new JailSetCommand());
		commands.put("jailpay", new JailPayCommand());
		commands.put("jailcreate", new JailCreateCommand());
		commands.put("jaildeletecells", new JailDeleteCellsCommand());
		commands.put("jaillistcells", new JailListCellsCommand());
		commands.put("jailstick", new JailStickCommand());
		commands.put("jailcreatewe", new JailCreateWeCommand());
		commands.put("jaildeletecell", new JailDeleteCellCommand());
		commands.put("jailreload", new JailReloadCommand());
		commands.put("jailrecord", new JailRecordCommand());
		commands.put("jailversion", new JailVersionCommand());
		commands.put("votejail", new JailVoteCommand());
		commands.put("handcuff", new JailHandcuffCommand());
		commands.put("unhandcuff", new JailUnHandcuffCommand());

		IO.initMetrics();
		
		final JailScoreboardManager manager = new JailScoreboardManager();
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run(){
				manager.displayJailTime();
			}
		}, 10, 50);
		
		this.handcuffs = new HandCuffManager();
		
		if(Settings.getGlobalBoolean(Setting.AllowUpdateNotifications)){
			this.update = new Update(31139);
			
			getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
				public void run() {
					update.query();
					updateNeeded = update.updateNeeded();
				}
			}, 600L, 36000L);
		}
		
		log.info("[Jail] " + getDescription().getFullName() + " loaded!");
	}

	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		BaseCommand cmd = commands.get(command.getName().toLowerCase());
		if (cmd != null) return cmd.execute(sender, args);
		return false;
	}
	
	public HandCuffManager getHandCuffManager() {
		return this.handcuffs;
	}

	class TimeEvent implements Runnable {
		public void run() {
			int timePassed;
			if (System.currentTimeMillis() - lastCheckTime >= 10000)
			{
				timePassed = (int) Math.round((double) (System.currentTimeMillis() - lastCheckTime) / 10000.0);
				lastCheckTime = System.currentTimeMillis();

			}
			else
				return;
			
			for (JailPrisoner prisoner : prisoners.values())
			{
				Util.debug(prisoner, "Time event");
				Util.debug(prisoner, "Name: \"" + prisoner.getName() + "\"");
				Player player = getServer().getPlayer(prisoner.getUUID());
				Util.debug(prisoner, "Remaining time:" + prisoner.getRemainingTime());
				Util.debug("Player: " + String.valueOf(player));
				if (prisoner.getRemainingTime() > 0 && (player != null || (prisoner.getJail() != null && prisoner.getJail().getSettings().getBoolean(Setting.CountdownTimeWhenOffline))))
				{
					Util.debug(prisoner, "Lowering remaining time for prisoner");
					prisoner.setRemainingTime(Math.max(0, prisoner.getRemainingTime() - timePassed));
					InputOutput.UpdatePrisoner(prisoner);
					if (prisoner.getRemainingTime() == 0) 
					{
						Util.debug(prisoner, "Releasing prisoner because his time is up");
						prisoner.release();
					}

				}

				if (player != null && prisoner.getJail() != null)
				{
					if (prisoner.getJail().getSettings().getDouble(Setting.MaximumAFKTime) > 0.0)
					{
						prisoner.setAFKTime(prisoner.getAFKTime() + 1);
						if (prisoner.getAFKTimeMinutes() > prisoner.getJail().getSettings().getDouble(Setting.MaximumAFKTime))
						{
							Util.debug(prisoner, "Prisoner is AFK. Let's kick him");
							prisoner.setAFKTime(0);
							player.kickPlayer(prisoner.getJail().getSettings().getString(Setting.MessageAFKKick));
						}
					}
				}
			}    	
		}
	}

}
