package com.massivecraft.vampire.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.entity.UPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

public class EngineFx extends Engine
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static EngineFx i = new EngineFx();
	public static EngineFx get() { return i; }
	
	// -------------------------------------------- //
	// LISTENERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void fxOnDeath(EntityDeathEvent event)
	{
		// If a vampire dies ...
		Player player = IdUtil.getAsPlayer(event.getEntity());
		if (MUtil.isntPlayer(player)) return;
		
		UPlayer uplayer = UPlayer.get(player);
		if (uplayer == null) return;
		
		if (!uplayer.isVampire()) return;
		
		// ... burns up with a violent scream ;,,;
		uplayer.runFxShriek();
		uplayer.runFxFlameBurst();
		uplayer.runFxSmokeBurst();
	}
	
}
