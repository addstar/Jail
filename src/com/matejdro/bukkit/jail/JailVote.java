package com.matejdro.bukkit.jail;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class JailVote {

	public static boolean voteStarted = false;
	
	public static int yesVotes = 0;
	public static int noVotes = 0;
	
	public static ArrayList<Player> hasVoted = new ArrayList<Player>();
	
}
