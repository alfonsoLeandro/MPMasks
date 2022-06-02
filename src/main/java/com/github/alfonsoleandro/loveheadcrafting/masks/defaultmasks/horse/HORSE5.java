package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.horse;

import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class HORSE5 extends Mask {

    public HORSE5(String skinUrl, String itemName, List<String> itemLore) {
        super(new HashSet<PotionEffect>(){{add(new PotionEffect(PotionEffectType.SPEED, 1, 2));}},
                "HORSE5",
                skinUrl,
                itemName,
                itemLore);
    }

}
