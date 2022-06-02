package com.github.alfonsoleandro.loveheadcrafting.events.maskeffects;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

public class HigherDamageWithEffects implements Listener {

    private final Player player;
    private final Set<PotionEffectType> effectTypes;
    private final double multiplier;


    public HigherDamageWithEffects(Player player, Set<PotionEffectType> effectTypes, double multiplier) {
        this.player = player;
        this.effectTypes = effectTypes;
        this.multiplier = multiplier;
    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getDamager().equals(player)){
            for(PotionEffectType type : effectTypes) {
                if(player.hasPotionEffect(type)) {
                    event.setDamage(event.getDamage() * multiplier);
                    break;
                }
            }
        }
    }



}
