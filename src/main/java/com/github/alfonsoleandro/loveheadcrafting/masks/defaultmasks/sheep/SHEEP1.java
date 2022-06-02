package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.sheep;

import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import com.github.alfonsoleandro.loveheadcrafting.masks.effects.StandOnBlockEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class SHEEP1 extends Mask {

    public SHEEP1(String skinUrl, String itemName, List<String> itemLore) {
        super(null, "SHEEP1",
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
    }

}
