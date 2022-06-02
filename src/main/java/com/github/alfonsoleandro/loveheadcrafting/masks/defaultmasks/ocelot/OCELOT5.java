package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.ocelot;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.FishRareLoot;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;

public class OCELOT5 extends Mask {

    public OCELOT5(String skinUrl, String itemName, List<String> itemLore) {
        super(null, "OCELOT5", skinUrl, itemName, itemLore);
    }

    @Override
    public void start(Player player) {
        LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
        Listener fishEvent = new FishRareLoot(plugin, player, 60, maskType);
        listeners.put(player, new HashSet<Listener>(){{add(fishEvent);}});
        Bukkit.getPluginManager().registerEvents(fishEvent, plugin);
    }
}