package com.github.alfonsoleandro.loveheadcrafting.managers;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import com.github.alfonsoleandro.loveheadcrafting.masks.Mask;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class EffectsManager{

    private final Map<Player, Mask> players = new HashMap<>();
    private final LoveHeadCrafting plugin;

    public EffectsManager(LoveHeadCrafting plugin){
        this.plugin = plugin;
    }


    /**
     * Checks if the given players has the given active effect.
     * @param player The player to look for effects.
     * @param maskType The type of the mask to look for.
     * @return true if the player has an effect and the effect is the one provided by the given mask.
     */
    public boolean hasEffect(Player player, String maskType){
        return players.containsKey(player) && players.get(player).getMaskType().equals(maskType);
    }

    /**
     * Checks if the given player does not have any active effect.
     * @param player The player to look for effects.
     * @return false if the player has an effect.
     */
    public boolean notHasEffect(Player player){
        return !players.containsKey(player);
    }


    /**
     * Gets the mask type the player is wearing.
     * @param player The player to check the mask type for.
     * @return Null if the player is not in the effects manager or the mask type.
     */
    public String getMaskType(Player player){
        return players.get(player).getMaskType();
    }




    /**
     * Applies the given mask type effect to the given player.
     * @param player The player to apply the effects to.
     * @param maskType The mask type of the given player.
     */
    public void applyEffect(Player player, String maskType){
        Mask mask = plugin.getMasksManager().getMask(maskType);
        players.put(player, mask);
        mask.start(player);
    }



    /**
     * Removes any effect on the given player.
     * @param player The player to remove the effects from.
     */
    public void removeAll(Player player){
        if(!players.containsKey(player)) return;
        players.get(player).remove(player);
        players.remove(player);
    }

}
