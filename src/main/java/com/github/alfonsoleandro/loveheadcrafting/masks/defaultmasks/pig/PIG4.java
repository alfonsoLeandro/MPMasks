package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.pig;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.MultiplyPotionDuration;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import com.github.alfonsoleandro.loveheadcrafting.masks.effects.Effect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class PIG4 extends Mask {

    public PIG4(String skinUrl, String itemName, List<String> itemLore) {
        super(new HashSet<PotionEffect>(){{add(new PotionEffect(PotionEffectType.SATURATION, 1, 0));}},
                "PIG4",
                skinUrl,
                itemName,
                itemLore);
    }

    @Override
    public void start(Player player) {
        super.start(player);
        Listener multiplyPotion = new MultiplyPotionDuration(player, 2.0);
        listeners.put(player, new HashSet<Listener>(){{add(multiplyPotion);}});
        Bukkit.getPluginManager().registerEvents(multiplyPotion, JavaPlugin.getPlugin(LoveHeadCrafting.class));
    }


}