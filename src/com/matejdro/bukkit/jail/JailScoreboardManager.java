package com.matejdro.bukkit.jail;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class JailScoreboardManager {
	
	public ScoreboardManager manager = Bukkit.getScoreboardManager();
	public HashMap<String, Scoreboard> scoreboards = new HashMap<String, Scoreboard>();
	
	public void displayJailTime(){
		for(String name: Jail.prisoners.keySet()){
			scoreboards.put(name, manager.getNewScoreboard());
		}
		
		//ConcurrentModificationException are bound to happen when you're iterating over a hashmap (or anything related) and while
		//you iterate over it you are adding to it or removing from it. To avoid this, just create a temporary copy of it and
		//iterate over the temporary one and make changes to the real one.
		HashMap<String, Scoreboard> temp = new HashMap<String, Scoreboard>(scoreboards);
		
		for(String name: temp.keySet()){
			Scoreboard board = scoreboards.get(name);
			Objective obj;
			if(!Jail.prisoners.containsKey(name)){
				scoreboards.remove(name);
				if(Bukkit.getPlayer(name) != null){
					Bukkit.getPlayer(name).setScoreboard(manager.getNewScoreboard());
				}
			}
			if(board.getObjective("test") == null){
				obj = board.registerNewObjective("test", "dummy");
			}else{
				obj = board.getObjective("test");
			}
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName("Jail Stats");
			Score score = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Time:"));
			if(scoreboards.containsKey(name)){
				score.setScore((int) Math.ceil(Jail.prisoners.get(name).getRemainingTimeMinutes()));
			}
			if(Bukkit.getPlayer(name) != null && scoreboards.containsKey(name)){
				Bukkit.getPlayer(name).setScoreboard(scoreboards.get(name));
			}
		}
	}
}
