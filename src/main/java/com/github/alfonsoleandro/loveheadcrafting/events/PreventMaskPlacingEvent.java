package com.github.alfonsoleandro.loveheadcrafting.events;

import com.github.alfonsoleandro.loveheadcrafting.LoveHeadCrafting;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class PreventMaskPlacingEvent implements Listener {

    private final LoveHeadCrafting plugin;

    public PreventMaskPlacingEvent(LoveHeadCrafting plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent event) {
        if(event.isCancelled() || plugin.getConfigYaml().getAccess().getBoolean("allow placing")) return;
        ItemStack inHand = event.getItemInHand();

        if(!inHand.getType().equals(Material.PLAYER_HEAD) || !inHand.hasItemMeta()) return;

        PersistentDataContainer data = Objects.requireNonNull(inHand.getItemMeta()).getPersistentDataContainer();

        if(data.getKeys().stream().anyMatch(k -> k.getKey().equalsIgnoreCase("MPMask"))){
            event.setCancelled(true);
            plugin.getConsoleLogger().send(event.getPlayer(),
                    plugin.getMessagesYaml().getAccess().getString("cannot place"));
        }

    }
}


