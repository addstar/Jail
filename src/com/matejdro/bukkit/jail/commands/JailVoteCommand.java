package com.matejdro.bukkit.jail.commands;

import java.awt.Color;

import me.muizers.Notifications.Notification;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.JailPrisoner;
import com.matejdro.bukkit.jail.JailVote;
import com.matejdro.bukkit.jail.PrisonerManager;
import com.matejdro.bukkit.jail.Setting;
import com.matejdro.bukkit.jail.Settings;
import com.matejdro.bukkit.jail.Util;

public class JailVoteCommand extends BaseCommand{

	public JailVoteCommand(){
		needPlayer = true;
		adminCommand = false;
		permission = "jail.usercmd.jailvote";
	}
	
	@Override
	public Boolean run(CommandSender sender, String[] args) {
		Player player = (Player)sender;
		if(Settings.getGlobalBoolean(Setting.VoteJailEnabled)){
			if(args.length < 1){
				Util.Message(ChatColor.RED + "You must specify a player", player);
			}else{
				if(Jail.instance.getServer().getPlayer(args[0]) != null){
					if(!player.hasPermission("jail.usercmd.jailvote.player")){
						Util.Message(ChatColor.RED + "You don't have permission to do this!", player);
					}else{
						if(JailVote.voteStarted){
							Util.Message(ChatColor.RED + "A vote is already running", player);
							return true;
						}
						
						if(Jail.instance.getServer().getPlayer(args[0]).hasPermission("jail.usercmd.jailvote.immune")){
							Util.Message(ChatColor.RED + "You cannot vote for this player!", player);
							return true;
						}
						
						JailVote.voteStarted = true;
						JailVote.hasVoted.add(player);
						
						final Player target = Jail.instance.getServer().getPlayer(args[0]);
						Jail.instance.getServer().broadcastMessage(ChatColor.DARK_AQUA + "------------ Jail -----------");
						Jail.instance.getServer().broadcastMessage(ChatColor.GREEN + player.getName() + ChatColor.AQUA + " has voted that " + ChatColor.RED + target.getName() + ChatColor.AQUA + " should be jailed.");
						Jail.instance.getServer().broadcastMessage(ChatColor.GOLD + "Type " + ChatColor.GREEN + "/votejail yes" + ChatColor.GOLD + " if you agree");
						Jail.instance.getServer().broadcastMessage(ChatColor.GOLD + "or type " + ChatColor.GREEN + "/votejail no" + ChatColor.GOLD + " if you disagree");
						Jail.instance.getServer().broadcastMessage(ChatColor.AQUA + "You have "  + ChatColor.GREEN + "60" + ChatColor.AQUA + " seconds to vote!");
						Jail.instance.getServer().broadcastMessage(ChatColor.DARK_AQUA + "----------------------------");
						@SuppressWarnings("unused")
						BukkitTask task = new BukkitRunnable(){
							public void run() {
								if(JailVote.yesVotes > JailVote.noVotes){
									JailPrisoner prisoner = new JailPrisoner(target.getName(), target.getUniqueId(), Settings.getGlobalInt(Setting.VoteJailTime) * 6, "", "", false, "", Settings.getGlobalString(Setting.VoteJailReason), true, "", "Players", "");
									PrisonerManager.Jail(prisoner, target);
									if(Jail.instance.notificationsPlugin != null){
										Notification jailNotification = new Notification("Jail", target.getName() + " was jailed by players vote", "For " + Settings.getGlobalInt(Setting.VoteJailTime) + " mins", Color.ORANGE, Color.RED, Color.RED);
										Jail.instance.notificationsPlugin.showNotification(jailNotification);
									}
									Jail.instance.getServer().broadcastMessage(ChatColor.GREEN + target.getName() + ChatColor.WHITE + " was jailed for " + Settings.getGlobalInt(Setting.VoteJailTime) + " mins , " + ChatColor.GOLD + JailVote.yesVotes + " players" + ChatColor.WHITE + " voted yes!");
									JailVote.yesVotes = 0;
									JailVote.noVotes = 0;
									JailVote.voteStarted = false;
									JailVote.hasVoted.clear();
								}else{
									Jail.instance.getServer().broadcastMessage(ChatColor.GREEN + target.getName() + ChatColor.WHITE + " was not jailed, " + ChatColor.GOLD + JailVote.noVotes + " players " + ChatColor.WHITE + " voted no and " + ChatColor.GOLD + JailVote.yesVotes + " players " + ChatColor.WHITE + " voted yes!");
									JailVote.yesVotes = 0;
									JailVote.noVotes = 0;
									JailVote.voteStarted = false;
									JailVote.hasVoted.clear();
								}
							}
						}.runTaskLater(Jail.instance, 1200);
					}
				}else{
					if(args[0].equalsIgnoreCase("yes") && JailVote.voteStarted && !JailVote.hasVoted.contains(player)){
						JailVote.yesVotes ++;
						JailVote.hasVoted.add(player);
						Util.Message("You Voted Yes!", player);
						return true;
					}
					
					if(args[0].equalsIgnoreCase("no") && JailVote.voteStarted && !JailVote.hasVoted.contains(player)){
						JailVote.noVotes ++;
						JailVote.hasVoted.add(player);
						Util.Message("You Voted No!", player);
						return true;
					}
					
					if(args[0].equalsIgnoreCase("yes") && !JailVote.voteStarted){
						Util.Message("There is no vote running at the moment", player);
						return true;
					}
					
					if(args[0].equalsIgnoreCase("no") && !JailVote.voteStarted){
						Util.Message("There is no vote running at the moment", player);
						return true;
					}
					
					if(!args[0].equalsIgnoreCase("yes") && !args[0].equalsIgnoreCase("no")){
						Util.Message(ChatColor.RED + "That player is offline!", player);
					}
					
					if(JailVote.hasVoted.contains(player)){
						Util.Message(ChatColor.RED + "You have already voted!", player);
					}
				}
			}
			
			return true;
		}else{
			Util.Message(ChatColor.RED + "This is not enabled on this server!", player);
			return true;
		}
	}
}
