package com.github.alfonsoleandro.loveheadcrafting.masks.effects;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Set;

public class StandOnBlockEffect extends Effect {

    protected final Material blockType;
    protected final Set<PotionEffect> standingOnBlockEffects;
    protected BukkitTask task;

    public StandOnBlockEffect(Player player, String maskType, Set<PotionEffect> effects,
                              Material blockType, Set<PotionEffect> standingOnBlockEffects) {
        super(player, maskType, effects);
        this.blockType = blockType;
        this.standingOnBlockEffects = standingOnBlockEffects;
        super.remove();
        this.startEffect();
    }

    @Override
    protected void startEffect(){
        super.startEffect();
        if(standingOnBlockEffects != null && standingOnBlockEffects.size() > 0) {
            task = new BukkitRunnable() {

                @Override
                public void run() {
                    Material standingBlockType = player.getLocation().add(0, -1, 0).getBlock().getType();
                    if(standingBlockType.equals(blockType)) {
                        for (PotionEffect effect : standingOnBlockEffects) {
                            PotionEffect e = new PotionEffect(effect.getType(), 40, effect.getAmplifier(), effect.isAmbient());
                            player.addPotionEffect(e);
                        }
                    }
                }

            }.runTaskTimer(JavaPlugin.getPlugin(LoveHeadCrafting.class), 0, 40);
        }

    }

    @Override
    public void remove(){
        super.remove();
        if(task != null && !task.isCancelled()) task.cancel();
    }



}
