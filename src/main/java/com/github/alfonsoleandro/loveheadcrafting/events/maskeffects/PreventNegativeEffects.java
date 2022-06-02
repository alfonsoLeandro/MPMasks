package com.github.alfonsoleandro.loveheadcrafting.events.maskeffects;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PreventNegativeEffects implements Listener {

    public final static Set<PotionEffectType> negativeEffects = new HashSet<PotionEffectType>(){{
        add(PotionEffectType.BLINDNESS);
        add(PotionEffectType.CONFUSION);
        add(PotionEffectType.HARM);
        add(PotionEffectType.HUNGER);
        add(PotionEffectType.LEVITATION);
        add(PotionEffectType.POISON);
        add(PotionEffectType.SLOW);
        add(PotionEffectType.SLOW_DIGGING);
        add(PotionEffectType.UNLUCK);
        add(PotionEffectType.WEAKNESS);
        add(PotionEffectType.WITHER);
    }};
    private final LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
    private final Player player;
    private final int probability;
    private final String maskType;
    private final Set<PotionEffectType> immuneTo;
    private final Random r = new Random();

    public PreventNegativeEffects(Player player, int probability, String maskType, Set<PotionEffectType> immuneTo){
        this.player = player;
        this.maskType = maskType;
        this.probability = probability;
        this.immuneTo = immuneTo;
    }


    @EventHandler
    public void onPotion(EntityPotionEffectEvent event){
        if(!player.equals(event.getEntity())) return;
        PotionEffect effect = event.getNewEffect();
        if(effect == null) return;

        if(immuneTo != null && immuneTo.contains(effect.getType())){
            event.setCancelled(true);
            plugin.getConsoleLogger().send(player,
                    plugin.getMessagesYaml().getAccess().getString("negative effect cancelled")
                            .replace("%mask%", maskType)
                            .replace("%effect%", effect.getType().getName()));
            return;
        }

        if(isNegative(effect.getType())){
            if(probability > r.nextInt(100)){
                event.setCancelled(true);
                plugin.getConsoleLogger().send(player,
                        plugin.getMessagesYaml().getAccess().getString("negative effect cancelled")
                .replace("%mask%", maskType)
                .replace("%effect%", effect.getType().getName()));
            }
        }

    }


    private boolean isNegative(PotionEffectType type){
        return negativeEffects.contains(type);
    }


}
