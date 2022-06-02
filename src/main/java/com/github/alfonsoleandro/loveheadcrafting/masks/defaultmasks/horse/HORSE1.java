package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.horse;

import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class HORSE1 extends Mask {

    public HORSE1(String skinUrl, String itemName, List<String> itemLore) {
        super(new HashSet<PotionEffect>(){{add(new PotionEffect(PotionEffectType.SPEED, 1, 0));}},
                "HORSE1",
                skinUrl,
                itemName,
                itemLore);
    }

}
