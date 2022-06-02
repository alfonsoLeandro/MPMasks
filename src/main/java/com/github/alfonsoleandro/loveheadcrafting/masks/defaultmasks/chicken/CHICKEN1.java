package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.chicken;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.PreventDamage;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;

public class CHICKEN1 extends Mask {

    public CHICKEN1(String skinUrl, String itemName, List<String> itemLore) {
        super(null, "CHICKEN1", skinUrl, itemName, itemLore);
    }

    @Override
    public void start(Player player){
        LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
        Listener preventNegativeEffects = new PreventDamage(player, EntityDamageEvent.DamageCause.FALL,
                20,
                maskType);
        listeners.put(player, new HashSet<Listener>(){{add(preventNegativeEffects);}});
        Bukkit.getPluginManager().registerEvents(preventNegativeEffects, plugin);
    }

}
