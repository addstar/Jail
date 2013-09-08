package com.matejdro.bukkit.jail;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JailCellCreation {
public static HashMap<String,CreationPlayer> players = new HashMap<String,CreationPlayer>();

	public static void selectstart(Player player, String name)
	{
		if (players.containsKey(player.getName()))
		{
			players.remove(player.getName());
		}
		if (!player.getInventory().contains(Settings.getGlobalInt(Setting.SelectionTool)))
			player.getInventory().addItem(new ItemStack(Settings.getGlobalInt(Setting.SelectionTool),1));
		
		Util.Message(ChatColor.AQUA + "---------- Jail Cell Creation ----------", player);
		Util.Message(ChatColor.GREEN + "First, you must select a teleport point for the cell! Move to the teleport point and then click anywhere with your wooden sword to set it", player);
		Util.Message(ChatColor.AQUA + "----------------------------------------", player);
		players.put(player.getName(), new CreationPlayer(name));
	}
	
	public static void select(Player player, Block block)
	{
		switch (players.get(player.getName()).state)
		{
			case 1:
				telepoint(player,block);
				break;
			case 2:
				sign(player,block);
				break;
			case 3:
				chest(player,block);
				break;
			case 4:
				noname(player, block);
			
			
		}
	}
	
	public static Boolean chatmessage(Player player, String message)
	{
		if (players.get(player.getName()).state == 4) 
		{
			name(player, message);
			return true;
		}
		return false;
	}
	
	private static void telepoint(Player player, Block block)
	{
		Util.Message(ChatColor.AQUA + "---------- Jail Cell Creation ----------", player);
		Util.Message(ChatColor.GREEN + "Teleport point selected. Now select signs associated with this cell. You may select multiple signs. After you are done with the sign selection, right click on any non-sign block.", player);
		Util.Message(ChatColor.AQUA + "----------------------------------------", player);
		CreationPlayer cr = players.get(player.getName());
		cr.cell.setTeleportLocation(player.getLocation());
		cr.state++;
		
	}

	private static void sign(Player player, Block block)
	{
		CreationPlayer cr = players.get(player.getName());
		if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
		{
			cr.cell.addSign(block.getLocation());
			Util.Message(ChatColor.GREEN + "Sign selected.", player);
		}
		else
		{
			Util.Message(ChatColor.AQUA + "---------- Jail Cell Creation ----------", player);
			Util.Message(ChatColor.GREEN + "Sign selection completed. Now select a double chest associated with this cell. If there is no such chest click on any non-chest block. (please note that having no chest may result in players items being lost)", player);
			Util.Message(ChatColor.AQUA + "----------------------------------------", player);
			cr.state++;
		}
		
		
	}
	
	
	private static void chest(Player player, Block block)
	{
		CreationPlayer cr = players.get(player.getName());
		Material bpos1 = block.getLocation().add(-1, 0, 0).getBlock().getType();
	    Material bpos2 = block.getLocation().add(+1, 0, 0).getBlock().getType();
	    Material bpos3 = block.getLocation().add(0, 0, -1).getBlock().getType();
	    Material bpos4 = block.getLocation().add(0, 0, +1).getBlock().getType();
		
		if (block.getType() == Material.CHEST)
		{
			if(bpos1 == Material.CHEST || bpos2 == Material.CHEST || bpos3 == Material.CHEST || bpos4 == Material.CHEST){
				cr.cell.setChest(block.getLocation());
				cr.state ++;
			}else{
				Util.Message(ChatColor.RED + "Chest must be a double chest, chest not selected", player);
			}
		}else{
			cr.state++;
		}
		Util.Message(ChatColor.AQUA + "---------- Jail Cell Creation ----------", player);
		Util.Message(ChatColor.GREEN + "Chest selected. Now type the name of the cell into chat. If you don't want to select a name for the cell just right click anywhere.", player);
		Util.Message(ChatColor.AQUA + "----------------------------------------", player);
	}
	
	private static void noname(Player player, Block block)
	{
		CreationPlayer cr = players.get(player.getName());
		Util.Message(ChatColor.AQUA + "---------- Jail Cell Creation -----------", player);
		Util.Message(ChatColor.GREEN + "Cell created. Now select the teleport point of the next cell. To stop creating cells, type /jailstop.", player);
		Util.Message(ChatColor.AQUA + "-----------------------------------------", player);
		finish(cr, player);
	}
	
	private static void name(Player player, String name)
	{
		CreationPlayer cr = players.get(player.getName());
		
		cr.cell.setName(name);
		Util.Message(ChatColor.AQUA + "---------- Jail Cell Creation ----------", player);
		Util.Message(ChatColor.GREEN + "Name set and cell created. Now select the teleport point of the next cell. To stop creating cells, type /jailstop.", player);
		Util.Message(ChatColor.AQUA + "----------------------------------------", player);

		finish(cr, player);
	}
	
	private static void finish(CreationPlayer cr, Player player)
	{
		cr.cell.getJail().getCellList().add(cr.cell);
		InputOutput.InsertCell(cr.cell);
		players.remove(player.getName());
		players.put(player.getName(), new CreationPlayer(cr.cell.getJail().getName()));

	}
	

	
	private static class CreationPlayer
	{
		public int state;
		
		private JailCell cell;		
		public CreationPlayer(String name)
		{
			state = 1;
			cell = new JailCell(name, "", "");
		}
}

}
