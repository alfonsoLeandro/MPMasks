package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.horse;

import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class HORSE2 extends Mask {

    public HORSE2(String skinUrl, String itemName, List<String> itemLore) {
        super(new HashSet<PotionEffect>(){{add(new PotionEffect(PotionEffectType.SPEED, 1, 0));}},
                "HORSE2",
                skinUrl,
                itemName,
                itemLore);
    }

}
