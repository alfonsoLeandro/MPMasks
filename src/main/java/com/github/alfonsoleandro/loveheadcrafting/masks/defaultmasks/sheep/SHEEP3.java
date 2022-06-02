package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.sheep;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.PreventCactusDamage;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import com.github.alfonsoleandro.loveheadcrafting.masks.effects.StandOnBlockEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class SHEEP3 extends Mask {

    public SHEEP3(String skinUrl, String itemName, List<String> itemLore) {
        super(null, "SHEEP3",
                skinUrl,
                itemName,
                itemLore);
    }

    @Override
    public void start(Player player) {
        StandOnBlockEffect effect = new StandOnBlockEffect(player,
                maskType,
                getPotionEffects(),
                Material.GRASS_BLOCK,
                new HashSet<PotionEffect>(){{add(new PotionEffect(PotionEffectType.SATURATION, 1, 0));}});
        playerEffects.get(player).add(effect);


        LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
        Listener cactusDamage = new PreventCactusDamage(player);
        listeners.put(player, new HashSet<Listener>(){{add(cactusDamage);}});
        Bukkit.getPluginManager().registerEvents(cactusDamage, plugin);
    }

}
