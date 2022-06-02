package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.cow;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.PreventNegativeEffects;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;

public class COW2 extends Mask {

    public COW2(String skinUrl, String itemName, List<String> itemLore) {
        super(null, "COW2", skinUrl, itemName, itemLore);
    }

    @Override
    public void start(Player player){
        LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
        Listener preventNegativeEffects = new PreventNegativeEffects(player, 30, maskType, null);
        listeners.put(player, new HashSet<Listener>(){{add(preventNegativeEffects);}});
        Bukkit.getPluginManager().registerEvents(preventNegativeEffects, plugin);
    }
}
