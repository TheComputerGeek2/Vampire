package com.massivecraft.vampire.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.entity.UConf;
import com.massivecraft.vampire.entity.UPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class EngineTruce extends Engine
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static EngineTruce i = new EngineTruce();
	public static EngineTruce get() { return i; }
	
	// -------------------------------------------- //
	// TRUCE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void truceTarget(EntityTargetEvent event)
	{
		// If a player is targeted...
		if (MUtil.isntPlayer(event.getTarget())) return;
		Player player = (Player)event.getTarget();
		UConf uconf = UConf.get(player);
		
		// ... by creature that cares about the truce with vampires ...
		if (!(uconf.truceEntityTypes.contains(event.getEntityType()))) return;
		
		UPlayer uplayer = UPlayer.get(player);
		
		// ... and that player is a vampire ...
		if (!uplayer.isVampire()) return;
		
		// ... that has not recently done something to break the truce...
		if (uplayer.truceIsBroken()) return;
		
		// ... then if the player is a ghast target nothing ...
		if (event.getEntityType() == EntityType.GHAST)
		{
			event.setTarget(null);
			return;
		}
		
		// ... otherwise cancel the event.
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void truceDamage(EntityDamageEvent event)
	{
		// If this is a combat event ...
		if (!MUtil.isCombatEvent(event)) return;
		
		// ... to a creature that cares about the truce with vampires...
		Entity entity = event.getEntity();
		UConf uconf = UConf.get(entity);
		if (!(uconf.truceEntityTypes.contains(entity.getType()))) return;
		
		// ... and the liable damager is a vampire ...
		Entity damager = MUtil.getLiableDamager(event);
		if (MUtil.isntPlayer(damager)) return;
		UPlayer vpdamager = UPlayer.get(damager);
		if (vpdamager == null) return;
		if (!vpdamager.isVampire()) return;
		
		// Then that vampire broke the truce.
		vpdamager.truceBreak();
	}
	
}
