package com.github.alfonsoleandro.loveheadcrafting.masks.effects;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Set;

public class Effect {

    protected final Player player;
    protected final String maskType;
    protected final Set<PotionEffect> effects;

    public Effect(Player player, String maskType, Set<PotionEffect> effects) {
        this.player = player;
        this.maskType = maskType;
        this.effects = effects;
        startEffect();
    }


    protected void startEffect(){
        if(effects != null) {
            for (PotionEffect effect : effects) {
                PotionEffect e = new PotionEffect(effect.getType(), Integer.MAX_VALUE, effect.getAmplifier(), effect.isAmbient());
                player.addPotionEffect(e);
            }
        }
    }

    public void remove(){
        if(effects == null) return;
        for(PotionEffect effect : effects){
            player.removePotionEffect(effect.getType());
        }
    }

    public String getMaskType(){
        return this.maskType;
    }



}
