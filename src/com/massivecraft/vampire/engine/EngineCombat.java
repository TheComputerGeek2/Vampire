package com.massivecraft.vampire.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.vampire.entity.MConf;
import com.massivecraft.vampire.entity.UConf;
import com.massivecraft.vampire.entity.UPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class EngineCombat extends Engine
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static EngineCombat i = new EngineCombat();
	public static EngineCombat get() { return i; }
	
	// -------------------------------------------- //
	// LISTENERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void combatVulnerability(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if (!MUtil.isCloseCombatEvent(event)) return;
		
		// ... where the liable damager is a human entity ...
		Entity damagerEntity = MUtil.getLiableDamager(event);
		if (!(damagerEntity instanceof HumanEntity)) return;
		HumanEntity damager = (HumanEntity) damagerEntity;
		UConf uconf = UConf.get(damager);
		
		// ... and the damagee is a vampire ...
		Entity entity = event.getEntity();
		if (MUtil.isntPlayer(entity)) return;
		UPlayer vampire = UPlayer.get(entity);
		if (vampire == null) return;
		if (!vampire.isVampire()) return;
		
		// ... and a wooden item was used ...
		ItemStack item = InventoryUtil.getWeapon(damager);
		if (item == null) return;
		Material itemMaterial = item.getType();
		if (!uconf.combatWoodMaterials.contains(itemMaterial)) return;
		
		// ... Then modify damage!
		MUtil.setDamage(event, uconf.combatWoodDamage);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void combatStrength(EntityDamageEvent event)
	{
		// If this is a close combat event ...
		if (!MUtil.isCloseCombatEvent(event)) return;
		
		// ... and the liable damager is a vampire ...
		Entity damager = MUtil.getLiableDamager(event);
		if (MUtil.isntPlayer(damager)) return;
		UPlayer vampire = UPlayer.get(damager);
		if (vampire == null) return;
		if (!vampire.isVampire()) return;
		
		// ... and this event isn't a forbidden mcmmo one ...
		if (!MConf.get().combatDamageFactorWithMcmmoAbilities && event.getClass().getName().equals("com.gmail.nossr50.events.fake.FakeEntityDamageByEntityEvent")) return;
		
		// ... Then modify damage!
		MUtil.scaleDamage(event, vampire.combatDamageFactor());
	}
	
}
