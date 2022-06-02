package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.pig;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.MultiplyPotionDuration;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;

public class PIG3 extends Mask {

    public PIG3(String skinUrl, String itemName, List<String> itemLore) {
        super(null, "PIG3", skinUrl, itemName, itemLore);
    }

    @Override
    public void start(Player player) {
        Listener multiplyPotion = new MultiplyPotionDuration(player, 1.5);
        listeners.put(player, new HashSet<Listener>(){{add(multiplyPotion);}});
        Bukkit.getPluginManager().registerEvents(multiplyPotion, JavaPlugin.getPlugin(LoveHeadCrafting.class));
    }


}