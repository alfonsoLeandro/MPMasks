package com.github.alfonsoleandro.loveheadcrafting.events.maskeffects;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class PreventDamage implements Listener {

    private final Player player;
    private final EntityDamageEvent.DamageCause damageCause;
    private final int probability;
    private final String maskType;
    private final Random r = new Random();
    private final LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);

    public PreventDamage(Player player, EntityDamageEvent.DamageCause damageCause, int probability, String maskType) {
        this.player = player;
        this.damageCause = damageCause;
        this.probability = probability;
        this.maskType = maskType;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity().equals(player) && event.getCause().equals(damageCause)){
            if(probability > r.nextInt(100)){
                event.setCancelled(true);
                sendDamagePreventedMessage();
            }
        }
    }

    private void sendDamagePreventedMessage(){
        plugin.getConsoleLogger().send(player,
                plugin.getMessagesYaml().getAccess()
                        .getString("damage prevented")
                        .replace("%cause%", damageCause.toString())
        .replace("%mask%", maskType));
    }


}
