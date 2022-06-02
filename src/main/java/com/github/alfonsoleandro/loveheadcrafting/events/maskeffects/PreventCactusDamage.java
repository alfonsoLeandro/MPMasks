package com.github.alfonsoleandro.loveheadcrafting.events.maskeffects;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.managers.EffectsManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;

public class PreventCactusDamage implements Listener {

    final private Player player;

    public PreventCactusDamage(Player player){
        this.player = player;
    }

    @EventHandler
    public void preventArmorDamage(EntityDamageByBlockEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(!event.getDamager().getType().equals(Material.CACTUS)) return;
        if(!player.equals(event.getEntity())) return;

        event.setCancelled(true);
    }

}
