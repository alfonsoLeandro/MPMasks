package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.rabbit;

import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class RABBIT2 extends Mask {

    public RABBIT2(String skinUrl, String itemName, List<String> itemLore) {
        super(new HashSet<PotionEffect>(){{add(new PotionEffect(PotionEffectType.JUMP,
                      1,
                      0));
              }},
                "RABBIT2",
                skinUrl,
                itemName,
                itemLore);
    }

}