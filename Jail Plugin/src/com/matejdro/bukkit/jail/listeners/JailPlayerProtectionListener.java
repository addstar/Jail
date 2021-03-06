package com.matejdro.bukkit.jail.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.PermissionDefault;

import com.matejdro.bukkit.jail.InputOutput;
import com.matejdro.bukkit.jail.Jail;
import com.matejdro.bukkit.jail.JailCell;
import com.matejdro.bukkit.jail.JailPrisoner;
import com.matejdro.bukkit.jail.JailZone;
import com.matejdro.bukkit.jail.Setting;
import com.matejdro.bukkit.jail.Settings;
import com.matejdro.bukkit.jail.Util;

@SuppressWarnings("deprecation")
public class JailPlayerProtectionListener implements Listener {
	private Jail plugin;

	public JailPlayerProtectionListener(Jail instance)
	{
		plugin = instance;
	}
	
	@EventHandler()
	public void onPlayerChat (PlayerChatEvent event)
	{
		if (event.isCancelled()) return;
		JailPrisoner prisoner = Jail.prisoners.get(event.getPlayer().getName().toLowerCase());
		if (prisoner != null && prisoner.isMuted())
		{
			Util.Message(prisoner.getJail().getSettings().getString(Setting.MessageMute), event.getPlayer());
			event.setCancelled(true);
		}
		for(String playerName: Jail.prisoners.keySet()){
			if(!Settings.getGlobalBoolean(Setting.PrisonersRecieveMessages)){
				event.getRecipients().remove(Jail.instance.getServer().getPlayer(playerName));
			}
		}
	}
	
	@EventHandler()
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled()) return;
		JailPrisoner prisoner = Jail.prisoners.get(event.getPlayer().getName().toLowerCase());
		JailZone jail = prisoner != null ? prisoner.getJail() : null;

		if (prisoner != null && jail != null)
		{			
			boolean whitelisted = false;
			
			if(jail.getSettings().getList(Setting.WhitelistedCommands).size() > 0){
				for(Object o : jail.getSettings().getList(Setting.WhitelistedCommands)){
					String i = null;
					if(o instanceof String){
						i = (String) o;
					}
					
					if(i != null && event.getMessage().toLowerCase().contains(i.toLowerCase())){
						whitelisted = true;
						break;
					}
				}
			}
			else
			{
				whitelisted = true;
				for (Object o : jail.getSettings().getList(Setting.PreventCommands))
				{
					String i = (String) o;
					if (event.getMessage().toLowerCase().startsWith(i.toLowerCase()))
					{
						whitelisted = false;
						break;
					}
				}
			}
			
			if (!whitelisted)
			{
				if (jail.getSettings().getInt(Setting.CommandProtectionPenalty) > 0 && prisoner.getRemainingTime() > 0)
				{
					
					Util.Message(ChatColor.RED + jail.getSettings().getString(Setting.MessageForbiddenCommandNoPenalty), event.getPlayer());
					prisoner.setRemainingTime(prisoner.getRemainingTime() + jail.getSettings().getInt(Setting.CommandProtectionPenalty) * 6);
					InputOutput.UpdatePrisoner(prisoner);
				}
				else
				{
					Util.Message(ChatColor.RED + jail.getSettings().getString(Setting.MessageForbiddenCommandPenalty), event.getPlayer());
				}
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler()
	 public void onPlayerMove(PlayerMoveEvent event) {
		 if (event.isCancelled()) return;
		 if (Jail.prisoners.containsKey(event.getPlayer().getName().toLowerCase()))
			{
				JailPrisoner prisoner = Jail.prisoners.get(event.getPlayer().getName().toLowerCase());
				if (prisoner.isBeingReleased() || prisoner.getJail() == null) return;
				
				if (event.getFrom().getX() != event.getTo().getX() && event.getFrom().getZ() != event.getTo().getZ()) prisoner.setAFKTime(0);
				
				JailZone jail = prisoner.getJail();
				if (!jail.getSettings().getBoolean(Setting.EnablePlayerMoveProtection)) return;
				if (!jail.isInside(event.getTo()))
				{
					if (jail.getSettings().getString(Setting.PlayerMoveProtectionAction).equals("guards") && prisoner.canGuardsBeSpawned() && event.getPlayer().getGameMode() == GameMode.SURVIVAL)
					{
						if (prisoner.getGuards().size() > 0)
						{
							for (Creature w : prisoner.getGuards().toArray(new Creature[0]))
							{
								if (w == null || w.isDead())
								{
									prisoner.getGuards().remove(w);
									Jail.guards.remove(w);
									continue;
								}
								if (w.getTarget() == null) w.setTarget(event.getPlayer());
								if (jail.getSettings().getInt(Setting.GuardTeleportDistance) > 0 && (w.getWorld() != w.getTarget().getWorld() || w.getLocation().distanceSquared(w.getTarget().getLocation()) > jail.getSettings().getInt(Setting.GuardTeleportDistance)))
									w.teleport(w.getTarget().getLocation());
							}
						}
						else
							prisoner.spawnGuards(jail.getSettings().getInt(Setting.NumbefOfGuards), event.getTo(), event.getPlayer());

					}
					
					else if (jail.getSettings().getString(Setting.PlayerMoveProtectionAction).equals("escape"))
					{
						prisoner.delete();
						plugin.getServer().broadcastMessage(event.getPlayer().getName() + " has escaped from jail!");
						return;
					}
					else 
					{
						if (!prisoner.canGuardsBeSpawned()) 
						{
							Jail.log.warning("[Jail] Unable to spawn guards for prisoner " + prisoner.getName() + "! Is this area protected against mobs?");
							prisoner.setGuardCanBeSpawned(true);
						}
						
						if (jail.getSettings().getInt(Setting.PlayerMoveProtectionPenalty) > 0  && prisoner.getRemainingTime() > 0 && Settings.getGlobalBoolean(Setting.EnablePlayerMoveProtection))
						{
							
							Util.Message(ChatColor.RED + jail.getSettings().getString(Setting.MessageEscapePenalty), event.getPlayer());
							prisoner.setRemainingTime(prisoner.getRemainingTime() + jail.getSettings().getInt(Setting.PlayerMoveProtectionPenalty) * 6);
							InputOutput.UpdatePrisoner(prisoner);
						}
					else
						{
							Util.Message(jail.getSettings().getString(Setting.MessageEscapeNoPenalty), event.getPlayer());
						}	
					
					Location teleport;
					teleport = prisoner.getTeleportLocation();
					event.setTo(teleport);
					}					
				}
				else if (jail.getSettings().getString(Setting.PlayerMoveProtectionAction).equals("guards"))
				{
						prisoner.killGuards();
				}
			}
		 
		 if(Jail.instance.handcuffed.contains(event.getPlayer().getName())){
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You are handcuffed and cant move!");
		 }
				
	 }
	 
	 
	 
	@EventHandler()
	 public void onPlayerTeleport(PlayerTeleportEvent event) {
		 if (event.isCancelled()) return;
		 onPlayerMove((PlayerMoveEvent) event);
     }
		 
	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST && (Jail.prisoners.containsKey(event.getPlayer().getName().toLowerCase()) || !Util.permission(event.getPlayer(), "jail.openchest", PermissionDefault.OP)))
				{
					for (int i = -1; i < 4; i++)
					{
						BlockFace face;
						if (i < 0) face = BlockFace.SELF;
						else face = BlockFace.values()[i];
						
						Block block = event.getClickedBlock().getRelative(face);
						if (block.getType() != Material.CHEST) continue;
						for (JailZone jail : Jail.zones.values())
							for (JailCell cell : jail.getCellList())
							{
								if ((!jail.getSettings().getBoolean(Setting.CanPrisonerOpenHisChest) || !cell.getPlayerName().toLowerCase().equals(event.getPlayer().getName().toLowerCase())) && ((cell.getChest() != null && block == cell.getChest().getBlock())))
										{
									event.setCancelled(true);
									return;
								}
							}
						}
					}

			JailPrisoner prisoner = Jail.prisoners.get(event.getPlayer().getName().toLowerCase());
			if (prisoner != null && prisoner.getJail() != null)
			{
				JailZone jail = prisoner.getJail();
				if (event.getClickedBlock() != null)
				{
					int id = event.getClickedBlock().getTypeId();
					if (jail.getSettings().getList(Setting.PreventInteractionBlocks).contains(String.valueOf(id)))
					{
						if (event.getAction() != Action.PHYSICAL && jail.getSettings().getInt(Setting.InteractionPenalty) > 0  && prisoner.getRemainingTime() > 0)
						{
							
							Util.Message(jail.getSettings().getString(Setting.MessagePreventedInteractionPenalty), event.getPlayer());
							prisoner.setRemainingTime(prisoner.getRemainingTime() + jail.getSettings().getInt(Setting.InteractionPenalty) * 6);
							InputOutput.UpdatePrisoner(prisoner);
						}
						else if (event.getAction() != Action.PHYSICAL)
						{
							Util.Message(jail.getSettings().getString(Setting.MessagePreventedInteractionNoPenalty), event.getPlayer());
						}
						
						event.setCancelled(true);
						return;
					}
				}
				if (event.getPlayer().getItemInHand() != null)
				{
					int id = event.getPlayer().getItemInHand().getTypeId();
					if (jail.getSettings().getList(Setting.PreventInteractionItems).contains(String.valueOf(id)))
					{
						if (event.getAction() != Action.PHYSICAL && jail.getSettings().getInt(Setting.InteractionPenalty) > 0  && prisoner.getRemainingTime() > 0)
						{
							
							Util.Message(jail.getSettings().getString(Setting.MessagePreventedInteractionPenalty), event.getPlayer());
							prisoner.setRemainingTime(prisoner.getRemainingTime() + jail.getSettings().getInt(Setting.InteractionPenalty) * 6);
							InputOutput.UpdatePrisoner(prisoner);
						}
						else if (event.getAction() != Action.PHYSICAL)
						{
							Util.Message(jail.getSettings().getString(Setting.MessagePreventedInteractionNoPenalty), event.getPlayer());
						}
						
						event.setCancelled(true);
						return;
					}
				}

			}
		}
	 
	@EventHandler()
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		 if (Jail.prisoners.containsKey(event.getPlayer().getName().toLowerCase()) && !Jail.prisoners.get(event.getPlayer().getName().toLowerCase()).isBeingReleased())
			{
				final JailPrisoner prisoner = Jail.prisoners.get(event.getPlayer().getName().toLowerCase());

                event.setRespawnLocation(prisoner.getTeleportLocation());

				JailZone jail = prisoner.getJail();
				if (!jail.isInside(event.getRespawnLocation()))
				{
					if (jail.getSettings().getInt(Setting.PlayerMoveProtectionPenalty) > 0  && prisoner.getRemainingTime() > 0 && Settings.getGlobalBoolean(Setting.EnablePlayerMoveProtection))
						{
							
							Util.Message(jail.getSettings().getString(Setting.MessageEscapePenalty), event.getPlayer());
							prisoner.setRemainingTime(prisoner.getRemainingTime() + jail.getSettings().getInt(Setting.PlayerMoveProtectionPenalty) * 6);
							InputOutput.UpdatePrisoner(prisoner);
						}
					else
						{
							Util.Message(jail.getSettings().getString(Setting.MessageEscapeNoPenalty), event.getPlayer());
						}
					prisoner.SetBeingReleased(true);
					final Location teleloc = prisoner.getTeleportLocation();
					final Player player = event.getPlayer();
					Jail.instance.getServer().getScheduler().scheduleSyncDelayedTask(Jail.instance, new Runnable() {

					    public void run() {
					        if (player != null)
					        	player.teleport(teleloc);
					        prisoner.SetBeingReleased(false);
					    }
					}, 1);
				}
			}
	 }
	
	@EventHandler
	public void onPlayerFoodchange(FoodLevelChangeEvent event)
	{
		JailPrisoner prisoner = Jail.prisoners.get(event.getEntity().getName().toLowerCase());
		if (prisoner != null && prisoner.getJail() != null && prisoner.getJail().getSettings().getBoolean(Setting.EnableFoodControl))
		{
			int minFood = prisoner.getJail().getSettings().getInt(Setting.FoodControlMinimumFood);
			if (event.getFoodLevel() <  minFood)
			{
				event.setFoodLevel(minFood);
				return;
			}
			
			int maxFood = prisoner.getJail().getSettings().getInt(Setting.FoodControlMaximumFood);
			if (event.getFoodLevel() > maxFood)
			{
				event.setFoodLevel(maxFood);
				return;
			}
		}
	}
}
