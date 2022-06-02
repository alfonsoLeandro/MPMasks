package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.pig;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.ReturnConsumedItem;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.MultiplyPotionDuration;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import com.github.alfonsoleandro.loveheadcrafting.masks.effects.Effect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class PIG5 extends Mask {

    public PIG5(String skinUrl, String itemName, List<String> itemLore) {
        super(new HashSet<PotionEffect>(){{add(new PotionEffect(PotionEffectType.SATURATION, 1, 1));}},
                "PIG5",
                skinUrl,
                itemName,
                itemLore);
    }

    @Override
    public void start(Player player) {
        super.start(player);
        Listener multiplyPotion = new MultiplyPotionDuration(player, 2.5);
        Listener giveBackGoldenApple = new ReturnConsumedItem(player, new HashSet<Material>(){{add(Material.GOLDEN_APPLE); add(Material.ENCHANTED_GOLDEN_APPLE);}}, 50);
        listeners.put(player, new HashSet<Listener>(){{add(multiplyPotion); add(giveBackGoldenApple);}});
        Bukkit.getPluginManager().registerEvents(multiplyPotion, JavaPlugin.getPlugin(LoveHeadCrafting.class));
        Bukkit.getPluginManager().registerEvents(giveBackGoldenApple, JavaPlugin.getPlugin(LoveHeadCrafting.class));
    }


}