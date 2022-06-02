package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.horse;

import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class HORSE3 extends Mask {

    public HORSE3(String skinUrl, String itemName, List<String> itemLore) {
        super(new HashSet<PotionEffect>(){{add(new PotionEffect(PotionEffectType.SPEED, 1, 1));}},
                "HORSE3",
                skinUrl,
                itemName,
                itemLore);
    }

}
