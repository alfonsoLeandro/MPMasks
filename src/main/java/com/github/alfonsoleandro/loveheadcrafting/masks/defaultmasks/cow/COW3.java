package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.cow;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.HigherDamageWithEffects;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.PreventNegativeEffects;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;

public class COW3 extends Mask {

    public COW3(String skinUrl, String itemName, List<String> itemLore) {
        super(null, "COW3", skinUrl, itemName, itemLore);
    }

    @Override
    public void start(Player player){
        LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
        Listener preventNegativeEffects = new PreventNegativeEffects(player, 50, maskType, null);
        Listener moreDamageWithNegative = new HigherDamageWithEffects(player,
                PreventNegativeEffects.negativeEffects, 2.0);
        listeners.put(player, new HashSet<Listener>(){{
            add(preventNegativeEffects);
            add(moreDamageWithNegative);}});
        Bukkit.getPluginManager().registerEvents(preventNegativeEffects, plugin);
        Bukkit.getPluginManager().registerEvents(moreDamageWithNegative, plugin);
    }
}
