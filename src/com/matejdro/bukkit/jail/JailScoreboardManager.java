package com.matejdro.bukkit.jail;

import java.util.Iterator;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class JailScoreboardManager {
	
	private ScoreboardManager manager = Bukkit.getScoreboardManager();
	private WeakHashMap<Player, Scoreboard> scoreboards = new WeakHashMap<Player, Scoreboard>();
	
	public void displayJailTime(){
		for(UUID id : Jail.prisoners.keySet())
		{
			Player player = Bukkit.getPlayer(id);
			if(player != null && !scoreboards.containsKey(player))
				scoreboards.put(player, manager.getNewScoreboard());
		}
		
		Iterator<Entry<Player, Scoreboard>> it = scoreboards.entrySet().iterator();
		
		while(it.hasNext())
		{
			Entry<Player, Scoreboard> entry = it.next();
			Scoreboard board = entry.getValue();
			Player player = entry.getKey();
			Objective obj;
			if(!Jail.prisoners.containsKey(player.getUniqueId())){
				it.remove();
				player.setScoreboard(manager.getMainScoreboard());
				continue;
			}
			
			if(board.getObjective("test") == null){
				obj = board.registerNewObjective("test", "dummy");
			}else{
				obj = board.getObjective("test");
			}
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName("Jail Stats");
			Score score = obj.getScore(ChatColor.GREEN + "Time:");
			score.setScore((int) Math.ceil(Jail.prisoners.get(player.getUniqueId()).getRemainingTimeMinutes()));
			
			player.setScoreboard(board);
		}
	}
}
