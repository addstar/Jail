package com.matejdro.bukkit.jail.commands;

import java.awt.Color;

import me.muizers.Notifications.Notification;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.JailLog;
import com.matejdro.bukkit.jail.JailPrisoner;
import com.matejdro.bukkit.jail.PrisonerManager;
import com.matejdro.bukkit.jail.Setting;
import com.matejdro.bukkit.jail.Settings;
import com.matejdro.bukkit.jail.Util;
import com.matejdro.bukkit.jail.events.OnlinePlayerJailedEvent;

public class JailCommand extends BaseCommand {	
	public JailCommand()
	{
		needPlayer = false;
		adminCommand = true;
		permission = "jail.command.jail";
	}

	public Boolean run(CommandSender sender, String[] args) {

            if (args.length < 1 && sender.hasPermission("jail.command.jail"))
            {
                Util.Message("Usage: /jail [Name] (time) (j:Jail name) (c:Cell name) (r:Reason) (m)", sender);
                return true;
            }

            if (Jail.zones.size() < 1)
            {
                Util.Message(Settings.getGlobalString(Setting.MessageNoJail), sender);
                return true;
            }

            //Initialize defaults
            String playerName = args[0].toLowerCase();
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            
            int time = Settings.getGlobalInt(Setting.DefaultJailTime);
            String jailname = "";
            String cellname = "";
            String reason = "";
            Boolean muted = Settings.getGlobalBoolean(Setting.AutomaticMute);

            //Check if the player is currently jailed
            if(Jail.prisoners.containsKey(player.getUniqueId())){
            	Util.Message(Settings.getGlobalString(Setting.MessagePlayerAlreadyJailed), sender);
            	return true;
            }
            
            //Parse command line
            for (int i = 1; i < args.length; i++)
            {
                String line = args[i];

                if (Util.isInteger(line))
                    time = Integer.parseInt(line);
                else if (line.startsWith("j:"))
                    jailname = line.substring(2);
                else if (line.startsWith("c:"))
                    cellname = line.substring(2);
                else if (line.equals("m"))
                    muted = !muted;
                else if (line.startsWith("r:"))
                {
                    if (line.startsWith("r:\""))
                    {
                        reason = line.substring(3);
                        while (!line.endsWith("\""))
                        {
                            i++;
                            if (i >= args.length)
                            {
                                Util.Message("Usage: /jail [Name] (t:time) (j:Jail name) (c:Cell name) (r:Reason) (m)", sender);
                                return true;
                            }

                            line = args[i];
                            if (line.endsWith("\""))
                                reason += " " + line.substring(0, line.length() - 1);
                            else
                                reason += " " + line;
                        }
                    }
                    else reason = line.substring(2);

                    int maxReason = Settings.getGlobalInt(Setting.MaximumReasonLength);
                    if (maxReason > 250) maxReason = 250; //DB Limit

                    if (reason.length() > maxReason)
                    {
                        Util.Message(Settings.getGlobalString(Setting.MessageTooLongReason), sender);
                        return true;
                    }
                }
            }

            JailPrisoner prisoner = null;
            String message;
            JailLog logger = new JailLog();
            
            if(!player.isOnline()) {//If the player isn't online, then let's handle him separately            	
            	prisoner = new JailPrisoner(player.getName(), player.getUniqueId(), time * 6, jailname, cellname, true, "", reason, muted, "", sender instanceof Player ? ((Player) sender).getName() : "console", "");
                PrisonerManager.PrepareJail(prisoner, null);
            	
                message = Settings.getGlobalString(Setting.MessagePrisonerOffline);
                if(Settings.getGlobalBoolean(Setting.EnableLogging)){
                    logger.logToFile(args[0], time, reason, sender.getName());
                }
                
            }else {//The player is online
            	if(((Player)player).hasPermission("jail.cantbejailed")){
                	sender.sendMessage(ChatColor.RED + "This player can not be jailed!");
                	return true;
                }
            	
            	playerName = player.getName().toLowerCase();
            	
            	OnlinePlayerJailedEvent event = new OnlinePlayerJailedEvent(player.getPlayer(), time, jailname, cellname, reason, muted, sender instanceof Player ? ((Player) sender).getName() : "console");
            	Jail.instance.getServer().getPluginManager().callEvent(event);
            	
            	if(event.isCancelled()) {
            		Util.Message("The jailing of " + event.getPlayer().getName() + " was cancelled due to another plugin.", sender);
            		return true;
            	}else {
            		player = event.getPlayer(); //Set our instance of the player to the one from the event, in case a listener changed something.
            		prisoner = new JailPrisoner(player.getName(), player.getUniqueId(),
            				event.getTime() * 6, event.getJail(), event.getCell(), false, "",
            				event.getReason(), event.isMuted(), "", event.getJailer(), "");
            	}
            	
                PrisonerManager.PrepareJail(prisoner, (Player)player);
                
                ((Player)player).setGameMode(GameMode.SURVIVAL);
                
                message = Settings.getGlobalString(Setting.MessagePrisonerJailed);
                if(Settings.getGlobalBoolean(Setting.BroadcastJailMessage)){
                    String reason1 = "No reason set";

                    if(reason != ""){
                        reason1 = reason;
                    }

                    Bukkit.broadcastMessage(Settings.getGlobalString(Setting.MessagePrisonerJailed) + " Reason: " + reason1);
                }
                
                if(Settings.getGlobalBoolean(Setting.EnableLogging)){
                    logger.logToFile(player.getName(), time, reason, sender.getName());
                }
            }

            
            if(Jail.instance.notificationsPlugin != null){
                Notification jailNotification = new Notification("Jail", playerName + " was jailed by " + sender.getName(), "For " + prisoner.getRemainingTimeMinutes() + " mins", Color.ORANGE, Color.RED, Color.RED);
                Jail.instance.notificationsPlugin.showNotification(jailNotification);
            }
            
            message = prisoner.parseTags(message);
            Util.Message(message, sender);
            return true;
	}

}
