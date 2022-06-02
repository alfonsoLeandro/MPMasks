package com.github.alfonsoleandro.loveheadcrafting.masks.defaultmasks.rabbit;

import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;

public class RABBIT5 extends Mask {

    public RABBIT5(String skinUrl, String itemName, List<String> itemLore) {
        super(new HashSet<PotionEffect>(){{add(new PotionEffect(PotionEffectType.JUMP,
                      1,
                      2));
                  add(new PotionEffect(PotionEffectType.NIGHT_VISION,
                          1,
                          0));
              }},
                "RABBIT5",
                skinUrl,
                itemName,
                itemLore);
    }

}