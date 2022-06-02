package com.github.alfonsoleandro.loveheadcrafting.events;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import com.codingforcookies.armorequip.ArmorType;
import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class ApplyRemoveEffects implements Listener {

    private final LoveHeadCrafting plugin;

    public ApplyRemoveEffects(LoveHeadCrafting plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String maskType = getMaskType(player.getInventory().getHelmet());
        if(maskType == null) return;

        applyEffects(player, maskType);
    }


    @EventHandler
    public void onPlayerEquipHelmet(ArmorEquipEvent event){
        if(!event.getType().equals(ArmorType.HELMET)) return;
        Player player = event.getPlayer();
        removeEffects(player);

        String newMaskType = getMaskType(event.getNewArmorPiece());

        if(newMaskType != null) applyEffects(player, newMaskType);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        removeEffects(event.getPlayer());
    }




    private String getMaskType(ItemStack helmet){
        if(helmet == null || !helmet.hasItemMeta()) return null;
        PersistentDataContainer data = Objects.requireNonNull(helmet.getItemMeta()).getPersistentDataContainer();

        for(NamespacedKey key : data.getKeys()){
            if(key.getKey().equalsIgnoreCase("MPMask") && data.has(key, PersistentDataType.STRING)){
                return data.get(key, PersistentDataType.STRING);
            }
        }

        return null;
    }


    public void applyEffects(Player player, String maskType){
        Bukkit.broadcastMessage("APPLY EFFECTS TO "+player+" USING MASK "+maskType);
        plugin.getEffectsManager().applyEffect(player, maskType);
    }

    public void removeEffects(Player player){
        Bukkit.broadcastMessage("REMOVE EFFECTS FROM "+player);
        plugin.getEffectsManager().removeAll(player);
    }

}
