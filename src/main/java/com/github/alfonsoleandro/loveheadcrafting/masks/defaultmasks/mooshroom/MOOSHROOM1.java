package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.mooshroom;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.HigherDamageWithEffects;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.PreventNegativeEffects;
import com.github.alfonsoleandro.loveheadcrafting.events.maskeffects.ReceiveEffectsWhenHit;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class MOOSHROOM1 extends Mask {

    public MOOSHROOM1(String skinUrl, String itemName, List<String> itemLore) {
        super(null, "MOOSHROOM1", skinUrl, itemName, itemLore);
    }

    @Override
    public void start(Player player){
        LoveHeadCrafting plugin = JavaPlugin.getPlugin(LoveHeadCrafting.class);
        Listener receiveEffectsWhenHit = new ReceiveEffectsWhenHit(player, new HashSet<PotionEffect>(){{
            add(new PotionEffect(PotionEffectType.ABSORPTION, 100, 2));
        }},40, maskType);
        listeners.put(player, new HashSet<Listener>(){{
            add(receiveEffectsWhenHit);}});
        Bukkit.getPluginManager().registerEvents(receiveEffectsWhenHit, plugin);
    }
}