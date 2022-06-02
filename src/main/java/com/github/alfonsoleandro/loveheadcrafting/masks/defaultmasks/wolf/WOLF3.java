package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.wolf;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.SpawnAttackingWolves;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;

public class WOLF3 extends Mask {


    public WOLF3(String skinUrl, String itemName, List<String> itemLore) {
        super(null, "WOLF3",
                skinUrl,
                itemName,
                itemLore);
    }

    @Override
    public void start(Player player) {
        LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
        Listener spawnWolves = new SpawnAttackingWolves(player, 2, 30, plugin.getMessagesYaml().getAccess().getString("wolves name"));
        listeners.put(player, new HashSet<Listener>(){{add(spawnWolves);}});
        Bukkit.getPluginManager().registerEvents(spawnWolves, plugin);
    }
}
