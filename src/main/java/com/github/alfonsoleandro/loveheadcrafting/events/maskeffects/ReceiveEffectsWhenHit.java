package com.github.alfonsoleandro.loveheadcrafting.events.maskeffects;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.Random;
import java.util.Set;

public class ReceiveEffectsWhenHit implements Listener {

    private final Player player;
    private final Set<PotionEffect> effects;
    private final int probability;
    private final String maskType;
    private final Random r = new Random();
    private final LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
    private final String effectsString;


    public ReceiveEffectsWhenHit(Player player, Set<PotionEffect> effects, int probability, String maskType) {
        this.player = player;
        this.effects = effects;
        this.probability = probability;
        this.maskType = maskType;
        StringBuilder sb = new StringBuilder();
        effects.forEach(e -> sb.append(e.getType().getName()).append(e.getAmplifier()+1).append(", "));
        this.effectsString = sb.toString().trim();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getEntity().equals(player)){
            if(probability > r.nextInt(100)){
                for(PotionEffect effect : effects){
                    player.addPotionEffect(effect);
                }
                receivedPotionEffectsMessage();
            }
        }
    }


    private void receivedPotionEffectsMessage(){
        plugin.getConsoleLogger().send(player, plugin.getMessagesYaml().getAccess()
                .getString("received potion effect")
        .replace("%effects%", effectsString)
        .replace("%mask%", maskType));
    }

}
